package com.cskaoyan.wordmemorize.service.excel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.ExcelParseException;
import com.cskaoyan.wordmemorize.dao.entity.VocBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapterDO;
import com.cskaoyan.wordmemorize.dao.mapper.VocBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapterMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VocExcelValidator extends AbstractVocExcelValidator{

    @Resource
    ResourceLoader resourceLoader;

    @Resource
    VocBookMapper vocBookMapper;

    @Resource
    VocChapterMapper vocChapterMapper;

    @PostConstruct
    public void init() {
        super.init(resourceLoader);
    }

    @Override
    public void doValidate(Integer rowNum, Map<Integer, String> data) {
        // 验证释义格式
        String interpretations = data.get(5);
        String[] definitionLines = interpretations.split("\n");
        for (int i = 0; i < definitionLines.length; i++) {
            String definition = definitionLines[i].trim();
            if (!definition.matches("^[a-z]+\\.\\s+.+$")) {
                ExcelParseException excelParseException = new ExcelParseException(
                        ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
                        rowNum, 5, definition,
                        "释义格式不正确，应为'词性. 释义内容'");
                throw excelParseException;
            }
        }


        // 验证例句格式
        String examples = data.get(6);
        String[] exampleLines = examples.split("\n");
        if (exampleLines.length % 2 != 0) {
            ExcelParseException excelParseException = new ExcelParseException(
                    ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
                    rowNum, 6, examples,
                    "例句格式不正确，每个例句后应紧跟其翻译");
            throw excelParseException;
        }


        String ukAudioFile = data.get(3);
//        // 验证音频文件格式
//        if (!ukAudioFile.endsWith(".mp3")) {
//            ExcelParseException excelParseException = new ExcelParseException(
//                    ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
//                    rowNum, 3, ukAudioFile,
//                    "英式发音文件格式不正确，应为.mp3格式");
//            throw excelParseException;
//        }
//
        String usAudioFile = data.get(4);
//        if (!usAudioFile.endsWith(".mp3")) {
//            ExcelParseException excelParseException = new ExcelParseException(
//                    ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
//                    rowNum, 4, usAudioFile,
//                    "美式发音文件格式不正确，应为.mp3格式");
//            throw excelParseException;
//        }

        String bookInfo = data.get(7);
        if (bookInfo != null && !bookInfo.trim().isEmpty()) {
            String[] location = bookInfo.split("#");
            if (location.length == 1) {
                LambdaQueryWrapper<VocBookDO> bookQueryWrapper = new LambdaQueryWrapper<VocBookDO>()
                        .eq(VocBookDO::getName, location[0]);
                VocBookDO vocBookDO = vocBookMapper.selectOne(bookQueryWrapper);
                if (vocBookDO == null) {
                    ExcelParseException excelParseException = new ExcelParseException(
                            ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
                            rowNum, 7, usAudioFile,
                            "单词所属的书籍不存在!");
                    throw excelParseException;
                }
                return;
            }

            if (location.length == 2) {
                LambdaQueryWrapper<VocChapterDO> bookQueryWrapper = new LambdaQueryWrapper<VocChapterDO>()
                        .eq(VocChapterDO::getBookName, location[0])
                        .eq(VocChapterDO::getChapterName, location[1]);
                Long l = vocChapterMapper.selectCount(bookQueryWrapper);
                if (l == 0) {
                    ExcelParseException excelParseException = new ExcelParseException(
                            ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
                            rowNum, 7, usAudioFile,
                            "单词所属的书籍或章节不存在!");
                    throw excelParseException;
                }
                return;
            }

            ExcelParseException excelParseException = new ExcelParseException(
                    ResultCodeEnum.VOC_EXCEL_DATA_ERROR,
                    rowNum, 7, usAudioFile,
                    "单词书籍和章节信息格式不正确!");
            throw excelParseException;
        }
    }
}
