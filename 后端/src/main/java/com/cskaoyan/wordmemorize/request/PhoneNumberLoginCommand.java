package com.cskaoyan.wordmemorize.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PhoneNumberLoginCommand {

    @Schema(description = "手机号")
    String phoneNumber;
    @Schema(description = "昵称")
    String displayName;
    @Schema(description = "头像url")
    String photoUrl;
    @Schema(description = "email")
    String email;
}
