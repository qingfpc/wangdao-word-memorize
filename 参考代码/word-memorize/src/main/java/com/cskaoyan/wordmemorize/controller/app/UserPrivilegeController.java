package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.*;
import com.cskaoyan.wordmemorize.service.UserPrivilegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "移动端-用户特权模块")
@RestController
@RequestMapping("user/priviledge")
public class UserPrivilegeController {

    @Resource
    UserPrivilegeService userPrivilegeService;


    @Operation(summary = "***词书进度",
            description = "在选词页面的当前所选书籍下展示的熟悉，一般，不熟，已选未学数据",
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
    @GetMapping("current/{bookId}/statistics")
    //@SaUserCheckPermission("current:book")
    public Result<UserVocBookStatisticsDTO> getSelectedBookStatistics(@Parameter(description = "书籍id")@PathVariable("bookId") Long bookId) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        UserVocBookStatisticsDTO selectedBookStatistics = userPrivilegeService.getSelectedBookStatistics(userId, bookId);
        return Result.ok(selectedBookStatistics);
    }

    @Operation(summary = "添加用户收藏单词",
            description = "主要用于用户在查看单词详细信息时收藏单词",
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
    @PostMapping("**favorite")
    //@SaUserCheckPermission("my:favorite")
    public Result addUserFavorite(@RequestBody UserFavoriteCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.addFavorite(command, userId);
        return Result.ok();
    }


    /*
         收藏单词, 用户可能对同一个单词多次收藏，这里后端要保证幂等
     */
    @Operation(summary = "**批量添加用户收藏单词",
            description = "主要用于用户在查看单词详细信息时收藏单词",
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
    @PostMapping("favorite/bulk")
    //@SaUserCheckPermission("my:favorite:bulk")
    public Result addUserFavoriteBulk(@RequestBody List<UserFavoriteCommand> command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.addFavoriteBulk(command, userId);
        return Result.ok();
    }

    /**
     *  获取收藏单词
     */
    @Operation(summary = "**获取用户收藏单词列表",
            description = "主要用于用户在查看收藏单词列表时",
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
    @GetMapping("favorite/list")
    //@SaUserCheckPermission("my:favorite")
    public Result<PageDTO<UserFavoriteDTO>> getUserFavoriteList(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<UserFavoriteDTO> userFavoriteList = userPrivilegeService.getUserFavoriteList(pageRequest, userId);
        return Result.ok(userFavoriteList);
    }

    /**
     *  移除收藏单词
     */
    @Operation(summary = "*取消收藏单词",
            description = "主要用于用户在查看收藏单词详情时，取消收藏单词",
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
    @PostMapping("favorite/cancel")
    //@SaUserCheckPermission("my:favorite")
    public Result removeUserFavorite(@RequestBody List<String> vocIds) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        List<Long> ids = vocIds.stream()
                .map(id -> Long.parseLong(id)).collect(Collectors.toList());
        userPrivilegeService.cancelFavorite(ids, userId);
        return Result.ok();
    }


    /*
        添加自建例句
     */
    @Operation(summary = "*添加自建例句",
            description = "用户在查看单词详情的时候添加自建例句",
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
    @PostMapping("/voc/example")
    //@SaUserCheckPermission("create:my:example")
    public Result addUserExampleSentence(@RequestBody UserVocExampleSentenceCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.addUserVocSentence(command, userId);
        return Result.ok();
    }
    /**
     *  获取自建例句
     */
    @Operation(summary = "**获取自建例句",
            description = "用户在查看单词详情的时候获取自建例句",
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
    @GetMapping("/voc/example/list")
    //@SaUserCheckPermission("create:my:example")
    public Result<PageDTO<DisplayUserVocSentenceDTO>> getUserExampleSentences(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<DisplayUserVocSentenceDTO> userVocSentence = userPrivilegeService.getUserVocSentence(pageRequest, userId);
        return Result.ok(userVocSentence);
    }
    /**
     *  修改自建例句
     */
    @Operation(summary = "*修改用户自建例句",
            description = "用户在自建列表中找到例句并修改",
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
    @PutMapping("/voc/example")
    //@SaUserCheckPermission("create:my:example")
    public Result updateUserExampleSentence(@RequestBody UserVocExampleSentenceCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.updateUserVocSentence(command, userId);
        return Result.ok();
    }
//    /**
//     *  删除自建例句
//     */
//    @Operation(summary = "*删除用户自建例句",
//            description = "用户在自建释义列表中删除自建例句",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "成功"
//                    ),
//                    @ApiResponse(responseCode = "500",
//                            description = "请求失败"
//                    )
//            }
//    )
//    @DeleteMapping("/voc/example/{id}")
//    //@SaUserCheckPermission("create:my:example")
//    public Result deleteUserExampleSentence(@Parameter(description = "删除用户自建例句")@PathVariable Long id) {
//        userPrivilegeService.deleteUserVocSentence(id);
//        return Result.ok();
//    }

    /**
     *  标识不想背
     */
    @Operation(summary = "***标识用户不想背",
            description = "用户在单词详情中标识单词不想背",
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
    @PostMapping("voc/non")
    //@SaUserCheckPermission("review:not:remember")
    public Result userNotRemember(Long vocId) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.notRemember(vocId, userId);
        return Result.ok();
    }

    @Operation(summary = "*取消用户不想背",
            description = "用户在单词详情中标识取消不想背",
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
    @PutMapping("voc/non/cancel")
    //@SaUserCheckPermission("review:not:remember")
    public Result userCancelNotRemember(@Parameter(description = "单词id") @RequestParam("vocId") Long vocId) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        userPrivilegeService.cancelNotRemember(vocId, userId);
        return Result.ok();
    }


//    /**
//     *   选词填空回顾
//     */
//    @Operation(summary = "*****获取单词对应的选词填空题目",
//            description = "点击今日回顾中的选词填空题回顾获取今日所学单词对应选词填空",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "成功"
//                    ),
//                    @ApiResponse(responseCode = "500",
//                            description = "请求失败"
//                    )
//            }
//    )
//    @PostMapping("review/fillWord")
//    //@SaUserCheckPermission("review:fill:word")
//    public Result<List<UserReviewFillWordDTO>> getVocWordFillTopic(@Schema(description = "题目对应的id集合")@RequestBody List<Long> vocIds) {
//        List<UserReviewFillWordDTO> userReviewFillWordTopics = userPrivilegeService.getUserReviewFillWordTopics(vocIds);
//        return Result.ok(userReviewFillWordTopics);
//    }
//
//    /**
//     *  单项选择回顾
//     */
//    @Operation(summary = "*****获取单词对应的单选题目",
//            description = "点击今日回顾中的单选题回顾获取今日所学单词对应的单选题目",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "成功"
//                    ),
//                    @ApiResponse(responseCode = "500",
//                            description = "请求失败"
//                    )
//            }
//    )
//    @PostMapping("review/singleChoice")
//    //@SaUserCheckPermission("review:single:choice")
//    public Result<List<UserReviewSingleChoiceDTO>> getVocSingleChoiceTopic(@Schema(description = "题目对应的id集合")@RequestBody List<Long> vocIds) {
//        List<UserReviewSingleChoiceDTO> vocSingleChoiceTopic = userPrivilegeService.getVocSingleChoiceTopic(vocIds);
//        return Result.ok(vocSingleChoiceTopic);
//    }

    @Operation(summary = "***AI阅读剩余免费使用次数",
            description = "进入AI阅读后，由剩余免费使用次数决定显示的是免费使用，还是消耗xx单词上限",
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
    @PostMapping("review/ai/read/free")
    //@SaUserCheckPermission("user:review:ai:read")
    public Result<Integer> getUserVocAIReadRemainFreeCount() {
        Long userId = StpKit.USER.getLoginIdAsLong();
        Integer count = userPrivilegeService.getUserVocAIReadRemainFreeCount(userId);
        return Result.ok(count);
    }

    @Operation(summary = "**AI阅读风格",
            description = "点击今日回顾中的AI阅读前先获取所需的生成风格",
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
    @PostMapping("review/ai/read/options")
    //@SaUserCheckPermission("user:review:ai:read")
    public Result<List<UserVocAISituationDTO>> getAIReviewReadOptions() {
        List<UserVocAISituationDTO> aiReviewReadOptions = userPrivilegeService.getAIReviewReadOptions();
        return Result.ok(aiReviewReadOptions);
    }

    /**
     *  AI阅读
     */
    @Operation(summary = "****AI阅读",
            description = "点击今日回顾中的AI阅读，选择20个单词生成，选择生成对话或者文章",
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
    @PostMapping("review/ai/read")
    //@SaUserCheckPermission("user:review:ai:read")
    public Result<List<AIReviewReadDTO>> getAIReviewRead(@RequestBody AIReviewReadCommand commands) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        List<AIReviewReadDTO> aiReviewRead = userPrivilegeService.getAIReviewReadContent(commands, userId);
        return Result.ok(aiReviewRead);
    }
}
