package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通用分页查询请求")
public class PageRequest {

    @Parameter(description = "查询的页数")
    Integer pageNum;
    @Parameter(description = "每页的条数")
    Integer count;
    @Parameter(description = "查询的关键词(后台需要，app接口不需要)")
    String keyword;

    public int getOffset() {
        if (pageNum == null || count == null) {
            return 0;
        }
        return (pageNum - 1) * count;
    }
}
