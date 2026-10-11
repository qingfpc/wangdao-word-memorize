package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VocCategoryDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "类目名称")
    String categoryName;
    @Schema(description = "类目状态")
    Integer status;
    @Schema(description = "排序字段")
    Integer sortBy;
//    @Schema(description = "包含的书籍数量")
//    Integer bookCount;
}
