package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/employee")
public class AdminEmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("info")
    public Result<EmployeeDTO> getEmployeeInfo() {
        // 获取当前登录的用户id
        Long employeeId = StpKit.ADMIN.getLoginIdAsLong();

        EmployeeDTO employeeInfo = employeeService.getEmployeeInfo(employeeId);

        return Result.ok(employeeInfo);
    }
}
