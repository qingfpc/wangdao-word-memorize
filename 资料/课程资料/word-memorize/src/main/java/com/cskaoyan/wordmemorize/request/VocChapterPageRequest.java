package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class VocChapterPageRequest extends PageRequest {
    @Parameter(description = "章节所属的书籍id")
    String bookId;
}
