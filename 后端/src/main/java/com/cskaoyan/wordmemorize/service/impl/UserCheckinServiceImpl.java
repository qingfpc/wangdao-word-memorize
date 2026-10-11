package com.cskaoyan.wordmemorize.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.constant.UserVocBoundEnum;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.dev33.satoken.SaManager.log;

@Service
public class UserCheckinServiceImpl implements UserCheckinService {

    @Autowired
    UserCheckinStatisticsMapper userCheckinStatisticsMapper;
    @Autowired
    ReviewConverter reviewConverter;
    @Autowired
    UserCheckinMapper userCheckinMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserVocBoundLogMapper userVocBoundLogMapper;
    @Autowired
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;
    @Autowired
    LevelMapper levelMapper;
    // APP端下载打卡数据(如果分页参数没有传的话，就返回所有打卡数据)
    @Override
    public PageDTO<UserCheckinDTO> getCheckInData(PageRequest pageRequest, Long userId) {
        LambdaQueryWrapper<UserCheckinDO> queryWrapper = new LambdaQueryWrapper<UserCheckinDO>()
                .eq(UserCheckinDO::getUserId, userId)
                .orderByDesc(UserCheckinDO::getSignDate);

        List<UserCheckinDO> checkinList;
        long total;

        if (pageRequest.getPageNum() == null || pageRequest.getCount() == null) {
            // 不分页，查询所有
            checkinList = userCheckinMapper.selectList(queryWrapper);
            total = checkinList.size();
        } else {
            // 分页
            Page<UserCheckinDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
            Page<UserCheckinDO> resultPage = userCheckinMapper.selectPage(page, queryWrapper);
            checkinList = resultPage.getRecords();
            total = resultPage.getTotal();
        }

        return reviewConverter.userCheckinPage2PageDTO(checkinList, total);
    }


    // APP端打卡
    /**
     * APP端用户签到功能实现
     * @param command 签到命令对象，包含签到日期和类型
     * @return 签到统计信息DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCheckinStatisticsDTO checkin(UserCheckinCommand command) {
        // 1. 参数校验（不再校验userId）
        validateCheckinCommand(command);

        // 2. 获取用户ID（唯一来源）
        Long userId = StpKit.USER.getLoginIdAsLong();
        if (userId == null) {
            throw new BusinessException("用户未登录", ResultCodeEnum.LOGIN_AUTH.getCode());
        }

        // 3. 检查是否已经签到过
        LocalDate signDate = command.getSignDate();
        int type = command.getType();
        checkAlreadyCheckedIn(userId, signDate, type);

        // 4. 获取用户信息
        UserDO user = getUser(userId);
        Integer currentLevel = user.getLevelValue();
        LocalDate levelUpDate = user.getLevelDate();

        // 5. 处理签到记录
        Long stickNo = processCheckinRecord(userId, signDate, type);

        // 6. 更新签到统计信息
        UserCheckinStatisticsDO statsDO = updateCheckinStatistics(userId, signDate, stickNo);

        // 7. 处理单词上限奖励（包含日志记录）
        processVocBoundReward(userId, statsDO);

        // 8. 处理用户等级升级
        processLevelUpgrade(userId, user, currentLevel, levelUpDate, statsDO);

        // 9. 构建返回DTO
        return buildCheckinStatisticsDTO(statsDO);
    }

    /**
     * 验证签到命令参数
     * @param command 签到命令对象
     */
    private void validateCheckinCommand(UserCheckinCommand command) {
        if (command == null) {
            throw new BusinessException("请求参数不能为空", ResultCodeEnum.PARAM_ERROR.getCode());
        }

        // 只校验type和signDate
        if (command.getSignDate() == null) {
            throw new BusinessException("签到日期不能为空", ResultCodeEnum.PARAM_ERROR.getCode());
        }

        if (command.getType() != CommonConstant.USER_CHECK_IN_NORMAL_TYPE &&
                command.getType() != CommonConstant.USER_CHECK_IN_RESIGN_TYPE) {
            throw new BusinessException("无效的签到类型", ResultCodeEnum.PARAM_ERROR.getCode());
        }
    }

    /**
     * 检查用户签到合法性
     * @param userId 用户ID
     * @param signDate 签到日期
     * @param type 签到类型（1普通签到，2补签）
     */
    private void checkAlreadyCheckedIn(Long userId, LocalDate signDate, int type) {
        // 参数校验
        Objects.requireNonNull(signDate, "签到日期不能为空");

        LocalDate today = LocalDate.now();
        log.debug("签到校验: user={}, date={}, type={}", userId, signDate, type);

        // 1. 拒绝未来日期
        if (signDate.isAfter(today)) {
            throw new BusinessException(ResultCodeEnum.CHECK_IN_DATE_NOT_TODAY);
        }

        // 2. 普通签到必须当天
        if (type == CommonConstant.USER_CHECK_IN_NORMAL_TYPE && !signDate.equals(today)) {
            throw new BusinessException(ResultCodeEnum.CHECK_IN_DATE_NOT_TODAY);
        }

        // 3. 检查重复签到（精确到年月日）
        QueryWrapper<UserCheckinDO> query = new QueryWrapper<>();
        query.eq("user_id", userId)
                .apply("DATE(sign_date) = DATE('" + signDate + "')"); // 确保日期比较准确

        if (userCheckinMapper.exists(query)) {
            throw new BusinessException(ResultCodeEnum.CHECK_IN_DATE_DUPLICATE);
        }
    }

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户DO对象
     */
    private UserDO getUser(Long userId) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在", ResultCodeEnum.FAIL.getCode());
        }
        return user;
    }

    /**
     * 处理签到记录
     * @param userId 用户ID
     * @param signDate 签到日期
     * @param type 签到类型
     * @return 签到段号
     */
    private Long processCheckinRecord(Long userId, LocalDate signDate, int type) {
        // 获取昨天的签到记录
        LocalDate yesterday = signDate.minusDays(1);
        UserCheckinDO yesterdayCheckin = getCheckinRecord(userId, yesterday);

        // 确定签到段号
        Long stickNo = yesterdayCheckin != null ? yesterdayCheckin.getStickNo() : null;
        if (stickNo == null) {
            // 新连续签到段，使用新ID作为段号
            stickNo = IdWorker.getId();
        }

        // 创建签到记录
        UserCheckinDO checkin = new UserCheckinDO();
        checkin.setUserId(userId);
        checkin.setSignDate(signDate);
        checkin.setType(type);
        checkin.setStickNo(stickNo);
        userCheckinMapper.insert(checkin);

        return stickNo;
    }

    /**
     * 获取用户签到记录
     * @param userId 用户ID
     * @param date 查询日期
     * @return 签到记录DO对象
     */
    private UserCheckinDO getCheckinRecord(Long userId, LocalDate date) {
        QueryWrapper<UserCheckinDO> query = new QueryWrapper<>();
        query.eq("user_id", userId).eq("sign_date", date);
        return userCheckinMapper.selectOne(query);
    }

    /**
     * 更新签到统计信息
     * @param userId 用户ID
     * @param signDate 签到日期
     * @param stickNo 签到段号
     * @return 更新后的签到统计信息DO对象
     */
    private UserCheckinStatisticsDO updateCheckinStatistics(Long userId, LocalDate signDate, Long stickNo) {
        // 获取或创建统计记录
        QueryWrapper<UserCheckinStatisticsDO> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        UserCheckinStatisticsDO statsDO = userCheckinStatisticsMapper.selectOne(query);

        if (statsDO == null) {
            statsDO = new UserCheckinStatisticsDO();
            statsDO.setUserId(userId);
            statsDO.setTotalDays(0);
            statsDO.setMaxContinuousDays(0);
            statsDO.setCurContinuousDays(0);
            statsDO.setTotalTime(0L);
            statsDO.setCurContinuousStartDate(signDate);
            userCheckinStatisticsMapper.insert(statsDO);
        }

        // 更新总签到天数
        statsDO.setTotalDays(statsDO.getTotalDays() + 1);

        // 计算连续签到天数
        LocalDate yesterday = signDate.minusDays(1);
        UserCheckinDO yesterdayCheckin = getCheckinRecord(userId, yesterday);

        if (yesterdayCheckin != null && yesterdayCheckin.getStickNo().equals(stickNo)) {
            // 连续签到
            statsDO.setCurContinuousDays(statsDO.getCurContinuousDays() + 1);
        } else {
            // 新连续签到段
            statsDO.setCurContinuousDays(1);
            statsDO.setCurContinuousStartDate(signDate);
        }

        // 更新最大连续签到天数
        if (statsDO.getCurContinuousDays() > statsDO.getMaxContinuousDays()) {
            statsDO.setMaxContinuousDays(statsDO.getCurContinuousDays());
            statsDO.setMaxContinuousStartDate(statsDO.getCurContinuousStartDate());
        }

        userCheckinStatisticsMapper.updateById(statsDO);
        return statsDO;
    }

    /**
     * 处理单词上限奖励
     * @param userId 用户ID
     * @param statsDO 签到统计信息
     */
    private void processVocBoundReward(Long userId, UserCheckinStatisticsDO statsDO) {
        // 1. 定义奖励数量
        int rewardAmount = CommonConstant.USER_CHECKIN_VOC_REWARD;

        // 2. 增加单词上限（免费额度）
        increaseVocBound(userId, rewardAmount);

        // 3. 记录单词上限变更日志
        UserVocBoundLogDO log = UserVocBoundLogDO.builder()
                .userId(userId)
                .type(UserVocBoundEnum.USER_VOC_BOUND_LOG_TYPE_CHECKIN.getValue())
                .logDate(LocalDate.now())
                .count(rewardAmount)
                .description(UserVocBoundEnum.USER_VOC_BOUND_LOG_TYPE_CHECKIN.getDescription())
                .build();
        userVocBoundLogMapper.insert(log);
    }

    /**
     * 构建签到统计信息DTO
     * @param statsDO 签到统计信息DO对象
     * @return 签到统计信息DTO对象
     */
    private UserCheckinStatisticsDTO buildCheckinStatisticsDTO(UserCheckinStatisticsDO statsDO) {
        UserCheckinStatisticsDTO dto = new UserCheckinStatisticsDTO();
        dto.setId(statsDO.getId().toString());
        dto.setUserId(statsDO.getUserId().toString());
        dto.setTotalDays(statsDO.getTotalDays());
        dto.setMaxContinuousDays(statsDO.getMaxContinuousDays());
        dto.setCurContinuousDays(statsDO.getCurContinuousDays());
        dto.setMaxContinuousStartDate(statsDO.getMaxContinuousStartDate());
        dto.setCurContinuousStartDate(statsDO.getCurContinuousStartDate());
        dto.setTotalTime(String.valueOf(statsDO.getTotalTime() != null ? statsDO.getTotalTime() / (60 * 1000) : 0));
        return dto;
    }

    /**
     * 处理用户等级升级逻辑
     * @param userId 用户ID
     * @param user 用户DO对象
     * @param currentLevel 当前等级
     * @param levelUpDate 上次升级日期
     * @param statsDO 签到统计信息
     */
    private void processLevelUpgrade(Long userId, UserDO user, Integer currentLevel,
                                     LocalDate levelUpDate, UserCheckinStatisticsDO statsDO) {
        // 1. 参数校验
        if (userId == null || user == null || !userId.equals(user.getId())) {
            throw new BusinessException("用户参数错误", ResultCodeEnum.PARAM_ERROR.getCode());
        }

        // 2. 获取所有有效等级（按等级值升序）
        List<LevelDO> allLevels = levelMapper.selectList(
                new LambdaQueryWrapper<LevelDO>()
                        .select(LevelDO::getId, LevelDO::getLevelName, LevelDO::getLevelValue,
                                LevelDO::getCheckInDays, LevelDO::getCheckInType,
                                LevelDO::getUnlockPrice, LevelDO::getDescription, LevelDO::getIsDeleted)
                        .eq(LevelDO::getIsDeleted, 0)
                        .orderByAsc(LevelDO::getLevelValue)
        );
        if (CollectionUtils.isEmpty(allLevels)) return;

        // 3. 准备签到数据
        int totalDays = statsDO.getTotalDays();
        int curContinuousDays = statsDO.getCurContinuousDays();
        LocalDate continuousStartDate = statsDO.getCurContinuousStartDate();

        // 4. 判断连续签到段是否与上次升级日期衔接
        boolean continuousConnected = continuousStartDate != null && levelUpDate != null
                && !continuousStartDate.isAfter(levelUpDate.plusDays(1));

        // 5. 遍历等级配置，检查升级条件
        for (LevelDO level : allLevels) {
            if (!isEligibleForUpgrade(level, currentLevel, totalDays, curContinuousDays, continuousConnected)) {
                continue;
            }

            // 6. 执行升级
            updateUserLevel(userId, level);
        }
    }

    /**
     * 检查是否满足升级条件
     */
    private boolean isEligibleForUpgrade(LevelDO level, int currentLevel,
                                         int totalDays, int curContinuousDays,
                                         boolean continuousConnected) {
        // 跳过不满足条件的等级
        if (level.getLevelValue() == null || level.getLevelValue() <= currentLevel) {
            return false;
        }

        int requiredDays = level.getCheckInDays();
        int checkInType = level.getCheckInType();

        if (checkInType == 1) { // 连续签到类型
            int actualContinuousDays = continuousConnected
                    ? curContinuousDays + (level.getLevelValue() - currentLevel - 1)
                    : curContinuousDays;
            return actualContinuousDays >= requiredDays;
        } else { // 累计签到类型
            return (totalDays - currentLevel) >= (requiredDays - currentLevel);
        }
    }

    /**
     * 更新用户等级信息
     */
    private void updateUserLevel(Long userId, LevelDO level) {
        UserDO updateUser = new UserDO();
        updateUser.setId(userId);
        updateUser.setLevelValue(level.getLevelValue());
        updateUser.setLevelName(level.getLevelName()); // 同步更新等级名称
        updateUser.setLevelDate(LocalDate.now());
        userMapper.updateById(updateUser);
    }


    /**
     * 增加用户单词上限
     * @param userId 用户ID
     * @param amount 要增加的数量
     */
    private void increaseVocBound(Long userId, int amount) {
        // 1. 查询当前统计记录
        QueryWrapper<UserVocBoundStatisticsDO> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        UserVocBoundStatisticsDO stats = userVocBoundStatisticsMapper.selectOne(query);

        // 2. 更新或创建记录
        if (stats == null) {
            stats = UserVocBoundStatisticsDO.builder()
                    .userId(userId)
                    .free(amount)  // 签到奖励属于免费额度
                    .total(amount)
                    .available(amount)
                    .occupied(0)
                    .pay(0)
                    .exchange(0)
                    .build();
            userVocBoundStatisticsMapper.insert(stats);
        } else {
            // 更新免费额度和总量
            stats.setFree(stats.getFree() + amount);
            stats.setTotal(stats.getTotal() + amount);
            stats.setAvailable(stats.getAvailable() + amount);
            userVocBoundStatisticsMapper.updateById(stats);
        }
    }

//    /**
//     * 记录等级变更日志
//     * @param userId 用户ID
//     * @param oldLevel 旧等级
//     * @param newLevel 新等级
//     */
//    private void recordLevelUpgradeLog(Long userId, Integer oldLevel, Integer newLevel) {
//        UserVocBoundLogDO log = new UserVocBoundLogDO();
//        log.setUserId(userId);
//        log.setType(UserVocBoundEnum.USER_VOC_BOUND_LOG_TYPE_CHECKIN.getValue());
//        log.setLogDate(LocalDate.now());
//        log.setCount(0); // 等级变更不涉及单词数量变化
//        log.setDescription(String.format("等级从%s升级到%s", oldLevel, newLevel));
//        userVocBoundLogMapper.insert(log);
//    }

    // APP端获取用户打卡数据
    @Override
    public UserCheckinStatisticsDTO getUserCheckinStatistics(Long userId) {
        // =============================================
        // 第一步：获取用户的基础统计数据
        // =============================================
        // 创建查询条件：根据用户ID查询统计信息
        QueryWrapper<UserCheckinStatisticsDO> statsQuery = new QueryWrapper<>();
        statsQuery.eq("user_id", userId);

        // 执行查询，获取用户的签到统计数据
        UserCheckinStatisticsDO statsDO = userCheckinStatisticsMapper.selectOne(statsQuery);

        // 如果查询不到统计记录，创建一个空的DTO返回
        if (statsDO == null) {
            UserCheckinStatisticsDTO emptyDTO = new UserCheckinStatisticsDTO();
            emptyDTO.setId("0");  // 默认ID为0
            emptyDTO.setUserId(userId.toString());  // 设置用户ID
            emptyDTO.setTotalDays(0);  // 总签到天数默认为0
            emptyDTO.setMaxContinuousDays(0);  // 最大连续签到天数默认为0
            emptyDTO.setCurContinuousDays(0);  // 当前连续签到天数默认为0
            emptyDTO.setMaxContinuousStartDate(null);  // 最大连续签到起始日期为空
            emptyDTO.setCurContinuousStartDate(null);  // 当前连续签到起始日期为空
            emptyDTO.setTotalTime("0");  // 总学习时间默认为0分钟
            return emptyDTO;
        }

        // =============================================
        // 第二步：准备日期信息用于后续判断
        // =============================================
        LocalDate today = LocalDate.now();  // 获取今天的日期
        LocalDate yesterday = today.minusDays(1);  // 获取昨天的日期

        // =============================================
        // 第三步：查询用户最后一次签到记录
        // =============================================
        QueryWrapper<UserCheckinDO> lastCheckinQuery = new QueryWrapper<>();
        lastCheckinQuery.eq("user_id", userId)  // 按用户ID筛选
                .orderByDesc("sign_date")  // 按签到日期降序排序
                .last("LIMIT 1");  // 只取最近的一条记录

        // 执行查询，获取用户最后一次签到记录
        UserCheckinDO lastCheckin = userCheckinMapper.selectOne(lastCheckinQuery);

        // 提取签到日期，如果记录不存在则为null
        LocalDate lastCheckinDate = lastCheckin != null ? lastCheckin.getSignDate() : null;

        // 如果用户从未签到过，返回基本统计信息（签到天数为0）
        if (lastCheckinDate == null) {
            UserCheckinStatisticsDTO emptyDTO = new UserCheckinStatisticsDTO();
            emptyDTO.setId(statsDO.getId().toString());
            emptyDTO.setUserId(statsDO.getUserId().toString());
            emptyDTO.setTotalDays(statsDO.getTotalDays());
            emptyDTO.setMaxContinuousDays(statsDO.getMaxContinuousDays());
            emptyDTO.setCurContinuousDays(0);  // 当前连续签到天数为0
            emptyDTO.setMaxContinuousStartDate(statsDO.getMaxContinuousStartDate());
            emptyDTO.setCurContinuousStartDate(null);  // 当前连续签到起始日期为空
            // 将毫秒转换为分钟：如果totalTime为null则视为0，否则除以60000转换为分钟
            emptyDTO.setTotalTime(String.valueOf(statsDO.getTotalTime() != null ? statsDO.getTotalTime() / (60 * 1000) : 0));
            return emptyDTO;
        }

        // =============================================
        // 第四步：判断是否需要重新计算连续签到天数
        // =============================================
        boolean needRecalculate = false;
        int currentStreak = statsDO.getCurContinuousDays();  // 从统计记录中获取当前连续天数
        LocalDate currentStartDate = statsDO.getCurContinuousStartDate();  // 获取当前连续签到的起始日期

        // 情况1：最后一次签到既不是今天也不是昨天，说明数据可能过期
        if (!lastCheckinDate.equals(today) && !lastCheckinDate.equals(yesterday)) {
            needRecalculate = true;
        }
        // 情况2：统计的起始日期与根据连续天数推算的起始日期不一致
        else if (!lastCheckinDate.minusDays(currentStreak - 1).equals(currentStartDate)) {
            needRecalculate = true;
        }

        // =============================================
        // 第五步：如果需要，重新计算连续签到天数
        // =============================================
        if (needRecalculate) {
            int streak = 0;  // 初始化连续签到天数
            LocalDate checkDate = lastCheckinDate;  // 从最后一次签到日期开始检查

            // 循环检查连续的签到记录
            while (true) {
                QueryWrapper<UserCheckinDO> checkQuery = new QueryWrapper<>();
                checkQuery.eq("user_id", userId)  // 按用户ID筛选
                        .eq("sign_date", checkDate);  // 按具体日期筛选

                // 检查该日期是否有签到记录
                boolean exists = userCheckinMapper.exists(checkQuery);

                // 如果没有签到记录，终止循环
                if (!exists) break;

                streak++;  // 增加连续签到天数
                checkDate = checkDate.minusDays(1);  // 检查前一天
            }

            // 更新当前连续签到信息
            currentStreak = streak;
            currentStartDate = lastCheckinDate.minusDays(streak - 1);

            // 更新统计记录
            statsDO.setCurContinuousDays(currentStreak);
            statsDO.setCurContinuousStartDate(currentStartDate);

            // 如果当前连续签到天数超过历史最大值，更新最大值
            if (currentStreak > statsDO.getMaxContinuousDays()) {
                statsDO.setMaxContinuousDays(currentStreak);
                statsDO.setMaxContinuousStartDate(currentStartDate);
            }

            // 将更新后的统计信息保存到数据库
            userCheckinStatisticsMapper.updateById(statsDO);
        }

        // =============================================
        // 第六步：构建返回的DTO对象
        // =============================================
        UserCheckinStatisticsDTO dto = new UserCheckinStatisticsDTO();
        dto.setId(statsDO.getId().toString());  // 设置统计记录ID
        dto.setUserId(statsDO.getUserId().toString());  // 设置用户ID
        dto.setTotalDays(statsDO.getTotalDays());  // 设置总签到天数
        dto.setMaxContinuousDays(statsDO.getMaxContinuousDays());  // 设置最大连续签到天数
        dto.setCurContinuousDays(currentStreak);  // 设置当前连续签到天数
        dto.setMaxContinuousStartDate(statsDO.getMaxContinuousStartDate());  // 设置最大连续签到起始日期
        dto.setCurContinuousStartDate(currentStartDate);  // 设置当前连续签到起始日期
        // 将总学习时间从毫秒转换为分钟（1分钟=60000毫秒）
        dto.setTotalTime(String.valueOf(statsDO.getTotalTime() != null ? statsDO.getTotalTime() / (60 * 1000) : 0));

        return dto;
    }


    /*
         APP端获取用户签到统计数据，注意:
         1. 连续签到是指从今天或者昨天(今天可能尚未打卡)向前计算的连续打卡天数
         2. 如果用户最近一次的连续签到不是今天或者昨天，那么最近连续签到天数为0
     */
    @Override
    public UserCheckinStatisticsDTO getUserContinuousCheckinInfo(Long userId) {
        return null;
    }

    //todo:补签
    @Override
    @Transactional
    public void userResignCard(Long userId) {

    }
}
