package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.converter.ReviewConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.DownLoadUserReviewDTO;
import com.cskaoyan.wordmemorize.dto.app.UserReviewStatisticsDTO;
import com.cskaoyan.wordmemorize.dto.app.UserVocRevDTO;
import com.cskaoyan.wordmemorize.request.*;
import com.cskaoyan.wordmemorize.service.UserReviewService;
import jakarta.annotation.Resource;
//import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements UserReviewService {

    @Resource
    UserRevLogMapper userRevLogMapper;

    @Resource
    ReviewConverter reviewConverter;


    @Resource
    UserRevStatisticsMapper userRevStatisticsMapper;

    @Resource
    UserCheckinStatisticsMapper userCheckinStatisticsMapper;

    @Resource
    UserVocRevMapper userVocRevMapper;

    @Resource
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;



    @Override
    @Transactional
    public void synchronizeUserReviewData(UserReviewDataCommand command, Long userId) {
        // userRevStatistics
        synchronizeUserRevStatistics(command.getRevStatisticsCommand(), userId);
        // userVocRev
        synchronizedUserVocRev(command.getVocRevCommands(), userId);

        // 全部是插入的服务器没有的那些天的学习统计数据
        LambdaUpdateWrapper<UserCheckinStatisticsDO> updateWrapper = new LambdaUpdateWrapper<UserCheckinStatisticsDO>()
                .setSql("total_time = total_time + " + command.getRevStatisticsCommand().getStudyTime())
                .eq(UserCheckinStatisticsDO::getUserId, userId);
        userCheckinStatisticsMapper.update(updateWrapper);
    }

    private boolean synchronizeUserRevStatistics(UserReviewStatisticsCommand revStatisticsCommand, Long userId) {

        LambdaQueryWrapper<UserRevStatisticsDO> queryWrapper = new LambdaQueryWrapper<UserRevStatisticsDO>()
                .eq(UserRevStatisticsDO::getUserId, userId)
                .eq(UserRevStatisticsDO::getReviewDate, revStatisticsCommand.getReviewDate());

        // 判断是否已经存在当天的统计数据
        Long count = userRevStatisticsMapper.selectCount(queryWrapper);

        revStatisticsCommand.setUserId(userId.toString());
        UserRevStatisticsDO userRevStatisticsDO = reviewConverter.userRevStatisticsCommand2DO(revStatisticsCommand);
        if (count == 0) {
            // 不存在，插入新的用户复习统计数据
            userRevStatisticsMapper.insert(userRevStatisticsDO);
        } else {
            // 存在
            updateUserRevStatistics(userId, userRevStatisticsDO);
        }
        return true;
    }

    private void updateUserRevStatistics(Long userId, UserRevStatisticsDO userRevStatisticsDO) {
        LambdaUpdateWrapper<UserRevStatisticsDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRevStatisticsDO::getUserId, userId)
                .eq(UserRevStatisticsDO::getReviewDate, userRevStatisticsDO.getReviewDate());
        userRevStatisticsMapper.update(userRevStatisticsDO, updateWrapper);
    }

    public boolean synchronizedUserVocRev(List<UserVocRevCommand> vocRevCommands, Long userId) {
        if (CollectionUtils.isEmpty(vocRevCommands)) {
            return false;
        }

        vocRevCommands.forEach(vocRevCommand -> vocRevCommand.setUserId(userId.toString()));
        List<UserVocRevDO> userVocRevDOs
                = reviewConverter.userVocRevCommands2DOs(vocRevCommands);

        // 依次更新
        for (UserVocRevDO userVocRevDO : userVocRevDOs) {
            LambdaUpdateWrapper<UserVocRevDO> userVocRevUpdateWrapper = new LambdaUpdateWrapper<>();
            userVocRevUpdateWrapper.eq(UserVocRevDO::getUserId, userId)
                    .eq(UserVocRevDO::getVocId, userVocRevDO.getVocId());
            if (userVocRevDO.getType() == 0) {
                // 如果是尚未复习，改为正常复习
                userVocRevDO.setType(1);
            }
            userVocRevMapper.update(userVocRevDO, userVocRevUpdateWrapper);
        }

        return true;

    }

    @Override
    public DownLoadUserReviewDTO downloadUserReviewData(PageRequest pageRequest, Long userId) {

        DownLoadUserReviewDTO result = new DownLoadUserReviewDTO();

        CompletableFuture<Void> userReviewStatisticsFuture = CompletableFuture.runAsync(() -> {
            Page<UserRevStatisticsDO> selectPage = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
            LambdaQueryWrapper<UserRevStatisticsDO> wrapper = new LambdaQueryWrapper<>();

            wrapper.eq(UserRevStatisticsDO::getUserId, userId);

            Page<UserRevStatisticsDO> page = userRevStatisticsMapper.selectPage(selectPage, wrapper);
            List<UserRevStatisticsDO> records = page.getRecords();
            if (CollectionUtils.isEmpty(records)) {
                return;
            }
            PageDTO<UserReviewStatisticsDTO> userReviewStatisticsDTOPageDTO = reviewConverter.userRevStatistcsPage2PageDTO(page.getRecords(), page.getTotal());
            result.setUserReviewStatisticsPage(userReviewStatisticsDTOPageDTO);
        });

        CompletableFuture<Void> userVocRevFuture = CompletableFuture.runAsync(() -> {
            LambdaQueryWrapper<UserVocRevDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserVocRevDO::getUserId, userId);

            Page<UserVocRevDO> pageParam = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
            Page<UserVocRevDO> page = userVocRevMapper.selectPage(pageParam, queryWrapper);
            List<UserVocRevDO> records = page.getRecords();
            if (CollectionUtils.isEmpty(records)) {
                return;
            }
            PageDTO<UserVocRevDTO> userVocRevDTOPageDTO = reviewConverter.userVocRevPage2PageDTO(page.getRecords(), page.getTotal());
            result.setUserVocRevPage(userVocRevDTOPageDTO);
        });

        CompletableFuture.allOf(userReviewStatisticsFuture, userVocRevFuture).join();
        return result;
    }

    @Override
    public void addUserRovRev(List<Long> vocIds, Long userId) {

        // 过滤掉已经添加复习的单词
        vocIds = vocIds.stream().filter( vocId -> {
            LambdaQueryWrapper<UserVocRevDO> userVocRevWrapper
                    = new LambdaQueryWrapper<>();
            userVocRevWrapper.eq(UserVocRevDO::getVocId, vocId)
                    .eq(UserVocRevDO::getUserId, userId);
            UserVocRevDO userVocRevDO = userVocRevMapper.selectOne(userVocRevWrapper);
            return userVocRevDO == null;
        }).collect(Collectors.toList());

        vocIds.stream().forEach(vocId -> {
            UserVocRevDO userVocRevDO = new UserVocRevDO();
            userVocRevDO.setUserId(userId);
            userVocRevDO.setVocId(vocId);
            userVocRevDO.setFactor(CommonConstant.USER_VOC_REV_FACTOR_INIT);
            userVocRevDO.setReviewInterval(1);
            userVocRevDO.setTotalReview(0);
            userVocRevDO.setFamiliarity(null);
            userVocRevDO.setType(0);
            userVocRevDO.setNextReviewDate(LocalDate.now().plusDays(1));
            userVocRevDO.setConsecutiveFailCount(0);
            userVocRevMapper.insert(userVocRevDO);
        });

        if (CollectionUtils.isEmpty(vocIds)) {
            return;
        }

        // 增加占用单词上限，扣减可用单词上限

        LambdaUpdateWrapper<UserVocBoundStatisticsDO> updateWrapper = new LambdaUpdateWrapper<UserVocBoundStatisticsDO>()
                .setSql("occupied = occupied + " + vocIds.size())
                .setSql("available = available - " + vocIds.size())
                .eq(UserVocBoundStatisticsDO::getUserId, userId);
        // 更新单词上限
        userVocBoundStatisticsMapper.update(updateWrapper);
    }
}
