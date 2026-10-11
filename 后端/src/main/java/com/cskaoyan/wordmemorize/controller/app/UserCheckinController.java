package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinStatisticsDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserCheckinCommand;
import com.cskaoyan.wordmemorize.service.UserCheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/checkin")
public class UserCheckinController {

    @Autowired
    UserCheckinService userCheckinService;

    //使用补签卡
    @PutMapping("card")
    public Result useCheckinCard() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userCheckinService.userResignCard(userId);
        return Result.ok("使用补签卡成功");
    }

    // APP端打卡
    @PostMapping("")
    public Result<UserCheckinStatisticsDTO> checkin(@RequestBody UserCheckinCommand command) {
        UserCheckinStatisticsDTO checkin = userCheckinService.checkin(command);
        return Result.ok(checkin);
    }

    //获取用户的打卡统计数据
    @GetMapping("statistics")
    public Result<UserCheckinStatisticsDTO> getCheckinStatistics() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserCheckinStatisticsDTO checkinStatistics = userCheckinService.getUserCheckinStatistics(userId);
        return Result.ok(checkinStatistics);
    }

    //下载用户的所有打卡数据
    @GetMapping("list")
    public Result<PageDTO<UserCheckinDTO>> getCheckinList(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<UserCheckinDTO> checkinList = userCheckinService.getCheckInData(pageRequest,userId);
        return Result.ok(checkinList);
    }
}
