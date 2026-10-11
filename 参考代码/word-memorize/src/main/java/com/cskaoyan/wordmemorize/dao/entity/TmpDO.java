package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.List;

@Data
@TableName("voc")
public class TmpDO extends BaseDO {
    String content;

    String ukPhonetic;
    String usPhonetic;
    String usPronounciation;
    String ukPronounciation;

    // 移动端需要：标识单词是否已选
    @TableField(exist = false)
    Integer reviewed;
    // 移动端需要: 标识单词是否不背
    @TableField(exist = false)
    Integer notRemember;

    // 单词释义
    @TableField(exist = false)
    List<VocMeaningDO> interpretations;

    // 单词例句
    @TableField(exist = false)
    List<ExampleSentenceDO> examples;

    // 用户自建例句(移动端需要)
    @TableField(exist = false)
    List<UserVocExampleSentenceDO> userExamples;
}
