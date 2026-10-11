package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinStatisticsDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserCheckinCommand;

public interface UserCheckinService {

    // APP端下载打卡数据(如果分页参数没有传的话，就返回所有打卡数据)
    PageDTO<UserCheckinDTO> getCheckInData(PageRequest pageRequest, Long userId);


    // APP端打卡
    UserCheckinStatisticsDTO checkin(UserCheckinCommand command);

    // APP端获取用户打卡数据(完整的签到统计数据)
    UserCheckinStatisticsDTO getUserCheckinStatistics(Long userId);

    /*
         主要仅用来获取当前连续签到的数据
         1. 连续签到是指从今天或者昨天(今天可能尚未打卡)向前计算的连续打卡天数
         2. 如果用户最近一次的连续签到不是今天或者昨天，那么最近连续签到天数为0
     */
    UserCheckinStatisticsDTO getUserContinuousCheckinInfo(Long userId);

    /*
        APP端使用补签卡
     */
    void userResignCard(Long userId);

}
