package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteVocRelationOperateDTO;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/voc")
public class AdminDeleteVocAndBookAndChapterRelation {
    @Autowired
    VocService vocService;

    //删除指定单词的单词-章节-书籍之间的关系
    @OperateLog(dateType = "删除指定单词", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("relation/{id}")
    public Result deleteVocRelation(@PathVariable("id") Long id) {
        DeleteVocRelationOperateDTO deleteVocRelationOperateDTO = vocService.deleteVocRelation(id);
        return Result.ok(deleteVocRelationOperateDTO);
    }
}
