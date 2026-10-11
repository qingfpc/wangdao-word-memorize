package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户当前正在查看的书籍的统计信息")
public class UserBookStatisticsDTO {

    @Schema(description = "单词总数")
    private Integer total;
    @Schema(description = "已选择单词数量")
    private Integer selected;
    @Schema(description = "未选的数量")
    private Integer unSelected;
}
