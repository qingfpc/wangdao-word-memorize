package com.cskaoyan.wordmemorize.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/*
    封装删除单词操作日志
 */
@Data
@Schema(description = "删除单词日志")
public class DeleteVocOperateDTO {

    /**
     * 单词ID
     */
    @Schema(description = "单词id")
    Long id;
    /**
     * 单词
     */
    @Schema(description = "单词")
    String content;
}
