package com.cskaoyan.wordmemorize.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/*
    封装删除题目操作日志
 */
@Data
@Schema(description = "删除题目操作日志")
public class DeleteTopicOperateDTO {

    @Schema(description = "题目id")
    Long id;
    /**
     * 题目内容
     */
    @Schema(description = "题目内容")
    String topic;

    @Schema(description = "题目对应的单词")
    String vocabulary;
}
