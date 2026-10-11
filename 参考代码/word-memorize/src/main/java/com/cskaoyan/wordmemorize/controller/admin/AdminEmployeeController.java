package com.cskaoyan.wordmemorize.controller.admin;

import cn.dev33.satoken.session.SaSession;
import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.EmployeeCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import com.cskaoyan.wordmemorize.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "后台管理-账号权限管理-员工管理相关接口")
@RestController
@RequestMapping("admin")
public class AdminEmployeeController {

    @Resource
    EmployeeService employeeService;

    @Operation(summary = "***获取当前当前登录用户信息",
            description = "主要用户前端页面更具不同的用户信息以及权限，显示不同内容",
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
    @GetMapping("employee/info")
    public Result<EmployeeDTO> getEmployeeInfo() {
        SaSession session = StpKit.ADMIN.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) session.get("employee");
        if (employeeDTO != null) {
            return Result.ok(employeeDTO);
        }
        Long id = StpKit.ADMIN.getLoginIdAsLong();
        EmployeeDTO employeeInfo = employeeService.getEmployeeInfo(id);
        return Result.ok(employeeInfo);
    }

    @Operation(summary = "**获取后台员工列表",
            description = "主要用于账号权限管理中的员工管理，获取所有员工列表",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "返回获取到的分页员工列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("employee/list")
    @SaAdminCheckPermission("admin:employee:search")
    public Result<PageDTO<EmployeeDTO>> getEmployeeList(PageRequest pageRequest) {
        PageDTO<EmployeeDTO> employeeList = employeeService.getEmployeePage(pageRequest);
        return Result.ok(employeeList);
    }

    @Operation(summary = "***后台添加员工",
            description = "主要用于管理员添加后台员工账号",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "添加成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("employee")
    @SaAdminCheckPermission("admin:employee:add")
    public Result addEmployee(@RequestBody EmployeeCommand employeeCommand) {
        employeeService.addEmployee(employeeCommand);
        return Result.ok();
    }

    @Operation(summary = "***后台修改员工信息",
            description = "主要用于修改后台员工信息, 主要用于账号管理的员工权限管理，注意角色的变更，有可能新增，也有可能删除",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "修改成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PutMapping("employee")
    @SaAdminCheckPermission("admin:employee:update")
    public Result updateEmployee(@RequestBody EmployeeCommand employeeCommand) {
        employeeService.updateEmployee(employeeCommand);
        return Result.ok();
    }

    @Operation(summary = "**后台删除员工信息",
            description = "主要用于账号权限管理-员工管理，删除员工账号",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "删除成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("employee/{employeeId}")
    @SaAdminCheckPermission("admin:employee:delete")
    public Result deleteEmployee(@PathVariable String employeeId) {
        employeeService.delete(Long.parseLong(employeeId));
        return Result.ok();
    }


    @Operation(summary = "**后台删除员工-角色关联关系",
            description = "主要用于账号权限管理-员工管理，删除员工账号",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "删除成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("employee/{employeeId}/role/{roleId}")
    @SaAdminCheckPermission("admin:employee:update")
    public Result deleteEmployeeRole(@PathVariable("employeeId") Long employeeId,
                                     @PathVariable("roleId") Long roleId) {
        employeeService.deleteEmployeeRole(employeeId, roleId);
        return Result.ok();
    }

    @Resource
    FileUploadService fileUploadService;
    @Operation(summary = "****上传头像",
            description = "主要用于账号权限管理-员工管理，添加或者编辑员工",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "删除成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("employee/avatar")
    @SaAdminCheckPermission("admin:employee:update")
    public Result<String> uploadAvatar(MultipartFile file) throws IOException {
        String upload = fileUploadService.upload(file);
        return Result.ok(upload);
    }

}
