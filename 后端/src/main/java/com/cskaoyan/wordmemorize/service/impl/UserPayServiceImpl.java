package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.PayConverter;
import com.cskaoyan.wordmemorize.dao.entity.UserDO;
import com.cskaoyan.wordmemorize.dao.entity.UserVocBoundLogDO;
import com.cskaoyan.wordmemorize.dao.entity.UserVocBoundStatisticsDO;
import com.cskaoyan.wordmemorize.dao.entity.VocPaySaleDO;
import com.cskaoyan.wordmemorize.dao.mapper.UserMapper;
import com.cskaoyan.wordmemorize.dao.mapper.UserVocBoundLogMapper;
import com.cskaoyan.wordmemorize.dao.mapper.UserVocBoundStatisticsMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocPaySaleMapper;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.PayOrderDTO;
import com.cskaoyan.wordmemorize.dto.app.VocPaySaleDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.PaySuccessNotifyCommand;
import com.cskaoyan.wordmemorize.request.VocPaySaleCommand;
import com.cskaoyan.wordmemorize.service.UserPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserPayServiceImpl implements UserPayService {

    @Autowired
    PayConverter payConverter;
    @Autowired
    VocPaySaleMapper vocPaySaleMapper;

    @Override
    public PayOrderDTO pay(VocPaySaleCommand command, Long userId) {
        //先判断是不是补签卡，如果是的话，再去user表里面看看有没有，有的话直接抛出异常
        if (command.getItemType()==3) {
            LambdaQueryWrapper<UserDO> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(UserDO::getId, userId);
            UserDO userDO = userMapper.selectOne(userWrapper);
            if (userDO.getHasSignCard()==1) {
                throw new BusinessException(ResultCodeEnum.USER_HAS_REPLACEMENT_CARD);
            }
        }
        //插入 数据到 voc_pay_sale表内，status默认是初始化状态
        VocPaySaleDO vocPaySaleDO = payConverter.vocPaySaleCommand2DO(command);
        vocPaySaleDO.setUserId(userId);
        vocPaySaleDO.setStatus(1);
        vocPaySaleMapper.insert(vocPaySaleDO);
        //获取支付订单的id
        Long paySaleId = vocPaySaleDO.getId();
        //获取订单itemName
        String itemName = vocPaySaleDO.getItemName();
        //获取订单payPrice
        BigDecimal payPrice = vocPaySaleDO.getPayPrice();
        //构建PayOrderDTO对象
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setId(paySaleId.toString());
        payOrderDTO.setSubject(itemName);
        payOrderDTO.setTotalAmount(payPrice);
        return payOrderDTO;
    }

    @Override
    public String paySuccess(PaySuccessNotifyCommand command, Long userId) {
        //支付id
        Long id = command.getId();
        VocPaySaleDO vocPaySaleDO = vocPaySaleMapper.selectById(id);
        if ("TRADE_SUCCESS".equals(command.getTradeStatus())) {
            vocPaySaleDO.setStatus(2);
            vocPaySaleMapper.updateById(vocPaySaleDO);
        }
        if (vocPaySaleDO.getStatus() == 2) {
            //根据vocPaySaleDO的itemType判断1.  单词上限 2. 等级解锁 3.补签卡
            //若交易类型为单词上限，则修改voc_bound_statistics(增加总单词上限，增加购买单词上限，以及增加可用单词上限数量)，
            // 并且向voc_bound_log中新增一条单词上限变更记录
            if (vocPaySaleDO.getItemType() == 1) {
                //根据vocPaySaleDO的payPrice判断加多少个上限：
                // 8元500个单词上限
                // 12元800个单词上限
                // 25元1800个单词
                //50元4000个单词
                BigDecimal bound = vocPaySaleDO.getPayPrice();
                LambdaQueryWrapper<UserVocBoundStatisticsDO> userVocBoundStatisticsWrapper = new LambdaQueryWrapper<>();
                userVocBoundStatisticsWrapper.eq(UserVocBoundStatisticsDO::getUserId, userId);
                UserVocBoundStatisticsDO userVocBoundStatisticsDO = userVocBoundStatisticsMapper.selectOne(userVocBoundStatisticsWrapper);
                //购买增加单词的数量
                int count = 0;
                if (bound.compareTo(new BigDecimal(8)) == 0) {
                    count = 500;
                    userVocBoundStatisticsDO.setAvailable(userVocBoundStatisticsDO.getAvailable() + count);
                    userVocBoundStatisticsDO.setTotal(userVocBoundStatisticsDO.getTotal() + count);
                    userVocBoundStatisticsDO.setPay(userVocBoundStatisticsDO.getPay() + count);
                } else if (bound.compareTo(new BigDecimal(12)) == 0) {
                    count = 800;
                    userVocBoundStatisticsDO.setAvailable(userVocBoundStatisticsDO.getAvailable() + count);
                    userVocBoundStatisticsDO.setTotal(userVocBoundStatisticsDO.getTotal() + count);
                    userVocBoundStatisticsDO.setPay(userVocBoundStatisticsDO.getPay() + count);
                } else if (bound.compareTo(new BigDecimal(25)) == 0) {
                    count = 1800;
                    userVocBoundStatisticsDO.setAvailable(userVocBoundStatisticsDO.getAvailable() + count);
                    userVocBoundStatisticsDO.setTotal(userVocBoundStatisticsDO.getTotal() + count);
                    userVocBoundStatisticsDO.setPay(userVocBoundStatisticsDO.getPay() + count);
                } else if (bound.compareTo(new BigDecimal(50)) == 0) {
                    count = 4000;
                    userVocBoundStatisticsDO.setAvailable(userVocBoundStatisticsDO.getAvailable() + count);
                    userVocBoundStatisticsDO.setTotal(userVocBoundStatisticsDO.getTotal() + count);
                    userVocBoundStatisticsDO.setPay(userVocBoundStatisticsDO.getPay() + count);
                }
                userVocBoundStatisticsMapper.update(userVocBoundStatisticsDO, userVocBoundStatisticsWrapper);
                //增加一条单词上限变化记录(user_voc_bound_log)
                UserVocBoundLogDO userVocBoundLogDO = new UserVocBoundLogDO().builder()
                        .userId(userId)
                        .type(CommonConstant.USER_VOC_BOUND_CHANGE_TYPE_PAY)
                        .description("购买单词上限")
                        .count(count)
                        .logDate(LocalDate.now())
                        .build();
                userVocBoundLogMapper.insert(userVocBoundLogDO);

            }
            //若交易类型为等级解锁，则修改用户的等级(等级名称\等级数值)，以及升级时间(修改user表)
            if (vocPaySaleDO.getItemType() == 2) {
                String itemName = vocPaySaleDO.getItemName();
                LambdaQueryWrapper<UserDO> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.eq(UserDO::getId, userId);
                UserDO userDO = userMapper.selectOne(userWrapper);

                // 动态提取等级数值和名称
                Pattern pattern = Pattern.compile("LVL(\\d{1,2})"); // 匹配LVL后跟1-2位数字
                Matcher matcher = pattern.matcher(itemName);

                if (matcher.find()) {
                    String levelNum = matcher.group(1); // 提取数字部分
                    userDO.setLevelName("LVL" + levelNum);
                    userDO.setLevelValue(Integer.parseInt(levelNum));
                    userDO.setLevelDate(LocalDate.now());
                    userMapper.update(userDO, userWrapper);
                }
            }
            //若交易类型为补签卡，则修user表，设置用户位有补签卡的状态，注意对于补签卡，
            if (vocPaySaleDO.getItemType() == 3) {
                // 在购买之前需要先判断，如果已经有补签卡，一个用户不允许购买多张补签卡
                LambdaQueryWrapper<UserDO> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.eq(UserDO::getId, userId);
                UserDO userDO = userMapper.selectOne(userWrapper);
                if (userDO.getHasSignCard() == 0) {
                    userDO.setHasSignCard(1);
                    userMapper.update(userDO, userWrapper);
                }
            }
            return "paySuccess";
        }
        return "payFail";
    }

    @Autowired
    UserVocBoundStatisticsMapper userVocBoundStatisticsMapper;

    @Autowired
    UserVocBoundLogMapper userVocBoundLogMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Integer queryTradeStatus(Long paySaleId) {
        VocPaySaleDO vocPaySaleDO = vocPaySaleMapper.selectById(paySaleId);
        return vocPaySaleDO.getStatus();

    }

    @Override
    public PageDTO<VocPaySaleDTO> getPaySaleHistory(Long userId, PageRequest pageRequest) {
        if (pageRequest.getPageNum() == null) {
            pageRequest.setPageNum(1);
            pageRequest.setCount(10);
        }
        //需要分页
        Page<VocPaySaleDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
        LambdaQueryWrapper<VocPaySaleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VocPaySaleDO::getUserId, userId);
        Page<VocPaySaleDO> pageResult = vocPaySaleMapper.selectPage(page, queryWrapper);
        return payConverter.vocPaySalePage2PageDTO(pageResult.getRecords(), pageResult.getTotal());
    }
}
