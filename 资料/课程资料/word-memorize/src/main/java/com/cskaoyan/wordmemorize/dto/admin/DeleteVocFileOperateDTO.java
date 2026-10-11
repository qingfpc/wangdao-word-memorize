package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除文件操作日志")
public class DeleteVocFileOperateDTO {
    @Schema(description = "删除的音频文件对应的单词id")
    Long vocId;
    @Schema(description = "删除音频文件的单词")
    String content;
    @Schema(description = "删除音频类型: uk, us")
    String type;
    @Schema(description = "本删除文件的名称")
    String fileName;
}
