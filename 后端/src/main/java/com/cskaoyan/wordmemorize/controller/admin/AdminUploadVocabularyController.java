package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/voc/excel")
public class AdminUploadVocabularyController {


    @Autowired
    VocService vocService;


    //    校验上传单词的excel表格
    @PostMapping("validate")
    public Result VerificationUploadVocExcel(@RequestParam("vocExcel") MultipartFile file) throws IOException {

        vocService.vocExcelValidate(file);

        return Result.ok();
    }


    //    通过excel表格上传单词
//    @OperateLog(dateType = "通过Excel表格上传单词", operateType = OperateLogConstant.OPERATE_EXCEL_ADD)
    @PostMapping("")
    public Result ExcelToUploadVoc(@RequestParam("vocExcel") MultipartFile file) throws IOException {

        long loginIdAsLong = StpKit.ADMIN.getLoginIdAsLong();

        vocService.addVocExcel(file, loginIdAsLong);

        return Result.ok();
    }
}