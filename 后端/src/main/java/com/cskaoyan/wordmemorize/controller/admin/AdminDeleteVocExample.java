package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteVocExampleOperateDTO;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/voc")
public class AdminDeleteVocExample {

    @Autowired
    VocService vocService;
    //删除单词例句
    @OperateLog(dateType = "删除单词例句", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("example/{id}")
    public Result deleteExample(@PathVariable("id") Long id) {
        DeleteVocExampleOperateDTO deleteVocExampleOperateDTO = vocService.deleteExample(id);
                return Result.ok(deleteVocExampleOperateDTO);
    }

}
