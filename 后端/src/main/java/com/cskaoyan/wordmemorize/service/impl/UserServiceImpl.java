package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.UserConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserRemindCommand;
import com.cskaoyan.wordmemorize.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    UserRemindMapper userRemindMapper;

    @Resource
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;
    @Resource
    UserVocBoundLogMapper userVocBoundLogMapper;

    @Resource
    UserConverter userConverter;

    @Resource
    GoodsMapper goodsMapper;

    @Value("${user.remind.description}")
    String messageRemindDescription;

    //获取用户信息
    @Override
    public UserDTO getUserInfo(Long userId) {
        // 查询基本信息
        UserDO userDO = userMapper.getUserInfo(userId);
//        System.out.println("userDO = " + userDO);
        // 查询等级对应的特权码
        UserDTO userDTO = userConverter.userDO2DTO(userDO);
//        System.out.println("userDTO = " + userDTO);
        return userDTO;
    }


    @Autowired
    LevelMapper levelMapper;
    @Autowired
    UserCheckinStatisticsMapper userCheckinStatisticsMapper;

    //返回用户的升级信息
    @Override
    public NextLevelDTO getUserNextDTO(Long userId) {
        // 查询用户
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.ILLEGAL_REQUEST); // 用户不存在
        }

        int currentLevel = user.getLevelValue() != null ? user.getLevelValue() : 0;

        // 查询下一等级
//        LevelDO nextLevel = levelMapper.selectOne(
//                new LambdaQueryWrapper<LevelDO>()
//                        .eq(LevelDO::getLevelValue, currentLevel + 1)
//                        .eq(LevelDO::getIsDeleted, 0)
//                        .last("LIMIT 1")
//        );
        LevelDO nextLevel = levelMapper.selectNextLevel(currentLevel + 1);
        if (nextLevel == null) {
            return null; // 已是最高等级
        }

        Integer requiredDays = nextLevel.getCheckInDays();         // 升级需要的天数
        Integer signType = nextLevel.getCheckInType();             // 签到类型：1连续 2累计

        // 查询用户签到统计信息
        UserCheckinStatisticsDO stats = userCheckinStatisticsMapper.selectOne(
                new LambdaQueryWrapper<UserCheckinStatisticsDO>()
                        .eq(UserCheckinStatisticsDO::getUserId, userId)
                        .eq(UserCheckinStatisticsDO::getIsDeleted, 0)
                        .last("LIMIT 1")
        );

        int actualDays = 0;
        if (stats != null) {
            if (signType == 1) {
                actualDays = stats.getCurContinuousDays() != null ? stats.getCurContinuousDays() : 0;
            } else {
                actualDays = stats.getTotalDays() != null ? stats.getTotalDays() : 0;
            }
        }

        int remainDays = Math.max(0, requiredDays - actualDays);

        NextLevelDTO dto = new NextLevelDTO();
        dto.setSignType(signType);
        dto.setRemainDays(remainDays);
        return dto;
    }

    @Autowired
    LevelPrivilegeMapper levelPrivilegeMapper;

    //返回用户的等级信息
    @Override
    public DisplayUserLevelDTO getUserLevelInfo(Long userId) {
        // 1. 验证用户
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        Integer userLevelValue = user.getLevelValue();

        // 2. 查询等级及关联商品ID（返回Map列表）
        List<Map<String, Object>> levelMaps = levelMapper.selectLevelsWithGoods();
        if (CollectionUtils.isEmpty(levelMaps)) {
            return new DisplayUserLevelDTO();
        }

        // 3. 批量查询特权信息（优化为单次查询）
        List<Long> levelIds = levelMaps.stream()
                .map(m -> (Long) m.get("id"))
                .collect(Collectors.toList());
        Map<Long, List<LevelPrivilegeDO>> privilegeMap = levelPrivilegeMapper.selectList(
                new LambdaQueryWrapper<LevelPrivilegeDO>()
                        .eq(LevelPrivilegeDO::getIsDeleted, 0)
                        .in(LevelPrivilegeDO::getLevelId, levelIds)
        ).stream().collect(Collectors.groupingBy(LevelPrivilegeDO::getLevelId));

        // 4. 构建响应数据
        DisplayUserLevelDTO result = new DisplayUserLevelDTO();
        List<LevelPageDTO> nowLevelList = new ArrayList<>();
        List<LevelPageDTO> upperLevelList = new ArrayList<>();

        for (Map<String, Object> levelMap : levelMaps) {
            LevelPageDTO dto = new LevelPageDTO();

            // 基础字段拷贝（需手动处理类型转换）
            dto.setId(String.valueOf(levelMap.get("id")));
            dto.setLevelName((String) levelMap.get("level_name"));
            dto.setLevelValue((Integer) levelMap.get("level_value"));
            dto.setCheckInDays((Integer) levelMap.get("check_in_days"));
            dto.setCheckInType((Integer) levelMap.get("check_in_type"));
            dto.setUnlockPrice((BigDecimal) levelMap.get("unlock_price"));
            dto.setDescription((String) levelMap.get("description"));

            // 关键修改：从JOIN结果获取商品ID
            dto.setGoodsId(levelMap.get("relation_goods_id") != null ?
                    levelMap.get("relation_goods_id").toString() : "0");

            // 转换特权信息
            Long levelId = (Long) levelMap.get("id");
            List<LevelPrivilegeDTO> privilegeDTOs = privilegeMap
                    .getOrDefault(levelId, Collections.emptyList())
                    .stream()
                    .map(priv -> {
                        LevelPrivilegeDTO pdto = new LevelPrivilegeDTO();
                        BeanUtils.copyProperties(priv, pdto);
                        pdto.setId(priv.getId().toString());
                        return pdto;
                    })
                    .collect(Collectors.toList());
            dto.setLevelPrivileges(privilegeDTOs);

            // 分类等级
            if ((Integer) levelMap.get("level_value") <= userLevelValue) {
                nowLevelList.add(dto);
            } else {
                upperLevelList.add(dto);
            }
        }

        result.setNowLevel(nowLevelList);
        result.setUpperLevel(upperLevelList);
        return result;
    }


    //修改每天复习单词数量
    @Override
    public void updateVocCountOfDay(Long userId, Integer count) {
        UpdateWrapper<UserDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        updateWrapper.set("voc_count_of_day", count);
        userMapper.update(updateWrapper);
    }

    //修改每天复习提醒时间
    @Override
    public void updateUserRemind(UserRemindCommand command) {
        UpdateWrapper<UserRemindDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", command.getUserId());
        updateWrapper.set("remind_time", command.getRemindTime());
        userRemindMapper.update(updateWrapper);
    }


    @Override
    public UserRemindDTO getUserRemind(Long userId) {
        return null;
    }


    @Override
    public void messageRemind() {


    }

    //返回用户的单词上限变化明细
    @Override
    public PageDTO<DisplayUserVocBoundLogDTO> getUserVocBoundLog(PageRequest pageRequest, Long userId) {
        //添加默认值，防止传入null
        int pageNum = pageRequest.getPageNum() != null ? pageRequest.getPageNum() : 1;
        int count = pageRequest.getCount() != null ? pageRequest.getCount() : 10;

        Page<UserVocBoundLogDO> page = new Page<>(pageNum, count);

        LambdaQueryWrapper<UserVocBoundLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserVocBoundLogDO::getUserId, userId)
                .orderByDesc(UserVocBoundLogDO::getLogDate);

        Page<UserVocBoundLogDO> resultPage = userVocBoundLogMapper.selectPage(page, wrapper);

        List<DisplayUserVocBoundLogDTO> dtoList = resultPage.getRecords().stream().map(log -> {
            DisplayUserVocBoundLogDTO dto = new DisplayUserVocBoundLogDTO();
            dto.setId(String.valueOf(log.getId()));
            dto.setUserId(String.valueOf(log.getUserId()));
            dto.setType(log.getType());
            dto.setLogDate(log.getLogDate());
            dto.setExchangeCount(log.getCount());
            dto.setDescription(log.getDescription());
            return dto;
        }).collect(Collectors.toList());

        PageDTO<DisplayUserVocBoundLogDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageList(dtoList);
        pageDTO.setTotalCount(resultPage.getTotal());

        return pageDTO;
    }


    //返回用户的单词上限统计数据
    @Override
    public UserVocBoundStatisticsDTO getUserVocBoundStatistics(Long userId) {
        //在user_voc_bound_static表中查询其他信息
        QueryWrapper<UserVocBoundStatisticsDO> userVocBoundStatisticsDOQueryWrapper = new QueryWrapper<>();
        userVocBoundStatisticsDOQueryWrapper.eq("user_id", userId);
        UserVocBoundStatisticsDO userVocBoundStatisticsDO
                = userVocBoundStatisticsMapper.selectOne(userVocBoundStatisticsDOQueryWrapper);
        return userConverter.userVocBoundStatisticsDO2DisplayDTO(userVocBoundStatisticsDO);
    }

    //返回用户页面展示条目的信息
    @Override
    public UserPageDTO getUserPage(Long userId) {
        UserPageDTO userPageDTO = new UserPageDTO();
        //在user表中查询补签卡的数量
        UserDO userDO = userMapper.selectById(userId);

        userPageDTO.setReSignCard(userDO.getHasSignCard());
        userPageDTO.setVocCountOfDay(userDO.getVocCountOfDay());
//        System.out.println("userPageDTO_1 = " + userPageDTO);

        //在user_remind表中查询提醒时间
        QueryWrapper<UserRemindDO> userRemindQueryWrapper = new QueryWrapper<>();
        userRemindQueryWrapper.eq("user_id", userId);
        UserRemindDO userRemindDO = userRemindMapper.selectOne(userRemindQueryWrapper);
        //转换格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        userPageDTO.setRemindTime(userRemindDO.getRemindTime().format(formatter));
//        System.out.println("userPageDTO_2 = " + userPageDTO);

        //在user_voc_bound_static表中查询其他信息
        QueryWrapper<UserVocBoundStatisticsDO> userVocBoundStatisticsDOQueryWrapper = new QueryWrapper<>();
        userVocBoundStatisticsDOQueryWrapper.eq("user_id", userId);
        UserVocBoundStatisticsDO userVocBoundStatisticsDO
                = userVocBoundStatisticsMapper.selectOne(userVocBoundStatisticsDOQueryWrapper);

        userPageDTO.setTotalVocBound(userVocBoundStatisticsDO.getTotal());
        userPageDTO.setFreeVocBound(userVocBoundStatisticsDO.getFree());
        userPageDTO.setPayVocBound(userVocBoundStatisticsDO.getPay());
//        System.out.println("userPageDTO_3 = " + userPageDTO);

        return userPageDTO;
    }

    @Override
    public Set<String> getPrivilegeCodesList(Long userId) {
        Set<String> privilegeCodesList = userMapper.getPrivilegeCodesList(userId);
        return privilegeCodesList;
    }

}
