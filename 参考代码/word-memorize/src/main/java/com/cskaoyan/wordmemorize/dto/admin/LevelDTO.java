package com.cskaoyan.wordmemorize.dto.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LevelDTO {

    String id;

    String levelName;

    Integer levelValue;

    /**
     签到天数
     */
    Integer checkInDays;

    /**
     *   签到类型
     */
    Integer checkInType;

    /**
     解锁所需的金额
     */
    BigDecimal unlockPrice;

    String description;


    /**
     *  特权集合
     */
    List<LevelPrivilegeDTO> privileges;
}
