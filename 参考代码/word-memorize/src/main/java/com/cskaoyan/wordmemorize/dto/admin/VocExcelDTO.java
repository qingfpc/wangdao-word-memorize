package com.cskaoyan.wordmemorize.dto.admin;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class VocExcelDTO {
    /**
     * 单词
     * Excel第1列
     */
    @ExcelProperty(index = 0)
    private String content;

    /**
     * 英式音标
     * Excel第2列
     */
    @ExcelProperty(index = 1)
    private String ukPhonetic;

    /**
     * 美式音标
     * Excel第3列
     */
    @ExcelProperty(index = 2)
    private String usPhonetic;

    /**
     * 英式发音文件名
     * Excel第4列
     */
    @ExcelProperty(index = 3)
    private String ukPronounciation;

    /**
     * 美式发音文件名
     * Excel第5列
     */
    @ExcelProperty(index = 4)
    private String usPronounciation;

    /**
     * 单词释义
     * Excel第6列
     * 多个释义以换行符分隔
     * 格式为：词性. 释义内容
     */
    @ExcelProperty(index = 5)
    private String interpretations;

    /**
     * 例句与翻译
     * Excel第7列
     * 例句和翻译交替出现，以换行符分隔
     */
    @ExcelProperty(index = 6)
    private String exampleSentences;

    /**
     * 书籍和章节信息
     * Excel第8列
     * 格式为：书籍名称#章节名称
     */
    @ExcelProperty(index = 7)
    private String bookInfos;

}
