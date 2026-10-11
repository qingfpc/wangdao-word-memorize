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


    @Value("${user.remind.description}")
    String messageRemindDescription;


    @Override
    public UserDTO getUserInfo(Long userId) {
        // 查询基本信息
        UserDO userDO = userMapper.getUserInfo(userId);
        // 查询等级对应的特权码

        return userConverter.userDO2DTO(userDO);
    }

    @Override
    public NextLevelDTO getUserNextDTO(Long userId) {
        return null;
    }

    @Override
    public DisplayUserLevelDTO getUserLevelInfo(Long userId) {

        return null;
    }

    @Override
    public void updateVocCountOfDay(Long userId, Integer count) {

    }

    @Override
    public void updateUserRemind(UserRemindCommand command) {

    }



    @Override
    public UserRemindDTO getUserRemind(Long userId) {
       return null;
    }


    @Override
    public void messageRemind() {


    }

    @Override
    public PageDTO<DisplayUserVocBoundLogDTO> getUserVocBoundLog(PageRequest pageRequest, Long userId) {
       return null;
    }

    @Override
    public UserVocBoundStatisticsDTO getUserVocBoundStatistics(Long userId) {
        return null;
    }

    @Override
    public UserPageDTO getUserPage(Long userId) {
        return null;
    }

}
