package com.cskaoyan.wordmemorize.dto.admin;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class FillTopicExcelDTO {

    /**
     * 单选题目
     * Excel第1列
     */
    @ExcelProperty(index = 0)
    private String subTopic1;

    /**
     * 选项1
     * Excel第2列
     */
    @ExcelProperty(index = 1)
    private String answer1;

    /**
     * 选项2
     * Excel第3列
     */
    @ExcelProperty(index = 2)
    private String subTopic2;

    /**
     * 选项3
     * Excel第4列
     */
    @ExcelProperty(index = 3)
    private String answer2;

    /**
     * 选项4
     * Excel第5列
     */
    @ExcelProperty(index = 4)
    private String subTopic3;

    /**
     * 正确答案
     * Excel第6列
     */
    @ExcelProperty(index = 5)
    private String answer3;

    /**
     * 正确答案
     * Excel第7列
     */
    @ExcelProperty(index = 6)
    private String error;

}
