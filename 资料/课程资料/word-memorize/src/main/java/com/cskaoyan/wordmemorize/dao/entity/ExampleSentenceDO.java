package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

@Data
@TableName("example_sentence")
public class ExampleSentenceDO extends BaseDO {

    Long vocId;

    String content;

    String meaning;
}
