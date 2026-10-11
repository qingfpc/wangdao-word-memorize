package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class UserVocAISituationOptionCommand {

    @Schema(description = "选项id")
    String id;
    @Schema(description = "选项所属的模式id")
    String situationId;
    @Schema(description = "选项名称")
    String name;
    @Schema(description = "选项描述")
    String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocAISituationOptionCommand that = (UserVocAISituationOptionCommand) o;
        return Objects.equals(id, that.id) && Objects.equals(situationId, that.situationId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, situationId, name);
    }
}
