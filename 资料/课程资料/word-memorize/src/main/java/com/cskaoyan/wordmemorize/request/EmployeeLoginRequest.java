package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "后台员工登录请求")
public class EmployeeLoginRequest {

    @Parameter(description = "登录用户名")
    String username;
    @Parameter(description = "登录密码")
    String password;
}
