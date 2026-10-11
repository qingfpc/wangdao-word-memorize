package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.Objects;

@TableName("voc_meaning")
@Data
public class VocMeaningDO extends BaseDO {

    Long vocId;

    String type;

    String interContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VocMeaningDO that)) return false;
        return Objects.equals(vocId, that.vocId) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vocId, type, interContent);
    }
}
