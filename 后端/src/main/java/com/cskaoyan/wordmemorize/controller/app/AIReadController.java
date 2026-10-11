package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.dto.app.AIReviewReadDTO;
import com.cskaoyan.wordmemorize.request.AIReviewReadCommand;
import com.cskaoyan.wordmemorize.service.UserPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/priviledge/review/ai/read")
public class AIReadController {


    @Autowired
    UserPrivilegeService userPrivilegeService;

    //点击今日回顾中的AI阅读前先获取所需的生成风格
    @PostMapping("options")
    public Result<List<UserVocAISituationDTO>> getAIReadOptions(){
        List<UserVocAISituationDTO> options = userPrivilegeService.getAIReviewReadOptions();
        return Result.ok(options);
    }



//    进入AI阅读后，由剩余免费使用次数决定显示的是免费使用，还是消耗xx单词上限
    @PostMapping("free")
    public Result<Integer> getUserVocAIReadFreeCount(){
        Long userId = StpKit.USER.getLoginIdAsLong();
        Integer freeCount = userPrivilegeService.getUserVocAIReadRemainFreeCount(userId);
        return Result.ok(freeCount);
    }



//    点击今日回顾中的AI阅读，选择20个单词生成，选择生成对话或者文章
    @PostMapping("")
    public Result<List<AIReviewReadDTO>> getAIReadContent(@RequestBody AIReviewReadCommand aiReviewReadCommand){
        Long userId = StpKit.USER.getLoginIdAsLong();
        List<AIReviewReadDTO> content = userPrivilegeService.getAIReviewReadContent(aiReviewReadCommand, userId);
        return Result.ok(content);
    }
}
