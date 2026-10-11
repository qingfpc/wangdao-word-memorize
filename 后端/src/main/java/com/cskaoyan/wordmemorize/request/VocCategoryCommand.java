package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "书籍类目请求体")
public class VocCategoryCommand {
    @Schema(description = "书籍类目id")
    String id;
    @Schema(description = "书籍类目名称")
    String categoryName;
    @Schema(description = "书籍类目上下架状态：0->下架；1->上架")
    Integer status;
    @Schema(description = "书籍类目排序字段")
    Integer sortBy;
}
