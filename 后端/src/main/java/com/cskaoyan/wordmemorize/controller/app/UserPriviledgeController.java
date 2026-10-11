package com.cskaoyan.wordmemorize.controller.app;


import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayUserVocSentenceDTO;
import com.cskaoyan.wordmemorize.dto.app.UserFavoriteDTO;
import com.cskaoyan.wordmemorize.dto.app.UserVocBookStatisticsDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserFavoriteCommand;
import com.cskaoyan.wordmemorize.request.UserVocExampleSentenceCommand;
import com.cskaoyan.wordmemorize.service.UserPrivilegeService;
import com.cskaoyan.wordmemorize.service.UserService;
import jakarta.annotation.Resource;
import org.apache.catalina.security.SecurityUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.List;

@RestController
@RequestMapping("/user/priviledge")
public class UserPriviledgeController {


    @Resource
    UserPrivilegeService userPrivilegeService;

    @Resource
    UserService userService;


    //词书进度
    //已重写
    @GetMapping("/current/{bookId}/statistics")
    public Result<UserVocBookStatisticsDTO> getStatisticsById(@PathVariable Long bookId){
        long userId = StpKit.USER.getLoginIdAsLong();
        UserVocBookStatisticsDTO bookStatistics = userPrivilegeService.getSelectedBookStatistics(userId,bookId);
        return Result.ok(bookStatistics);
    }

    //获取用户收藏单词列表
    //已重新写
    @GetMapping("/favorite/list")
    public Result getFavoriteList(PageRequest pageRequest){
        PageDTO<UserFavoriteDTO> userFavoriteList = userPrivilegeService.getUserFavoriteList(pageRequest);
        return Result.ok(userFavoriteList);
    }


    //取消收藏单词
    //已经重新写
    @PostMapping("/favorite/cancel")
    public Result favoriteCancel(@RequestBody Long[] ids){
        long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.cancelFavorite(ids,userId);
        return Result.ok();
    }

    //添加自建例句
    //已重新写
    @PostMapping("/voc/example")
    public Result vocExample(@RequestBody UserVocExampleSentenceCommand command){
        long userId = StpKit.USER.getLoginIdAsLong();

            userPrivilegeService.addUserVocSentence(command,userId);
    return Result.ok();
    }

    //修改用户自建例句
    //已重新写

    @PutMapping("/voc/example")
    public Result upDataVocExample(@RequestBody UserVocExampleSentenceCommand command){
        long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.updateUserVocSentence(command,userId);
        return Result.ok();
    }

    //添加用户收藏单词
    //已经重新写
    @PostMapping("/favorite")
    public Result addFavorite(@RequestBody UserFavoriteCommand userFavoriteCommand){
        long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.addFavorite(userFavoriteCommand,userId);
        return Result.ok();
    }

    //取消用户不想背
    @PutMapping("/voc/non/cancel")
    public Result vocNonCancel(@RequestParam("vocId") Long vocId){
        long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.cancelNotRemember(vocId,userId);
        return Result.ok();
    }


//    批量添加用户收藏单词
    @PostMapping("/favorite/bulk")
    public Result addFavoriteBulk(@RequestBody UserFavoriteCommand[] userFavoriteCommands){
        long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.addFavoriteBulk(List.of(userFavoriteCommands),userId);
    return Result.ok();
    }


    // 获取自建例句
    @GetMapping("/voc/example/list")
    public Result<PageDTO<DisplayUserVocSentenceDTO>> getExampleList(@RequestBody PageRequest pageRequest){
        long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<DisplayUserVocSentenceDTO> userVocSentence = userPrivilegeService.getUserVocSentence(pageRequest, userId);
        return Result.ok(userVocSentence);
    }

    //标识用户不想背
    @PostMapping("/voc/non")
    public Result postVocNon(@RequestParam("vocId") Long vocId){
        long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.notRemember(vocId,userId);
        return Result.ok();
    }















}
