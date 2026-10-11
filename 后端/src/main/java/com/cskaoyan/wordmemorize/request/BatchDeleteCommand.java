package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "批量删除请求体")
public class BatchDeleteCommand {

    @Schema(description = "要删除的id集合")
    List<String> ids;
}
