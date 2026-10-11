package com.cskaoyan.wordmemorize.dao.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

@Data
@TableName("topic_options")
public class TopicOptionDO extends BaseDO {

    Long tid;

    String item;

    Integer isAnswer;

    Integer optionNumber;
}
