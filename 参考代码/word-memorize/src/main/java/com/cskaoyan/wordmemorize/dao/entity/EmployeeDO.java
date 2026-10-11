package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("employee")
public class EmployeeDO extends BaseDO {

    String name;

    String nickName;

    String phoneNumber;

    String password;

    String avatar;

    String lastLoginIp;

    LocalDateTime lastLoginTime;

    @TableField(exist = false)
    List<Long> roleIds;

    // permission_code集合
    @TableField(exist = false)
    List<String> perms;
}
