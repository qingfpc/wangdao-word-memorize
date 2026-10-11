package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@TableName("user_voc_ai_situation")
public class UserVocAISituationDO extends BaseDO {

    String name;

    Integer status;

    String description;

    @TableField(exist = false)
    List<UserVocAISituationOptionDO> options;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocAISituationDO that = (UserVocAISituationDO) o;
        return Objects.equals(name, that.name) && Objects.equals(status, that.status) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, status, description);
    }
}
