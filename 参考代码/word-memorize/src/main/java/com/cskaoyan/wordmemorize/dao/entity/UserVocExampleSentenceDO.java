package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
@TableName("user_voc_sentence")
public class UserVocExampleSentenceDO extends BaseDO {
    Long userId;
    Long vocId;
    String sentence;
    String meaning;
    String vocContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocExampleSentenceDO that = (UserVocExampleSentenceDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocId, that.vocId) && Objects.equals(sentence, that.sentence) && Objects.equals(meaning, that.meaning) && Objects.equals(vocContent, that.vocContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, vocId, sentence, meaning, vocContent);
    }
}
