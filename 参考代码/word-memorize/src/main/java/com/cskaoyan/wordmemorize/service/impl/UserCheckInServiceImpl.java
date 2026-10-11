package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.constant.UserVocBoundEnum;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.ReviewConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinStatisticsDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserCheckinCommand;
import com.cskaoyan.wordmemorize.service.UserCheckinService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class UserCheckInServiceImpl implements UserCheckinService {

    @Resource
    UserCheckinMapper userCheckinMapper;

    @Resource
    ReviewConverter reviewConverter;

    @Resource
    SqlSessionFactory sqlSessionFactory;

    @Resource
    UserMapper userMapper;

    @Resource
    UserVocBoundLogMapper userVocBoundLogMapper;

    @Resource
    UserCheckinStatisticsMapper userCheckinStatisticsMapper;

    @Resource
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;

    @Resource
    LevelMapper levelMapper;


    @Override
    public PageDTO<UserCheckinDTO> getCheckInData(PageRequest pageRequest, Long userId) {
        Page<UserCheckinDO> selectPage = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
        LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper = new LambdaQueryWrapper<>();
        userCheckinWrapper.eq(UserCheckinDO::getUserId, userId);

        Page<UserCheckinDO> userCheckinDOPage = userCheckinMapper.selectPage(selectPage, userCheckinWrapper);
        PageDTO<UserCheckinDTO> userCheckinDTOPageDTO = reviewConverter.userCheckinPage2PageDTO(userCheckinDOPage.getRecords(), userCheckinDOPage.getTotal());
        return userCheckinDTOPageDTO;
    }


    @Override
    @Transactional
    public UserCheckinStatisticsDTO checkin(UserCheckinCommand command) {
        // 校验打卡时间
        LocalDate today = LocalDate.now();
        if (!today.equals(command.getSignDate())) {
            throw new BusinessException(ResultCodeEnum.CHECK_IN_DATE_NOT_TODAY);
        }
        Long userId = Long.parseLong(command.getUserId());


        command.setType(CommonConstant.USER_CHECK_IN_NORMAL_TYPE);

        // 规避重复打卡数据
        LambdaQueryWrapper<UserCheckinDO> userCheckinDOWrapper = new LambdaQueryWrapper<>();
        userCheckinDOWrapper.eq(UserCheckinDO::getUserId, userId);
        userCheckinDOWrapper.eq(UserCheckinDO::getSignDate, command.getSignDate());
        UserCheckinDO todayCheckin = userCheckinMapper.selectOne(userCheckinDOWrapper);
        if (todayCheckin != null) {
            throw new BusinessException(ResultCodeEnum.CHECK_IN_DATE_DUPLICATE);
        }

        UserCheckinDO userCheckinDO = reviewConverter.userCheckinCommand2DO(command);
        saveNewDateCheckin(userCheckinDO);

        // 更新单词奖励
        userVocBoundStatisticsMapper.rewardUserVocBound(CommonConstant.USER_CHECKIN_VOC_REWARD, userId);

        // 添加用户单词上限更新日志
        UserVocBoundLogDO userVocBoundLogDO = new UserVocBoundLogDO();
        userVocBoundLogDO.setUserId(userId);
        userVocBoundLogDO.setType(UserVocBoundEnum.USER_VOC_BOUND_LOG_TYPE_CHECKIN.getValue());
        userVocBoundLogDO.setLogDate(command.getSignDate());
        userVocBoundLogDO.setCount(CommonConstant.USER_CHECKIN_VOC_REWARD);
        userVocBoundLogDO.setDescription(CommonConstant.USER_VOC_BOUND_DESCRIPTION_CHECKIN);
        userVocBoundLogMapper.insert(userVocBoundLogDO);


        // 查询旧的签到统计信息
        LambdaQueryWrapper<UserCheckinStatisticsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCheckinStatisticsDO::getUserId, userId);

        // 更新签到统计信息
        UserCheckinStatisticsDO oldUserCheckinStatistics = userCheckinStatisticsMapper.selectOne(queryWrapper);
        UserCheckinStatisticsDO userCheckinStatisticsDO = updateCheckinStatistics(userId, today, oldUserCheckinStatistics);

        // 更新等级信息
        updateUserLevel(userId, userCheckinStatisticsDO);

        // 返回最新统计信息
        return reviewConverter.userCheckinStatisticsDO2DTO(userCheckinStatisticsDO);
    }

    private UserCheckinStatisticsDO updateCheckinStatistics(Long userId, LocalDate today, UserCheckinStatisticsDO oldUserCheckinStatistics) {
        UserCheckinStatisticsDO latestContinuousCheckinInfo
                = userCheckinMapper.getLatestContinuousCheckinInfo(userId, today);
        if (latestContinuousCheckinInfo.getCurContinuousDays() > oldUserCheckinStatistics.getMaxContinuousDays()) {
            latestContinuousCheckinInfo.setMaxContinuousDays(latestContinuousCheckinInfo.getCurContinuousDays());
            latestContinuousCheckinInfo.setMaxContinuousStartDate(latestContinuousCheckinInfo.getCurContinuousStartDate());

        } else {
            latestContinuousCheckinInfo.setMaxContinuousDays(oldUserCheckinStatistics.getMaxContinuousDays());
            latestContinuousCheckinInfo.setMaxContinuousStartDate(oldUserCheckinStatistics.getMaxContinuousStartDate());
        }
        latestContinuousCheckinInfo.setId(oldUserCheckinStatistics.getId());
        latestContinuousCheckinInfo.setUserId(userId);
        latestContinuousCheckinInfo.setTotalDays(oldUserCheckinStatistics.getTotalDays() + 1);
        userCheckinStatisticsMapper.updateById(latestContinuousCheckinInfo);
        return latestContinuousCheckinInfo;
    }


    private void updateUserLevel(Long userId , UserCheckinStatisticsDO userCheckinStatisticsDO) {

        UserDO userDO = userMapper.selectById(userId);
        Integer nowLevel = userDO.getLevelValue();
        String levelName = userDO.getLevelName();

        if (nowLevel >= CommonConstant.CHECK_IN_TYPE_MAX_LEVEL) {
            // 已达到最高等级，无需更新
            return;
        }

        LambdaQueryWrapper<LevelDO> levelWrapper = new LambdaQueryWrapper<>();
        levelWrapper.eq(LevelDO::getLevelValue, nowLevel);
        LevelDO levelDO = levelMapper.selectOne(levelWrapper);
        // 获取当前等级对应的签到天数
        Integer nowLevelCheckinDays = levelDO.getCheckInDays();

        LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper = new LambdaQueryWrapper<>();
        userCheckinWrapper.eq(UserCheckinDO::getUserId, userId)
                .gt(UserCheckinDO::getSignDate, userDO.getLevelDate());
        if (nowLevel < CommonConstant.ACCUMULATE_CHECK_IN_TYPE_MAX_LEVEL) {
            nowLevel = handleAccumulateCheckin(userId, userCheckinWrapper, nowLevel, userDO, nowLevelCheckinDays);
            if (nowLevel < CommonConstant.ACCUMULATE_CHECK_IN_TYPE_MAX_LEVEL) {
                // 无需考虑连续签到
                return;
            }
            // 否则，原本还没到累计签单的要求，现在达到了，那就要继续看看还有没有连续签到数据
        }

        // 当前已经达到最大连续签到的最大等级
        if (nowLevel > CommonConstant.CHECK_IN_TYPE_MAX_LEVEL) {
            return;
        }
        // 计算当前连续签到的起始日期
        LocalDate curContinuousStartDate = userCheckinStatisticsDO.getCurContinuousStartDate();

        // 默认就只算真实签到的当前连续签到
        Integer allCurContinuousCheckinDays = userCheckinStatisticsDO.getCurContinuousDays();
        // 判断升级日期是否可以和当前连续签到连起来
        Boolean isContinuousFromLastLevel = ChronoUnit.DAYS.between(userDO.getLevelDate(), curContinuousStartDate) <= 1 ? true : false;
        if (isContinuousFromLastLevel) {
            Long daysFromLastLevel =  ChronoUnit.DAYS.between(userDO.getLevelDate(), LocalDate.now());
            allCurContinuousCheckinDays =  daysFromLastLevel.intValue() + nowLevelCheckinDays;
        }

        LevelDO newLevelDO = levelMapper.getLevel(CommonConstant.USER_CHECKIN_TYPE_CONTINUOUS, allCurContinuousCheckinDays);
        if (nowLevel < newLevelDO.getLevelValue()) {
            // 升级了，计算新的升级日期
            UserDO toUpdate = new UserDO();
            toUpdate.setId(userId);
            toUpdate.setLevelName(newLevelDO.getLevelName());
            toUpdate.setLevelValue(newLevelDO.getLevelValue());
            toUpdate.setLevelDate(LocalDate.now());
            userMapper.updateById(toUpdate);
        }
    }

    private int handleAccumulateCheckin(Long userId, LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper, Integer nowLevel, UserDO userDO, Integer nowCheckinDays) {
        // 从上次升级开始的累计签到天数
        Long totalCount = userCheckinMapper.selectCount(userCheckinWrapper);

        // 计算累计签到天数对应的等级
        LevelDO newLevel = levelMapper.getLevel(CommonConstant.USER_CHECKIN_TYPE_ACCUMULATE, totalCount.intValue() + nowCheckinDays);

        // 如果等级发生变化
        if (userDO.getLevelValue() < newLevel.getLevelValue()) {
            UserDO toUpdate = new UserDO();
            toUpdate.setId(userId);
            toUpdate.setLevelName(newLevel.getLevelName());
            toUpdate.setLevelValue(newLevel.getLevelValue());
            LocalDate endSignDateWithTotalCheckin = userCheckinMapper.getLatestSignDateWithDays(userId, newLevel.getCheckInDays() - nowCheckinDays, userDO.getLevelDate());
            toUpdate.setLevelDate(endSignDateWithTotalCheckin);
            userMapper.updateById(toUpdate);
        }
        return newLevel.getLevelValue();
    }


    @Override
    public UserCheckinStatisticsDTO getUserCheckinStatistics(Long userId) {

        LambdaQueryWrapper<UserCheckinStatisticsDO> userCheckinStatisticsWrapper = new LambdaQueryWrapper<>();
        userCheckinStatisticsWrapper.eq(UserCheckinStatisticsDO::getUserId, userId);
        UserCheckinStatisticsDO userCheckinStatisticsDO = userCheckinStatisticsMapper.selectOne(userCheckinStatisticsWrapper);

        //if (!userCheckinStatisticsDO.getCurContinuousStartDate().equals(LocalDate.now())) {
        if (!LocalDate.now().equals(userCheckinStatisticsDO.getCurContinuousStartDate())) {
            UserCheckinStatisticsDTO userContinuousCheckinInfo = getUserContinuousCheckinInfo(userId);
            // 说明最近没有签到，防止用户如果一直未签到，则最近签到数据未更新
            userCheckinStatisticsDO.setCurContinuousDays(userContinuousCheckinInfo.getCurContinuousDays());
            userCheckinStatisticsDO.setCurContinuousStartDate(userContinuousCheckinInfo.getCurContinuousStartDate());
            // 更新最近签到数据
            userCheckinStatisticsMapper.updateById(userCheckinStatisticsDO);
        }


        Long totalTime = userCheckinStatisticsDO.getTotalTime();
        Long time = ((totalTime + 500) / 1000 + 30) / 60; // 四舍五入计算分钟
        userCheckinStatisticsDO.setTotalTime(time);
        return reviewConverter.userCheckinStatisticsDO2DTO(userCheckinStatisticsDO);
    }

    @Override
    public UserCheckinStatisticsDTO getUserContinuousCheckinInfo(Long userId) {

        // 获取最近一次的连续签到数据
        UserCheckinStatisticsDO latestContinuousCheckinInfo = userCheckinMapper
                .getLatestContinuousCheckinInfo(userId, LocalDate.now());
        if (latestContinuousCheckinInfo.getCurContinuousDays() == 0) {
            // 说明今天还没签到，那就以到昨天为止的连续签到为准
            latestContinuousCheckinInfo = userCheckinMapper.getLatestContinuousCheckinInfo(userId, LocalDate.now().minusDays(1));
        }
        return reviewConverter.userCheckinStatisticsDO2DTO(latestContinuousCheckinInfo);
    }

    private void saveNewDateCheckin(UserCheckinDO valueDO) {
        // 查询前一天是否签到
        LocalDate yesterdayDate = valueDO.getSignDate().minusDays(1);
        UserCheckinDO userCheckinBeforeDO = queryCheckinByDate(valueDO, yesterdayDate);

        if (userCheckinBeforeDO == null) {

            userCheckinMapper.insert(valueDO);
            Long newStickNumber = valueDO.getId();
            UserCheckinDO updateUserCheckinStickNo = new UserCheckinDO();
            updateUserCheckinStickNo.setId(newStickNumber);
            updateUserCheckinStickNo.setStickNo(newStickNumber);
            // 更新同一日期
            userCheckinMapper.updateById(updateUserCheckinStickNo);
        } else {
            // 算到前一天的连续签到中
            Long stickNo = userCheckinBeforeDO.getStickNo();
            valueDO.setStickNo(stickNo);
            userCheckinMapper.insert(valueDO);
        }
    }

    private UserCheckinDO queryCheckinByDate(UserCheckinDO valueDO, LocalDate yesterdayDate) {
        LambdaQueryWrapper<UserCheckinDO> userCheckinBeforeWrapper = new LambdaQueryWrapper<>();
        userCheckinBeforeWrapper.eq(UserCheckinDO::getUserId, valueDO.getUserId())
                .eq(UserCheckinDO::getSignDate, yesterdayDate)
                .select(Arrays.asList(UserCheckinDO::getStickNo));
        UserCheckinDO userCheckinDO = userCheckinMapper.selectOne(userCheckinBeforeWrapper);
        return userCheckinDO;
    }

    @Override
    public void userResignCard(Long userId) {

        // 获取当天连续签到数据
        LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper = new LambdaQueryWrapper<>();
        userCheckinWrapper
                .eq(UserCheckinDO::getUserId, userId)
                .eq(UserCheckinDO::getSignDate, LocalDate.now());
        UserCheckinDO userCheckinDO = userCheckinMapper.selectOne(userCheckinWrapper);


        if (userCheckinDO == null) {
            throw new BusinessException(ResultCodeEnum.USER_CHECKIN_DATA_NEED_SYNC);
        }

        LambdaQueryWrapper<UserCheckinStatisticsDO> checkinWrapper = new LambdaQueryWrapper<>();
        checkinWrapper.eq(UserCheckinStatisticsDO::getUserId, userId);
        // 查询签到统计数据
        UserCheckinStatisticsDO userCheckinStatisticsDO = userCheckinStatisticsMapper.selectOne(checkinWrapper);
        if (userCheckinStatisticsDO == null) {
            return;
        }
        // 获取当前连续签到天数和历史最大连续签到天数
        Integer maxContinuousDays = userCheckinStatisticsDO.getMaxContinuousDays();
        Integer curContinuousDays = userCheckinStatisticsDO.getCurContinuousDays();
        Integer resignDays = maxContinuousDays - curContinuousDays;
        if (resignDays <= 0) {
            return;
        }

        makeResignCheckinAndUpdateStatistics(userId, userCheckinStatisticsDO);

        // 更新等级, 补签卡
        updateUserInfo(userId, userCheckinStatisticsDO);


    }

    private void makeResignCheckinAndUpdateStatistics(Long userId, UserCheckinStatisticsDO userCheckinStatisticsDO) {
        // 获取当前连续签到的起始日期
        LocalDate curContinuousStartDate = userCheckinStatisticsDO.getCurContinuousStartDate();
        // 计算等于最大连续签到那天的当前连续签到的起始日期
        LocalDate startDate = curContinuousStartDate.minusDays(
                userCheckinStatisticsDO.getMaxContinuousDays() - userCheckinStatisticsDO.getCurContinuousDays());


        Long stickNo = null;
        //[insertStartDate, insertEndDate) 指的是插入补签记录的日期范围
        LocalDate insertStartDate = startDate;
        LocalDate insertEndDate = curContinuousStartDate;
        // 查询startDate那天是否有签到记录
        LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper = new LambdaQueryWrapper<>();
        userCheckinWrapper.eq(UserCheckinDO::getUserId, userId)
                .eq(UserCheckinDO::getSignDate, startDate);
        UserCheckinDO userCheckinDO = userCheckinMapper.selectOne(userCheckinWrapper);


        // 查询当天的签到段号
        LambdaQueryWrapper<UserCheckinDO> userCheckinDOWrapper = new LambdaQueryWrapper<>();
        userCheckinDOWrapper.eq(UserCheckinDO::getUserId, userId)
                .eq(UserCheckinDO::getSignDate, LocalDate.now());
        // 查询当天签到信息
        UserCheckinDO todayCheckinDO = userCheckinMapper.selectOne(userCheckinDOWrapper);
        stickNo = todayCheckinDO.getStickNo();
        if (userCheckinDO != null) {
            // 说明那天有签到记录
            UserCheckStickDO stickInfoContainsDate = userCheckinMapper.getStickInfoContainsDate(userId, startDate);

            // 那么补签的签到记录只能从那天的签到段的最后一天的下一天开始
            insertStartDate = stickInfoContainsDate.getCurContinuousStartDate()
                    .plusDays(stickInfoContainsDate.getCurContinuousDays());

            if (stickInfoContainsDate.getCurContinuousStartDate().equals(startDate)) {
                // 如果startDate刚好是其所在的签到段的起始日期
                stickNo = stickInfoContainsDate.getStickNo();
            } else {
                // 找到startDate所在签到段的最后一天的下一个日期
                startDate = insertStartDate;
            }
        }
        // [insertStartDate, insertEndDate)是指补签之后的当前连续签到的范围
        // 为了计算签到总天数的变化，所以在插入前先计算[insertStartDate, insertEndDate)这段时间内签到的天数
        LambdaQueryWrapper<UserCheckinDO> checkinQueryWrapper = new LambdaQueryWrapper<>();
        checkinQueryWrapper.eq(UserCheckinDO::getUserId, userId)
                .ge(UserCheckinDO::getSignDate, insertStartDate)
                .lt(UserCheckinDO::getSignDate, insertEndDate);
        // 已签到天数
        Long alreadyCheckinCount = userCheckinMapper.selectCount(checkinQueryWrapper);

        // 插入补签记录
        insertUserResignCheckinLog(userId, insertStartDate, insertEndDate, stickNo);

        // 计算最新的签到统计信息
        Long newMoreCurContinuousDays = ChronoUnit.DAYS.between(startDate, curContinuousStartDate);
        Long insertResignCount = newMoreCurContinuousDays - alreadyCheckinCount;
        // 最新当前连续签到 = 新增连续签到数 + 旧的当前连续签到数
        Long newCurContinuousDays = newMoreCurContinuousDays + userCheckinStatisticsDO.getCurContinuousDays();
        userCheckinStatisticsDO.setTotalDays(userCheckinStatisticsDO.getTotalDays().intValue() + insertResignCount.intValue());
        userCheckinStatisticsDO.setCurContinuousDays(newCurContinuousDays.intValue());
        userCheckinStatisticsDO.setCurContinuousStartDate(startDate);


        LambdaUpdateWrapper<UserCheckinStatisticsDO> updateWrapper = new LambdaUpdateWrapper<UserCheckinStatisticsDO>()
                .eq(UserCheckinStatisticsDO::getUserId, userId);


        // 更新签到统计信息
        userCheckinStatisticsMapper.update(userCheckinStatisticsDO, updateWrapper);
    }

    // [startDate, endDate)
    private void insertUserResignCheckinLog(Long userId, LocalDate startDate, LocalDate endDate, Long stickNo) {

        LocalDate date = startDate;
        int batchCount = 0;
        List<UserCheckinDO> userCheckStickDOs = new ArrayList<>();
        while (date.isBefore(endDate)) {
            UserCheckinDO userCheckinDO = new UserCheckinDO();
            // 生成雪花ID
            Long id = IdWorker.getId(userCheckinDO);
            userCheckinDO.setId(id);
            userCheckinDO.setUserId(userId);
            userCheckinDO.setSignDate(date);
            userCheckinDO.setStickNo(stickNo);
            userCheckinDO.setType(CommonConstant.USER_CHECK_IN_RESIGN_TYPE);
            userCheckStickDOs.add(userCheckinDO);
            date = date.plusDays(1);
            batchCount++;
            if (batchCount == CommonConstant.BATCH) {
                userCheckinMapper.addResignCheckinRecord(userCheckStickDOs);
                batchCount = 0;
                userCheckStickDOs.clear();
            }

        }

        if (userCheckStickDOs.size() > 0) {
            userCheckinMapper.addResignCheckinRecord(userCheckStickDOs);
        }
    }


    private void updateUserInfo(Long userId, UserCheckinStatisticsDO userCheckinStatisticsDO) {
        UserDO userDO = userMapper.selectById(userId);

        LambdaQueryWrapper<LevelDO> userLevelWrapper = new LambdaQueryWrapper<>();
        userLevelWrapper.eq(LevelDO::getLevelValue, userDO.getLevelValue());
        LevelDO userLevelDO = levelMapper.selectOne(userLevelWrapper);

        LambdaQueryWrapper<LevelDO> levelWrapper = new LambdaQueryWrapper<>();
        levelWrapper.eq(LevelDO::getLevelValue, CommonConstant.ACCUMULATE_CHECK_IN_TYPE_MAX_LEVEL);
        LevelDO accumulateLevel = levelMapper.selectOne(levelWrapper);

        // 如果还没到累计签到的最大等级
        if (userDO.getLevelValue() < CommonConstant.ACCUMULATE_CHECK_IN_TYPE_MAX_LEVEL) {

            LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper = new LambdaQueryWrapper<>();
            userCheckinWrapper.eq(UserCheckinDO::getUserId, userId)
                    .gt(UserCheckinDO::getSignDate, userDO.getLevelDate());
            // 计算升级日期之后的累计签到签署
            Long checkinDaysAfterLevelDate = userCheckinMapper.selectCount(userCheckinWrapper);
            Long totalAccumulateCheckinDays = checkinDaysAfterLevelDate + userLevelDO.getCheckInDays();
            // 目前升级先看累加签到
            if (totalAccumulateCheckinDays < accumulateLevel.getCheckInDays()) {
                LevelDO level = levelMapper.getLevel(CommonConstant.USER_CHECKIN_TYPE_ACCUMULATE, userCheckinStatisticsDO.getTotalDays());
                if (level.getLevelValue() > userDO.getLevelValue()) {
                    userDO.setLevelValue(level.getLevelValue());
                    userDO.setLevelName(level.getLevelName());
                    LocalDate levelDate = userCheckinMapper.getLatestSignDateWithDays(userId, level.getCheckInDays() - userLevelDO.getCheckInDays(), userDO.getLevelDate());
                    userDO.setLevelDate(levelDate);

                    // 修改补签卡
                    userDO.setHasSignCard(0);
                    userMapper.updateById(userDO);
                    return;
                }
            } else {
                // 累计签到天数已经达到最大等级
                userDO.setLevelValue(accumulateLevel.getLevelValue());
                userDO.setLevelName(accumulateLevel.getLevelName());
                LocalDate levelDate = userCheckinMapper.getLatestSignDateWithDays(userId,accumulateLevel.getCheckInDays()- userLevelDO.getCheckInDays(),userDO.getLevelDate());
                // 设置升级到最大累计签到天数的日期
                userDO.setLevelDate(levelDate);
            }
        }

        // 获取用户的当前连续签到的其实日期
        LocalDate curContinuousStartDate = userCheckinStatisticsDO.getCurContinuousStartDate();
        // 当前连续签到是否和用户升级日期连起来了
        boolean isContinuousFromLevelDate = ChronoUnit.DAYS.between(userDO.getLevelDate(), curContinuousStartDate) <= 1;
        Long totalContinuousCheckinDays = isContinuousFromLevelDate ?
                userLevelDO.getCheckInDays() + ChronoUnit.DAYS.between(userDO.getLevelDate(), LocalDate.now()) : userCheckinStatisticsDO.getCurContinuousDays();
        // 已达到累计签到的最大等级
        LevelDO continuousLevelDO = levelMapper.getLevel(CommonConstant.USER_CHECKIN_TYPE_CONTINUOUS, totalContinuousCheckinDays.intValue());
        if (continuousLevelDO == null) {
            // 已到达最高等级
            // 等级没更新，将用户的补签卡置为0
            UserDO user = new UserDO();
            user.setId(userId);
            user.setHasSignCard(0);
            userMapper.updateById(user);
            return;
        }

        // 未达到最大等级, 但是升级了
        if (userDO.getLevelValue() < continuousLevelDO.getLevelValue()) {
            userDO.setLevelValue(continuousLevelDO.getLevelValue());
            userDO.setLevelName(continuousLevelDO.getLevelName());
            LocalDate levelDate = userCheckinStatisticsDO.getCurContinuousStartDate().plusDays(continuousLevelDO.getCheckInDays() - 1);
            if (isContinuousFromLevelDate) {
                levelDate = userDO.getLevelDate().plusDays(continuousLevelDO.getCheckInDays() - 1);
            }
            userDO.setLevelDate(levelDate);

            // 修改补签卡
            userDO.setHasSignCard(0);
            userMapper.updateById(userDO);
            return;
        }


        // 等级没更新，将用户的补签卡置为0
        UserDO user = new UserDO();
        user.setId(userId);
        user.setHasSignCard(0);
        userMapper.updateById(user);
    }
}
