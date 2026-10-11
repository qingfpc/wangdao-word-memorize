package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeCommand {
    @Schema(description = "后台员工id")
    String id;
    @Schema(description = "员工姓名")
    String name;
    @Schema(description = "员工昵称")
    String nickName;
    @Schema(description = "员工密码")
    String password;
    @Schema(description = "员工头像")
    String avatar;
    @Schema(description = "员工电话")
    String phoneNumber;
    @Schema(description = "员工所拥有的角色集合")
    List<String> roleIds;

}
