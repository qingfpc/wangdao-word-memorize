package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.business.IdBatchDeleter;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.TopicConverter;
import com.cskaoyan.wordmemorize.converter.UserPrivilegeConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.*;
import com.cskaoyan.wordmemorize.service.UserPrivilegeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.SecurityUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatPromptTemplate;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserPrivilegeServiceImpl implements UserPrivilegeService {

    @Resource
    UserPrivilegeConverter converter;

    @Resource
    UserFavoriteMapper userFavoriteMapper;

    @Resource
    UserVocRevMapper userVocRevMapper;

    @Resource
    LevelPrivilegeMapper levelPrivilegeMapper;

    @Resource
    UserNotRememberMapper userNotRememberMapper;

    @Resource
    UserVocExampleSentenceMapper userVocExampleSentenceMapper;

    @Resource
    UserCheckinStatisticsMapper userCheckinStatisticsMapper;


    @Resource
    UserCheckinMapper userCheckinMapper;


    @Resource
    ReviewSubTopicMapper reviewSubTopicMapper;

    @Resource
    ReviewTopicOptionMapper reviewTopicOptionMapper;

    @Resource
    TopicConverter topicConverter;

    @Resource
    ReviewTopicMapper reviewTopicMapper;

    @Resource
    UserVocAISituationMapper userVocAISituationMapper;

    @Resource
    UserPrivilegeHistoryMapper userPrivilegeHistoryMapper;

    @Resource
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    UserVocBoundLogMapper userVocBoundLogMapper;

    @Resource
    LevelMapper levelMapper;


    @Resource
    VocMapper vocMapper;


    @Resource
    UserPrivilegeServiceImpl own;

    @Resource
    VocBookMapper vocBookMapper;


    private ChatClient chatClient;

    public UserPrivilegeServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    @Override
    public UserVocBookStatisticsDTO getSelectedBookStatistics(Long userId, Long bookId) {

        UserVocBookStatisticsDTO result = new UserVocBookStatisticsDTO();
        Integer userVocRevBookNotStarted = userVocRevMapper.getUserVocRevBookStatisticByType(userId, bookId
                , CommonConstant.USER_VOC_NOT_STARTED_TYPE);
        int notStarted = userVocRevBookNotStarted == null ? 0 : userVocRevBookNotStarted;
        result.setNotStarted(notStarted);

        Integer familiarCount = userVocRevMapper.getUserVocRevBookStatisticByInterval(userId, bookId, true, CommonConstant.USER_REVIEW_FAMILIAR_MIN_INTERVAL);
        Integer unFamiliarCount = userVocRevMapper.getUserVocRevBookStatisticByInterval(userId, bookId, false, CommonConstant.USER_PRE_REVIEW_MAX_INTERVAL);
        // 熟悉的单词数量
        result.setFamiliar(familiarCount == null ? 0 : familiarCount);
        // 不熟悉的单词数量
        result.setUnFamiliar(unFamiliarCount == null ? 0 : unFamiliarCount);

        Integer userVocRevBookStarted = userVocRevMapper.getUserVocRevBookStatisticByType(userId, bookId
                , 1);
        int startedBookVocCount = userVocRevBookStarted == null ? 0 : userVocRevBookStarted.intValue();
//        LambdaQueryWrapper<UserVocRevDO> userVocRevWrapper = new LambdaQueryWrapper<>();
//        userVocRevWrapper.eq(UserVocRevDO::getUserId, userId);
//        Long totalRevCount = userVocRevMapper.selectCount(userVocRevWrapper);
        Integer generalCount = startedBookVocCount - familiarCount - unFamiliarCount;
        // 一般的单词数量
        result.setGeneral(generalCount);


        VocBookDO vocBookDO = vocBookMapper.selectById(bookId);
        Integer userVocRevOfBookCount = userVocRevMapper.getUserVocRevOfBookCount(userId, bookId);
        Integer unSelectedCount = vocBookDO.getVocCount() - userVocRevOfBookCount;
        // 未选择的单词数量
        result.setUnSelected(unSelectedCount);
        // 总的书籍单词数量
        result.setTotal(vocBookDO.getVocCount());
        return result;
    }


    @Override
    @Transactional
    public void addFavoriteBulk(List<UserFavoriteCommand> userFavoriteCommands, Long userId) {


        List<Long> ids = userFavoriteCommands.stream()
                .map(command -> Long.parseLong(command.getVocId())).collect(Collectors.toList());

        List<VocDO> vocDOs = vocMapper.selectByIds(ids);
        for (VocDO vocDO : vocDOs) {
            LambdaQueryWrapper<UserFavoriteDO> userVocRevWrapper = new LambdaQueryWrapper<>();
            userVocRevWrapper.eq(UserFavoriteDO::getUserId, userId)
                    .eq(UserFavoriteDO::getVocId, vocDO.getId());
            UserFavoriteDO userFavoriteDO = userFavoriteMapper.selectOne(userVocRevWrapper);
            if (userFavoriteDO != null) {
                continue;
            }
            addSingleFavorite(userId, vocDO);

        }
    }

    private void addSingleFavorite(Long userId, VocDO vocDO) {

        UserFavoriteDO userFavoriteDO = new UserFavoriteDO();
        userFavoriteDO.setUserId(userId);
        userFavoriteDO.setVocContent(vocDO.getContent());
        userFavoriteDO.setVocId(vocDO.getId());

        LambdaQueryWrapper<UserVocRevDO> userVocRevWrapper = new LambdaQueryWrapper<>();
        userVocRevWrapper.eq(UserVocRevDO::getUserId, userId)
                .eq(UserVocRevDO::getVocId, vocDO.getId());
        UserVocRevDO userVocRevDO = userVocRevMapper.selectOne(userVocRevWrapper);
        if (userVocRevDO != null) {
            userFavoriteDO.setSelected(1);
        } else {
            userFavoriteDO.setSelected(0);
        }

        userFavoriteMapper.insert(userFavoriteDO);
    }

    @Override
    public void addFavorite(UserFavoriteCommand userFavoriteCommand, Long userId) {
        String vocIdStr = userFavoriteCommand.getVocId();
        long vocId = Long.parseLong(vocIdStr);
        LambdaQueryWrapper<UserFavoriteDO> favoriteWrapper = Wrappers.lambdaQuery(UserFavoriteDO.class)
                .eq(UserFavoriteDO::getUserId, userId)
                .eq(UserFavoriteDO::getVocId, vocId);
        UserFavoriteDO userFavoriteDO = userFavoriteMapper.selectOne(favoriteWrapper);
        if (userFavoriteDO != null) {
            // 如果已收藏，不在重复收藏
            return;
        }
        VocDO vocDO = vocMapper.selectById(vocId);
        addSingleFavorite(userId, vocDO);
    }

    @Override
    @Transactional
    public void cancelFavorite(List<Long> ids, Long userId) {
        // 批量删除
        IdBatchDeleter<UserFavoriteDO> idsDeleter
                = new IdBatchDeleter<>(CommonConstant.BATCH, userFavoriteMapper);
        idsDeleter.delete(UserFavoriteDO::getId, ids);
    }

    @Override
    public PageDTO<UserFavoriteDTO> getUserFavoriteList(PageRequest pageRequest, Long userId) {
        Integer offset = null;
        if (pageRequest.getPageNum() == null) {
            offset = (pageRequest.getPageNum() - 1) * pageRequest.getCount();
        }
        String keyword = pageRequest.getKeyword() + "%";
        List<UserFavoriteDO> userFavoriteDOPage = userFavoriteMapper.getUserFavoritePage(userId, keyword);
        LambdaQueryWrapper<UserFavoriteDO> userFavoriteWrapper = new LambdaQueryWrapper<>();
        userFavoriteWrapper.eq(UserFavoriteDO::getUserId, userId);
        if (pageRequest.getKeyword() != null) {
            userFavoriteWrapper.likeRight(UserFavoriteDO::getVocContent, pageRequest.getKeyword());
        }
        Long count = userFavoriteMapper.selectCount(userFavoriteWrapper);
        return converter.userFavoriteDOPage2PageDTO(userFavoriteDOPage, count);
    }


//    @Override
//    public LevelPrivilegeDTO getPrivilegeInfo(String privilegeCode) {
//        LambdaQueryWrapper<LevelPrivilegeDO> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(LevelPrivilegeDO::getPrivilegeCode, privilegeCode);
//
//        LevelPrivilegeDO levelPrivilegeDO = levelPrivilegeMapper.selectOne(queryWrapper);
//
//        return converter.levelPrivilegeDO2DTO(levelPrivilegeDO);
//    }


    @Override
    public void addUserVocSentence(UserVocExampleSentenceCommand command, Long userId) {

        command.setUserId(userId.toString());

        UserVocExampleSentenceDO userVocExampleSentenceDO
                = converter.userVocExampleSentenceCommand2DO(command);


        // 为指定单词添加一条自定义例句
        userVocExampleSentenceMapper.insert(userVocExampleSentenceDO);
    }

    @Override
    public PageDTO<DisplayUserVocSentenceDTO> getUserVocSentence(PageRequest pageRequest, Long userId) {

        Integer offset = (pageRequest.getPageNum() - 1) * pageRequest.getCount();

        LambdaQueryWrapper<UserVocExampleSentenceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserVocExampleSentenceDO::getUserId, userId);

        Integer total = userVocExampleSentenceMapper.getUserVocSentenceCount(userId);

        List<UserVocExampleSentenceDO> userVocSentences
                = userVocExampleSentenceMapper.getUserVocSentencePage(userId, pageRequest.getCount(), offset);

        Map<Long, List<UserVocExampleSentenceDO>> userVocSentenceMap
                = userVocSentences.stream().collect(Collectors.groupingBy(UserVocExampleSentenceDO::getVocId));

        List<DisplayUserVocSentenceDTO> resultList = userVocSentenceMap.entrySet().stream().map(entry -> {
            Long vocId = entry.getKey();
            List<UserVocExampleSentenceDO> sentences = entry.getValue();

            DisplayUserVocSentenceDTO target = new DisplayUserVocSentenceDTO();
            target.setUserId(userId.toString());
            target.setVocId(vocId.toString());
            String vocContent = sentences.get(0).getVocContent();
            target.setVocContent(vocContent);
            List<UserVocSentenceDTO> userVocSentenceDTOs = converter.userVocSentenceDOs2DTOs(sentences);
            target.setSentences(userVocSentenceDTOs);
            return target;

        }).collect(Collectors.toList());

        return converter.displayUserVocSentenceDTOList2PageDTO(resultList, total.longValue());

    }

    @Override
    public void updateUserVocSentence(UserVocExampleSentenceCommand command, Long userId) {

        command.setUserId(userId.toString());

        UserVocExampleSentenceDO userVocExampleSentenceDO
                = converter.userVocExampleSentenceCommand2DO(command);

        // 例句是一次就一条，所以可以直接更新
        userVocExampleSentenceMapper.updateById(userVocExampleSentenceDO);
    }

    @Override
    public void deleteUserVocSentence(Long id) {
        userVocExampleSentenceMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void notRemember(Long vocId, Long userId) {
        LambdaQueryWrapper<UserNotRememberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNotRememberDO::getUserId, userId)
                .eq(UserNotRememberDO::getVocId, vocId);

        // 添加不想背记录
        UserNotRememberDO userNotRememberDO = userNotRememberMapper.selectOne(queryWrapper);
        if (userNotRememberDO != null) {
            return;
        }

        // 没有这条记录，则添加记录
        UserNotRememberDO toInsert = new UserNotRememberDO();
        toInsert.setUserId(userId);
        toInsert.setVocId(vocId);
        userNotRememberMapper.insert(toInsert);

        // 删除用户的复习记录
        LambdaQueryWrapper<UserVocRevDO> toDeleteWrapper = new LambdaQueryWrapper<>();
        toDeleteWrapper.eq(UserVocRevDO::getUserId, userId)
                .eq(UserVocRevDO::getVocId, vocId);
        userVocRevMapper.delete(toDeleteWrapper);

        // 修改用户的可用单词上限
        userVocBoundStatisticsMapper.updateOccupiedUserVocBound(-1, userId);
    }

    /*
          取消标识不背的话
     */
    @Override
    public void cancelNotRemember(Long vocId, Long userId) {


        LambdaQueryWrapper<UserNotRememberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNotRememberDO::getUserId, userId)
                .eq(UserNotRememberDO::getVocId, vocId);

        userNotRememberMapper.delete(queryWrapper);
    }

    @Override
    public List<UserReviewFillWordDTO> getUserReviewFillWordTopics(List<Long> vocIds) {
        LambdaQueryWrapper<ReviewSubTopicDO> reviewSubTopicQueryWrapper = new LambdaQueryWrapper<>();
        reviewSubTopicQueryWrapper.in(ReviewSubTopicDO::getVocId, vocIds)
                .select(ReviewSubTopicDO::getTid, ReviewSubTopicDO::getVocId);
        List<ReviewSubTopicDO> reviewSubTopics = reviewSubTopicMapper.selectList(reviewSubTopicQueryWrapper);

        // 处理一个单词多个题目的情况
        Map<Long, List<ReviewSubTopicDO>> reviewSubTopicMapByVocId
                = reviewSubTopics.stream().collect(Collectors.groupingBy(ReviewSubTopicDO::getVocId));

        // 利用set这样一来tid也就唯一了
        Set<Long> tidsSet = new TreeSet();
        Random random = new Random();
        for (Map.Entry<Long, List<ReviewSubTopicDO>> entry : reviewSubTopicMapByVocId.entrySet()) {
            List<ReviewSubTopicDO> subTopics = entry.getValue();
            if (subTopics.size() == 1) {
                tidsSet.add(subTopics.get(0).getTid());
                continue;
            }

            // 如果一个单词对应多个小题，随机选择一个
            int randomIndex = random.nextInt(subTopics.size());
            tidsSet.add(subTopics.get(randomIndex).getTid());

            //List<ReviewSubTopicDO> collect = subTopics.stream().sorted((o1, o2) -> o1.getTid().compareTo(o2.getTid())).collect(Collectors.toList());
            //tidsSet.add(collect.get(0).getTid());
        }


        // 查询所有小题
        reviewSubTopicQueryWrapper = new LambdaQueryWrapper<>();
        reviewSubTopicQueryWrapper.in(ReviewSubTopicDO::getTid, tidsSet);
        List<ReviewSubTopicDO> subTopics = reviewSubTopicMapper.selectList(reviewSubTopicQueryWrapper);

        // 查询所有题目对应的选项
        LambdaQueryWrapper<TopicOptionDO> reviewSubTopicWrapper = new LambdaQueryWrapper<>();
        reviewSubTopicWrapper.in(TopicOptionDO::getTid, tidsSet);
        List<TopicOptionDO> subTopicOptions = reviewTopicOptionMapper.selectList(reviewSubTopicWrapper);

        Map<Long, List<ReviewSubTopicDO>> tidSubTopicsMap = subTopics.stream()
                .collect(Collectors.groupingBy(ReviewSubTopicDO::getTid));


        Map<String, TopicOptionDTO> tidSubTopicOptionsMap = subTopicOptions.stream().map(subTopicOption -> converter.reviewTopicOptionDO2DTO(subTopicOption))
                .collect(Collectors.toMap(TopicOptionDTO::getTid, topicOptionDTO -> topicOptionDTO));
        List<UserReviewFillWordDTO> result = new LinkedList<>();
        for (Long tid : tidsSet) {
            UserReviewFillWordDTO userReviewFillWordDTO = new UserReviewFillWordDTO();
            List<ReviewSubTopicDO> subTopicDOs = tidSubTopicsMap.get(tid.toString());
            List<ReviewSubTopicDTO> reviewSubTopicDTOs = converter.reviewSubTopicDOs2DTOs(subTopicDOs);
            reviewSubTopicDTOs.sort(Comparator.comparing(ReviewSubTopicDTO::getSubTopicNumber));
            userReviewFillWordDTO.setSubTopicDTOs(reviewSubTopicDTOs);
            userReviewFillWordDTO.setOption(tidSubTopicOptionsMap.get(tid.toString()));
            result.add(userReviewFillWordDTO);
        }
        return result;
    }

    @Override
    public List<UserReviewSingleChoiceDTO> getVocSingleChoiceTopic(List<Long> vocIds) {

        LambdaQueryWrapper<ReviewTopicDO> reviewTopicQueryWrapper = new LambdaQueryWrapper<>();
        reviewTopicQueryWrapper.in(ReviewTopicDO::getVocId, vocIds)
                .eq(ReviewTopicDO::getTopicType, CommonConstant.TOPIC_TYPE_SINGLE_CHOICE);

        List<ReviewTopicDO> reviewTopicDOs = reviewTopicMapper.selectList(reviewTopicQueryWrapper);

        // 题目
        List<ReviewTopicDO> topics = new ArrayList<>();
        Map<Long, List<ReviewTopicDO>> reviewTopicMap = reviewTopicDOs.stream()
                .collect(Collectors.groupingBy(ReviewTopicDO::getVocId));

        Random random = new Random();
        reviewTopicMap.forEach((id, reviewTopicDOsByVocId) -> {
            if (reviewTopicDOsByVocId.size() == 1) {
                topics.add(reviewTopicDOsByVocId.get(0));
                return;
            }
            int randomIndex = random.nextInt(reviewTopicDOsByVocId.size());
            topics.add(reviewTopicDOsByVocId.get(randomIndex));

//            reviewTopicDOsByVocId.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
//            topics.add(reviewTopicDOsByVocId.get(0));
        });

        // 选项
        List<Long> tids = topics.stream().map(ReviewTopicDO::getId).collect(Collectors.toList());

        LambdaQueryWrapper<TopicOptionDO> topicOptionWrapper = new LambdaQueryWrapper<>();
        topicOptionWrapper.in(TopicOptionDO::getTid, tids);

        // 查询所有题目对应的选项
        List<TopicOptionDO> topicOptionDOs
                = reviewTopicOptionMapper.selectList(topicOptionWrapper);

        Map<Long, List<TopicOptionDO>> options
                = topicOptionDOs.stream().collect(Collectors.groupingBy(TopicOptionDO::getTid));

        for (ReviewTopicDO topicDO : topics) {
            List<TopicOptionDO> items = options.get(topicDO.getId());
            topicDO.setOptions(items);
        }

        List<UserReviewSingleChoiceDTO> result = topicConverter.topicDOs2UserReviewSingleChoiceDTOs(topics);
        return result;
    }

    @Override
    public List<UserVocAISituationDTO> getAIReviewReadOptions() {
        List<UserVocAISituationDO> aiReadSituationInfo
                = userVocAISituationMapper.getAIReadSituationInfo();

        return converter.userVocAISituationDOs2DTOs(aiReadSituationInfo);
    }

    @Override
    public Integer getUserVocAIReadRemainFreeCount(Long userId) {

        // 查询特权信息，查询免费使用次数
        LambdaQueryWrapper<LevelPrivilegeDO> levelPrivilegeWrapper = new LambdaQueryWrapper<>();
        levelPrivilegeWrapper.eq(LevelPrivilegeDO::getPrivilegeCode, CommonConstant.USER_PRIVILEGE_CODE_AI_READ);
        LevelPrivilegeDO levelPrivilegeDO = levelPrivilegeMapper.selectOne(levelPrivilegeWrapper);

        // 查询使用历史数据
        LambdaQueryWrapper<UserPrivilegeUseStatisticsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPrivilegeUseStatisticsDO::getUserId, userId)
                .eq(UserPrivilegeUseStatisticsDO::getPrivilegeCode, CommonConstant.USER_PRIVILEGE_CODE_AI_READ);

        UserPrivilegeUseStatisticsDO userPrivilegeUseStatisticsDO = userPrivilegeHistoryMapper.selectOne(queryWrapper);

        if (userPrivilegeUseStatisticsDO == null) {
            // 说明是第一次使用
            UserPrivilegeUseStatisticsDO toInsert = new UserPrivilegeUseStatisticsDO();
            toInsert.setUserId(userId);
            toInsert.setPrivilegeCode(CommonConstant.USER_PRIVILEGE_CODE_AI_READ);
            toInsert.setUseCount(0);
            userPrivilegeHistoryMapper.insert(toInsert);
            // 直接返回免费使用次数
            return levelPrivilegeDO.getFreeCount();
        }
        log.info("free count {}, use count {}", levelPrivilegeDO.getFreeCount(), userPrivilegeUseStatisticsDO.getUseCount());
        int remains = levelPrivilegeDO.getFreeCount() - userPrivilegeUseStatisticsDO.getUseCount();
        if (remains > 0) {
            return remains;
        }
        return 0;
    }

    @Value("classpath:/prompt/ai-read-system-message.st")
    private org.springframework.core.io.Resource systemResource;

    @Value("classpath:/prompt/ai-read-user-message.st")
    private org.springframework.core.io.Resource userResource;

    @Override
    public List<AIReviewReadDTO> getAIReviewReadContent(AIReviewReadCommand command, Long userId) {
        // 扣减单词上限
        LambdaQueryWrapper<UserVocBoundStatisticsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserVocBoundStatisticsDO::getUserId, userId);
        UserVocBoundStatisticsDO userVocBoundStatisticsDO
                = userVocBoundStatisticsMapper.selectOne(queryWrapper);

        LambdaQueryWrapper<LevelPrivilegeDO> levelPrivilegeWrapper = new LambdaQueryWrapper<>();
        levelPrivilegeWrapper.eq(LevelPrivilegeDO::getPrivilegeCode, CommonConstant.USER_PRIVILEGE_CODE_AI_READ);
        LevelPrivilegeDO levelPrivilegeDO = levelPrivilegeMapper.selectOne(levelPrivilegeWrapper);
        if (userVocBoundStatisticsDO.getAvailable() < levelPrivilegeDO.getRequireVocBound()) {
            throw new BusinessException(ResultCodeEnum.USER_VOC_BOUND_NOT_ENOUGH);
        }


        // 构造系统提示词
        PromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);



        // 构造用户提示词
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("situation", command.getSituationName());
        paramMap.put("option", command.getOptionName());
        paramMap.put("words", command.getVocList().toString());
        if ("教育模式".equals(command.getSituationName())) {
            paramMap.put("type", "短文");
        } else if ("对话模式".equals(command.getSituationName())) {
            paramMap.put("type", "对话");
        } else {
            paramMap.put("type", "故事");
        }
        PromptTemplate userPrompt = new PromptTemplate(userResource, paramMap);



        ChatPromptTemplate chatPromptTemplate = new ChatPromptTemplate(Arrays.asList(systemPromptTemplate, userPrompt));

        List<AIReviewReadDTO> result = chatClient.prompt(chatPromptTemplate.create())
                .call()
                .entity(new ParameterizedTypeReference<List<AIReviewReadDTO>>() {
                });

        own.afterGenerateAIRead(userId, levelPrivilegeDO);
        return result;
    }

    @Transactional
    public void afterGenerateAIRead(Long userId, LevelPrivilegeDO levelPrivilegeDO) {
        // 扣减单词上限
        LambdaUpdateWrapper<UserVocBoundStatisticsDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserVocBoundStatisticsDO::getUserId, userId)
                .setSql("available = available - " + levelPrivilegeDO.getRequireVocBound())
                .setSql("exchange = exchange - " + levelPrivilegeDO.getRequireVocBound());
        userVocBoundStatisticsMapper.update(updateWrapper);

        // 添加单词上限使用记录
        UserVocBoundLogDO userVocBoundLogDO = new UserVocBoundLogDO();
        userVocBoundLogDO.setUserId(userId);
        userVocBoundLogDO.setType(CommonConstant.USER_VOC_BOUND_CHANGE_TYPE_EXCHANGE);
        userVocBoundLogDO.setCount(levelPrivilegeDO.getRequireVocBound());
        userVocBoundLogDO.setDescription(CommonConstant.USER_VOC_BOUND_DESCRIPTION_AI_READ);
        userVocBoundLogDO.setLogDate(LocalDate.now());
        userVocBoundLogMapper.insert(userVocBoundLogDO);

        // 修改数据库中的使用次数
        LambdaUpdateWrapper<UserPrivilegeUseStatisticsDO> privelegeUseHistoryWrapper = new LambdaUpdateWrapper<>();
        privelegeUseHistoryWrapper.eq(UserPrivilegeUseStatisticsDO::getUserId, userId)
                .eq(UserPrivilegeUseStatisticsDO::getPrivilegeCode, CommonConstant.USER_PRIVILEGE_CODE_AI_READ)
                .setSql("use_count = use_count + 1");
        userPrivilegeHistoryMapper.update(privelegeUseHistoryWrapper);
    }

}
