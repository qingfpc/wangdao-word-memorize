package com.cskaoyan.wordmemorize.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除题目选项日志")
public class DeleteTopicOptionOperateDTO {
    @Schema(description = "题目id")
    Long tid;
    @Schema(description = "题目")
    String topic;
    @Schema(description = "选项id")
    Long optionId;
    @Schema(description = "选项")
    String option;
}
