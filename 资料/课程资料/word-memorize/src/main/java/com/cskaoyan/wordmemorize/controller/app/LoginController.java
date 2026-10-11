package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.common.util.IpUtil;
import com.cskaoyan.wordmemorize.dto.app.AppLoginDTO;
import com.cskaoyan.wordmemorize.request.PhoneNumberLoginCommand;
import com.cskaoyan.wordmemorize.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class LoginController {

    @Resource
    LoginService loginService;


    // APP端手机号登录
    @PostMapping("login/phone")
    public Result<AppLoginDTO> phoneLogin(@RequestBody PhoneNumberLoginCommand command, HttpServletRequest request) {
        // 检查校验码是否正确

        String ipAddr = IpUtil.getIpAddr(request);
        AppLoginDTO appLoginDTO = loginService.phoneNumberLogin(command, ipAddr);
        return Result.ok(appLoginDTO);
    }

    // APP端登出
    @PostMapping("logout")
    public Result logout() {
        StpKit.USER.logout();
        return Result.ok();
    }
}
