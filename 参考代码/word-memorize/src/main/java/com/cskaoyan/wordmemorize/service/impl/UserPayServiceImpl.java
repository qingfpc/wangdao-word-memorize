package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.PayConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.*;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.PayOrderDTO;
import com.cskaoyan.wordmemorize.dto.app.VocPaySaleDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.PaySuccessNotifyCommand;
import com.cskaoyan.wordmemorize.request.VocPaySaleCommand;
import com.cskaoyan.wordmemorize.service.UserPayService;
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserPayServiceImpl implements UserPayService {

    @Resource
    PayConverter payConverter;

    @Resource
    VocPaySaleMapper vocPaySaleMapper;

    @Resource
    UserVocBoundLogMapper userVocBoundLogMapper;

    @Resource
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;

    @Resource
    GoodsMapper goodsMapper;

    @Resource
    LevelMapper levelMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    LevelPrivilegeMapper levelPrivilegeMapper;



    @Override
    public PayOrderDTO pay(VocPaySaleCommand command, Long userId) {

        if (command.getIsSignCard()) {
            UserDO userDO = userMapper.selectById(userId);
            if (userDO.getHasSignCard() == 1) {
                // 已经有补签卡
                throw new BusinessException(ResultCodeEnum.USER_ALREADY_OWN_RESIGN_CARD);
            }

            LambdaQueryWrapper<VocPaySaleDO> vocPaySaleWrapper = new LambdaQueryWrapper<>();
            vocPaySaleWrapper.eq(VocPaySaleDO::getUserId, userId);
            vocPaySaleWrapper.eq(VocPaySaleDO::getItemType, CommonConstant.PAY_TYPE_USER_RESIGN_CARD);
            vocPaySaleWrapper.eq(VocPaySaleDO::getStatus, CommonConstant.NOT_PAY_SUCCESS);
            Long count = vocPaySaleMapper.selectCount(vocPaySaleWrapper);
            if (count > 0) {
                throw new BusinessException(ResultCodeEnum.USER_ALREADY_OWN_RESIGN_CARD);
            }
        }
        command.setUserId(userId.toString());
        command.setStatus(CommonConstant.NOT_PAY_SUCCESS);

        // 校验价格
        String itemId = command.getItemId();
        GoodsDO goodsDO = goodsMapper.selectById(Long.parseLong(itemId));
        if (goodsDO == null) {
            throw new BusinessException(ResultCodeEnum.GOODS_NOT_EXIST);
        }

        if (goodsDO.getPrice().compareTo(command.getPayPrice()) != 0) {
            throw new BusinessException(ResultCodeEnum.PAY_PRICE_ERROR);
        }

        if (command.getItemType() == CommonConstant.PAY_TYPE_USER_LEVEL) {
            UserDO userDO = userMapper.selectById(userId);
            if (userDO.getLevelValue() >= goodsDO.getValue()) {
                throw new BusinessException(ResultCodeEnum.LEVEL_UPGRADED);
            }
        }

        VocPaySaleDO vocPaySaleDO = payConverter.vocPaySaleCommand2DO(command);
        vocPaySaleMapper.insert(vocPaySaleDO);
        return payConverter.vocPaySaleDO2PayOrderDTO(vocPaySaleDO);
    }

    @Override
    public String paySuccess(PaySuccessNotifyCommand command, Long userId) {

        VocPaySaleDO toUpdate = new VocPaySaleDO();
        toUpdate.setId(command.getId());
        toUpdate.setStatus(CommonConstant.PAY_SUCCESS);
        vocPaySaleMapper.updateById(toUpdate);
        // 查询交易信息
        VocPaySaleDO vocPaySaleDO = vocPaySaleMapper.selectById(command.getId());
        Integer itemType = vocPaySaleDO.getItemType();
        if (itemType == CommonConstant.PAY_TYPE_USER_VOC_BOUND) {
            GoodsDO goodsDO = goodsMapper.selectById(vocPaySaleDO.getItemId());

            // 添加单词上限变更记录
            UserVocBoundLogDO userVocBoundLogDO = new UserVocBoundLogDO();
            userVocBoundLogDO.setType(CommonConstant.USER_VOC_BOUND_CHANGE_TYPE_PAY);
            userVocBoundLogDO.setUserId(userId);
            userVocBoundLogDO.setCount(goodsDO.getValue());
            userVocBoundLogDO.setPaySaleId(command.getId());
            userVocBoundLogDO.setDescription(CommonConstant.USER_VOC_BOUND_DESCRIPTION_CHECKIN);
            userVocBoundLogDO.setLogDate(LocalDate.now());
            userVocBoundLogMapper.insert(userVocBoundLogDO);

            // 更新单词上限数据
            userVocBoundStatisticsMapper.payUserVocBound(goodsDO.getValue(), userId);

        } else if (itemType == CommonConstant.PAY_TYPE_USER_LEVEL) {
            GoodsDO goodsDO = goodsMapper.selectById(vocPaySaleDO.getItemId());
            LambdaQueryWrapper<LevelDO> levelWrapper = new LambdaQueryWrapper<>();
            levelWrapper.eq(LevelDO::getLevelValue, goodsDO.getValue());
            LevelDO levelDO = levelMapper.selectOne(levelWrapper);
            UserDO userDO = new UserDO();
            userDO.setId(userId);
            userDO.setLevelName(levelDO.getLevelName());
            userDO.setLevelValue(levelDO.getLevelValue());
            userDO.setLevelDate(LocalDate.now());

            // 更新等级
            userMapper.updateById(userDO);

            // 这里无需更新签到信息，因为签到升级，会从上一级之后的签到开始计算

        } else if (itemType == CommonConstant.PAY_TYPE_USER_RESIGN_CARD) {
            UserDO userDO = new UserDO();
            userDO.setId(userId);
            userDO.setHasSignCard(1);
            userMapper.updateById(userDO);
        }

        return "success";
    }

    @Override
    public Integer queryTradeStatus(Long paySaleId) {
        VocPaySaleDO vocPaySaleDO = vocPaySaleMapper.selectById(paySaleId);
        return vocPaySaleDO.getStatus();
    }

    @Override
    public PageDTO<VocPaySaleDTO> getPaySaleHistory(Long userId, PageRequest pageRequest) {
        LambdaQueryWrapper<VocPaySaleDO> vocPaySaleWrapper = new LambdaQueryWrapper<>();
        vocPaySaleWrapper.eq(VocPaySaleDO::getUserId, userId)
                .eq(VocPaySaleDO::getStatus, CommonConstant.PAY_SUCCESS);
        if (pageRequest.getPageNum() == null) {
            List<VocPaySaleDO> vocPaySaleDOS = vocPaySaleMapper.selectList(vocPaySaleWrapper);
            return payConverter.vocPaySalePage2PageDTO(vocPaySaleDOS, (long)vocPaySaleDOS.size());
        }

        Page<VocPaySaleDO> pageParam = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
        Page<VocPaySaleDO> vocPaySaleDOPage = vocPaySaleMapper.selectPage(pageParam, vocPaySaleWrapper);

        return payConverter.vocPaySalePage2PageDTO(vocPaySaleDOPage.getRecords(), vocPaySaleDOPage.getTotal());
    }
}
