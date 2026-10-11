package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.common.exception.ExcelParseException;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.VocConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.DisplayBookVocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayChapterDTO;
import com.cskaoyan.wordmemorize.dto.app.UserBookStatisticsDTO;
import com.cskaoyan.wordmemorize.dto.app.UserVocBookStatisticsDTO;
import com.cskaoyan.wordmemorize.request.*;
import com.cskaoyan.wordmemorize.service.FileUploadService;
import com.cskaoyan.wordmemorize.service.VocService;
//import org.apache.shiro.SecurityUtils;
import com.cskaoyan.wordmemorize.service.excel.VocExcelSaver;
import com.cskaoyan.wordmemorize.service.excel.VocExcelValidator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VocServiceImpl implements VocService {

    @Resource
    VocMapper vocMapper;

    @Resource
    VocConverter vocConverter;

    @Resource
    VocChapBookMapper vocChapBookMapper;

    @Resource
    VocMeaningMapper vocMeaningMapper;

    @Resource
    ExampleSentenceMapper exampleSentenceMapper;

    @Resource
    VocChapterMapper vocChapterMapper;

    @Resource
    VocBookMapper vocBookMapper;

    @Resource
    UserVocRevMapper userVocRevMapper;

    @Resource
    UserMapper userMapper;

    @Value("${excel.template.vocabulary.path}")
    private String templatePath;


    @Resource
    private VocCategoryMapper vocCategoryMapper;

    @Resource
    VocExcelSaver vocExcelSaver;

    @Resource
    VocExcelValidator vocExcelValidator;


    @Resource
    FileUploadService fileUploadService;

    @Resource
    UserFavoriteMapper userFavoriteMapper;


    @Override
    public PageDTO<VocDTO> getVocabularies(VocPageRequest pageRequest) {
        // 分页条件
        Page<VocDO> vocPage = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
        if (pageRequest.getBookId() == null) {
            Page<VocDO> vocabularyPage = vocMapper.getVocabularyInfo(null, null, pageRequest.getKeyword(), vocPage);
            return vocConverter.vocList2Page(vocabularyPage.getRecords(), vocabularyPage.getTotal());
        }

        Long bookId = Long.parseLong(pageRequest.getBookId());
        if (pageRequest.getChapterId() == null) {
            Page<VocDO> vocabularyPage = vocMapper.getVocabularyInfo(bookId, null, pageRequest.getKeyword(), vocPage);
            return vocConverter.vocList2Page(vocabularyPage.getRecords(), vocabularyPage.getTotal());
        }
        long chapterId = Long.parseLong(pageRequest.getChapterId());
        Page<VocDO> vocabularyPage = vocMapper.getVocabularyInfo(bookId, chapterId, pageRequest.getKeyword(), vocPage);
        return vocConverter.vocList2Page(vocabularyPage.getRecords(), vocabularyPage.getTotal());
    }

    @Override
    @Transactional
    @OperateLog(dateType = "单词", operateType = 1)
    public void addVoc(VocCommand command) {

        command.setId(null);

        LambdaQueryWrapper<VocDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VocDO::getContent, command.getContent());
        if (vocMapper.selectOne(queryWrapper) != null) {
            throw new BusinessException(ResultCodeEnum.VOC_EXISTED);
        }

        // 保存单词
        VocDO vocDO = vocConverter.command2VocDO(command);
        vocMapper.insert(vocDO);

        // 保存关系
        List<VocChapBookDO> vocChapBookDOs = vocConverter.commands2VocChapBookDOs(command.getRelations());
        vocChapBookDOs.forEach(vocChapBookDO -> {
            // 新增
            vocChapBookDO.setVocId(vocDO.getId());
            vocChapBookMapper.insert(vocChapBookDO);

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
            vocBookMapper.update(vocBookWrapper);
        });

        // 保存释义每一个释义
        if (command.getInterpretations() == null) {
            return;
        }

        List<VocMeaningDO> vocMeaningDOs = vocConverter.commands2VocMeaningDOs(command.getInterpretations());
        vocMeaningDOs.forEach(vocMeaningDO -> {
            vocMeaningDO.setVocId(vocDO.getId());
            vocMeaningMapper.insert(vocMeaningDO);
        });
        if (command.getExamples() == null) {
            return;
        }
        List<ExampleSentenceDO> exampleSentenceDOs
                = vocConverter.commands2ExampleSentenceDOs(command.getExamples());
        exampleSentenceDOs.forEach(exampleSentenceDO -> {
            exampleSentenceDO.setVocId(vocDO.getId());
            exampleSentenceMapper.insert(exampleSentenceDO);

        });
    }

    @Override
    @Transactional
    @OperateLog(dateType = "单词", operateType = 4)
    public void updateVoc(VocCommand command) {
        VocDO vocDO = vocConverter.command2VocDO(command);
        // 跟新单词本身的信息
        vocMapper.updateById(vocDO);

        // 处理关系
        handleRelations(command, vocDO);
        // 更新每一个释义
        if (command.getInterpretations() == null) {
            return;
        }
        // 处理释义
        handleMeanings(command, vocDO);

        if (command.getExamples() == null) {
            return;
        }
        // 处理例句
        handleExampleSentences(command, vocDO);
    }

    private void handleExampleSentences(VocCommand command, VocDO vocDO) {

        List<ExampleSentenceDO> exampleSentenceDOs = vocConverter.commands2ExampleSentenceDOs(command.getExamples());
        exampleSentenceDOs.forEach(exampleSentenceDO -> {
            if (exampleSentenceDO.getId() == null) {
                // 新增
                exampleSentenceDO.setVocId(vocDO.getId());
                exampleSentenceMapper.insert(exampleSentenceDO);
                return;
            }
            // 更新
            exampleSentenceMapper.updateById(exampleSentenceDO);
        });
    }

    private void handleRelations(VocCommand command, VocDO vocDO) {
        // 前端上传的关系集合
        List<VocChapBookDO> vocChapBookDOs = vocConverter.commands2VocChapBookDOs(command.getRelations());
        if (CollectionUtils.isEmpty(vocChapBookDOs)) {
            return;
        }
        // 查询数据库中旧的关系id集合
        List<VocChapBookDO> vocRelationsFromDb = vocChapBookMapper.getVocRelations(vocDO.getId());

        //deleteOldVocRelations(vocRelationsFromDb, vocChapBookDOs);

        updateOrInsertVocRelations(vocRelationsFromDb, vocChapBookDOs, vocDO);
    }

    private void handleMeanings(VocCommand command, VocDO vocDO) {
        // 删除释义
        LambdaQueryWrapper<VocMeaningDO> vocMeaningDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        vocMeaningDOLambdaQueryWrapper.eq(VocMeaningDO::getVocId, vocDO.getId());

        // 获取前端上传的释义
        List<VocMeaningDO> vocMeaningDOs = vocConverter.commands2VocMeaningDOs(command.getInterpretations());

        vocMeaningDOs.forEach(vocMeaningDO -> {
            if (vocMeaningDO.getId() == null) {
                vocMeaningDO.setVocId(vocDO.getId());
                vocMeaningMapper.insert(vocMeaningDO);
            }

            vocMeaningMapper.updateById(vocMeaningDO);
        });
    }

    private void updateOrInsertVocRelations(List<VocChapBookDO> vocRelationsFromDb, List<VocChapBookDO> vocChapBookDOs, VocDO vocDO) {
        // key:id
        // value:VocChapBookDO
        Map<Long, VocChapBookDO> vocChapBookMap = vocRelationsFromDb.stream()
                .collect(Collectors.toMap(vocChapBookDO -> vocChapBookDO.getId(), vocChapBookDO -> vocChapBookDO));
        // 一次判断每一个关系
        vocChapBookDOs.forEach(vocChapBookDO -> {
            if (vocChapBookDO.getId() == null) {
                // 新增
                vocChapBookDO.setVocId(vocDO.getId());
                vocChapBookMapper.insert(vocChapBookDO);
                if (vocChapBookDO.getChapterId() != null) {
                    // 有章节(此时一定是新增)
                    vocMapper.updateChapterVocCount(vocChapBookDO.getChapterId(), 1);
                }
                vocMapper.updateBookVocCount(vocChapBookDO.getBookId(), 1);
                return;
            }
            // 如果不是新增
            updateVocChapBook(vocChapBookDO, vocChapBookMap);
        });
    }

    private void deleteOldVocRelations(List<VocChapBookDO> vocRelationsFromDb, List<VocChapBookDO> vocChapBookDOs) {
        List<Long> oldVocChapBookList = vocRelationsFromDb
                .stream().map(relation -> relation.getId()).collect(Collectors.toList());
        // 得到前端传递的关系id集合
        List<Long> newVocChapBookList = vocChapBookDOs.stream()
                .filter(vocChapBookDO -> vocChapBookDO.getId() != null)
                .map(relation -> relation.getId())
                .collect(Collectors.toList());
        List<Long> toRemoveVocChapBookList = oldVocChapBookList
                .stream().filter(oldId -> !newVocChapBookList.contains(oldId)).collect(Collectors.toList());

        if (!toRemoveVocChapBookList.isEmpty()) {
            vocChapBookMapper.deleteBatchIds(toRemoveVocChapBookList);
        }
    }

    private void updateVocChapBook(VocChapBookDO vocChapBookDO, Map<Long, VocChapBookDO> vocChapBookMap) {
        VocChapBookDO vocChapBookFromDb = vocChapBookMap.get(vocChapBookDO.getId());
        if (vocChapBookFromDb.getBookId().equals(vocChapBookDO.getBookId())) {
            if (Objects.equals(vocChapBookFromDb.getChapterId(), vocChapBookDO.getChapterId())) {
                // 如果bookId和chapterId都没有变，那么单词数量不需要变
                return;
            }

            // chapterId变了，book的单词数量不需要变
            // 旧的章节单词数量-1
            if (vocChapBookFromDb.getChapterId() != null) {
                vocMapper.updateChapterVocCount(vocChapBookFromDb.getChapterId(), -1);
            }
            if (vocChapBookDO.getChapterId() != null) {
                // 新的章节单词数量+1
                vocMapper.updateChapterVocCount(vocChapBookDO.getChapterId(), 1);
            }
            // 更新关系
            vocChapBookMapper.updateById(vocChapBookDO);
            return;
        }

        // 说明改变了单词所属的书籍，那么书籍，章节的词汇数量都要跟着变
        // 旧的书籍单词数量-1
        vocMapper.updateBookVocCount(vocChapBookFromDb.getBookId(), -1);
        // 新的书籍单词数量+1
        vocMapper.updateBookVocCount(vocChapBookDO.getBookId(), 1);
        if (vocChapBookFromDb.getChapterId() != null) {
            // 旧的章节单词数量-1
            vocMapper.updateChapterVocCount(vocChapBookFromDb.getChapterId(), -1);
        }

        if (vocChapBookDO.getChapterId() != null) {
            // 新的章节单词数量+1
            vocMapper.updateChapterVocCount(vocChapBookDO.getChapterId(), 1);
        }

        vocChapBookMapper.updateById(vocChapBookDO);

    }

    @Override
    @Transactional
    @OperateLog(dateType = "单词", operateType = 2)
    public DeleteVocOperateDTO deleteVocabulary(Long vocId) {
        // 删除单词本身
        VocDO vocDO = vocMapper.selectById(vocId);
        vocMapper.deleteById(vocId);

        LambdaUpdateWrapper<VocChapBookDO> vocChapterDOWrapper = new LambdaUpdateWrapper<>();
        vocChapterDOWrapper.eq(VocChapBookDO::getVocId, vocId);
        List<VocChapBookDO> vocChapBookDOs = vocChapBookMapper.selectList(vocChapterDOWrapper);
        if (!CollectionUtils.isEmpty(vocChapBookDOs)) {
            vocChapBookDOs.forEach(vocChapBookDO -> {
                if (vocChapBookDO.getChapterId()!= null) {
                    // 有章节
                    vocMapper.updateChapterVocCount(vocChapBookDO.getChapterId(), -1);
                }
                vocMapper.updateBookVocCount(vocChapBookDO.getBookId(), -1);
            });
        }

        // 删除关系
        LambdaQueryWrapper<VocChapBookDO> vocChapBookWrapper = new LambdaQueryWrapper<>();
        vocChapBookWrapper.eq(VocChapBookDO::getVocId, vocId);
        vocChapBookMapper.delete(vocChapBookWrapper);

        // 删除释义
        LambdaQueryWrapper<VocMeaningDO> meaningWrapper = new LambdaQueryWrapper<>();
        meaningWrapper.eq(VocMeaningDO::getVocId, vocId);
        vocMeaningMapper.delete(meaningWrapper);

        // 删除例句
        LambdaQueryWrapper<ExampleSentenceDO> exampleWrapper = new LambdaQueryWrapper<>();
        exampleWrapper.eq(ExampleSentenceDO::getVocId, vocId);
        exampleSentenceMapper.delete(exampleWrapper);

        return vocConverter.toDeleteVocOperateDTO(vocDO);
    }

    @Override
    @Transactional
    @OperateLog(dateType = "单词", operateType = 3)
    public List<DeleteVocOperateDTO> deleteVocabularies(BatchDeleteCommand command) {
        List<Long> ids = command.getIds().stream()
                .map(idStr -> Long.parseLong(idStr)).collect(Collectors.toList());

        List<VocDO> vocDOs = vocMapper.selectByIds(ids);


        int from = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (i % 100 == 0 && i > 0) {
                List<Long> batchDeleteVocIds = ids.subList(from, i);
                batchDeleteVocs(batchDeleteVocIds);

                from = i;
            }
        }
        List<Long> batchDeleteVocIds = ids.subList(from, ids.size());
        if (batchDeleteVocIds.size() > 0) {
            batchDeleteVocs(batchDeleteVocIds);
        }

        return vocConverter.toDeleteVocOperateDTOs(vocDOs);
    }

    @Override
    public List<VocChapBookDTO> getVocRelations(Long vocId) {
        List<VocChapBookDO> vocRelations = vocChapBookMapper.getVocRelations(vocId);
        return vocConverter.vocChapBookDOs2DTOs(vocRelations);
    }

    @Override
    public PageDTO<DisplayBookVocDTO> getDisplayBookVocsInfo(AppVocPageRequest pageRequest) {
        VocBookDO vocBookDO = vocBookMapper.selectById(pageRequest.getBookId());
        Long userId = Long.parseLong(pageRequest.getUserId());
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getCount();
        String bookId = pageRequest.getBookId();
        List<VocDO> vocsOfUserBook = userMapper.getVocsOfUserBook(userId,
                Long.parseLong(bookId), offset, pageRequest.getCount());

        LambdaQueryWrapper<VocChapBookDO> vocChapBookWrapper = new LambdaQueryWrapper<>();
        vocChapBookWrapper.eq(VocChapBookDO::getBookId, pageRequest.getBookId());
        Long total = vocChapBookMapper.selectCount(vocChapBookWrapper);
        return vocConverter.displayBookVocList2Page(vocsOfUserBook, total);
    }

    @Override
    public PageDTO<DisplayChapterDTO> getDisplayBookChapterVocsInfo(AppVocPageRequest pageRequest) {
        String bookId = pageRequest.getBookId();
        VocBookDO vocBookDO = vocBookMapper.selectById(Long.parseLong(bookId));
        Long userId = Long.parseLong(pageRequest.getUserId());
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getCount();

        List<VocChapterDO> chapterVocsOfUserBook = userMapper.getChapterVocsOfUserBook(userId,
                Long.parseLong(bookId), offset, pageRequest.getCount());

        LambdaQueryWrapper<VocChapBookDO> vocChapBookWrapper = new LambdaQueryWrapper<>();
        vocChapBookWrapper.eq(VocChapBookDO::getBookId, Long.parseLong(bookId))
                .isNotNull(VocChapBookDO::getChapterId)
                .select(VocChapBookDO::getChapterId)
                .groupBy(VocChapBookDO::getChapterId);
        int size = vocChapBookMapper.selectList(vocChapBookWrapper).size();
        return vocConverter.displayChapterVocList2Page(chapterVocsOfUserBook, (long) size);
    }

    @Override
    public VocDTO getVocDetail(Long vocId, Long userId) {

        VocDO vocDetail = vocMapper.getVocDetail(userId, vocId);
        List<UserVocExampleSentenceDO> userVocSentence = vocMapper.getUserVocExampleSentence(userId, vocId);
        vocDetail.setUserExamples(userVocSentence);

        LambdaQueryWrapper<UserFavoriteDO> favoriteWrapper = Wrappers.lambdaQuery(UserFavoriteDO.class)
                .eq(UserFavoriteDO::getUserId, userId)
                .eq(UserFavoriteDO::getVocId, vocId);
        // 获取用户是否收藏
        Long count = userFavoriteMapper.selectCount(favoriteWrapper);
        vocDetail.setFavorite(count > 0 ? 1 : 0);
        return vocConverter.vocDO2DTO(vocDetail);
    }


    @Override
    public UserBookStatisticsDTO getUserChooseCountInBook(Long userId, Long bookId) {

        LambdaQueryWrapper<VocChapBookDO> vocChapBookWrapper = new LambdaQueryWrapper<>();
        vocChapBookWrapper.eq(VocChapBookDO::getBookId, bookId);
        Long total = vocChapBookMapper.selectCount(vocChapBookWrapper);


        UserBookStatisticsDO userBookStatisticsDO = new UserBookStatisticsDO();
        // 总的单词数量
        userBookStatisticsDO.setTotal(total.intValue());
        Integer userChooseCountInBook = userMapper.getUserChooseCountInBook(userId, bookId);
        // 已选择复习单词数量
        userBookStatisticsDO.setSelected(userChooseCountInBook);

        // 未选择复习单词数量
        userBookStatisticsDO.setUnSelected(total.intValue() - userChooseCountInBook);
        return vocConverter.userBookStatisticsDO2DTO(userBookStatisticsDO);
    }

    @Override
    public void vocExcelValidate(MultipartFile file) throws IOException {
        // 文件基本检查
        if (file == null || file.isEmpty()) {
            throw new ExcelParseException(ResultCodeEnum.PARAM_ERROR, "Excel文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||
                !(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"))) {
            throw new ExcelParseException(ResultCodeEnum.PARAM_ERROR, "请上传Excel文件(.xlsx或.xls)");
        }

        vocExcelValidator.validate(templatePath, file.getInputStream());
    }

    @Override
    @OperateLog(dateType = "单词-例句-释义", operateType = 6)
    public ExcelAddOperateDTO addVocExcel(MultipartFile file, Long employeeId) throws IOException {


        Integer totalCount = vocExcelSaver.upload(file, VocExcelDTO.class, (data -> {
            VocDO vocDO = vocConverter.vocExcelDTO2VocDO(data);
            // 验证释义
            String interpretations = data.getInterpretations();
            List<VocMeaningDO> vocMeaningDOs = parseInterpretations(interpretations);
            vocDO.setInterpretations(vocMeaningDOs);
            // 验证例句
            String exampleSentences = data.getExampleSentences();
            List<ExampleSentenceDO> userVocExampleSentenceDOs = parseExamples(exampleSentences);
            vocDO.setExamples(userVocExampleSentenceDOs);
            // 验证书籍章节信息
            String bookInfo = data.getBookInfos();
            List<VocChapBookDO> vocChapBookDOs = parseBookInfo(vocDO.getContent(), bookInfo);

            VocExcelParsedDO vocExcelParsedDO = new VocExcelParsedDO();
            vocExcelParsedDO.setVoc(vocDO);
            vocExcelParsedDO.setVocChapBooks(vocChapBookDOs);

            return vocExcelParsedDO;
        }));

        ExcelAddOperateDTO excelAddOperateDTO = new ExcelAddOperateDTO();
        excelAddOperateDTO.setFileName(file.getOriginalFilename());
        excelAddOperateDTO.setTotalCount(totalCount);
        return excelAddOperateDTO;
    }

    @Override
    public DeleteVocFileOperateDTO deleteFile(Long id, String type, String name) {
        // 删除oss中的音频文件
        fileUploadService.deleteFile(name);

        LambdaUpdateWrapper<VocDO> updateWrapper = new LambdaUpdateWrapper<>();
        SFunction<VocDO, String> field = "uk".equals(type) ?
                VocDO::getUkPronounciation : VocDO::getUsPronounciation;
        updateWrapper.eq(VocDO::getId, id)
                .set(field, null);
        vocMapper.update(null, updateWrapper);

        VocDO vocDO = vocMapper.selectById(id);
        DeleteVocFileOperateDTO deleteVocFileOperateDTO = new DeleteVocFileOperateDTO();
        deleteVocFileOperateDTO.setVocId(id);
        deleteVocFileOperateDTO.setContent(vocDO.getContent());
        deleteVocFileOperateDTO.setType(type);
        deleteVocFileOperateDTO.setFileName(name);

        return deleteVocFileOperateDTO;
    }
    @Override
    public DeleteVocRelationOperateDTO deleteVocRelation(Long id) {

        VocChapBookDO vocChapBookDO = vocChapBookMapper.selectById(id);
        vocChapBookMapper.deleteById(id);

        // 删除章节与书籍包含的单词数量
        if (vocChapBookDO.getChapterId()!= null) {
            // 有章节(此时一定是新增)
            vocMapper.updateChapterVocCount(vocChapBookDO.getChapterId(), -1);
        }

        vocMapper.updateBookVocCount(vocChapBookDO.getBookId(), -1);
        return vocConverter.toDeleteVocRelationOperateDTO(vocChapBookDO);
    }

    @Override
    public DeleteInterpretationOperateDTO deleteInterpretation(Long id) {

        VocMeaningDO vocMeaningDO = vocMeaningMapper.selectById(id);
        VocDO vocDO = vocMapper.selectById(vocMeaningDO.getVocId());

        vocMeaningMapper.deleteById(id);

        return vocConverter.toDeleteInterpretationOperateDTO(vocMeaningDO, vocDO.getContent());
    }

    @Override
    public DeleteVocExampleOperateDTO deleteExample(Long id) {

        ExampleSentenceDO sentenceDO = exampleSentenceMapper.selectById(id);
        VocDO vocDO = vocMapper.selectById(sentenceDO.getVocId());

        exampleSentenceMapper.deleteById(id);
        return vocConverter.toDeleteExampleOperateDTO(sentenceDO, vocDO.getContent());
    }


    private List<VocMeaningDO> parseInterpretations(String definitions) {
        List<VocMeaningDO> result = new ArrayList<>();

        if (definitions == null || definitions.trim().isEmpty()) {
            return result;
        }

        String[] definitionLines = splitByLineBreak(definitions);
        for (int i = 0; i < definitionLines.length; i++) {
            VocMeaningDO vocMeaningDO = new VocMeaningDO();
            String definition = definitionLines[i].trim();
            // 分离词性和释义内容
            int dotIndex = definition.indexOf('.');
            if (dotIndex > 0) {
                String partOfSpeech = definition.substring(0, dotIndex).trim(); // 词性
                vocMeaningDO.setType(partOfSpeech);
                String content = definition.substring(dotIndex + 1).trim(); // 释义内容
                vocMeaningDO.setInterContent(content);
                log.debug("解析释义: 词性={}, 内容={}", partOfSpeech, content);
                result.add(vocMeaningDO);
            }
        }

        return result;
    }

    private List<ExampleSentenceDO> parseExamples(String examples) {
        List<ExampleSentenceDO> result = new ArrayList<>();

        if (examples == null || examples.trim().isEmpty()) {
            return result;
        }

        String[] exampleLines = splitByLineBreak(examples);

        // 按照例句-翻译的顺序解析
        for (int i = 0; i < exampleLines.length; i += 2) {
            ExampleSentenceDO userVocExampleSentenceDO = new ExampleSentenceDO();
            String sentence = exampleLines[i].trim();
            userVocExampleSentenceDO.setContent(sentence);
            String translation = (i + 1 < exampleLines.length) ? exampleLines[i + 1].trim() : "";
            userVocExampleSentenceDO.setMeaning(translation);
            result.add(userVocExampleSentenceDO);
            log.debug("解析例句: 句子={}, 翻译={}", sentence, translation);

        }

        return result;
    }

    private List<VocChapBookDO> parseBookInfo(String vocContent, String bookInfo) {
        List<VocChapBookDO> result = new ArrayList<>();

        String[] bookInfoSplits = splitByLineBreak(bookInfo);

        for (int i = 0; i < bookInfoSplits.length; i++) {
            VocChapBookDO vocChapBookDO = new VocChapBookDO();
            String[] parts = bookInfo.split("#");
            String bookName = parts[0].trim();
            vocChapBookDO.setBookName(bookName);
            vocChapBookDO.setVocName(vocContent);
            if (parts.length == 1) {
                // 验证书籍是否存在
                LambdaQueryWrapper<VocBookDO> bookQueryWrapper = new LambdaQueryWrapper<VocBookDO>()
                        .eq(VocBookDO::getName, bookName);
                VocBookDO vocBookDO = vocBookMapper.selectOne(bookQueryWrapper);
                vocChapBookDO.setBookId(vocBookDO.getId());
                return result;
            }
            if (parts.length == 2) {
                String chapterName = parts[1].trim();
                vocChapBookDO.setChapterName(chapterName);

                // 验证章节是否存在
                LambdaQueryWrapper<VocChapterDO> chapterQuery = new LambdaQueryWrapper<VocChapterDO>()
                        .eq(VocChapterDO::getBookName, bookName)
                        .eq(VocChapterDO::getChapterName, chapterName);
                VocChapterDO vocChapterDO = vocChapterMapper.selectOne(chapterQuery);
                vocChapBookDO.setBookId(vocChapterDO.getBookId());
                vocChapBookDO.setChapterId(vocChapterDO.getId());
            }
            result.add(vocChapBookDO);

        }

        return result;

    }

    /**
     * 使用正则表达式分割字符串，支持所有类型的换行符
     */
    private String[] splitByLineBreak(String input) {
        if (input == null || input.isEmpty()) {
            return new String[0];
        }

        // 正则表达式匹配所有类型的换行符
        return input.split("\\r\\n|\\r|\\n");
    }

    public void batchDeleteVocs(List<Long> batchDeleteVocIds) {
        // 删除单词
        vocMapper.deleteBatchIds(batchDeleteVocIds);


        // 删除关系
        LambdaQueryWrapper<VocChapBookDO> vocChapBookWrapper
                = new LambdaQueryWrapper<>();
        vocChapBookWrapper.in(VocChapBookDO::getVocId, batchDeleteVocIds);

        // 更新书籍，章节包含的单词数量
        updateVocCount(vocChapBookWrapper);

        // 删除关系
        vocChapBookMapper.delete(vocChapBookWrapper);
        // 删除释义
        LambdaQueryWrapper<VocMeaningDO> vocMeaningWrapper
                = new LambdaQueryWrapper<>();
        vocChapBookWrapper.in(VocChapBookDO::getVocId, batchDeleteVocIds);
        vocMeaningMapper.delete(vocMeaningWrapper);

        //删除例句
        LambdaQueryWrapper<ExampleSentenceDO> exampleWrapper
                = new LambdaQueryWrapper<>();
        exampleWrapper.in(ExampleSentenceDO::getVocId, batchDeleteVocIds);
        exampleSentenceMapper.delete(exampleWrapper);

    }

    private void updateVocCount(LambdaQueryWrapper<VocChapBookDO> vocChapBookWrapper) {
        List<VocChapBookDO> vocChapBookDOs
                = vocChapBookMapper.selectList(vocChapBookWrapper);


        List<Long> bookIds = vocChapBookDOs.stream()
                .map(vocChapBookDO -> vocChapBookDO.getBookId())
                .collect(Collectors.toList());

        LambdaUpdateWrapper<VocBookDO> vocBookDOLambdaUpdateWrapper
                = Wrappers.lambdaUpdate(VocBookDO.class)
                .in(VocBookDO::getId, bookIds)
                .setSql("voc_count=voc_count - 1");
        vocBookMapper.update(vocBookDOLambdaUpdateWrapper);


        List<Long> chapterIds = vocChapBookDOs.stream()
                .map(vocChapBookDO -> vocChapBookDO.getChapterId())
                .collect(Collectors.toList());
        LambdaUpdateWrapper<VocChapterDO> vocChapterDOLambdaUpdateWrapper = Wrappers.lambdaUpdate(VocChapterDO.class)
                .in(VocChapterDO::getId, chapterIds)
                .setSql("voc_count=voc_count - 1");
        vocChapterMapper.update(vocChapterDOLambdaUpdateWrapper);
    }

}
