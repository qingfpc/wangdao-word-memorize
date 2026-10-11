package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class UserVocAISituationDTO {

    @Schema(description = "模式id")
    String id;
    @Schema(description = "模式名称")
    String name;
    @Schema(description = "状态")
    Integer status;
    @Schema(description = "模式描述")
    String description;
    @Schema(description = "模式包含的选项集合")
    List<UserVocAISituationOptionDTO> options;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocAISituationDTO that = (UserVocAISituationDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status);
    }
}
