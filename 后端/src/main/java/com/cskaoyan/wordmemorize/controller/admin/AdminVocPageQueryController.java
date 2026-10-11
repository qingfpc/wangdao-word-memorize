package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapterMapper;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapterDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocDTO;
import com.cskaoyan.wordmemorize.request.VocPageRequest;
import com.cskaoyan.wordmemorize.service.VocChapterService;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/voc")
public class AdminVocPageQueryController {

    @Autowired
    VocService vocService;

    //分页查询单词列表
    @GetMapping("list")
    public Result<PageDTO<VocDTO>> queryVocChapterList(VocPageRequest vocPageRequest) {

        PageDTO<VocDTO> vocabularies = vocService.getVocabularies(vocPageRequest);

        return Result.ok(vocabularies);

    }
}