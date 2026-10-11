package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.OperateLogDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.OperateLogPageRequest;
import com.cskaoyan.wordmemorize.service.AdminLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "后台管理-系统日志相关接口")
@RestController
@RequestMapping("admin/log")
public class AdminLogController {


    @Resource
    AdminLogService adminLogService;

    @Operation(summary = "****查询系统操作日志",
            description = "主要用于系统管理-操作日志",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "添加"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("operation/list")
    public Result<PageDTO<OperateLogDTO>> operateLogList(OperateLogPageRequest request) {
        PageDTO<OperateLogDTO> operateLogPage = adminLogService.getOperateLogPage(request);
        return Result.ok(operateLogPage);
    }

}
