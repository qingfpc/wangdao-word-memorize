package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.Objects;

@TableName("user_voc_ai_situation_option")
@Data
public class UserVocAISituationOptionDO extends BaseDO {

    Long situationId;
    String name;

    String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocAISituationOptionDO that = (UserVocAISituationOptionDO) o;
        return Objects.equals(situationId, that.situationId) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), situationId, name, description);
    }
}
