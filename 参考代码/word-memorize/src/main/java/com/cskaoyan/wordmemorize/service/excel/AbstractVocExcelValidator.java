package com.cskaoyan.wordmemorize.service.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.ExcelParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public abstract class AbstractVocExcelValidator implements ExcelValidator{

    private ResourceLoader resourceLoader;

    public void init(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @Override
    public void validate(String templatePath, InputStream inputStream) {

        // 校验excel头
        Map<Integer, String> headerMap = readTemplateHead(templatePath, inputStream);
        if (headerMap == null) {
            throw new ExcelParseException(ResultCodeEnum.VOC_EXCEL_TEMPLATE_READ_ERROR, "Excel模板读取失败");
        }

        // 校验exel数据
        EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>() {
            private boolean headersValid = false;

            @Override
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                headersValid = validateHeaders(headerMap, headMap);
                if (!headersValid) {
                    throw new ExcelParseException(ResultCodeEnum.VOC_EXCEL_FORMAT_ERROR,
                            "Excel表头格式不正确，与模板不匹配");
                }
            }

            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
                if (!headersValid) return;

                int rowNum = context.readRowHolder().getRowIndex() + 1;
                // 保证excel中没有空数据
                ensureRowData(rowNum, data);

                // 数据处理逻辑 - 这里只是验证，并不需要处理数据
                doValidate(rowNum, data);

            }

            @Override
            public boolean hasNext(AnalysisContext context) {
                return !headersValid;
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                log.info("Excel解析完成");
            }
        }).sheet().doRead();

    }

    public Map<Integer, String> readTemplateHead(String templatePath, InputStream inputStream){
        Resource resource = resourceLoader.getResource(templatePath);
        if (!resource.exists()) {
            log.error("找不到模板文件: {}", templatePath);
            return null;
        }

        final Map<Integer, String>[] headerMap = new Map[1]; // 使用数组存储以便在匿名类中修改
        try (InputStream is = resource.getInputStream()) {
            EasyExcel.read(is, new AnalysisEventListener<Map<Integer, String>>() {
                @Override
                public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                    headerMap[0] = headMap;
                }

                @Override
                public void invoke(Map<Integer, String> data, AnalysisContext context) {
                    // 不需要处理内容行
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 不会被调用
                }

                @Override
                public boolean hasNext(AnalysisContext context) {
                    return headerMap[0] == null;
                }
            }).sheet().doRead();
        } catch (IOException e) {
            throw new ExcelParseException(ResultCodeEnum.VOC_EXCEL_TEMPLATE_READ_ERROR, "Excel模板读取失败");
        }


        return headerMap[0];
    }

    public abstract void doValidate(Integer rowNum, Map<Integer, String> data);

    public void ensureRowData(int rowNum, Map<Integer, String> data) {

        data.keySet().forEach(key -> {
            checkNull(data.get(key), rowNum, "第" + key + "列不能为空");
        });

    }

    private static void checkNull(String content, int rowNum, String fieldName) {
        if (content == null || content.trim().isEmpty()) {
            ExcelParseException excelParseException = new ExcelParseException(
                    ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
                    rowNum, 0, "<空>",
                    fieldName + "不能为空");
            throw excelParseException;
        }
    }

    private boolean validateHeaders(Map<Integer, String> templateHeader, Map<Integer, String> fileHeader) {
        // 检查列数是否相同
        if (templateHeader.size() != fileHeader.size()) {
            return false;
        }

        // 检查每列的标题是否相同
        for (Integer key : templateHeader.keySet()) {
            if (!templateHeader.get(key).equals(fileHeader.get(key))) {
                return false;
            }
        }

        return true;
    }
}
