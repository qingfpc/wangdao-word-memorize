package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.*;

import java.util.List;

public interface UserPrivilegeService {


    // APP端获取词书进度
    UserVocBookStatisticsDTO getSelectedBookStatistics(Long userId, Long bookId);
    // APP端批量添加收藏单词
    void addFavoriteBulk(List<UserFavoriteCommand> userFavoriteCommand, Long userId);

    // APP端单个添加收藏单词
    void addFavorite(UserFavoriteCommand userFavoriteCommand, Long userId);
    // APP端取消收藏单词
    void cancelFavorite(List<Long> ids, Long userId);

    // APP端获取单词收藏列表
    PageDTO<UserFavoriteDTO> getUserFavoriteList(PageRequest pageRequest, Long userId);

    // APP端添加用户自建例句
    void addUserVocSentence(UserVocExampleSentenceCommand command, Long userId);

    // APP端获取用户自建例句
    PageDTO<DisplayUserVocSentenceDTO>  getUserVocSentence(PageRequest pageRequest, Long userId);

    // APP端更新用户自建例句
    void updateUserVocSentence(UserVocExampleSentenceCommand command, Long userId);

    // APP端删除用户自建例句
    void deleteUserVocSentence(Long id);

    // APP端表示不想背
    void notRemember(Long vocId, Long userId);

    // APP端取消标识不想背
    void cancelNotRemember(Long vocId, Long userId);

    // APP端获取选词填空题列表
    List<UserReviewFillWordDTO> getUserReviewFillWordTopics(List<Long> vocIds);
    // APP端获取单选题目列表
    List<UserReviewSingleChoiceDTO> getVocSingleChoiceTopic(List<Long> vocIds);
    // APP端获取AI阅读模式以及选项
    List<UserVocAISituationDTO> getAIReviewReadOptions();

    // APP端获取用户剩余免费AI阅读次数
    Integer getUserVocAIReadRemainFreeCount(Long userId);

    // APP端获取AI阅读结果
    List<AIReviewReadDTO> getAIReviewReadContent(AIReviewReadCommand command, Long userId);
}
