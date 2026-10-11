package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteInterpretationOperateDTO;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/voc")
public class AdminDeleteVocInterpretation {
    @Autowired
    VocService vocService;

    //删除单词释义
    @OperateLog(dateType = "删除单词释义", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("interpretation/{id}")
    public Result deleteInterpretation(@PathVariable("id") Long id) {
        DeleteInterpretationOperateDTO deleteInterpretationOperateDTO = vocService.deleteInterpretation(id);
        return Result.ok(deleteInterpretationOperateDTO);
    }
}
