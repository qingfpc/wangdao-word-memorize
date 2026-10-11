package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("admin_operate_log")
public class OperateLogDO {

    @TableId
    Long id;

    Long employeeId;

    String dataType;

    Integer operateType;

    String data;

    Integer status;

    Timestamp createTime;
}
