package com.cskaoyan.wordmemorize.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.sms.SmsSender;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.constant.UserVocBoundEnum;
import com.cskaoyan.wordmemorize.converter.LoginConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.app.AppLoginDTO;
import com.cskaoyan.wordmemorize.dto.app.UserDTO;
import com.cskaoyan.wordmemorize.request.PhoneNumberLoginCommand;
import com.cskaoyan.wordmemorize.service.LoginService;
import com.cskaoyan.wordmemorize.service.UserService;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class LoginServiceImpl implements LoginService {



    @Resource
    UserMapper userMapper;

    @Resource
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;

    @Resource
    UserCheckinStatisticsMapper userCheckinStatisticsMapper;

    @Resource
    UserRemindMapper userRemindMapper;

    @Resource
    UserVocBoundLogMapper userVocBoundLogMapper;



    @Resource
    UserService userService;

    @Override
    public AppLoginDTO phoneNumberLogin(PhoneNumberLoginCommand command, String ipAddr) {

        LambdaQueryWrapper<UserDO> userPhoneWrapper = new LambdaQueryWrapper<>();
        userPhoneWrapper.eq(UserDO::getPhoneNumber, command.getPhoneNumber())
                .select(UserDO::getId);
        // 判断用户是否存在
        UserDO userDO = userMapper.selectOne(userPhoneWrapper);
        Boolean isFirst = false;
        if (userDO == null) {
            isFirst = true;
           // 保存用户信息，相当于注册
            userDO = UserDO.builder()
                    .thirdPartyName(command.getDisplayName())
                    .email(command.getEmail())
                    .headImg(command.getPhotoUrl())
                    .phoneNumber(command.getPhoneNumber())
                    .lastLoginIp(ipAddr)
                    .nickName(getName())
                    .lastLoginTime(LocalDateTime.now())
                    // 默认等级
                    .levelName(CommonConstant.REGISTER_LEVEL_NAME)
                    .levelValue(CommonConstant.REGISTER_LEVEL_VALUE)
                    .levelDate(LocalDate.now())
                    // 默认无补签卡
                    .hasSignCard(0)
                    // 默认单词上限
                    // 默认规划的每天单词量
                    .vocCountOfDay(CommonConstant.REGISTER_VOC_OF_DAY)
                    .build();
            // 保存用户信息
            userMapper.insert(userDO);

            // vocBoundStatistics`
            UserVocBoundStatisticsDO userVocBoundStatisticsDO = UserVocBoundStatisticsDO.builder()
                    .userId(userDO.getId())
                    .total(CommonConstant.REGISTER_VOC_BOUND)
                    .available(CommonConstant.REGISTER_VOC_BOUND)
                    .pay(0)
                    .free(0)
                    .occupied(0)
                    .exchange(0)
                    .build();
            userVocBoundStatisticsMapper.insert(userVocBoundStatisticsDO);

            // userCheckinStatistics
            UserCheckinStatisticsDO userCheckinStatisticsDO = UserCheckinStatisticsDO
                    .builder()
                    .userId(userDO.getId())
                    .totalDays(0)
                    .curContinuousDays(0)
                    .maxContinuousDays(0)
                    .totalTime(0L)
                    .build();
            userCheckinStatisticsMapper.insert(userCheckinStatisticsDO);

            // remind
            UserRemindDO userRemindDO = UserRemindDO.builder()
                    .phoneNumber(userDO.getPhoneNumber())
                    .userId(userDO.getId())
                    .userName(userDO.getNickName())
                    .messageStatus(CommonConstant.DEFAULT_REMIND_MESSAGE_STATUS)
                    .remindTime(LocalTime.parse(CommonConstant.DEFAULT_REMIND_TIME))
                    .build();
            userRemindMapper.insert(userRemindDO);

            // vocBoundLog
            UserVocBoundLogDO userVocBoundLogDO = UserVocBoundLogDO.builder()
                    .logDate(LocalDate.now())
                    .userId(userDO.getId())
                    .count(CommonConstant.REGISTER_VOC_BOUND)
                    .type(UserVocBoundEnum.USER_VOC_BOUND_LOG_REGISTER.getValue())
                    .description(UserVocBoundEnum.USER_VOC_BOUND_LOG_REGISTER.getDescription())
                    .build();
            userVocBoundLogMapper.insert(userVocBoundLogDO);
        }

        // 登录
        StpKit.USER.login(userDO.getId());

        // 向session中放入用户当前等级权限
        SaSession session = StpKit.USER.getSession();

        UserDTO userInfo = userService.getUserInfo(userDO.getId());
        session.set(CommonConstant.LOGIN_USER_INFO, userInfo);
        // 更新登录时间
        userDO.setLastLoginTime(LocalDateTime.now());
        userDO.setLastLoginIp(ipAddr);
        userMapper.updateById(userDO);

        AppLoginDTO appLoginDTO = new AppLoginDTO();
        appLoginDTO.setToken(StpUtil.getTokenValue());
        appLoginDTO.setIsFirst(isFirst);
        return appLoginDTO;
    }



    protected String getName() {
        Long userCount = userMapper.selectCount(null);
        return CommonConstant.USER_NICK_NAME_PREFIX + (userCount + 1);
    }

}
