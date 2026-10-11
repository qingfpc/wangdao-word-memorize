package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.common.util.IpUtil;
import com.cskaoyan.wordmemorize.dto.admin.AdminLoginDTO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.request.EmployeeLoginRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("admin")
public class AdminLoginController {



    @Resource
    EmployeeService employeeService;


    @PostMapping("login")
    public Result<AdminLoginDTO> login(@RequestBody EmployeeLoginRequest loginRequest, HttpServletRequest request) {

        // 匹配用户登录时输入的用户名和密码
        EmployeeDTO employee = employeeService.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 登录成功，开启用户的account-session
        String idStr = employee.getId();
        Long id = Long.parseLong(idStr);
        StpKit.ADMIN.login(id);


        // 更新用户登录的ip, 登录时间
        employeeService.updateLoginInfo(id, IpUtil.getIpAddr(request), LocalDateTime.now());


        // 获取用户的account-session对应的token
        String tokenValue = StpKit.ADMIN.getTokenValue();
        AdminLoginDTO adminLoginDTO = new AdminLoginDTO();
        adminLoginDTO.setToken(tokenValue);

        return Result.ok(adminLoginDTO);
    }
}
