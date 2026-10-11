package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.List;

@Data
@TableName("review_sub_topic")
public class ReviewSubTopicDO extends BaseDO {

    Long tid;

    Long vocId;

    String vocabulary;

    String topicItem;

    Integer subTopicNumber;
}
