package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.converter.UserConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserRemindCommand;
import com.cskaoyan.wordmemorize.service.UserCheckinService;
import com.cskaoyan.wordmemorize.service.UserService;
import com.cskaoyan.wordmemorize.sms.SmsDelayMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    UserConverter userConverter;

    @Resource
    UserRemindMapper userRemindMapper;

    @Resource
    UserCheckinStatisticsMapper userCheckinStatisticsMapper;

    @Resource
    UserCheckinMapper userCheckinMapper;

    @Resource
    UserCheckinService userCheckinService;

    @Resource
    SpelExpressionParser parser;


    @Resource
    UserRevStatisticsMapper userRevStatisticsMapper;

    @Resource
    UserRemindMessageMapper userRemindMessageMapper;

    @Resource
    UserVocBoundLogMapper userVocBoundLogMapper;

    @Resource
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;

    @Resource
    GoodsMapper goodsMapper;


    @Resource(name = "sms-delay-queue")
    DelayQueue<SmsDelayMessage> smsQueue;

    @Autowired
    LevelMapper levelMapper;


    @Override
    public UserDTO getUserInfo(Long userId) {
        // 查询基本信息
        UserDO userDO = userMapper.getUserInfo(userId);
        // 查询等级对应的特权码

        LambdaQueryWrapper<UserVocBoundStatisticsDO> userVocBoundStatisticWrapper = new LambdaQueryWrapper<>();
        userVocBoundStatisticWrapper
                .eq(UserVocBoundStatisticsDO::getUserId, userId)
                .select(UserVocBoundStatisticsDO::getAvailable);
        //UserVocBoundStatisticsDO userVocBoundStatisticsDO = userVocBoundStatisticsMapper.selectOne(userVocBoundStatisticWrapper);
        // 设置当前用户可用的单词数量
        //userDO.setVocBound(userVocBoundStatisticsDO.getAvailable());
        return userConverter.userDO2DTO(userDO);
    }

    @Override
    public NextLevelDTO getUserNextDTO(Long userId) {

        UserDO userDO = userMapper.selectById(userId);
        Integer levelValue = userDO.getLevelValue();
        LocalDate levelDate = userDO.getLevelDate();
        // 根据levelValue查询当前用户等级对应的已签到天数
        LambdaQueryWrapper<LevelDO> levelWrapper = new LambdaQueryWrapper<>();
        levelWrapper.eq(LevelDO::getLevelValue, levelValue);
        LevelDO levelDO = levelMapper.selectOne(levelWrapper);
        Integer checkInDays = levelDO.getCheckInDays();


        // 查询最大等级，以及下一个等级
        LevelDO nextLevelInfo = userMapper.getNextLevelInfo(userId);
        if (nextLevelInfo == null) {
            return null;
        }

        NextLevelDTO nextLevelDTO = new NextLevelDTO();

        if (nextLevelInfo.getCheckInType() == CommonConstant.USER_CHECKIN_TYPE_CONTINUOUS) {
            nextLevelDTO.setSignType(CommonConstant.USER_CHECKIN_TYPE_CONTINUOUS);
            UserCheckinStatisticsDTO userContinuousCheckinInfo = userCheckinService.getUserContinuousCheckinInfo(userId);
            if (userContinuousCheckinInfo == null) {
                // 说明用户没有签到记录
                nextLevelDTO.setRemainDays(nextLevelInfo.getCheckInDays());
                return nextLevelDTO;
            }
            // 计算升级日期和最近连续签到起始日期间的差值
            long between = ChronoUnit.DAYS.between(levelDate, userContinuousCheckinInfo.getCurContinuousStartDate());
            if (between >= 2) {
                // 如果连续签到的起始日期在升级日期的两天后，那么计算时就不考虑用户升级时已有的连续签到天数
                nextLevelDTO.setRemainDays(nextLevelInfo.getCheckInDays() - userContinuousCheckinInfo.getCurContinuousDays());
                return nextLevelDTO;
            }
            // 在计算时就要考虑用户所在等级所需的连续签到数，所以升级所需连续签到数=用户当前等级已经达到的连续签到 + 从升级日期后的最近连续签到数
            if (between == 1) {
                int alreadyCheckinDays = checkInDays + userContinuousCheckinInfo.getCurContinuousDays();
                nextLevelDTO.setRemainDays(nextLevelInfo.getCheckInDays() - alreadyCheckinDays);
                return nextLevelDTO;
            }

            if (between <= 0) {
                long checkinDaysAfterLevelDays = ChronoUnit.DAYS.between(userContinuousCheckinInfo.getCurContinuousStartDate(), levelDate) + 1;
                long alreadyCheckinDays = checkInDays - checkinDaysAfterLevelDays + userContinuousCheckinInfo.getCurContinuousDays();
                nextLevelDTO.setRemainDays(nextLevelInfo.getCheckInDays() - (int)alreadyCheckinDays);
                return nextLevelDTO;
            }

        }

        // 累计打卡
        // 计算自上一次升级之后的打卡天数
        LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper = new LambdaQueryWrapper<>();
        userCheckinWrapper.eq(UserCheckinDO::getUserId, userId)
                .gt(UserCheckinDO::getSignDate, levelDate);

        Integer totalCheckinDays = userCheckinMapper.selectCount(userCheckinWrapper).intValue();
        log.info("checin days {}, next checkinDays {}", checkInDays, nextLevelInfo.getCheckInDays());
        nextLevelDTO.setSignType(CommonConstant.USER_CHECKIN_TYPE_ACCUMULATE);
        nextLevelDTO.setRemainDays(nextLevelInfo.getCheckInDays() - totalCheckinDays - checkInDays);
        return nextLevelDTO;
    }

    @Override
    public DisplayUserLevelDTO getUserLevelInfo(Long userId) {

        UserDO userDO = userMapper.selectById(userId);
        Integer nowLevelValue = userDO.getLevelValue();

        List<LevelDO> levelInfo = userMapper.getLevelInfo();
        List<LevelPageDTO> upperLevelInfo = new ArrayList<>();
        List<LevelPageDTO> lowerLevelInfo = new ArrayList<>();

        int size = levelInfo.size();
        for (int i = 0; i < size; i++) {
            LevelDO levelDO = levelInfo.get(i);
            LevelPageDTO levelPageDTO = userConverter.levelDO2LevelPageDTO(levelDO);
            // 查询等级对应的商品
            if (levelDO.getLevelValue() != 0) {
                LambdaQueryWrapper<GoodsDO> goodsWrapper = Wrappers.lambdaQuery(GoodsDO.class)
                        .eq(GoodsDO::getType, 2)
                        .eq(GoodsDO::getValue, levelDO.getLevelValue());
                GoodsDO goodsDO = goodsMapper.selectOne(goodsWrapper);
                if (goodsDO == null) {
                    levelPageDTO.setGoodsId("无此商品");
                } else {
                    levelPageDTO.setGoodsId(goodsDO.getId().toString());
                }

            }

            if (nowLevelValue >= levelDO.getLevelValue()) {
                lowerLevelInfo.add(levelPageDTO);
            } else {
                // 设置商品id
                upperLevelInfo.add(levelPageDTO);
            }
        }

        return new DisplayUserLevelDTO(lowerLevelInfo, upperLevelInfo);
    }

    @Override
    public void updateVocCountOfDay(Long userId, Integer count) {
        UserDO userDO = new UserDO();
        userDO.setId(userId);
        userDO.setVocCountOfDay(count);
        userMapper.updateById(userDO);
    }

    @Override
    public void updateUserRemind(UserRemindCommand command) {
        UserRemindDO userRemindDO = userConverter.userRemindCommand2DO(command);
        LambdaUpdateWrapper<UserRemindDO> userRemindWrapper = new LambdaUpdateWrapper<>();
        userRemindWrapper.eq(UserRemindDO::getUserId, userRemindDO.getUserId())
                .set(UserRemindDO::getRemindTime, userRemindDO.getRemindTime());
        userRemindMapper.update(null, userRemindWrapper);
    }

    @Value("${user.remind.description}")
    String messageRemindDescription;

    @Override
    public UserRemindDTO getUserRemind(Long userId) {
        UserRemindDO userRemindInfo = userRemindMapper.getUserRemindInfo(userId);
        UserRemindDTO userRemindDTO = userConverter.userRemindDO2DTO(userRemindInfo, messageRemindDescription);
        userRemindDTO.setId(userRemindInfo.getId().toString());
        return userRemindDTO;
    }

    @Value("${user.remind.expr}")
    String userRemindExpression;

    @Override
    public void messageRemind() {

        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime after = now.plusMinutes(10);

        // 遍历每一个满足条件的用户
        int page = 1;
        int count = CommonConstant.REMIND_MESSAGE_BATCH;

        long total = 0;
        Expression expression = parser.parseExpression(userRemindExpression);
        ;
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 构造用户提醒查询条件
        LambdaQueryWrapper<UserRemindDO> userRemindWrapper = new LambdaQueryWrapper<>();
        userRemindWrapper
                .ge(UserRemindDO::getRemindTime, now)
                .lt(UserRemindDO::getRemindTime, after)
                .eq(UserRemindDO::getMessageStatus, CommonConstant.REMIND_SYSTEM_STATUS_OPEN);
        while (true) {
            Page<UserRemindDO> selectPage = new Page<>(page, count);
            Page<UserRemindDO> userRemindDOPage = userRemindMapper.selectPage(selectPage, userRemindWrapper);
            List<UserRemindDO> records = userRemindDOPage.getRecords();
            records.forEach(userRemindDO -> {
                // 发送短信
                // 1). 看看今天是否签到了
                if (checkSign(userRemindDO)) {
                    // 已签到，不用通知
                    return;
                }

                // 2). 判断发送短信的条件
                if (!checkSendCondition(userRemindDO, context, expression)) {
                    // 不满足条件
                    return;
                }


                // 满足条件准备添加到延迟队列中
                Integer reviewedCount = getReviewedCount(userRemindDO);
                SmsDelayMessage smsDelayMessage = userConverter.userRemindDO2SmsDelayMessage(userRemindDO, reviewedCount);
                smsQueue.put(smsDelayMessage);

            });

            // 处理
            total += records.size();

            if (total >= userRemindDOPage.getTotal()) {
                break;
            }
            page++;

        }

    }

    @Override
    public PageDTO<DisplayUserVocBoundLogDTO> getUserVocBoundLog(PageRequest pageRequest, Long userId) {
        LambdaQueryWrapper<UserVocBoundLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserVocBoundLogDO::getUserId, userId);

        if (pageRequest.getPageNum() == null) {
            List<UserVocBoundLogDO> userVocBoundLogDOs = userVocBoundLogMapper.selectList(queryWrapper);
            List<DisplayUserVocBoundLogDTO> displayUserVocBoundLogDTOs = userConverter
                    .userVocBoundLogDTOs2DisplayDTOs(userVocBoundLogDOs);
            return userConverter.userVocBoundLogPage2PageDTO(displayUserVocBoundLogDTOs, (long) userVocBoundLogDOs.size());
        }

        Page<UserVocBoundLogDO> pageParam = new Page<>(pageRequest.getPageNum()
                , pageRequest.getCount());
        Page<UserVocBoundLogDO> userVocBoundLogPage = userVocBoundLogMapper.selectPage(pageParam, queryWrapper);
        List<DisplayUserVocBoundLogDTO> displayUserVocBoundLogDTOs = userConverter
                .userVocBoundLogDTOs2DisplayDTOs(userVocBoundLogPage.getRecords());
        return userConverter.userVocBoundLogPage2PageDTO(displayUserVocBoundLogDTOs, userVocBoundLogPage.getTotal());
    }

    @Override
    public UserVocBoundStatisticsDTO getUserVocBoundStatistics(Long userId) {

        LambdaQueryWrapper<UserVocBoundStatisticsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserVocBoundStatisticsDO::getUserId, userId);
        UserVocBoundStatisticsDO userVocBoundLogStaticsDO = userVocBoundStatisticsMapper.selectOne(queryWrapper);

        return userConverter.userVocBoundStatisticsDO2DisplayDTO(userVocBoundLogStaticsDO);
    }

    private Integer getReviewedCount(UserRemindDO userRemindDO) {
        LambdaQueryWrapper<UserRevStatisticsDO> userRevStatisticsWrapper = new LambdaQueryWrapper<>();
        userRevStatisticsWrapper.eq(UserRevStatisticsDO::getUserId, userRemindDO.getUserId())
                .eq(UserRevStatisticsDO::getReviewDate, LocalDate.now());
        UserRevStatisticsDO userRevStatisticsDO
                = userRevStatisticsMapper.selectOne(userRevStatisticsWrapper);
        int vocNum = 0;
        if (userRevStatisticsDO != null) {
            vocNum = userRevStatisticsDO.getTotalCount();
        }

        UserDO userDO = userMapper.selectById(userRemindDO.getUserId());

        return userDO.getVocCountOfDay() - vocNum;

    }

    private boolean checkSendCondition(UserRemindDO userRemindDO, StandardEvaluationContext context, Expression expression) {

        // 查询最近签到天数
        UserCheckinStatisticsDTO userContinuousCheckinInfo = userCheckinService.getUserContinuousCheckinInfo(userRemindDO.getUserId());

        context.setVariable(CommonConstant.REMIND_MESSAGE_CONFIG_EXPR_FIRST_PARAM
                , userContinuousCheckinInfo.getCurContinuousDays());


        // 3) 满足条件，还要控制发送频率
        QueryWrapper<UserRemindMessageDO> userRemindMessageWrapper = new QueryWrapper<>();
        userRemindMessageWrapper
                .select("max(send_date) as send_date")
                .eq("user_Id", userRemindDO.getUserId());
        UserRemindMessageDO userRemindMessageDO = userRemindMessageMapper.selectOne(userRemindMessageWrapper);
        if (userRemindMessageDO == null) {
            context.setVariable(CommonConstant.REMIND_MESSAGE_CONFIG_EXPR_SECOND_PARAM
                    , 6);
            // 发送
            return (boolean) expression.getValue(context);
        }

        long between = ChronoUnit.DAYS.between(userRemindMessageDO.getSendDate(), LocalDate.now());
        context.setVariable(CommonConstant.REMIND_MESSAGE_CONFIG_EXPR_SECOND_PARAM, between);
        return (boolean) expression.getValue(context);
    }

    private boolean checkSign(UserRemindDO userRemindDO) {
        LambdaQueryWrapper<UserCheckinDO> userCheckinWrapper = new LambdaQueryWrapper<>();
        userCheckinWrapper.eq(UserCheckinDO::getUserId, userRemindDO.getUserId())
                .eq(UserCheckinDO::getSignDate, LocalDate.now());
        UserCheckinDO userCheckinDO = userCheckinMapper.selectOne(userCheckinWrapper);
        return userCheckinDO != null;
    }


    @Override
    public UserPageDTO getUserPage(Long userId) {

        UserPageDTO userPageDTO = new UserPageDTO();
        // 获取用户每日规划的单词量，以及补签卡
        UserDO userDO = userMapper.selectById(userId);
        userPageDTO.setVocCountOfDay(userDO.getVocCountOfDay());
        userPageDTO.setReSignCard(userDO.getHasSignCard());

        // 获取提醒时间
        LambdaQueryWrapper<UserRemindDO> userRemindWrapper = new LambdaQueryWrapper<>();
        userRemindWrapper.eq(UserRemindDO::getUserId, userId);
        UserRemindDO userRemindDO = userRemindMapper.selectOne(userRemindWrapper);
        userPageDTO.setRemindTime(userRemindDO.getRemindTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        // 获取单词上限
        LambdaQueryWrapper<UserVocBoundStatisticsDO> userVocBoundStatisticsWrapper = new LambdaQueryWrapper<>();
        userVocBoundStatisticsWrapper.eq(UserVocBoundStatisticsDO::getUserId, userId);
        UserVocBoundStatisticsDO userVocBoundStatisticsDO
                = userVocBoundStatisticsMapper.selectOne(userVocBoundStatisticsWrapper);

        userPageDTO.setTotalVocBound(userVocBoundStatisticsDO.getTotal());
        userPageDTO.setPayVocBound(userVocBoundStatisticsDO.getPay());
        userPageDTO.setFreeVocBound(userVocBoundStatisticsDO.getFree());


        return userPageDTO;
    }
}
