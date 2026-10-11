package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("voc_pay_sale")
public class VocPaySaleDO extends BaseDO {

    Long userId;

    Long itemId;

    String itemName;

    Integer itemType;

    BigDecimal payPrice;

    Integer status;

    LocalDateTime createTime;
}
