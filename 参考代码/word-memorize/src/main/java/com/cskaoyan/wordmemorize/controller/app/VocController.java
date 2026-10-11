package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayBookVocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayChapterDTO;
import com.cskaoyan.wordmemorize.dto.app.UserBookStatisticsDTO;
import com.cskaoyan.wordmemorize.request.AppVocPageRequest;
import com.cskaoyan.wordmemorize.service.VocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "移动端-单词模块")
@RestController
@RequestMapping("user")
public class VocController {

    @Resource
    VocService vocService;

//    /*
//        单词列表
//     */
//
//    @Operation(summary = "***获取书籍中当前用户已选的数量",
//            description = "用户查看书籍单词列表选择单词时可以看到当前书籍中已选单词数量",
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
//    @GetMapping("voc/{bookId}/selection/info")
//    public Result<UserBookStatisticsDTO> getVocBookStatistics(@Parameter(description = "书籍id")@PathVariable("bookId") Long bookId) {
//        Long userId = StpKit.USER.getLoginIdAsLong();
//        UserBookStatisticsDTO userChooseCountInBook = vocService.getUserChooseCountInBook(userId, bookId);
//        return Result.ok(userChooseCountInBook);
//    }

    /*
         书籍单词列表
     */
    @Operation(summary = "***获取书籍单词列表有章节版",
            description = "在单词列表页中，当选中类目，选中书籍之后，会给用户展示该书籍中包含的单词列表以供用户选择",
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
    @GetMapping("/voc/book/chapter/display")
    public Result<PageDTO<DisplayChapterDTO>> getBookChapterVocList(AppVocPageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        pageRequest.setUserId(userId.toString());
        PageDTO<DisplayChapterDTO> displayBookChapterVocsInfo = vocService.getDisplayBookChapterVocsInfo(pageRequest);
        return Result.ok(displayBookChapterVocsInfo);
    }

    /*
         书籍单词列表
     */
    @Operation(summary = "***获取书籍单词列表(无章节版)",
            description = "在单词列表页中，当选中类目，选中书籍之后，会给用户展示该书籍中包含的单词列表以供用户选择",
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
    @GetMapping("/voc/book/display")
    public Result<PageDTO<DisplayBookVocDTO>> getBookVocList(AppVocPageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        pageRequest.setUserId(userId.toString());
        PageDTO<DisplayBookVocDTO> displayBookVocsInfo = vocService.getDisplayBookVocsInfo(pageRequest);
        return Result.ok(displayBookVocsInfo);
    }

    /*
         单词详情
     */
    @Operation(summary = "***查看单词详情",
            description = "在复习，或者点击已选单词或者单词列中的单词时，会进入单词详情页",
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
    @GetMapping("/voc/detail/{vocId}")
    public Result<VocDTO> getDisplayVocDetail(@Parameter(description = "单词id") @PathVariable Long vocId) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        VocDTO vocDetail = vocService.getVocDetail(vocId, userId);
        return Result.ok(vocDetail);
    }



}
