package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.OperateLogDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.OperateLogPageRequest;
import com.cskaoyan.wordmemorize.service.AdminLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/log/operation/list")
public class AdminLogOperationListController {

    @Resource
    AdminLogService adminLogService;

    //查询系统操作日志
    @GetMapping("")
    public Result<PageDTO<OperateLogDTO>> getOperationLog(OperateLogPageRequest request){
        PageDTO<OperateLogDTO> operateLogPage = adminLogService.getOperateLogPage(request);
        return Result.ok(operateLogPage);
    }
}
