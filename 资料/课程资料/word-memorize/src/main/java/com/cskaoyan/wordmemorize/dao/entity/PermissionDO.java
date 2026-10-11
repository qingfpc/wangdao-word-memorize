package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

@Data
@TableName("permission")
public class PermissionDO extends BaseDO {
    Long parentPermission;
    String name;
    String permissionAlias;
    Integer permissionType;
    String permissionCode;
    @TableField("`desc`")
    String desc;
    String path;
    String accessType;
}
