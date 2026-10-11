package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.DisplayBookVocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayChapterDTO;
import com.cskaoyan.wordmemorize.dto.app.UserBookStatisticsDTO;
import com.cskaoyan.wordmemorize.dto.app.UserVocBookStatisticsDTO;
import com.cskaoyan.wordmemorize.request.*;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface VocService {

    // 分页查询单词信息(物料管理-词汇管理)
    PageDTO<VocDTO> getVocabularies(VocPageRequest pageQuery);
    // 添加单词信息(物料管理-词汇管理)
    void addVoc(VocCommand command);
    // 更新单词信息(物料管理-词汇管理)
    void updateVoc(VocCommand command);
    // 删除单词信息(物料管理-词汇管理)
    DeleteVocOperateDTO deleteVocabulary(Long vocId);
    // 批量删除单词信息(物料管理-词汇管理)
    List<DeleteVocOperateDTO> deleteVocabularies(BatchDeleteCommand command);
    // 获取书籍-章节-单词关系列表(物料管理-词汇管理)
    List<VocChapBookDTO> getVocRelations(Long vocId);
    // APP端获取无章节的书籍单词列表
    PageDTO<DisplayBookVocDTO> getDisplayBookVocsInfo(AppVocPageRequest pageRequest);
    // APP端获取有章节的书籍单词列表
    PageDTO<DisplayChapterDTO> getDisplayBookChapterVocsInfo(AppVocPageRequest pageRequest);
    // APP端获取单词详情
    VocDTO getVocDetail(Long vocId, Long userId);
    // APP端目标书籍已选单词，未选单词数据
    UserBookStatisticsDTO getUserChooseCountInBook(Long userId, Long bookId);
    // 校验上传单词excel文件(后端-物料管理-词汇管理)
    void vocExcelValidate(MultipartFile file) throws IOException;
    // excel文件上传单词(后端-物料管理-词汇管理)
    ExcelAddOperateDTO addVocExcel(MultipartFile file, Long userId) throws IOException;
    // 删除单词音频文件(后端-物料管理-词汇管理)
    DeleteVocFileOperateDTO deleteFile(Long id, String type, String name);
    // 删除书籍-章节-单词关系(后端-物料管理-词汇管理)
    DeleteVocRelationOperateDTO deleteVocRelation(Long id);
    // 删除单词释义(后端-物料管理-词汇管理)
    DeleteInterpretationOperateDTO deleteInterpretation(Long id);
    // 删除单词例句(后端-物料管理-词汇管理)
    DeleteVocExampleOperateDTO deleteExample(Long id);
}
