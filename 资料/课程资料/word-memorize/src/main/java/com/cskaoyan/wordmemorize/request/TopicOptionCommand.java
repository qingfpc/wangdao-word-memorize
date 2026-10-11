package com.cskaoyan.wordmemorize.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class TopicOptionCommand {

    @Schema(description = "id值")
    String id;
    @Schema(description = "选项所属的题目id")
    Long tid;
    @Schema(description = "选项")
    String item;
    @Schema(description = "该选项是否是答案，1是，0否")
    Integer isAnswer;
    @Schema(description = "该选项编号，1,2,3,4(针对单选题)")
    Integer optionNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicOptionCommand that = (TopicOptionCommand) o;
        return Objects.equals(id, that.id) && Objects.equals(tid, that.tid) && Objects.equals(item, that.item) && Objects.equals(isAnswer, that.isAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tid, item, isAnswer);
    }
}
