package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppLoginDTO {

    @Schema(description = "是否首次登录")
    Boolean isFirst;
    @Schema(description = "登录token字符串")
    String token;
}
