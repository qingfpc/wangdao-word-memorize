package com.cskaoyan.wordmemorize.controller.admin;

import cn.dev33.satoken.session.SaSession;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.common.util.IpUtil;
import com.cskaoyan.wordmemorize.dto.admin.AdminLoginDTO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.request.EmployeeLoginRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "后台管理-登录登出相关接口")
@RestController
@RequestMapping("admin")
public class AdminLoginController {

    @Resource
    EmployeeService employeeService;
    @Operation(summary = "****后台登录接口",
            description = "主要用于后台工作人员登录后台系统",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登录成功，注意登录成功会返回token字符串，用于后续的请求"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("login")
    public Result<AdminLoginDTO> login(@RequestBody EmployeeLoginRequest employeeLoginRequest, HttpServletRequest request) {

        // 验证登录
        EmployeeDTO employeeDTO = employeeService.login(employeeLoginRequest.getUsername(), employeeLoginRequest.getPassword());
        EmployeeDTO employeeInfo = employeeService.getEmployeeInfo(Long.parseLong(employeeDTO.getId()));

        // 登录
        StpKit.ADMIN.login(Long.parseLong(employeeDTO.getId()));
        SaSession session = StpKit.ADMIN.getSession();
        session.set("employee", employeeInfo);

        // 更新登录信息
        long employeeId = Long.parseLong(employeeDTO.getId());
        employeeService.updateLoginInfo(employeeId, IpUtil.getIpAddr(request), LocalDateTime.now());

        AdminLoginDTO adminLogin = new AdminLoginDTO();
        // 设置token字符串
        adminLogin.setToken(StpKit.ADMIN.getTokenValue());
        return Result.ok(adminLogin);
    }

    @Operation(summary = "*后台员工登出",
            description = "后台工作人员退出登录",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PutMapping("logout")
    public Result logout() {
        StpKit.ADMIN.logout();
        return Result.ok();
    }

}
