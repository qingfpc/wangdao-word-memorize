package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.List;

@Data
@TableName("review_topic")
public class ReviewTopicDO extends BaseDO {

    Long vocId;

    Integer topicType;

    String topic;

    String vocabulary;

    @TableField(exist = false)
    List<TopicOptionDO> options;
}
