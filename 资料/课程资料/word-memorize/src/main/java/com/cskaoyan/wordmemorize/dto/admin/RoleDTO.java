package com.cskaoyan.wordmemorize.dto.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色信息")
public class RoleDTO {
    @Schema(description = "角色id")
    String id;
    @Schema(description = "角色名称")
    String name;
    @Schema(description = "角色描述")
    String description;
}
