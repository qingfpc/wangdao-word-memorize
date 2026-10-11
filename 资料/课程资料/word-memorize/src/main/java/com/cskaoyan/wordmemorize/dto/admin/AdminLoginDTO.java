package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminLoginDTO {
    @Schema(description = "登录token字符串")
    String token;
}
