package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色请求体")
public class RoleCommand {
    @Schema(description = "角色id")
    String id;
    @Schema(description = "角色名称")
    String name;
    @Schema(description = "角色启用状态: 0未启用，1启用")
    Integer state;
    @Schema(description = "角色描述")
    String description;
}
