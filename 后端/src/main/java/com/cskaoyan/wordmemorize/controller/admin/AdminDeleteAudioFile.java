package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteVocFileOperateDTO;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/admin/voc")
public class AdminDeleteAudioFile {
    @Autowired
    VocService vocService;
    //删除音频文件
    @OperateLog(dateType = "删除文件", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("file/{type}/{id}")
    public Result deleteFile(@PathVariable("type") String type, @PathVariable("id") Long id, @RequestParam String name){
        DeleteVocFileOperateDTO deleteFile = vocService.deleteFile(id, type, name);
        return Result.ok(deleteFile);
    }
}
