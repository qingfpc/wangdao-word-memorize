package com.cskaoyan.wordmemorize.service.excel;

import java.io.InputStream;
import java.util.Map;

public interface ExcelValidator {

    void validate(String templatePath, InputStream inputStream);

}
