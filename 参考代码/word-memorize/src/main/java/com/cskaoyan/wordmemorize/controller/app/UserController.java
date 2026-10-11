package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserRemindCommand;
import com.cskaoyan.wordmemorize.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "移动端-用户(我的)模块")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @Operation(summary = "***获取用户信息",
            description = "在用户打开APP或者其他路由跳转的时候，通过token获取用户信息",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功. 返回登录成功的用户信息，以及当前等级权限码集合"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("info")
    public Result<UserDTO> getUserInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserDTO userInfo = userService.getUserInfo(userId);
        return Result.ok(userInfo);
    }

    @Operation(summary = "*修改每天复习单词数量",
            description = "主要用于用户在我的页面修改已规划的每天复习单词数量",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PutMapping("day/voc")
    public Result updateUserVocCountOfDay(@Parameter(description = "新规划的每天复习单词数量")
                                          @RequestParam("count") Integer count) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userService.updateVocCountOfDay(userId, count);
        return Result.ok();
    }

    @Operation(summary = "*修改每天复习提醒时间",
            description = "主要用于用户在我的页面修改每天复习提醒时间",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PutMapping("remind")
    public Result updateUserRemind(@RequestBody UserRemindCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        command.setUserId(userId.toString());
        userService.updateUserRemind(command);
        return Result.ok();
    }


    @Operation(summary = "****返回用户的升级信息",
            description = "主要用于用户的我的页面，展示用户距离升级剩多少天",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功, 若data值为null说明已达到最大等级"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("upgrade/info")
    public Result<NextLevelDTO> getUpgradeInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        NextLevelDTO userNextDTO = userService.getUserNextDTO(userId);
        return Result.ok(userNextDTO);
    }

    @Operation(summary = "***返回用户的等息信息",
            description = "主要用于点击用户头像，进入等级信息页面的等级信息展示",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("level/info")
    public Result<DisplayUserLevelDTO> getLevelInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        DisplayUserLevelDTO userLevelInfo = userService.getUserLevelInfo(userId);
        return Result.ok(userLevelInfo);
    }

    @Operation(summary = "**返回用户的单词上限变化明细",
            description = "主要用于用户在我的页面点击现单词上限总量",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("voc/bound/detail")
    public Result<PageDTO<DisplayUserVocBoundLogDTO>> getUserVocBoundDetail(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<DisplayUserVocBoundLogDTO> userVocBoundLog = userService.getUserVocBoundLog(pageRequest, userId);
        return Result.ok(userVocBoundLog);
    }

    @Operation(summary = "*返回用户的单词上限统计数据",
            description = "主要用于用户在我的页面展示单词上限，以及已规划使用的单词上限，点击现单词上限总量看到的统计数据",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("voc/bound/statistics")
    public Result<UserVocBoundStatisticsDTO> getUserVocBoundStatistics() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserVocBoundStatisticsDTO userVocBoundStatistics = userService.getUserVocBoundStatistics(userId);
        return Result.ok(userVocBoundStatistics);
    }

    @Operation(summary = "**返回用户页面展示条目的信息",
            description = "主要用于在我的页面展示用户规划单词量，单词上限，提醒时间等信息",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("voc/my/page")
    public Result<UserPageDTO> getUserPageInfo() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserPageDTO userPage = userService.getUserPage(userId);
        return Result.ok(userPage);
    }


}
