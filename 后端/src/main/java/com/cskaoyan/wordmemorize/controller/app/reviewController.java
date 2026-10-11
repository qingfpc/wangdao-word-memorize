package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.app.DownLoadUserReviewDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserReviewDataCommand;
import com.cskaoyan.wordmemorize.service.UserReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user/review")
public class reviewController {

    @Autowired
    UserReviewService userReviewService;

    //添加用户的单词复习数据
    @PostMapping("voc")
    public Result addUserVocReview(@RequestBody List<Long> vocIds){
        Result result = userReviewService.addUserRovRev(vocIds);
        return result.ok(result);
    }
    //下载用户的所有单词复习数据
    @GetMapping("data")
    public Result<DownLoadUserReviewDTO> downloadUserReviewData(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer count
    ){
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pageNum);
        pageRequest.setCount(count);
        Long loginIdAsLong = StpKit.USER.getLoginIdAsLong();
        DownLoadUserReviewDTO downLoadUserReviewDTO = userReviewService.downloadUserReviewData(pageRequest, loginIdAsLong);
        return Result.ok(downLoadUserReviewDTO);
    }
    //同步用户的单词复习数据
    @PostMapping("data")
    public Result synchronizeUserReviewData(@RequestBody UserReviewDataCommand command){
        Long loginIdAsLong = StpKit.USER.getLoginIdAsLong();
        userReviewService.synchronizeUserReviewData(command, loginIdAsLong);
        return Result.ok();
    }

}
