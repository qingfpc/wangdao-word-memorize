package com.cskaoyan.wordmemorize.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class TopicOptionDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "选项所属的题目id")
    String tid;
    @Schema(description = "选项")
    String item;
    @Schema(description = "该选项是否是答案")
    Integer isAnswer;
    @Schema(description = "单选题编号")
    Integer optionNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicOptionDTO that = (TopicOptionDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(tid, that.tid) && Objects.equals(item, that.item) && Objects.equals(isAnswer, that.isAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tid, item, isAnswer);
    }
}
