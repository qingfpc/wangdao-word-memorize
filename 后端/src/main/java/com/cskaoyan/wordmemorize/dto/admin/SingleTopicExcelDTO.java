package com.cskaoyan.wordmemorize.dto.admin;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SingleTopicExcelDTO {

    /**
     * 单选题目
     * Excel第1列
     */
    @ExcelProperty(index = 0)
    private String topic;

    /**
     * 选项1
     * Excel第2列
     */
    @ExcelProperty(index = 1)
    private String option1;

    /**
     * 选项2
     * Excel第3列
     */
    @ExcelProperty(index = 2)
    private String option2;

    /**
     * 选项3
     * Excel第4列
     */
    @ExcelProperty(index = 3)
    private String option3;

    /**
     * 选项4
     * Excel第5列
     */
    @ExcelProperty(index = 4)
    private String option4;

    /**
     * 正确答案
     * Excel第6列
     */
    @ExcelProperty(index = 5)
    private Integer answerIndex;

}
