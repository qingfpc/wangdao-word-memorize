package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayBookVocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayChapterDTO;
import com.cskaoyan.wordmemorize.request.AppVocPageRequest;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/voc")
public class VocController {
    @Autowired
    VocService vocService;

    //移动端查看单词详情
    @GetMapping("/detail/{vocId}")
    public Result<VocDTO> getVocDetail(@PathVariable("vocId") Long vocId) {
        Long loginIdAsLong = StpKit.USER.getLoginIdAsLong();
        VocDTO vocDTO = vocService.getVocDetail(vocId, loginIdAsLong);
        return Result.ok(vocDTO);
    }

    //移动端获取书籍单词列表（无章节版）
    @GetMapping("/book/display")
    public  Result<PageDTO<DisplayBookVocDTO>> getBookVocList(AppVocPageRequest appVocPageRequest) {
        Long loginIdAsLong = StpKit.USER.getLoginIdAsLong();
        appVocPageRequest.setUserId(loginIdAsLong.toString());
        PageDTO<DisplayBookVocDTO> displayBookVocDTOList = vocService.getDisplayBookVocsInfo(appVocPageRequest);
        return Result.ok(displayBookVocDTOList);
    }

    //移动端获取书籍单词列表（有章节版）
    @GetMapping("/book/chapter/display")
    public Result<PageDTO<DisplayChapterDTO>> getBookChapterVocList(AppVocPageRequest appVocPageRequest) {
        Long loginIdAsLong = StpKit.USER.getLoginIdAsLong();
        appVocPageRequest.setUserId(loginIdAsLong.toString());
        PageDTO<DisplayChapterDTO> displayChapterDTOList = vocService.getDisplayBookChapterVocsInfo(appVocPageRequest);
        return Result.ok(displayChapterDTOList);
    }


}
