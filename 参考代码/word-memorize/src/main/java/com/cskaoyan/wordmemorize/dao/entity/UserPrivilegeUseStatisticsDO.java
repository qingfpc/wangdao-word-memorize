package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

@TableName("user_privilege_use_statistics")
@Data
public class UserPrivilegeUseStatisticsDO extends BaseDO {

    Long userId;
    String privilegeCode;

    Integer useCount;

}
