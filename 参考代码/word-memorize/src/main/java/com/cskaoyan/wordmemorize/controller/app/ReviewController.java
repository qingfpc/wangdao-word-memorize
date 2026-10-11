package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.app.DownLoadUserReviewDTO;
import com.cskaoyan.wordmemorize.request.*;
import com.cskaoyan.wordmemorize.service.UserReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "移动端-复习模块")
@RestController
@RequestMapping("user/review")
public class ReviewController {

    @Resource
    UserReviewService userReviewService;



    @Operation(summary = "***同步用户的复习数据",
            description = "用于APP同步用户的复习数据",
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
    @PostMapping("data")
    public Result synchronizeUserReviewData(@RequestBody UserReviewDataCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userReviewService.synchronizeUserReviewData(command, userId);
        return Result.ok();
    }




    @Operation(summary = "**添加用户的单词复习数据(参数为用户已选单词的id集合)",
            description = "用于用户在点击加入记忆的时候，添加用户的单词复习数据",
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
    @PostMapping("voc")
    public Result addUserRovRev(@RequestBody List<Long> vocIds) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userReviewService.addUserRovRev(vocIds, userId);
        return Result.ok();
    }

    @Operation(summary = "**下载用户的所有单词复习数据",
            description = "用于用户在登录时下载自己的复习数据志(卸载APP后，第一次登录时会发送该请求)",
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
    @GetMapping("data")
    public Result<DownLoadUserReviewDTO> downloadUserReviewData(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        DownLoadUserReviewDTO downLoadUserReviewDTO = userReviewService.downloadUserReviewData(pageRequest, userId);
        return Result.ok(downLoadUserReviewDTO);
    }

}
