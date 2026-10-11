package com.cskaoyan.wordmemorize.dto.admin;

import lombok.Data;

@Data
public class ExcelAddOperateDTO {

    /**
     *  excel文件名称
     */
    String fileName;

    /**
     *  处理条数
     */
    Integer totalCount;
}
