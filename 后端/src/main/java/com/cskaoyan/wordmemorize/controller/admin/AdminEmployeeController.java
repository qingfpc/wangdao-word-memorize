package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.EmployeeCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import com.cskaoyan.wordmemorize.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/admin/employee")
public class AdminEmployeeController {

    @Autowired
    EmployeeService employeeService;



    @Autowired
    @Qualifier("fileUploadServiceImpl")
    FileUploadService fileUploadService;



    @GetMapping("info")
    public Result<EmployeeDTO> getEmployeeInfo() {
        // 获取当前登录的用户id
        Long employeeId = StpKit.ADMIN.getLoginIdAsLong();

        EmployeeDTO employeeInfo = employeeService.getEmployeeInfo(employeeId);

        return Result.ok(employeeInfo);
    }


        //后台添加员工
        //POST
        ///admin/employee

    @OperateLog(dateType = "添加员工", operateType = OperateLogConstant.OPERATE_ADD)
    @SaAdminCheckPermission("admin:employee:add")
    @PostMapping("")
    public Result addEmployee(@RequestBody EmployeeCommand command) {
        employeeService.addEmployee(command);
        return Result.ok();
    }




    //后台修改员工信息
    //PUT
    //admin/employee
    @OperateLog(dateType = "修改员工", operateType = OperateLogConstant.OPERATE_UPDATE)
    @SaAdminCheckPermission("admin:employee:update")
    @PutMapping("")
    public Result updateEmployee(@RequestBody EmployeeCommand command) {
        employeeService.updateEmployee(command);
        return Result.ok();
    }

    //上传头像
    //POST
    //admin/employee/avatar
//    @OperateLog(dateType = "上传头像", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("avatar")
    public Result<String> uploadAvatar(@RequestParam("file")MultipartFile file) throws IOException {

        String upload = fileUploadService.upload(file);
        return Result.ok(upload);
    }




    //获取后台员工列表
    //GET
    //admin/employee/list
    //query	true
    //PageRequest
    //PageRequest
    @SaAdminCheckPermission("admin:employee:search")
    @GetMapping("list")
    public Result<PageDTO<EmployeeDTO>> getEmployeeList(PageRequest request) {
        PageDTO<EmployeeDTO> employeeList = employeeService.getEmployeePage(request);
        return Result.ok(employeeList);
    }




    //后台删除员工信息
    //DELETE
    //admin/employee/{employeeId}
    @OperateLog(dateType = "删除员工信息", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @SaAdminCheckPermission("admin:employee:delete")
    @DeleteMapping("{employeeId}")
    public Result deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        employeeService.delete(employeeId);
        return Result.ok();
    }



    //后台删除员工角色
    //DELETE
    //admin/employee/{employeeId}/{roleId}

    @OperateLog(dateType = "删除员工角色", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("{employeeId}/role/{roleId}")
    public Result deleteEmployeeRole(@PathVariable("employeeId") Long employeeId,
                                     @PathVariable("roleId") Long roleId) {
        employeeService.deleteRole(employeeId, roleId);
        return Result.ok();
    }


}










