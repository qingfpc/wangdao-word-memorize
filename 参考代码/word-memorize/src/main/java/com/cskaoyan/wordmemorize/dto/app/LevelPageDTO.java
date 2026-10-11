package com.cskaoyan.wordmemorize.dto.app;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cskaoyan.wordmemorize.dao.entity.LevelPrivilegeDO;
import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "在等级页面展示的等级信息")
public class LevelPageDTO {

    @Schema(description = "等级id")
    String id;

    @Schema(description = "等级名称")
    String levelName;

    @Schema(description = "等级值")
    Integer levelValue;

    /**
     签到天数
     */
    @Schema(description = "签到天数")
    Integer checkInDays;

    /**
     *   签到类型
     */
    @Schema(description = "签到类型")
    Integer checkInType;

    /**
     解锁所需的金额
     */
    @Schema(description = "解锁金额")
    BigDecimal unlockPrice;

    @Schema(description = "描述")
    String description;

    @Schema(description = "商品id(等级对应的等级解锁商品id)")
    String goodsId;

    @Schema(description = "等级特权集合")
    List<LevelPrivilegeDTO> levelPrivileges;


}
