package com.cskaoyan.wordmemorize.service.impl;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.VocConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.*;
import com.cskaoyan.wordmemorize.service.VocService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


import static cn.dev33.satoken.SaManager.log;

@Service
public class VocServiceImpl implements VocService {
    private static final String audio_file_dir = "C:\\Users\\Xman\\Desktop\\voice";


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


    @Autowired
    UserVocExampleSentenceMapper userVocExampleSentenceMapper;

    // 注入Mapper
    @Autowired
    UserVocRevMapper userVocRevMapper;
    @Autowired
    UserFavoriteMapper userFavoriteMapper;
    @Autowired
    UserNotRememberMapper userNotRememberMapper;


    //分页查询单词列表
    @Override
    public PageDTO<VocDTO> getVocabularies(VocPageRequest pageQuery) {

        int pageNum = pageQuery.getPageNum();
        int pageSize = pageQuery.getCount();
        int offset = (pageNum - 1) * pageSize;

        // 查询单词列表
        List<VocDO> vocDOList = vocMapper.getVocabulariesPage(
                pageQuery.getBookId(),
                pageQuery.getChapterId(),
                pageQuery.getKeyword(),
                offset,
                pageSize
        );

        // 查询总数
        int total = vocMapper.getVocabulariesTotal(
                pageQuery.getBookId(),
                pageQuery.getChapterId(),
                pageQuery.getKeyword()
        );

        // 构建分页对象
        Page<VocDO> page = new Page<>(pageNum, pageSize, total);
        page.setRecords(vocDOList);

        // 将 DO 列表转换为 DTO 列表
        List<VocDTO> vocDTOList = new ArrayList<>();
        for (VocDO vocDO : vocDOList) {
            VocDTO vocDTO = vocConverter.vocDO2DTO(vocDO);
            vocDTOList.add(vocDTO);
        }

        // 构建分页结果对象
        PageDTO<VocDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageList(vocDTOList);
        pageDTO.setTotalCount((long) total);
        return pageDTO;
    }

    //添加单词
    @Override
    public void addVoc(VocCommand command) {
        System.out.println("单词id"+command.getId());
        // 检查单词是否已存在
        LambdaQueryWrapper<VocDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VocDO::getContent, command.getContent());
        if (vocMapper.selectOne(queryWrapper) != null) {
            throw new BusinessException(ResultCodeEnum.VOC_EXISTED);
        }

        // 保存单词基本信息
        VocDO voc = vocConverter.command2VocDO(command);
        vocMapper.insert(voc);
        System.out.println(voc.getId());//有单词id

        // 保存释义
        saveVocMeanings(voc.getId(), command.getInterpretations());

        // 保存例句
        saveExampleSentences(voc.getId(), command.getExamples());

        // 保存单词-章节-书籍关系
        saveVocChapBookRelations(voc.getId(), command.getRelations());
    }

    /**
     * 保存单词释义
     */
    private void saveVocMeanings(Long vocId, List<VocMeaningCommand> meanings) {
        if (CollectionUtils.isEmpty(meanings)) {
            return;
        }
        List<VocMeaningDO> meaningDOs = vocConverter.commands2VocMeaningDOs(meanings);
        meaningDOs.forEach(meaningDO -> {
            meaningDO.setVocId(vocId);
            vocMeaningMapper.insert(meaningDO);
        });
    }

    /**
     * 保存单词例句
     */
    private void saveExampleSentences(Long vocId, List<ExampleSentenceCommand> examples) {
        if (CollectionUtils.isEmpty(examples)) {
            return;
        }
        List<ExampleSentenceDO> sentenceDOs = vocConverter.commands2ExampleSentenceDOs(examples);
        sentenceDOs.forEach(sentenceDO -> {
            sentenceDO.setVocId(vocId);
            exampleSentenceMapper.insert(sentenceDO);
        });
    }

    /**
     * 保存单词与章节、书籍的关联关系
     * @param vocId 单词ID
     * @param relations 关联关系列表
     */
    private void saveVocChapBookRelations(Long vocId, List<VocChapBookCommand> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return;
        }

        List<VocChapBookDO> vocChapBookDOs = vocConverter.commands2VocChapBookDOs(relations);
        vocChapBookDOs.forEach(vocChapBookDO -> {
            // 绑定单词ID
            vocChapBookDO.setVocId(vocId);
            vocChapBookMapper.insert(vocChapBookDO);

            // 更新章节的单词计数
            updateChapterVocCount(vocChapBookDO.getChapterId());

            // 更新书籍的单词计数
            updateBookVocCount(vocChapBookDO.getBookId());
        });
    }

    /**
     * 更新章节的单词计数
     * @param chapterId 章节ID
     */
    private void updateChapterVocCount(Long chapterId) {
        if (chapterId != null) {
            LambdaUpdateWrapper<VocChapterDO> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(VocChapterDO::getId, chapterId)
                    .setSql("voc_count = voc_count + 1");
            vocChapterMapper.update(null, wrapper);
        }
    }

    /**
     * 更新书籍的单词计数
     * @param bookId 书籍ID
     */
    private void updateBookVocCount(Long bookId) {
        if (bookId != null) {
            LambdaUpdateWrapper<VocBookDO> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(VocBookDO::getId, bookId)
                    .setSql("voc_count = voc_count + 1");
            vocBookMapper.update(null, wrapper);
        }
    }

    //更新单词
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVoc(VocCommand command) {
        // 1. 检查要修改的单词是否存在
        Long vocId = Long.parseLong(command.getId());
        VocDO existingVoc = vocMapper.selectById(vocId);
        if (existingVoc == null) {
            throw new BusinessException(ResultCodeEnum.VOC_NOT_EXISTED);
        }

        // 2. 检查单词内容是否冲突
        if (!existingVoc.getContent().equals(command.getContent())) {
            LambdaQueryWrapper<VocDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(VocDO::getContent, command.getContent())
                    .ne(VocDO::getId, vocId);
            if (vocMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException(ResultCodeEnum.VOC_EXISTED);
            }
        }

        // 3. 更新单词基本信息
        VocDO updateVoc = vocConverter.command2VocDO(command);
        updateVoc.setId(vocId);
        vocMapper.updateById(updateVoc);

        // 4. 更新单词释义（统一处理ID）
        LambdaQueryWrapper<VocMeaningDO> meaningDeleteWrapper = new LambdaQueryWrapper<>();
        meaningDeleteWrapper.eq(VocMeaningDO::getVocId, vocId);
        vocMeaningMapper.delete(meaningDeleteWrapper);

        if (!CollectionUtils.isEmpty(command.getInterpretations())) {
            List<VocMeaningDO> vocMeaningDOs = vocConverter.commands2VocMeaningDOs(command.getInterpretations());
            vocMeaningDOs.forEach(meaning -> {
                meaning.setVocId(vocId);
                meaning.setId(null); // 强制清除ID
                vocMeaningMapper.insert(meaning);
            });
        }

        // 5. 更新例句（统一处理ID）
        LambdaQueryWrapper<ExampleSentenceDO> exampleDeleteWrapper = new LambdaQueryWrapper<>();
        exampleDeleteWrapper.eq(ExampleSentenceDO::getVocId, vocId);
        exampleSentenceMapper.delete(exampleDeleteWrapper);

        if (!CollectionUtils.isEmpty(command.getExamples())) {
            List<ExampleSentenceDO> exampleSentenceDOs = vocConverter.commands2ExampleSentenceDOs(command.getExamples());
            exampleSentenceDOs.forEach(example -> {
                example.setVocId(vocId);
                example.setId(null); // 强制清除ID
                exampleSentenceMapper.insert(example);
            });
        }

        // 6. 更新单词-章节-书籍关系（统一处理ID和唯一索引）
        LambdaQueryWrapper<VocChapBookDO> relationDeleteWrapper = new LambdaQueryWrapper<>();
        relationDeleteWrapper.eq(VocChapBookDO::getVocId, vocId);
        List<VocChapBookDO> oldRelations = vocChapBookMapper.selectList(relationDeleteWrapper);

        // 减少旧关系计数
        oldRelations.forEach(relation -> {
            if (relation.getChapterId() != null) {
                vocChapterMapper.update(null, new LambdaUpdateWrapper<VocChapterDO>()
                        .eq(VocChapterDO::getId, relation.getChapterId())
                        .setSql("voc_count = voc_count - 1"));
            }
            vocBookMapper.update(null, new LambdaUpdateWrapper<VocBookDO>()
                    .eq(VocBookDO::getId, relation.getBookId())
                    .setSql("voc_count = voc_count - 1"));
        });

        // 删除旧关系
        vocChapBookMapper.delete(relationDeleteWrapper);

        if (!CollectionUtils.isEmpty(command.getRelations())) {
            List<VocChapBookDO> newRelations = vocConverter.commands2VocChapBookDOs(command.getRelations());
            Set<String> uniqueKeys = new HashSet<>();
            for (VocChapBookDO relation : newRelations) {
                relation.setVocId(vocId);
                relation.setId(null); // 强制清除ID
                String key = relation.getVocId() + "-" + relation.getChapterId() + "-" + relation.getBookId();
                if (!uniqueKeys.add(key)) continue;

                // 1. 先查 is_deleted=1 的数据，存在则 update
                LambdaQueryWrapper<VocChapBookDO> logicDeletedWrapper = new LambdaQueryWrapper<VocChapBookDO>()
                        .eq(VocChapBookDO::getVocId, relation.getVocId())
                        .eq(VocChapBookDO::getChapterId, relation.getChapterId())
                        .eq(VocChapBookDO::getBookId, relation.getBookId())
                        .eq(VocChapBookDO::getIsDeleted, 1);
                List<VocChapBookDO> logicDeletedList = vocChapBookMapper.selectList(logicDeletedWrapper);
                if (!logicDeletedList.isEmpty()) {
                    for (VocChapBookDO logicDeleted : logicDeletedList) {
                        logicDeleted.setIsDeleted(0);
                        logicDeleted.setVocName(relation.getVocName());
                        logicDeleted.setChapterName(relation.getChapterName());
                        logicDeleted.setBookName(relation.getBookName());
                        vocChapBookMapper.updateById(logicDeleted);
                    }
                    continue; // 已update，无需再插入
                }

                // 2. 再查 is_deleted=0 的数据，存在则跳过
                LambdaQueryWrapper<VocChapBookDO> existWrapper = new LambdaQueryWrapper<VocChapBookDO>()
                        .eq(VocChapBookDO::getVocId, relation.getVocId())
                        .eq(VocChapBookDO::getChapterId, relation.getChapterId())
                        .eq(VocChapBookDO::getBookId, relation.getBookId())
                        .eq(VocChapBookDO::getIsDeleted, 0);
                if (vocChapBookMapper.selectCount(existWrapper) > 0) {
                    // 已存在未删除的，跳过
                    continue;
                }

                // 3. 只有都不存在时才插入
                vocChapBookMapper.insert(relation);

                // 更新计数
                if (relation.getChapterId() != null) {
                    vocChapterMapper.update(null, new LambdaUpdateWrapper<VocChapterDO>()
                            .eq(VocChapterDO::getId, relation.getChapterId())
                            .setSql("voc_count = voc_count + 1"));
                }
                vocBookMapper.update(null, new LambdaUpdateWrapper<VocBookDO>()
                        .eq(VocBookDO::getId, relation.getBookId())
                        .setSql("voc_count = voc_count + 1"));
            }
        }
    }


    //删除指定单词信息
    @Override
    public DeleteVocOperateDTO deleteVocabulary(Long vocId) {
        // 1. 检查要删除的单词是否存在（根据ID）
        VocDO existingVoc = vocMapper.selectById(vocId);
        if (existingVoc == null) {
            throw new BusinessException(ResultCodeEnum.VOC_NOT_EXISTED);
        }
        //2.删除单词释义
        LambdaQueryWrapper<VocMeaningDO> meaningDeleteWrapper = new LambdaQueryWrapper<>();
        meaningDeleteWrapper.eq(VocMeaningDO::getVocId, vocId);
        vocMeaningMapper.delete(meaningDeleteWrapper);
        //3.删除例句
        LambdaQueryWrapper<ExampleSentenceDO> exampleSentenceDOWrapper = new LambdaQueryWrapper<>();
        exampleSentenceDOWrapper.eq(ExampleSentenceDO::getVocId, vocId);
        exampleSentenceMapper.delete(exampleSentenceDOWrapper);
        //4.删除单词-章节-书籍关系,并更新计数
        LambdaQueryWrapper<VocChapBookDO> relationDeleteWrapper = new LambdaQueryWrapper<>();
        relationDeleteWrapper.eq(VocChapBookDO::getVocId, vocId);
        List<VocChapBookDO> oldRelations = vocChapBookMapper.selectList(relationDeleteWrapper);
        vocChapBookMapper.delete(relationDeleteWrapper);
        for (VocChapBookDO relation : oldRelations) {
            if (relation.getChapterId() != null) {
                vocChapterMapper.update(null, new LambdaUpdateWrapper<VocChapterDO>()
                        .eq(VocChapterDO::getId, relation.getChapterId())
                        .setSql("voc_count = voc_count - 1"));
            }

            vocBookMapper.update(null, new LambdaUpdateWrapper<VocBookDO>()
                    .eq(VocBookDO::getId, relation.getBookId())
                    .setSql("voc_count = voc_count - 1"));
        }
        //5.删除单词
        vocMapper.deleteById(vocId);
        // 6. 构建删除操作日志对象
        DeleteVocOperateDTO dto = new DeleteVocOperateDTO();
        dto.setId(vocId);
        dto.setContent(existingVoc.getContent());
        return dto;
    }


    //批量删除单词？
    @Override
    public List<DeleteVocOperateDTO> deleteVocabularies(BatchDeleteCommand command) {

        ArrayList<DeleteVocOperateDTO> resultList = new ArrayList<>();
        if (command == null || CollectionUtils.isEmpty(command.getIds())) {
            return resultList;
        }
        for (String vocId : command.getIds()) {
            DeleteVocOperateDTO dto = null;
            try {
                //调用单个删除单词的方法
                dto = deleteVocabulary(Long.valueOf(vocId));
                resultList.add(dto);
            } catch (BusinessException e) {
                DeleteVocOperateDTO deleteVocOperateDTO = new DeleteVocOperateDTO();
                deleteVocOperateDTO.setId(Long.valueOf(vocId));
                deleteVocOperateDTO.setContent(dto.getContent());
                resultList.add(deleteVocOperateDTO);
            }
        }
        return resultList;
    }

    //获取指定单词的单词-章节-书籍之间的关系
    @Override
    public List<VocChapBookDTO> getVocRelations(Long vocId) {
        if (vocId == null) {
            throw new BusinessException(ResultCodeEnum.VOC_NOT_EXISTED);
        }
        LambdaQueryWrapper<VocChapBookDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VocChapBookDO::getVocId, vocId);
        List<VocChapBookDO> vocChapBookDOS = vocChapBookMapper.selectList(queryWrapper);
        //将Do列表转换为Dto列表
        return vocConverter.vocChapBookDOs2DTOs(vocChapBookDOS);
    }

    //移动端获取书籍单词列表（无章节版）
    @Override
    public PageDTO<DisplayBookVocDTO> getDisplayBookVocsInfo(AppVocPageRequest pageRequest) {

        if (pageRequest.getBookId() == null || pageRequest.getUserId() == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }

        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getCount();
        int offset = (pageNum - 1) * pageSize;

        // 将 String 类型的 userId 和 bookId 转换为 Long 类型
        Long userId = Long.parseLong(pageRequest.getUserId());
        Long bookId = Long.parseLong(pageRequest.getBookId());

        List<DisplayBookVocDTO> displayBookVocDTOList;
        long totalCount;
        if (pageSize == 0) {
            // 全表查询：查出该用户该书的所有单词
            List<VocDO> allVocs = vocMapper.getAllVocsOfUserBook(userId, bookId);
            displayBookVocDTOList = allVocs.stream()
                    .map(vocConverter::vocDOToDisplayBookVocDTO)
                    .collect(Collectors.toList());
            totalCount = displayBookVocDTOList.size();
        } else {
            // 创建分页对象
            Page<VocDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
            // 调用 Mapper 方法，传入分页对象
            Page<VocDO> vocDOPage = vocMapper.getVocsOfUserBook(userId, bookId, page);
            // 转换结果
            displayBookVocDTOList = vocDOPage.getRecords().stream()
                    .map(vocConverter::vocDOToDisplayBookVocDTO)
                    .collect(Collectors.toList());
            totalCount = vocDOPage.getTotal();
        }

        PageDTO<DisplayBookVocDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageList(displayBookVocDTOList);
        pageDTO.setTotalCount(totalCount);

        return pageDTO;
        //更新测试
    }

    //移动端获取书籍单词列表（有章节版）
    @Override
    public PageDTO<DisplayChapterDTO> getDisplayBookChapterVocsInfo(AppVocPageRequest pageRequest) {
        if (pageRequest.getBookId() == null || pageRequest.getUserId() == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getCount();
        int offset = (pageNum - 1) * pageSize;

        // 将 String 类型的 userId 和 bookId 转换为 Long 类型
        Long userId = Long.parseLong(pageRequest.getUserId());
        Long bookId = Long.parseLong(pageRequest.getBookId());

        List<DisplayChapterDTO> displayChapterDTOList;
        long totalCount;
        if (pageSize == 0) {
            // 全表查询：查出该用户该书的所有章节单词
            List<VocChapterDO> allChapters = vocMapper.getAllChapterVocsOfUserBook(userId, bookId);
            displayChapterDTOList = allChapters.stream()
                    .map(vocConverter::vocChapterDO2displayChapterDTO)
                    .collect(Collectors.toList());
            totalCount = displayChapterDTOList.size();
        } else {
            // 创建分页对象
            Page<VocChapterDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
            // 调用 Mapper 方法
            Page<VocChapterDO> chapterDOPage = vocMapper.getChapterVocsOfUserBook(userId, bookId, page);
            // 转换结果
            displayChapterDTOList = chapterDOPage.getRecords().stream()
                    .map(vocConverter::vocChapterDO2displayChapterDTO)
                    .collect(Collectors.toList());
            totalCount = chapterDOPage.getTotal();
        }

        PageDTO<DisplayChapterDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageList(displayChapterDTOList);
        pageDTO.setTotalCount(totalCount);
        return pageDTO;
    }

    //移动端查看单词详情
    @Override
    public VocDTO getVocDetail(Long vocId, Long userId) {
        if(vocId == null||userId==null){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        //1.查询单词基本信息
        VocDO voc = vocMapper.selectById(vocId);
        if(voc == null){
            System.out.println("单词的基本信息+"+voc);
            throw new BusinessException(ResultCodeEnum.VOC_NOT_EXISTED);
        }
        //2.查询单词释义
        LambdaQueryWrapper<VocMeaningDO> meaningQueryWrapper = new LambdaQueryWrapper<>();
        meaningQueryWrapper.eq(VocMeaningDO::getVocId,vocId);
        List<VocMeaningDO> meaningDOS = vocMeaningMapper.selectList(meaningQueryWrapper);
        //3.查询例句
        LambdaQueryWrapper<ExampleSentenceDO> exampleSentenceDOWrapper = new LambdaQueryWrapper<>();
        exampleSentenceDOWrapper.eq(ExampleSentenceDO::getVocId,vocId);
        
        List<ExampleSentenceDO> exampleSentenceDOS = exampleSentenceMapper.selectList(exampleSentenceDOWrapper);

        //查询用户自建例句
        LambdaQueryWrapper<UserVocExampleSentenceDO> userExampleQueryWrapper = new LambdaQueryWrapper<>();
        userExampleQueryWrapper.eq(UserVocExampleSentenceDO::getVocId,vocId);
        List<UserVocExampleSentenceDO> userVocExampleSentenceDOS = userVocExampleSentenceMapper.selectList(userExampleQueryWrapper);
        //5.将Do列表转换为Dto列表
        VocDTO vocDTO = vocConverter.vocDO2DTO(voc);
        // 填充单词释义
        List<VocMeaningDTO> meaningDTOs = vocConverter.vocMeaningDOs2DTOs(meaningDOS);
        vocDTO.setInterpretations(meaningDTOs);

        // 填充例句
        List<ExampleSentenceDTO> exampleDTOs = vocConverter.exampleSentenceDO2DTO(exampleSentenceDOS);
        vocDTO.setExamples(exampleDTOs);

        // 填充用户自建例句
        List<UserExampleSentenceDTO> userExampleDTOs = new ArrayList<>();
        for (UserVocExampleSentenceDO userVocExampleSentenceDO : userVocExampleSentenceDOS) {
            UserExampleSentenceDTO userExampleSentenceDTO = new UserExampleSentenceDTO();
            userExampleSentenceDTO.setId(userVocExampleSentenceDO.getId().toString());
            userExampleSentenceDTO.setUserId(userVocExampleSentenceDO.getUserId().toString());
            userExampleSentenceDTO.setVocId(userVocExampleSentenceDO.getVocId().toString());
            userExampleSentenceDTO.setSentence(userVocExampleSentenceDO.getSentence());
            userExampleSentenceDTO.setMeaning(userVocExampleSentenceDO.getMeaning());
            userExampleSentenceDTO.setVocContent(userVocExampleSentenceDO.getVocContent());
            userExampleDTOs.add(userExampleSentenceDTO);
        }
        vocDTO.setUserExamples(userExampleDTOs);

        // 查询并设置状态字段
        int reviewed = userVocRevMapper.countByUserIdAndVocId(userId, vocId) > 0 ? 1 : 0;
        vocDTO.setReviewed(reviewed);
        int notRemember = userNotRememberMapper.countByUserIdAndVocId(userId, vocId) > 0 ? 1 : 0;
        vocDTO.setNotRemember(notRemember);
        int favorite = userFavoriteMapper.countByUserIdAndVocId(userId, vocId) > 0 ? 1 : 0;
        vocDTO.setFavorite(favorite);

        return  vocDTO;
    }

    @Override
    public UserBookStatisticsDTO getUserChooseCountInBook(Long userId, Long bookId) {
        return null;
    }

    //校验上传单词的excel表格
    @Override
    public void vocExcelValidate(MultipartFile file) throws IOException {
        //检查文件是否为空
        if (file.isEmpty()) {
            throw new ServiceException("上传文件不为空");
        }
        //检查文件类型是否为Excel
        //抛出excel模版读取失败异常
        if (!file.getOriginalFilename().endsWith(".xlsx")&&!file.getOriginalFilename().endsWith(".xls")) {
            throw new BusinessException(ResultCodeEnum.VOC_EXCEL_TEMPLATE_READ_ERROR);
        }

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            //获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new ServiceException("表格未找到");
            }
            //检查表头
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new ServiceException("表头未找到");
            }

            //检查单词列
            if (headerRow.getCell(0) == null || !"单词".equals(headerRow.getCell(0).getStringCellValue())) {
                throw new ServiceException("单词列错误");
            }
            //检查英式英标列是否为空
            if (headerRow.getCell(1) == null || !"英式音标".equals(headerRow.getCell(1).getStringCellValue())) {
                throw new ServiceException("英式英标列错误");
            }
            //检查美式英标列是否为空
            if (headerRow.getCell(2) == null || !"美式音标".equals(headerRow.getCell(2).getStringCellValue())) {
                throw new ServiceException("美式英标列错误");
            }
            //检查释义列是否为空
            if (headerRow.getCell(5) == null || !"释义".equals(headerRow.getCell(5).getStringCellValue())) {
                throw new ServiceException("释义列错误");
            }
            //检查例句列是否为空
            if (headerRow.getCell(6) == null || !"例句".equals(headerRow.getCell(6).getStringCellValue())) {
                throw new ServiceException("例句列错误");
            }

            //检查数据行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                //获取当前行
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;//跳过空行
                }
                //检查单词列是否为空
                if (row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty()) {
                    throw new ServiceException("单词为空");
                }
            }
        }
    }

    //通过excel表格上传单词
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExcelAddOperateDTO addVocExcel(MultipartFile file, Long userId) throws IOException {

        //先校验文件
        vocExcelValidate(file);

        //初始化结果统计
        ExcelAddOperateDTO result = new ExcelAddOperateDTO();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            //获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return result;
            }

            //从第二行开始读取数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    //创建单词命令对象
                    VocCommand command = new VocCommand();
                    command.setContent(row.getCell(0).getStringCellValue());//单词
                    command.setUkPhonetic(row.getCell(1).getStringCellValue());//英式英标
                    command.setUsPhonetic(row.getCell(2).getStringCellValue());//美式英标

                    //处理释义
                    List<VocMeaningCommand> interpretations = new ArrayList<>();
                    String meaning = row.getCell(5).getStringCellValue();
                    VocMeaningCommand meaningCommand = new VocMeaningCommand();
                    meaningCommand.setType(meaning);
                    interpretations.add(meaningCommand);
                    command.setInterpretations(interpretations);

                    //处理例句
                    List<ExampleSentenceCommand> examples = new ArrayList<>();
                    String example = row.getCell(6).getStringCellValue();
                    ExampleSentenceCommand exampleCommand = new ExampleSentenceCommand();
                    exampleCommand.setContent(example);
                    examples.add(exampleCommand);
                    command.setExamples(examples);

                    //建立单词-章节-书籍关系
                    List<VocChapBookCommand> relations = new ArrayList<>();
                    //读取单词-章节-书籍关系
                    String vocRelationship = row.getCell(7).getStringCellValue();
                    if (vocRelationship != null && !vocRelationship.isEmpty()) {
                        //多个关系之间用;分隔
                        String[] relationships = vocRelationship.split(";");
                        System.out.println(Arrays.stream(relationships).toList());
//                        for (String relationship : relationships) {
//                            String[] parts = relationship.split("#");
//                            if (parts.length == 3) {
//                                Long chapId = Long.valueOf(parts[0]);
//                                Long bookId = Long.valueOf(parts[1]);
//                                VocChapBookCommand relationCommand = new VocChapBookCommand();
//                                relationCommand.setVocId(command.getId());
//                                relationCommand.setChapId(chapId);
//                                relationCommand.setBookId(bookId);
//                                command.getRelations().add(relationCommand);
//                            }
//                        }
                    }

                    //调用添加单词的方法
                    addVoc(command);

                }catch (Exception e){
                    log.error("添加单词失败，行号：" + (i + 1),e);
                }
            }
        }
        return result;
    }

    //删除对应单词的音频文件
    @Override
    public DeleteVocFileOperateDTO deleteFile(Long id, String type, String name) {
        //根据id获取单词
        VocDO voc = vocMapper.selectById(id);
        //判断单词是否存在
        if (voc == null) {
            throw new BusinessException(ResultCodeEnum.VOC_NOT_EXISTED);
        }
        //获取单词内容
        String content = voc.getContent();

        //根据单词内容删除对应的音频文件
        String fileName = content + "-" + type + ".wav";
        String audioFile = audio_file_dir + File.separator + fileName;

        File file = new File(audioFile);
        if (file.exists() && file.isFile()) {
            //删除文件
            file.delete();
        }
        // 构建删除操作日志对象
        DeleteVocFileOperateDTO dto = new DeleteVocFileOperateDTO();
        dto.setVocId(id);
        dto.setContent(content);
        dto.setType(type);
        dto.setFileName(fileName);

        return dto;
    }

    //删除单词-书籍-章节关系
    @Override
    public DeleteVocRelationOperateDTO deleteVocRelation(Long id) {
        DeleteVocRelationOperateDTO deleteVocRelationOperateDTO = new DeleteVocRelationOperateDTO();
        deleteVocRelationOperateDTO.setId(id);

        //查询要删除的关系
        VocChapBookDO relation = vocChapBookMapper.selectById(id);
        if (relation == null) {
            //没有找到对应关系，返回结果
            return deleteVocRelationOperateDTO;
        }
        //设置单词信息
        deleteVocRelationOperateDTO.setId(relation.getVocId());
        VocDO voc = vocMapper.selectById(relation.getVocId());
        if (voc != null) {
            deleteVocRelationOperateDTO.setVocName(voc.getContent());
        }
        //设置书籍信息
        deleteVocRelationOperateDTO.setBookId(relation.getBookId());
        VocBookDO book = vocBookMapper.selectById(relation.getBookId());
        if (book != null) {
            deleteVocRelationOperateDTO.setBookName(book.getName());
        }
        //设置章节信息
        deleteVocRelationOperateDTO.setChapterId(relation.getChapterId());
        VocChapterDO chapter = vocChapterMapper.selectById(relation.getChapterId());
        if (chapter != null) {
            deleteVocRelationOperateDTO.setChapterName(chapter.getChapterName());
        }

        try {
            //执行删除操作
            vocChapBookMapper.deleteById(id);
            //更新单词-章节-书籍关系的计数
            if (relation.getChapterId() != null) {
                vocChapterMapper.update(null, new LambdaUpdateWrapper<VocChapterDO>()
                        .eq(VocChapterDO::getId, relation.getChapterId())
                        .setSql("voc_count = voc_count - 1"));
            }
            if (relation.getBookId() != null) {
                vocBookMapper.update(null, new LambdaUpdateWrapper<VocBookDO>()
                        .eq(VocBookDO::getId, relation.getBookId())
                        .setSql("voc_count = voc_count - 1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deleteVocRelationOperateDTO;
    }

    //删除单词释义
    @Override
    public DeleteInterpretationOperateDTO deleteInterpretation(Long id) {
        //根据id删除单词释义
        DeleteInterpretationOperateDTO result = new DeleteInterpretationOperateDTO();
        result.setId(id); // 设置删除的 id
        try {
            // 执行删除操作并获取受影响的记录数
            int rowsAffected = vocMeaningMapper.deleteById(id);
            if (rowsAffected > 0) {
                // 删除成功
                result.setType("删除成功");
                result.setInterContent("单词释义删除成功");
            } else {
                // 未找到对应记录，删除失败
                result.setType("删除失败");
                result.setInterContent("未找到对应单词释义，删除失败");
            }
        } catch (Exception e) {
            // 处理异常
            result.setType("删除异常");
            result.setInterContent("删除单词释义时出现异常: " + e.getMessage());
        }
        return result;
    }

    //删除单词例句
    @Override
    public DeleteVocExampleOperateDTO deleteExample(Long id) {
        //根据id删除单词的例句
        DeleteVocExampleOperateDTO result = new DeleteVocExampleOperateDTO();
        result.setId(id); // 设置删除的 id
        try {
            // 先查询要删除的例句内容和对应的单词信息
            ExampleSentenceDO exampleSentence = exampleSentenceMapper.selectById(id);
            if (exampleSentence != null) {
                result.setSentence(exampleSentence.getContent());
                result.setVocId(exampleSentence.getVocId());
                // 通过 vocId 查询对应的单词名称
                VocDO voc = vocMapper.selectById(exampleSentence.getVocId());
                if (voc != null) {
                    result.setVocName(voc.getContent());
                }

                // 执行删除操作并获取受影响的记录数
                int rowsAffected = exampleSentenceMapper.deleteById(id);
                if (rowsAffected > 0) {
                    // 删除成功
                    result.setSentence("单词例句删除成功: " + exampleSentence.getContent());
                } else {
                    // 理论上不会进入这里，因为前面已经查询到记录
                    result.setSentence("未找到对应单词例句，删除失败");
                }
            } else {
                // 未找到对应记录，删除失败
                result.setSentence("未找到对应单词例句，删除失败");
            }
        } catch (Exception e) {
            // 处理异常
            result.setSentence("删除单词例句时出现异常: " + e.getMessage());
        }
        return result;
    }


}
