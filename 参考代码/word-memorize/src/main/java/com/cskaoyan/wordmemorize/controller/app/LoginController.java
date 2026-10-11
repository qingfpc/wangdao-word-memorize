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
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "移动端-登录模块")
@RestController
@RequestMapping("user")
public class LoginController {

    @Resource
    LoginService loginService;


    @Operation(summary = "****手机号+验证码登录",
            description = "当用户使用手机号，发送的验证码登录，如果还未注册则自动注册",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功. 返回登录成功之后对应的会话所关联的token"
                    ),
                    @ApiResponse(
                            responseCode = "215",
                            description = "本地登录失败"
                    ),
                    @ApiResponse(
                            responseCode = "217",
                            description = "保存验证码失败"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PostMapping("login/phone")
    public Result<AppLoginDTO> phoneLogin(@RequestBody PhoneNumberLoginCommand command, HttpServletRequest request) {
        // 检查校验码是否正确

        String ipAddr = IpUtil.getIpAddr(request);
        AppLoginDTO appLoginDTO = loginService.phoneNumberLogin(command, ipAddr);
        return Result.ok(appLoginDTO);
    }

    @Operation(summary = "*登出",
            description = "用于用户点击登出",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PostMapping("logout")
    public Result logout() {
        StpKit.USER.logout();
        return Result.ok();
    }
}
