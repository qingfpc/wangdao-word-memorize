package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegeCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegePageRequest;
import com.cskaoyan.wordmemorize.service.LevelPrivilegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理-等级权限管理-等级特权管理相关接口")
@RestController
@RequestMapping("admin")
public class AdminLevelPrivilegeController {

    @Resource
    LevelPrivilegeService levelPrivilegeService;

    @Operation(summary = "**获取等级特权列表",
            description = "主要用于等级特权管理-特权管理",
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
    @GetMapping("privilege/list")
    public Result<PageDTO<LevelPrivilegeDTO>> getPrivilegeList(LevelPrivilegePageRequest request) {
        PageDTO<LevelPrivilegeDTO> levelPrivilegeList = levelPrivilegeService.getLevelPrivilegeList(request);
        return Result.ok(levelPrivilegeList);
    }

    @Operation(summary = "*添加等级特权",
            description = "主要用于等级特权管理-特权管理",
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
    @PostMapping("privilege")
    public Result addPrivilege(@RequestBody LevelPrivilegeCommand command) {
        levelPrivilegeService.addLevelPrivilege(command);
        return Result.ok();
    }
    @Operation(summary = "*修改等级特权",
            description = "主要用于等级特权管理-特权管理",
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
    @PutMapping("privilege")
    public Result updatePrivilege(@RequestBody LevelPrivilegeCommand command) {
        levelPrivilegeService.updateLevelPrivilege(command);
        return Result.ok();
    }

    @Operation(summary = "*删除指定的等级特权",
            description = "主要用于等级特权管理-特权管理",
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
    @DeleteMapping("privilege/{id}")
    public Result deletePrivilege(@PathVariable("id")@Parameter(description = "特权id") Long id) {
        levelPrivilegeService.deleteLevelPrivilege(id);
        return Result.ok();
    }

    @Operation(summary = "*批量删除等级特权",
            description = "主要用于等级特权管理-特权管理",
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
    @DeleteMapping("privilege/batch")
    public Result batchDeletePrivilege(@RequestBody BatchDeleteCommand command) {
        levelPrivilegeService.batchDeleteLevelPrivilege(command);
        return Result.ok();
    }
}
