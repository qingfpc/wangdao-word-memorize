package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
@Schema(description = "修改等级特权请求体")
public class LevelPrivilegeCommand {

    @Schema(description = "id")
    String id;
    @Schema(description = "所属等级id")
    String levelId;
    @Schema(description = "所属等级值")
    Integer levelValue;
    @Schema(description = "特权名称")
    String privilegeName;
    @Schema(description = "特权描述")
    String description;
    @Schema(description = "特权描述")
    String privilegeCode;
    @Schema(description = "使用方式: 1. 免费 2.收费 3.先免费在收费")
    Integer useType;
    @Schema(description = "使用一次所需消耗的单词上限数量(仅针对收费或者新免费后收费)")
    Integer requireVocBound;
    @Schema(description = "免费使用次数(针对先免费后收费的情况)")
    Integer freeCount;
}
