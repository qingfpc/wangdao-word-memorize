package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.constant.UserVocBoundEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.UserConverter;
import com.cskaoyan.wordmemorize.converter.UserPrivilegeConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationOptionDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.AIReviewReadCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserFavoriteCommand;
import com.cskaoyan.wordmemorize.request.UserVocExampleSentenceCommand;
import com.cskaoyan.wordmemorize.service.UserPrivilegeService;
import io.swagger.v3.oas.models.examples.Example;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.dev33.satoken.SaManager.log;


@Service
public class UserPrivilegeServiceImpl implements UserPrivilegeService {
    //定义默认免费使用次数
    final int FREE_USE_COUNT = 10;

    @Autowired
    UserFavoriteMapper userFavoriteMapper;

    @Autowired
    ExampleSentenceMapper exampleSentenceMapper;

    @Autowired
    VocBookMapper vocBookMapper;

    @Autowired
    VocMapper vocMapper;

    @Autowired
    VocChapterMapper vocChapterMapper;

    @Autowired
    VocChapBookMapper vocChapBookMapper;

    @Autowired
    UserVocRevMapper userVocRevMapper;


    @Autowired
    UserNotRememberMapper userNotRememberMapper;

    @Autowired
    UserVocExampleSentenceMapper userVocExampleSentenceMapper;

    @Autowired
    UserPrivilegeConverter userPrivilegeConverter;

    @Autowired
    UserRevStatisticsMapper userRevStatisticsMapper;

    @Autowired
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;


    @Autowired
    UserConverter userConverter;


    @Autowired
    UserPrivilegeHistoryMapper userPrivilegeHistoryMapper;


    @Autowired
    UserVocBoundLogMapper userVocBoundLogMapper;

    @Value("classPath:/prompt/ai-read-system-message.st")
    Resource systemResource;

    @Value("classPath:/prompt/ai-read-user-message.st")
    Resource userResource;


    @Override
    public UserVocBookStatisticsDTO getSelectedBookStatistics(Long userId, Long bookId) {


        // 1. 获取单词书总数量
        VocBookDO vocBookDO = vocBookMapper.selectById(bookId);
        Integer totalCount = vocBookDO.getVocCount();

// 2. 获取用户已学习的单词总数
        Long selectedCount = userVocRevMapper.selectUserVocRevDataCount(userId);
        selectedCount = selectedCount == null ? 0 : selectedCount;

// 3. 获取所有已学习单词的详细数据
        List<UserVocRevDO> learnedVocList = userVocRevMapper.selectUserVocRevData(0, selectedCount.intValue(), userId);

// 4. 统计各状态数量
        int notStarted = 0;
        int familiar = 0;
        int general = 0;
        int unFamiliar = 0;


        for (UserVocRevDO revDO : learnedVocList) {
            // 使用familiarity字段替代memoryLevel字段
            Integer familiarity = revDO.getFamiliarity();

            if (familiarity == null) {
                notStarted++;
            } else if (familiarity == 1) {
                unFamiliar++;
            } else if (familiarity == 2) {
                general++;
            } else if (familiarity == 3) {
                familiar++;
            } else {
                notStarted++;
            }
        }


// 5. 计算未选择数量
        int unSelected = totalCount - selectedCount.intValue();
        unSelected = Math.max(unSelected, 0); // 确保非负

// 6. 组装返回结果
        UserVocBookStatisticsDTO statisticsDTO = new UserVocBookStatisticsDTO();
        statisticsDTO.setTotal(totalCount);
        statisticsDTO.setNotStarted(notStarted);
        statisticsDTO.setFamiliar(familiar);
        statisticsDTO.setGeneral(general);
        statisticsDTO.setUnFamiliar(unFamiliar);
        statisticsDTO.setUnSelected(unSelected);

        return statisticsDTO;

    }


    @Override
    public void addFavoriteBulk(List<UserFavoriteCommand> commands, Long userId) {
        // 参数校验
        if (commands == null || commands.isEmpty()) {
            throw new IllegalArgumentException("批量收藏列表不能为空");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID无效");
        }

        // 2. 手动遍历转换：逐个将Command转为DO
        List<UserFavoriteDO> userFavoriteDOs = new ArrayList<>(commands.size());
        for (UserFavoriteCommand command : commands) {
            UserFavoriteDO favoriteDO = userPrivilegeConverter.userFavoriteCommand2DO(command);
            if (favoriteDO != null) {
                // 设置用户ID和必要的审计字段
                favoriteDO.setUserId(userId);
                userFavoriteDOs.add(favoriteDO);
            }
        }
        // 3. 检查转换后的集合是否为空
        if (userFavoriteDOs.isEmpty()) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }

        // 4. 重复收藏校验
        List<Long> vocIds = userFavoriteDOs.stream()
                .map(UserFavoriteDO::getVocId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!vocIds.isEmpty()) {
            LambdaQueryWrapper<UserFavoriteDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserFavoriteDO::getUserId, userId)
                    .in(UserFavoriteDO::getVocId, vocIds);

            List<UserFavoriteDO> existingFavorites = userFavoriteMapper.selectList(queryWrapper);
            if (!existingFavorites.isEmpty()) {
                Set<Long> existingVocIds = existingFavorites.stream()
                        .map(UserFavoriteDO::getVocId)
                        .collect(Collectors.toSet());

                // 过滤已收藏的记录
                userFavoriteDOs = userFavoriteDOs.stream()
                        .filter(obj -> obj.getVocId() == null || !existingVocIds.contains(obj.getVocId()))
                        .collect(Collectors.toList());


                if (userFavoriteDOs.isEmpty()) {
                    throw new IllegalArgumentException("所选词汇已全部收藏，无需重复操作");

                }
            }
        }


        List<BatchResult> batchResults = userFavoriteMapper.insert(userFavoriteDOs);
        // 简单判断：如果返回结果为空或者插入条数和预期不一致，可抛异常

        if (batchResults == null || batchResults.isEmpty()) {
            throw new BusinessException("批量收藏失败,请稍后重试试", 4005);

        }
    }


    @Override
    public void addFavorite(UserFavoriteCommand command, Long userId) {

        UserFavoriteDO userFavoriteDO = userPrivilegeConverter.userFavoriteCommand2DO(command);
        userFavoriteDO.setUserId(userId);

        //重复收藏检查
        LambdaQueryWrapper<UserFavoriteDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFavoriteDO::getUserId, userId)
                .eq(UserFavoriteDO::getVocId, userFavoriteDO.getVocId());
        UserFavoriteDO aDo = userFavoriteMapper.selectOne(queryWrapper);
        if (aDo == null) {
            userFavoriteMapper.insert(userFavoriteDO);
        } else {
            userFavoriteMapper.update(userFavoriteDO, queryWrapper);
        }
    }

    @Override
    public void cancelFavorite(Long[] ids, Long userId) {

        // 验证用户ID和要取消收藏的ID列表不为空
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("要取消收藏的ID列表不能为空");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        // 检查数组中是否有无效ID
        for (Long id : ids) {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("收藏ID无效: " + id);
            }
        }

        // 构建删除条件：必须同时满足ID在列表中且属于当前用户
        LambdaQueryWrapper<UserFavoriteDO> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(UserFavoriteDO::getVocId, Arrays.asList(ids))
                .eq(UserFavoriteDO::getUserId, userId);

        int affectedRows = userFavoriteMapper.delete(queryWrapper);
        System.out.println("affectedRows = " + affectedRows);

    }

    @Override
    public PageDTO<UserFavoriteDTO> getUserFavoriteList(PageRequest pageRequest) {

        //空指针防护
        if (pageRequest == null) {
            return new PageDTO<>();
        }

        // 处理无分页参数的情况
        if (pageRequest.getPageNum() == null) {
            LambdaQueryWrapper<UserFavoriteDO> queryWrapper = new LambdaQueryWrapper<>();
            // 无分页时也应处理关键词过滤
            if (pageRequest.getKeyword() != null && pageRequest.getKeyword().trim().isEmpty()) {
                queryWrapper.like(UserFavoriteDO::getVocContent, pageRequest.getKeyword().trim());
            }


            List<UserFavoriteDO> userFavoriteDOS = userFavoriteMapper.selectList(queryWrapper);

            return userPrivilegeConverter.userFavoriteDOPage2PageDTO(userFavoriteDOS, (long) userFavoriteDOS.size());
        }
        //需要分页
        // 处理分页情况，添加参数校验
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getCount() != null ? pageRequest.getCount() : 10;

        Page<UserFavoriteDO> pageParam = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserFavoriteDO> userFavoriteWrapper = new LambdaQueryWrapper<>();

        // 处理关键词查询，去除首尾空格
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().trim().isEmpty()) {
            userFavoriteWrapper.like(
                    UserFavoriteDO::getVocContent,
                    pageRequest.getKeyword().trim()
            );
        }
        Page<UserFavoriteDO> userFavoriteDOPage = userFavoriteMapper.selectPage(pageParam, userFavoriteWrapper);
        return userPrivilegeConverter.userFavoriteDOPage2PageDTO(userFavoriteDOPage.getRecords(), userFavoriteDOPage.getTotal());
    }

    @Override
    public void addUserVocSentence(UserVocExampleSentenceCommand command, Long userId) {
        if (command == null) {
            System.out.println("添加用户自建例句失败：命令对象不能为空");
            throw new IllegalArgumentException("用户自建例句命令对象不能为空");
        }
        // 确保命令中包含要更新的例句ID
        if (userId == null || userId <= 0) {
            System.out.println("添加用户自建例句失败：用户ID不能为空或无效");
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }

        // 可以根据实际需求添加更多字段校验
        if (StringUtils.isEmpty(command.getVocContent())) {
            System.out.println("添加用户自建例句失败：例句内容不能为空");
            throw new IllegalArgumentException("例句内容不能为空");
        }


        UserVocExampleSentenceDO userVocExampleSentenceDO =
                userPrivilegeConverter.userVocExampleSentenceCommand2DO(command);

        userVocExampleSentenceDO.setUserId(userId);

        int affectedRows = userVocExampleSentenceMapper.insert(userVocExampleSentenceDO);
        System.out.println("affectedRows = " + affectedRows);
    }

    @Override
    public PageDTO<DisplayUserVocSentenceDTO> getUserVocSentence(PageRequest pageRequest, Long userId) {
        // 1. 参数校验（增强版）
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (pageRequest == null) {
            pageRequest = new PageRequest();
        }
        int pageNum = pageRequest.getPageNum() == null || pageRequest.getPageNum() < 1 ? 1 : pageRequest.getPageNum();
        int pageSize = pageRequest.getCount() == null || pageRequest.getCount() < 1 ? 10 : Math.min(pageRequest.getCount(), 100);
        int start = (pageNum - 1) * pageSize;
        // 2. 构建查询条件
        LambdaQueryWrapper<UserVocExampleSentenceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserVocExampleSentenceDO::getUserId, userId);

        if (org.springframework.util.StringUtils.hasText(pageRequest.getKeyword())) {
            wrapper.like(UserVocExampleSentenceDO::getMeaning, pageRequest.getKeyword()).or()
                    .like(UserVocExampleSentenceDO::getSentence, pageRequest.getKeyword());
        }
        List<Long> allVocIds = userVocExampleSentenceMapper.selectObjs(wrapper.select(UserVocExampleSentenceDO::getVocId))
                .stream()
                .map(obj -> (Long) obj)
                .distinct()
                .collect(Collectors.toList());

        // 4. 基于vocId进行分页
        List<Long> pageVocIds = allVocIds.stream()
                .skip(start)
                .limit(pageSize)
                .collect(Collectors.toList());

        if (pageVocIds.isEmpty()) {
            return new PageDTO<>();
        }
        // 5. 批量查询词汇信息
        Map<Long, VocDO> vocMap = vocMapper.selectByIds(pageVocIds).stream()
                .collect(Collectors.toMap(VocDO::getId, Function.identity()));

        // 6. 查询当前页vocId对应的所有例句
        LambdaQueryWrapper<UserVocExampleSentenceDO> exampleWrapper = new LambdaQueryWrapper<>();
        exampleWrapper.eq(UserVocExampleSentenceDO::getUserId, userId)
                .in(UserVocExampleSentenceDO::getVocId, pageVocIds);
        List<UserVocExampleSentenceDO> examples = userVocExampleSentenceMapper.selectList(exampleWrapper);
        // 7. 按vocId分组
        Map<Long, List<UserVocExampleSentenceDO>> exampleByVocId = examples.stream()
                .collect(Collectors.groupingBy(UserVocExampleSentenceDO::getVocId));
        // 8. 转换为DTO
        List<DisplayUserVocSentenceDTO> resultList = pageVocIds.stream()
                .map(vocId -> {
                    VocDO voc = vocMap.get(vocId);
                    if (voc == null) {
                        return null;
                    }
                    DisplayUserVocSentenceDTO dto = new DisplayUserVocSentenceDTO();
                    dto.setUserId(String.valueOf(userId));
                    dto.setVocId(String.valueOf(vocId));
                    dto.setVocContent(voc.getContent());
                    dto.setSentences(userPrivilegeConverter.userVocSentenceDOs2DTOs(exampleByVocId.getOrDefault(vocId, Collections.emptyList())));
                    return dto;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 9. 返回分页结果
        return userPrivilegeConverter.displayUserVocSentenceDTOList2PageDTO(resultList, (long) allVocIds.size());


    }


    @Override
    public void updateUserVocSentence(UserVocExampleSentenceCommand command, Long userId) {
        if (command == null || userId == null) {
            throw new IllegalArgumentException("用户自建例句和用户ID不能为空");
        }
        // 确保命令中包含要更新的例句ID
        if (command.getId() == null) {
            throw new IllegalArgumentException("要更新的例句的ID不能为空");
        }

        UserVocExampleSentenceDO userVocExampleSentenceDO =
                userPrivilegeConverter.userVocExampleSentenceCommand2DO(command);

        userVocExampleSentenceDO.setUserId(userId);

        UpdateWrapper<UserVocExampleSentenceDO> userVocExampleSentenceDOUpdateWrapper = new UpdateWrapper<>();
        userVocExampleSentenceDOUpdateWrapper.eq("id", command.getId())
                .eq("user_id", userVocExampleSentenceDO.getUserId());

        int update = userVocExampleSentenceMapper.update(userVocExampleSentenceDO, userVocExampleSentenceDOUpdateWrapper);
        System.out.println("update = " + update);
    }

    @Override
    public void deleteUserVocSentence(Long id) {

    }

    @Override
    public void notRemember(Long vocId, Long userId) {
        // 参数校验
        if (vocId == null || vocId <= 0) {
            throw new IllegalArgumentException("词汇ID无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID无效");
        }

        // 先查询是否已存在该记录
        LambdaQueryWrapper<UserNotRememberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNotRememberDO::getUserId, userId)
                .eq(UserNotRememberDO::getVocId, vocId);

        UserNotRememberDO existing = userNotRememberMapper.selectOne(queryWrapper);
        if (existing != null) {
            // 已存在则无需重复插入，可根据业务需求选择更新或直接返回
            return;
        }

        UserNotRememberDO notRememberDO = new UserNotRememberDO();
        notRememberDO.setUserId(userId);
        notRememberDO.setVocId(vocId);
        userNotRememberMapper.insert(notRememberDO);
    }

    @Override
    public void cancelNotRemember(Long vocId, Long userId) {
        // 参数校验
        if (vocId == null || vocId <= 0) {
            throw new IllegalArgumentException("词汇ID无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID无效");
        }
        // 构建精确的删除条件（用户ID+词汇ID）
        LambdaQueryWrapper<UserNotRememberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNotRememberDO::getVocId, vocId)
                .eq(UserNotRememberDO::getUserId, userId);
        int affectedRows = userNotRememberMapper.delete(queryWrapper);
        System.out.println("affectedRows = " + affectedRows);
    }

    @Override
    public List<UserReviewFillWordDTO> getUserReviewFillWordTopics(List<Long> vocIds) {
        return null;
    }

    @Override
    public List<UserReviewSingleChoiceDTO> getVocSingleChoiceTopic(List<Long> vocIds) {
        return null;
    }


    //    APP端获取AI阅读模式以及选项
    @Override
    public List<UserVocAISituationDTO> getAIReviewReadOptions() {

//        构建AI阅读模式和选项
        List<UserVocAISituationDTO> situations = new ArrayList<>();

//        教育模式
        UserVocAISituationDTO educationSituation = new UserVocAISituationDTO();
        educationSituation.setName("教育模式");

        UserVocAISituationOptionDTO userEducationSituation = new UserVocAISituationOptionDTO();
        userEducationSituation.setName("小学");
        userEducationSituation.setName("初中");
        userEducationSituation.setName("高中");

        List<UserVocAISituationOptionDTO> educationOptions = Arrays.asList(userEducationSituation);
        educationSituation.setOptions(educationOptions);
        situations.add(educationSituation);

//        故事模式
        UserVocAISituationDTO storySituation = new UserVocAISituationDTO();
        storySituation.setName("故事模式");

        UserVocAISituationOptionDTO userStorySituation = new UserVocAISituationOptionDTO();
        userStorySituation.setName("通话风格");
        userStorySituation.setName("科幻风格");
        userStorySituation.setName("历史风格");

        List<UserVocAISituationOptionDTO> storyOptions = Arrays.asList(userStorySituation);
        storySituation.setOptions(storyOptions);
        situations.add(storySituation);

//        对话模式
        UserVocAISituationDTO dialogueSituation = new UserVocAISituationDTO();
        dialogueSituation.setName("故事模式");

        UserVocAISituationOptionDTO userDialogueSituation = new UserVocAISituationOptionDTO();
        userDialogueSituation.setName("通话风格");
        userDialogueSituation.setName("科幻风格");
        userDialogueSituation.setName("历史风格");

        List<UserVocAISituationOptionDTO> dialogueOptions = Arrays.asList(userDialogueSituation);
        dialogueSituation.setOptions(dialogueOptions);
        situations.add(dialogueSituation);

        return situations;
    }


    //    APP端获取用户剩余免费AI阅读次数
    @Override
    public Integer getUserVocAIReadRemainFreeCount(Long userId) {
        //根据用户ID查询用户AI阅读使用统计信息
        LambdaQueryWrapper<UserPrivilegeUseStatisticsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPrivilegeUseStatisticsDO::getUserId, userId)
                .eq(UserPrivilegeUseStatisticsDO::getPrivilegeCode, "review:ai:read");
        UserPrivilegeUseStatisticsDO useStatistics = userPrivilegeHistoryMapper.selectOne(queryWrapper);

//        若第一次使用，则插入新的使用统计记录
        if (useStatistics == null) {
            useStatistics = new UserPrivilegeUseStatisticsDO();
            useStatistics.setUserId(userId);
            useStatistics.setPrivilegeCode("review:ai:read");
            useStatistics.setUseCount(0);
            userPrivilegeHistoryMapper.insert(useStatistics);
            return FREE_USE_COUNT;
        }

//        计算剩余免费使用次数
        int remainCount = FREE_USE_COUNT - useStatistics.getUseCount();

        return Math.max(remainCount, 0);
    }

    @Autowired
    ChatClient chatClient;
    //    APP端获取AI阅读结果
    @Override
    @Transactional
    public List<AIReviewReadDTO> getAIReviewReadContent(AIReviewReadCommand command, Long userId) {
        //获取用户个人信息
        QueryWrapper<UserVocBoundStatisticsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserVocBoundStatisticsDO userVocBoundStatistics = userVocBoundStatisticsMapper.selectOne(queryWrapper);

        //没有找到这个用户信息，则直接抛出用户不存在的异常
        if (userVocBoundStatistics == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }

//        //根据用户ID查询用户AI阅读使用统计信息
//        LambdaQueryWrapper<UserPrivilegeUseStatisticsDO> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(UserPrivilegeUseStatisticsDO::getUserId, userId)
//                .eq(UserPrivilegeUseStatisticsDO::getPrivilegeCode, "review:ai:read");
//        UserPrivilegeUseStatisticsDO useStatistics = userPrivilegeHistoryMapper.selectOne(queryWrapper);
//        //获取用户已经使用的次数
//        int useCount = useStatistics.getUseCount();

        //获取用户剩余免费使用次数和剩余可用的单词个数
        Integer remainFreeCount = getUserVocAIReadRemainFreeCount(userId);
        Integer availableVocNumber = userVocBoundStatistics.getAvailable();
        //先扣除，再使用
        //先检查免费次数是否大于0
        if (remainFreeCount > 0) {
            //免费次数大于0的情况,记录使用次数+1
            LambdaUpdateWrapper<UserPrivilegeUseStatisticsDO> useCountLambdaUpdateWrapper
                    =new LambdaUpdateWrapper<>();
            useCountLambdaUpdateWrapper.eq(UserPrivilegeUseStatisticsDO::getUserId,userId)
                    .setSql("use_count = use_count + 1");
            userPrivilegeHistoryMapper.update(null,useCountLambdaUpdateWrapper);
            //免费次数不足,则判断单词上限是否>=10个
        }else if(availableVocNumber >= 10){
            availableVocNumber-=10;
            //获取抵扣消耗的单词上限数量
            Integer occupiedVocNumber = userVocBoundStatistics.getOccupied();
            //进入到这里,说明单词上限>=10个,则扣除单词上限并且记录日志
            LambdaUpdateWrapper<UserVocBoundStatisticsDO> userVocBoundStatisticsDOLambdaUpdateWrapper
                    =new LambdaUpdateWrapper<>();
            userVocBoundStatisticsDOLambdaUpdateWrapper.eq(UserVocBoundStatisticsDO::getUserId,userId)
                    //可用单词数量-10
                    .set(UserVocBoundStatisticsDO::getAvailable,availableVocNumber)
                    //抵扣消耗的单词数量+10
                    .set(UserVocBoundStatisticsDO::getOccupied,occupiedVocNumber);
            userVocBoundStatisticsMapper.update(userVocBoundStatisticsDOLambdaUpdateWrapper);

            //记录日志
            UserVocBoundLogDO log = UserVocBoundLogDO.builder()
                    .userId(userId)
                    .type(UserVocBoundEnum.USER_VOC_BOUND_LOG_TYPE_CONSUME.getValue())
                    .logDate(LocalDate.now())
                    .count(10)
                    .description("抵扣AI阅读使用次数")
                    .build();
            userVocBoundLogMapper.insert(log);
        }else{
            //单词上限也不足10个,直接抛出单词上限不足的异常
            throw new BusinessException(ResultCodeEnum.USER_VOC_BOUND_NOT_ENOUGH);
        }

        // 构造用户提示词
        HashMap<String, Object> paramMap = new HashMap<>();
        // 设置场景
        paramMap.put("situation", command.getSituationName());
        // 设置场景中的选项
        paramMap.put("option", command.getOptionName());
        // 设置单词
        paramMap.put("words", command.getVocList().toString());
        // 设置模式
        if ("教育模式".equals(command.getSituationName())) {
            paramMap.put("type", "短文");
        } else if ("对话模式".equals(command.getSituationName())) {
            paramMap.put("type", "对话");
        } else {
            paramMap.put("type", "故事");
        }

        //创建系统提示词模板和用户提示词模板
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);
        PromptTemplate userPrompt = new PromptTemplate(userResource, paramMap);

        //创建系统提示词信息和用户提示词信息
        Message systemMessage = systemPromptTemplate.createMessage();
        Message userMessage = userPrompt.createMessage();

        //基于用户提示词信息和系统提示词信息，构造完整的提示词
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        //调用ChatClient对象的方法发起请求
        List<AIReviewReadDTO> result = chatClient.prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<AIReviewReadDTO>>() {
                });

        return result;
    }
}


