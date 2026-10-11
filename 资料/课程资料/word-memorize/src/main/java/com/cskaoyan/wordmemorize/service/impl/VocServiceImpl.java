package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.VocConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.DisplayBookVocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayChapterDTO;
import com.cskaoyan.wordmemorize.dto.app.UserBookStatisticsDTO;
import com.cskaoyan.wordmemorize.request.*;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class VocServiceImpl implements VocService {


    @Autowired
    VocMapper vocMapper;

    @Autowired
    VocConverter vocConverter;


    @Autowired
    VocMeaningMapper vocMeaningMapper;

    @Autowired
    ExampleSentenceMapper exampleSentenceMapper;

    @Autowired
    VocChapBookMapper vocChapBookMapper;

    @Autowired
    VocChapterMapper vocChapterMapper;


    @Autowired
    VocBookMapper vocBookMapper;

    @Override
    public PageDTO<VocDTO> getVocabularies(VocPageRequest pageQuery) {
        return null;
    }

    @Override
    public void addVoc(VocCommand command) {

        // 判断要添加的单词是否存在
        LambdaQueryWrapper<VocDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VocDO::getContent, command.getContent());

        VocDO vocDO = vocMapper.selectOne(queryWrapper);
        if (vocDO != null) {
           throw new BusinessException(ResultCodeEnum.VOC_EXISTED);
        }

        // 保存单词
        VocDO voc = vocConverter.command2VocDO(command);
        // 保存基本的单词数据
        vocMapper.insert(voc);


        // 保存单词释义
        List<VocMeaningCommand> interpretations = command.getInterpretations();
        List<VocMeaningDO> vocMeaningDOs = vocConverter.commands2VocMeaningDOs(interpretations);

        if (!CollectionUtils.isEmpty(vocMeaningDOs)) {

            vocMeaningDOs.forEach(vocMeaningDO -> {

                // 设置释义多对应的单词id
                vocMeaningDO.setVocId(voc.getId());
                vocMeaningMapper.insert(vocMeaningDO);
            });
        }

        // 保存例句
        List<ExampleSentenceCommand> examples = command.getExamples();
        List<ExampleSentenceDO> exampleSentenceDOs = vocConverter.commands2ExampleSentenceDOs(examples);
        if (!CollectionUtils.isEmpty(exampleSentenceDOs)) {
            exampleSentenceDOs.forEach(exampleSentenceDO -> {

                // 设置例句对应的单词id
                exampleSentenceDO.setVocId(voc.getId());
                exampleSentenceMapper.insert(exampleSentenceDO);
            });
        }


        // 保存单词-章节-书籍关系
        List<VocChapBookCommand> relations = command.getRelations();
        List<VocChapBookDO> vocChapBookDOs = vocConverter.commands2VocChapBookDOs(relations);
        if (!CollectionUtils.isEmpty(vocChapBookDOs)) {
            vocChapBookDOs.forEach(vocChapBookDO -> {

                // 设置单词id
                vocChapBookDO.setVocId(voc.getId());
                vocChapBookMapper.insert(vocChapBookDO);


                //修改章节中包含的单词数量
                if (vocChapBookDO.getChapterId() != null) {

                    LambdaUpdateWrapper<VocChapterDO> chapterWrapper = new LambdaUpdateWrapper<>();
                    chapterWrapper.eq(VocChapterDO::getId, vocChapBookDO.getChapterId())
                            .setSql("voc_count = voc_count + 1");
                    vocChapterMapper.update(null, chapterWrapper);

                }

                // 书籍中包含的单词数量
                LambdaUpdateWrapper<VocBookDO> vocBookWrapper = new LambdaUpdateWrapper<>();
                vocBookWrapper.eq(VocBookDO::getId, vocChapBookDO.getBookId())
                        .setSql("voc_count = voc_count + 1");

                vocBookMapper.update(null, vocBookWrapper);
            });
        }

    }

    @Override
    public void updateVoc(VocCommand command) {

    }

    @Override
    public DeleteVocOperateDTO deleteVocabulary(Long vocId) {
        return null;
    }

    @Override
    public List<DeleteVocOperateDTO> deleteVocabularies(BatchDeleteCommand command) {
        return null;
    }

    @Override
    public List<VocChapBookDTO> getVocRelations(Long vocId) {
        return null;
    }

    @Override
    public PageDTO<DisplayBookVocDTO> getDisplayBookVocsInfo(AppVocPageRequest pageRequest) {
        return null;
    }

    @Override
    public PageDTO<DisplayChapterDTO> getDisplayBookChapterVocsInfo(AppVocPageRequest pageRequest) {
        return null;
    }

    @Override
    public VocDTO getVocDetail(Long vocId, Long userId) {
        return null;
    }

    @Override
    public UserBookStatisticsDTO getUserChooseCountInBook(Long userId, Long bookId) {
        return null;
    }

    @Override
    public void vocExcelValidate(MultipartFile file) throws IOException {

    }

    @Override
    public ExcelAddOperateDTO addVocExcel(MultipartFile file, Long userId) throws IOException {
        return null;
    }

    @Override
    public DeleteVocFileOperateDTO deleteFile(Long id, String type, String name) {
        return null;
    }

    @Override
    public DeleteVocRelationOperateDTO deleteVocRelation(Long id) {
        return null;
    }

    @Override
    public DeleteInterpretationOperateDTO deleteInterpretation(Long id) {
        return null;
    }

    @Override
    public DeleteVocExampleOperateDTO deleteExample(Long id) {
        return null;
    }
}
