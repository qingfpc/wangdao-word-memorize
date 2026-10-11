package com.cskaoyan.wordmemorize.service.impl;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.converter.ReviewConverter;
import com.cskaoyan.wordmemorize.dao.entity.UserRevStatisticsDO;
import com.cskaoyan.wordmemorize.dao.entity.UserVocRevDO;
import com.cskaoyan.wordmemorize.dao.mapper.UserRevStatisticsMapper;
import com.cskaoyan.wordmemorize.dao.mapper.UserVocRevMapper;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.DownLoadUserReviewDTO;
import com.cskaoyan.wordmemorize.dto.app.UserVocRevDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserReviewDataCommand;
import com.cskaoyan.wordmemorize.service.UserReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@Service
public class UserReviewServiceImpl implements UserReviewService {
    @Autowired
    UserVocRevMapper userVocRevMapper;

    @Autowired
    ReviewConverter reviewConverter;

    @Autowired
    UserRevStatisticsMapper userRevStatisticsMapper;



    //同步用户的单词复习数据
    @Override
    public void synchronizeUserReviewData(UserReviewDataCommand command, Long userId) {
        if (command == null || userId == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }

        // 处理用户复习统计数据
        if (command.getRevStatisticsCommand() != null) {
            UserRevStatisticsDO userRevStatisticsDO = reviewConverter.userRevStatisticsCommand2DO(command.getRevStatisticsCommand());
            userRevStatisticsDO.setUserId(userId);
            // 如果id为null，自动生成唯一id（userId * 100000 + reviewDate的yyyyMMdd数字）
            if (userRevStatisticsDO.getId() == null) {
                LocalDate reviewDate = userRevStatisticsDO.getReviewDate();
                long dateNum = reviewDate != null ? reviewDate.getYear() * 10000L + reviewDate.getMonthValue() * 100 + reviewDate.getDayOfMonth() : 0L;
                userRevStatisticsDO.setId(userId * 100000 + dateNum);
            }
            userRevStatisticsMapper.upsertUserRevStatistics(userRevStatisticsDO);
        }

        // 处理用户已选单词复习情况
        if (!CollectionUtils.isEmpty(command.getVocRevCommands())) {
            List<UserVocRevDO> userVocRevDOList = reviewConverter.userVocRevCommands2DOs(command.getVocRevCommands());
            for (UserVocRevDO userVocRevDO : userVocRevDOList) {
                userVocRevDO.setUserId(userId);
                // 如果id为null，自动生成唯一id（userId*100000000+vocId）
                if (userVocRevDO.getId() == null) {
                    userVocRevDO.setId(userId * 100000000L + userVocRevDO.getVocId());
                }
                userVocRevMapper.upsertUserVocRev(userVocRevDO);
            }
        }
    }


    //下载用户的所有单词复习数据
    @Override
    public DownLoadUserReviewDTO downloadUserReviewData(PageRequest pageRequest, Long userId) {
        DownLoadUserReviewDTO downLoadUserReviewDTO = new DownLoadUserReviewDTO();

        //处理分页参数，若未传入参数则设置默认值
        int pageNum = pageRequest.getPageNum()==null?1:pageRequest.getPageNum();
        int count = pageRequest.getCount()==null?10:pageRequest.getCount();
        int offset = (pageNum - 1) * count;
        //获取用户已选单词(复习数据)
        List<UserVocRevDO> userVocRevDOS = userVocRevMapper.selectUserVocRevData(offset, count, userId);
        List<UserVocRevDTO> userVocRevDTOS = reviewConverter.userVocRevDOs2DTOs(userVocRevDOS);

        //构建分页DTO
        PageDTO<UserVocRevDTO> userVocRevPage = new PageDTO<>();
        userVocRevPage.setPageList(userVocRevDTOS);
        userVocRevPage.setTotalCount(userVocRevMapper.selectUserVocRevDataCount(userId));
        downLoadUserReviewDTO.setUserVocRevPage(userVocRevPage);
        return downLoadUserReviewDTO;
    }




    //添加用户单词的复习数据
    @Override
    public Result addUserRovRev(List<Long> vocIds) {
        if (CollectionUtils.isEmpty(vocIds)) {

            return Result.fail(ResultCodeEnum.PARAM_ERROR);
        }

        Long userId = StpKit.USER.getLoginIdAsLong();
//        if (userId == null) {
//
//            return Result.fail(ResultCodeEnum.LOCAL_LOGIN_FAIL);
//        }

        List<Long> insertedVocIds = new ArrayList<>();
        for (Long vocId : vocIds) {
            try {
                // 检查该用户该单词是否已存在复习数据
                UserVocRevDO exist = userVocRevMapper.selectByUserIdAndVocId(userId, vocId);
                if (exist != null) {
                    continue;
                }

                UserVocRevDO userVocRevDO = new UserVocRevDO();
                userVocRevDO.setUserId(userId);
                userVocRevDO.setVocId(vocId);
                userVocRevMapper.insert(userVocRevDO);
                insertedVocIds.add(vocId);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return Result.ok("成功添加 " + insertedVocIds.size() + " 条复习数据");
    }
}
