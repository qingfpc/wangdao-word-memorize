package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Schema(description = "通用分页结果")
public class PageDTO <T>{

    @Schema(description = "分页列表")
    List<T> pageList;

    @Schema(description = "满足条件的总条数")
    Long totalCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDTO<?> pageDTO = (PageDTO<?>) o;
        return Objects.equals(pageList, pageDTO.pageList) && Objects.equals(totalCount, pageDTO.totalCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageList, totalCount);
    }
}
