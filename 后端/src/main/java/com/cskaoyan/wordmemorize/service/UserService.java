package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserRemindCommand;

import java.util.Set;

public interface UserService {

    // APP端获取用户信息
    UserDTO getUserInfo(Long userId);

    // APP端获取用户带升级信息
    NextLevelDTO getUserNextDTO(Long userId);

    // APP端获取用户等级信息(包括已达到等级和未达到等级)
    DisplayUserLevelDTO getUserLevelInfo(Long userId);

    // APP端更新用户每天计划学习的单词量
    void updateVocCountOfDay(Long userId, Integer count);

    // APP端更新用户的学习提醒时间
    void updateUserRemind(UserRemindCommand command);
    // APP端更获取用户的提醒信息
    UserRemindDTO getUserRemind(Long userId);

    // APP端发送消息提醒(在定时任务中调用该方法将待发送的提醒信息放入延迟队列)
    void messageRemind();
    // APP端发获取用户的单词上限变化明细，如果未传分页信息则返回所有
    PageDTO<DisplayUserVocBoundLogDTO> getUserVocBoundLog(PageRequest pageRequest, Long userId);

    // APP端获取用户的单词上限统计信息
    UserVocBoundStatisticsDTO getUserVocBoundStatistics(Long userId);


    // APP端获取用户我的页面展示的信息
    UserPageDTO getUserPage(Long userId);



    /*
        APP鉴权在StpInterfaceImpl的getPrivilegeCodesList中调用.
     */
    Set<String> getPrivilegeCodesList(Long userId);
}
