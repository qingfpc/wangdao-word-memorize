package com.cskaoyan.wordmemorize.controller.admin;


import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理-等级权限管理-等级管理相关接口")
@RestController
@RequestMapping("admin")
public class AdminLevelController {

    @Resource
    LevelService levelService;

    @Operation(summary = "**获取等级列表",
            description = "主要用于等级特权管理-等级管理获取等级列表",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "获取用户等级分页列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("level/list")
    public Result<PageDTO<LevelDTO>> getLevelList(PageRequest pageRequest){
        PageDTO<LevelDTO> levelList = levelService.getLevelList(pageRequest);
        return Result.ok(levelList);
    }

    @Operation(summary = "*添加等级",
            description = "主要用于等级特权管理-等级管理添加用户等级",
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
    @PostMapping("level")
    public Result addLevel(@RequestBody LevelCommand command){
        levelService.addLevel(command);
        return Result.ok();
    }

    @Operation(summary = "*更新等级",
            description = "主要用于等级特权管理-等级管理更新用户等级信息",
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
    @PutMapping("level")
    public Result updateLevel(@RequestBody LevelCommand command){
        levelService.updateLevel(command);
        return Result.ok();
    }

    @Operation(summary = "**删除等级",
            description = "主要用于等级特权管理-等级管理删除指定用户等级信息",
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
    @DeleteMapping("level/{id}")
    public Result deleteLevel(@PathVariable@Parameter(description = "等级id") Long id){
        levelService.deleteLevel(id);
        return Result.ok();
    }

    @Operation(summary = "**批量删除等级",
            description = "主要用于等级特权管理-等级管理批量删除用户的等级信息",
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
    @DeleteMapping("level/batch")
    public Result deleteLevels(@RequestBody BatchDeleteCommand command){
        levelService.deleteLevels(command);
        return Result.ok();
    }
}
