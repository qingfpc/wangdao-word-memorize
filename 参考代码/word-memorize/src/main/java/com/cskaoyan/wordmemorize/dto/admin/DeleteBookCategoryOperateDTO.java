package com.cskaoyan.wordmemorize.dto.admin;

import lombok.Data;

/**
 *  封装删除书籍分类操作日志
 */
@Data
public class DeleteBookCategoryOperateDTO {

    /**
     * 书籍分类ID
     */
    Long id;
    /**
     * 书籍分类名称
     */
    String categoryName;
}
