package com.cskaoyan.wordmemorize.dto.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Schema(description = "员工信息")
public class EmployeeDTO {
    @Schema(description = "员工id")
    String id;
    @Schema(description = "员工姓名")
    String name;
    @Schema(description = "员工昵称")
    String nickName;
    @Schema(description = "员工密码")
    String password;
    @Schema(description = "员工电话")
    String phoneNumber;
    @Schema(description = "员工头像")
    String avatar;
    @Schema(description = "最近登录ip")
    String lastLoginIp;
    @Schema(description = "最近登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastLoginTime;
    @Schema(description = "roId集合")
    List<String> roleIds;
    @Schema(description = "permission_code集合")
    List<String> perms;

}
