package com.cskaoyan.wordmemorize.service.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.ExcelParseException;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractExcelSaver<T, R> implements ExcelSaver<T, R> {


    List<R> targetList;

    private TransactionTemplate transactionTemplate;

    int batch;

    Integer totalItem;

    public void init(int batch, TransactionTemplate transactionTemplate) {
        this.batch = batch;
        this.transactionTemplate = transactionTemplate;
    }


    @Override
    public Integer upload(MultipartFile file, Class<T> clz, Function<T, R> converter) throws IOException {
        totalItem = 0;

        targetList = new ArrayList<>();
        EasyExcel.read(file.getInputStream(), clz, new AnalysisEventListener<T>(){

            @Override
            public void invoke(T data, AnalysisContext context) {
                totalItem++;
                int rowNum = context.readRowHolder().getRowIndex() + 1;
                R targetData = converter.apply(data);
                targetList.add(targetData);
                if (targetList.size() == batch) {
                    try {
                        // 批量保存数据
                        transactionalSave(targetList);
                        targetList.clear();
                    } catch (Exception e) {
                        ExcelParseException excelParseException = new ExcelParseException(ResultCodeEnum.EXCEL_SAVE_ERROR, rowNum, "保存失败");
                        throw excelParseException;
                    }

                }

            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if (!CollectionUtils.isEmpty(targetList)) {
                    transactionalSave(targetList);
                }
            }
        }).sheet().doRead();

        return totalItem;
    }


    public void transactionalSave(List<R> targetList) {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            doSave(targetList);
        });
    }

    public abstract void doSave(List<R> targetList);

}
