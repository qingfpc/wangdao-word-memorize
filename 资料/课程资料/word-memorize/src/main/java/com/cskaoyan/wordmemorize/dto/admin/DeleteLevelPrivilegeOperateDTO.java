package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除用户等级权限操作日志")
public class DeleteLevelPrivilegeOperateDTO {
    @Schema(description = "特权id")
    Long id;

    @Schema(description = "特权名称")
    String privilegeName;
    @Schema(description = "用户等级id")
    Long levelId;
    @Schema(description = "用户等级名称")
    String levelValue;

}
