package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VocPageRequest extends PageRequest {

    @Parameter(description = "书籍id")
    String bookId;
    @Parameter(description = "章节id(移动端获取书籍单词列表不需要传)")
    String chapterId;
    @Parameter(description = "题目类型: 单选题或者多选题")
    Integer topicType;
}
