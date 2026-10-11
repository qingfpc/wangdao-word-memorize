package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class VocBookPageRequest extends PageRequest {

    @Parameter(description = "书籍所属类目id")
    String categoryId;
}
