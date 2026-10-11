package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "封装APP查询书籍列表的参数")
public class AppVocPageRequest {

    @Schema(description = "用户id")
    String userId;
    @Schema(description = "要查询的书籍名称")
    String bookId;
    @Schema(description = "要查询的页数")
    Integer pageNum;
    @Schema(description = "要查询的每页条数")
    Integer count;
}
