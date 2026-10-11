package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.request.TopicCommand;
import com.cskaoyan.wordmemorize.request.VocPageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TopicService {

    // 检查单词是否存在(物料管理-题目管理，添加题目时发起的请求)
    VocDTO checkWord(String word);
    // 分页获取单选题列表(物料管理-题目管理)
    PageDTO<ReviewTopicDTO> getSingleChoicePage(VocPageRequest vocPageRequest);
    // 分页获取选词填空题列表(物料管理-题目管理)
    PageDTO<ReviewTopicDTO> getFillWordPage(VocPageRequest vocPageRequest);
    // 添加题目(物料管理-题目管理)
    void addTopic(TopicCommand command);

    // 获取题目(物料管理-题目管理)
    ReviewTopicDTO getTopic(Long topicId);
    // 更新题目(物料管理-题目管理)
    void updateTopic(TopicCommand command);
    // 删除题目(物料管理-题目管理)
    DeleteTopicOperateDTO deleteTopic(Long topicId, Integer topicType);

    // 检查excel中的题目(物料管理-题目管理)
    void topicExcelValidate(String type, MultipartFile file) throws IOException;
    // excel添加单选题目(物料管理-题目管理)
    ExcelAddOperateDTO addSinleChoiceExcel(MultipartFile file, Long employeeId) throws IOException;

    // excel添加单选题目(物料管理-题目管理)
    ExcelAddOperateDTO addFillWordExcel(MultipartFile file, Long employeeId) throws IOException;

    // 删除选词填空题的小题(物料管理-题目管理)
    DeleteSubTopicOperateDTO deleteSubTopic(Long id);
    // 删除选词填空题的干扰项或者删除单选题选项(物料管理-题目管理)
    DeleteTopicOptionOperateDTO deleteTopicOption(Long id);
}
