package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@TableName("goods")
@Data
public class GoodsDO extends BaseDO {

    String name;

    BigDecimal price;

    @TableField("`desc`")
    String desc;

    Integer status;

    Integer value;

    Integer type;
}
