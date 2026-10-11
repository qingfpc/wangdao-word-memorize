package com.cskaoyan.wordmemorize.controller.app;


import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinDTO;
import com.cskaoyan.wordmemorize.dto.app.UserCheckinStatisticsDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserCheckinCommand;
import com.cskaoyan.wordmemorize.service.UserCheckinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "移动端-打卡模块")
@RestController
@RequestMapping("user/checkin")
public class CheckinController {

    @Resource
    UserCheckinService userCheckinService;


    @Operation(summary = "**下载用户的所有打卡数据",
            description = "主要用于用户每次登录时下载用户数据(卸载APP后，第一次登录时会发送该请求)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功下载用户所有打卡数据"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("list")
    public Result<PageDTO<UserCheckinDTO>> getCheckInData(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<UserCheckinDTO> checkInData = userCheckinService.getCheckInData(pageRequest, userId);
        return Result.ok(checkInData);
    }


    @Operation(summary = "*****同步用户打卡数据",
            description = "主要用户用客户端点击打卡",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "打卡数据同步成功, 返回一个最新的签到统计数据"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("")
    public Result<UserCheckinStatisticsDTO> checkin(@RequestBody UserCheckinCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        command.setUserId(userId.toString());
        UserCheckinStatisticsDTO userCheckinStatisticsDTO
                = userCheckinService.checkin(command);
        return Result.ok(userCheckinStatisticsDTO);
    }

    @Operation(summary = "**获取用户的打卡统计数据",
            description = "主要用于app中我的页面的数据展示",
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
    @GetMapping("statistics")
    public Result<UserCheckinStatisticsDTO> getUserCheckinStatistics() {
        Long  userId = StpKit.USER.getLoginIdAsLong();
        UserCheckinStatisticsDTO userCheckinStatistics = userCheckinService.getUserCheckinStatistics(userId);
        return Result.ok(userCheckinStatistics);
    }


    /**
     *  使用补签卡
     */
    @Operation(summary = "*****使用补签卡",
            description = "用户在签到日历旁点击使用补签卡",
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
    @PutMapping("card")
    public Result useCheckinCard() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userCheckinService.userResignCard(userId);
        return Result.ok();
    }
}
