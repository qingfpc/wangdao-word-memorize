package com.cskaoyan.wordmemorize.common.exception;

import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;

public class ExcelParseException extends BusinessException {
    // 行数
    private int rowNum;
    // 列数
    private int colNum;
    // 单元格值
    private String cellValue;

    public ExcelParseException(ResultCodeEnum codeEnum, int rowNum, int colNum, String cellValue, String message) {
        super(message, codeEnum.getCode());
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.cellValue = cellValue;
    }

    public ExcelParseException(ResultCodeEnum codeEnum, int rowNum, String message) {
        super(message, codeEnum.getCode());
        this.rowNum = rowNum;
        this.colNum = -1;
        this.cellValue = null;
    }

    public ExcelParseException(ResultCodeEnum codeEnum, String message) {
        super(message, codeEnum.getCode());
        this.rowNum = -1;
        this.colNum = -1;
        this.cellValue = null;
    }

    @Override
    public String getMessage() {
        if (rowNum >= 0 && colNum >= 0 && cellValue != null) {
            return String.format("Excel解析错误(行:%d,列:%d,值:%s) - %s",
                                rowNum, colNum, cellValue, super.getMessage());
        } else if (rowNum >= 0) {
            return String.format("Excel解析错误(行:%d) - %s", rowNum, super.getMessage());
        } else {
            return String.format("Excel解析错误 - %s", super.getMessage());
        }
    }
}
