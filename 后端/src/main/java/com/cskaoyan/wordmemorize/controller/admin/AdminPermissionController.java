package com.cskaoyan.wordmemorize.controller.admin;


import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeletePermissionOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PermissionDTO;
import com.cskaoyan.wordmemorize.dto.admin.RolePermissionDTO;
import com.cskaoyan.wordmemorize.request.PermissionCommand;
import com.cskaoyan.wordmemorize.service.RolePermissionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/permission")
public class AdminPermissionController {


    @Resource
    RolePermissionService rolePermissionService;


    //添加账号权限
    @OperateLog(dateType = "添加账号权限", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("")
    public Result addPermission(@RequestBody PermissionCommand permissionCommand) {
        rolePermissionService.savePermissions(permissionCommand);
        return Result.ok();
    }

//    修改账号权限


    @OperateLog(dateType = "修改账号权限", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("")
    public Result putPermission(@RequestBody PermissionCommand permissionCommand) {
        rolePermissionService.updatePermission(permissionCommand);
        return Result.ok();
    }

    //    删除指定账号权限
    @OperateLog(dateType = "添加商品", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("{permissionId}")
    public Result deletePermissionById(@PathVariable Long permissionId) {
        DeletePermissionOperateDTO deletePermissionOperateDTO = rolePermissionService.deletePermission(permissionId);
        return Result.ok(deletePermissionOperateDTO);
    }


//****账号权限列表
//            GET
///admin/permission/list


    @GetMapping("/list")
    public Result<List<PermissionDTO>> getPermissionList() {
        List<PermissionDTO> permissions = rolePermissionService.getPermissions();
        return Result.ok(permissions);
    }
}
