package com.cskaoyan.wordmemorize.service.excel;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.VocExcelDTO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class VocExcelSaver extends AbstractExcelSaver<VocExcelDTO, VocExcelParsedDO>{


    @Resource
    TransactionTemplate transactionTemplate;

    @Resource
    VocMapper vocMapper;

    @Resource
    VocMeaningMapper vocMeaningMapper;

    @Resource
    VocChapBookMapper vocChapBookMapper;

    @Resource
    VocChapterMapper vocChapterMapper;

    @Resource
    ExampleSentenceMapper exampleSentenceMapper;
    @PostConstruct
    public void init() {
        super.init(CommonConstant.BATCH, transactionTemplate);
    }
    @Override
    public void doSave(List<VocExcelParsedDO> targetList) {
        for (VocExcelParsedDO vocExcelParsedDO : targetList) {
            VocDO vocabulary = vocExcelParsedDO.getVoc();
            Long vocId = saveVoc(vocabulary);
            saveInterpretations(vocId, vocabulary.getInterpretations());
            saveExampleSentence(vocId, vocabulary.getExamples());
            saveVocChapBook(vocId, vocExcelParsedDO.getVocChapBooks());
        }
    }


    private void saveVocChapBook(Long vocId, List<VocChapBookDO> vocChapBooks) {
        for (VocChapBookDO vocChapBookDO : vocChapBooks) {
            vocChapBookDO.setVocId(vocId);
            vocChapBookDO.setVocName(vocChapBookDO.getVocName());
            vocChapBookMapper.insert(vocChapBookDO);

            // 更新数量
            // 针对每一个关系，修改对应书籍，章节的单词数量
            if (vocChapBookDO.getChapterId() != null) {
                LambdaUpdateWrapper<VocChapterDO> vocChapterWrapper = new LambdaUpdateWrapper<>();
                vocChapterWrapper
                        .eq(VocChapterDO::getId, vocChapBookDO.getChapterId())
                        .setSql("voc_count = voc_count + 1");
                vocChapterMapper.update( vocChapterWrapper);
            }

            LambdaUpdateWrapper<VocBookDO> vocBookWrapper = new LambdaUpdateWrapper<>();
            vocBookWrapper
                    .eq(VocBookDO::getId, vocChapBookDO.getBookId())
                    .setSql("voc_count = voc_count + 1");

        }
    }

    private void saveExampleSentence(Long vocId, List<ExampleSentenceDO> examples) {
        for (ExampleSentenceDO sentenceDO : examples) {
            sentenceDO.setVocId(vocId);
            exampleSentenceMapper.insert(sentenceDO);
        }
    }

    private void saveInterpretations(Long vocId, List<VocMeaningDO> interpretations) {
        for (VocMeaningDO vocMeaningDO : interpretations) {
            vocMeaningDO.setVocId(vocId);
            vocMeaningMapper.insert(vocMeaningDO);
        }
    }

    private Long saveVoc(VocDO vocabulary) {
        vocMapper.insert(vocabulary);
        return vocabulary.getId();
    }
}
