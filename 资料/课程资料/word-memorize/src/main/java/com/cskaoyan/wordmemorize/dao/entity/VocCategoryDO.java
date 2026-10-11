package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

@Data
@TableName("book_category")
public class VocCategoryDO extends BaseDO {

    String categoryName;

    Integer sortBy;

    Integer bookCount;
}
