package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dao.entity.UserVocBoundStatisticsDO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserRemindCommand;
import com.cskaoyan.wordmemorize.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    //修改每天复习提醒时间
    @PutMapping("remind")
    public Result updateUserRemind(@RequestBody UserRemindCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        System.out.println("command = " + command);
        command.setUserId(userId.toString());
        System.out.println("command = " + command);
        userService.updateUserRemind(command);
        return Result.ok("修改每天复习提醒时间成功");
    }

    //修改每天复习单词数量
    @PutMapping("day/voc")
    public Result updateUserVocCountOfDay(@RequestBody Integer vocCount) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userService.updateVocCountOfDay(userId,vocCount);
        return Result.ok("修改每天复习单词数量成功");
    }

    //返回用户页面展示条目的信息
    @GetMapping("voc/my/page")
    public Result<UserPageDTO> getUserPageInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserPageDTO userPageInfo = userService.getUserPage(userId);
        return Result.ok(userPageInfo);
    }

    //返回用户的单词上限统计数据
    @GetMapping("voc/bound/statistics")
    public Result<UserVocBoundStatisticsDTO> getUserVocBoundStatistics() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserVocBoundStatisticsDTO userVocBoundStatistics = userService.getUserVocBoundStatistics(userId);
        return Result.ok(userVocBoundStatistics);
    }

    //返回用户的单词上限变化明细
    @GetMapping("voc/bound/detail")
    public Result<PageDTO<DisplayUserVocBoundLogDTO>> getUserVocBoundDetail(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<DisplayUserVocBoundLogDTO> userVocBoundDetail = userService.getUserVocBoundLog(pageRequest,userId);
        return Result.ok(userVocBoundDetail);
    }

    //返回用户的升级信息
    @GetMapping("upgrade/info")
    public Result<NextLevelDTO> getUserUpgradeInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        NextLevelDTO upgradeInfo = userService.getUserNextDTO(userId);
        return Result.ok(upgradeInfo);
    }

    //返回用户的等级信息
    @GetMapping("level/info")
    public Result<DisplayUserLevelDTO> getUserLevelInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        DisplayUserLevelDTO userLevelInfo = userService.getUserLevelInfo(userId);
        return Result.ok(userLevelInfo);
    }

    // 获取用户信息
    @GetMapping("info")
    public Result<UserDTO> getUserInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserDTO userInfo = userService.getUserInfo(userId);
        return Result.ok(userInfo);
    }

}
