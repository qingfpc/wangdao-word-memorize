package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserVocAISituationCommand;
import com.cskaoyan.wordmemorize.service.AIReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台管理-等级权限管理-ai阅读相关接口")
@RestController
@RequestMapping("/admin/voc")
public class AdminAIReadController {


    @Resource
    AIReadService aiReadService;

    @Operation(summary = "**添加ai阅读模式",
            description = "主要用于等级特权管理-ai阅读",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "获取到的后台用户信息，包括其所具有的权限列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PostMapping("/ai-read/situation")
    public Result addSituation(@RequestBody UserVocAISituationCommand command) {
        aiReadService.addAISituation(command);
        return Result.ok();
    }

    @Operation(summary = "**修改指定ai阅读的模式",
            description = "主要用于等级特权管理-ai阅读",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "获取到的后台用户信息，包括其所具有的权限列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )

    @PutMapping("/ai-read/situation")
    public Result updateSituation(@RequestBody UserVocAISituationCommand command) {
        aiReadService.updateAISituation(command);
        return Result.ok();
    }

    @Operation(summary = "**删除指定的ai阅读模式",
            description = "主要用于等级特权管理-ai阅读",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "获取到的后台用户信息，包括其所具有的权限列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )

    @DeleteMapping("/ai-read/situation/{id}")
    public Result deleteSituation(@PathVariable("id") Long id) {
        aiReadService.deleteAISituation(id);
        return Result.ok();
    }

    @Operation(summary = "**批量删除ai阅读模式",
            description = "主要用于等级特权管理-ai阅读",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "获取到的后台用户信息，包括其所具有的权限列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )

    @DeleteMapping("/ai-read/situation")
    public Result batchDeleteSituation(@RequestBody BatchDeleteCommand command) {
        List<String> ids = command.getIds();
        List<Long> list = ids.stream().map(Long::parseLong).toList();
        aiReadService.batchDeleteAISituationOption(list);
        return Result.ok();
    }

    @Operation(summary = "**获取ai阅读的场景列表",
            description = "主要用于等级特权管理-ai阅读",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "获取到的后台用户信息，包括其所具有的权限列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )

    @GetMapping("/ai-read/situation/list")
    public Result<PageDTO<UserVocAISituationDTO>> getSituationList(PageRequest pageRequest) {
        PageDTO<UserVocAISituationDTO> pageDTO = aiReadService.getAISituationList(pageRequest);
        return Result.ok(pageDTO);
    }


    @Operation(summary = "*删除指定的ai阅读模式的选项",
            description = "主要用于等级特权管理-ai阅读",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "获取到的后台用户信息，包括其所具有的权限列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("/ai-read/option/{id}")
    public Result deleteOption(@PathVariable("id") Long id) {
        aiReadService.deleteAISituationOption(id);
        return Result.ok();
    }
}
