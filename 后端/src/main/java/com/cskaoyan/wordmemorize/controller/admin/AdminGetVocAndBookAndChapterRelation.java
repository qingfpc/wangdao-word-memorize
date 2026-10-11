package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.VocChapBookDTO;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/voc")
public class AdminGetVocAndBookAndChapterRelation {
    @Autowired
    VocService vocService;

    //获取指定单词的单词-章节-书籍之间的关系
    @GetMapping("relations/{vocId}")
    public Result<List<VocChapBookDTO>> getVocRelations(@PathVariable("vocId") Long vocId){
        List<VocChapBookDTO> vocChapBookDTOS = vocService.getVocRelations(vocId);
        return Result.ok(vocChapBookDTOS);
    }
}
