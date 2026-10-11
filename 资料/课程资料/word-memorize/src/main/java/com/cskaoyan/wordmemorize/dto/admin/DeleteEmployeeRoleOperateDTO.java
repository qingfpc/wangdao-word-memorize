package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除员工角色日志")
public class DeleteEmployeeRoleOperateDTO {
    @Schema(description = "员工id")
    Long employeeId;
    @Schema(description = "员工姓名")
    String employeeName;
    @Schema(description = "角色id")
    String roleId;
    @Schema(description = "角色名称")
    String roleName;
}
