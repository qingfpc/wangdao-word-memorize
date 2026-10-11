package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.PayOrderDTO;
import com.cskaoyan.wordmemorize.dto.app.VocPaySaleDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.PaySuccessNotifyCommand;
import com.cskaoyan.wordmemorize.request.VocPaySaleCommand;

public interface UserPayService {

    // APP支付接口(解锁等级，购买代词上限, 购买补签卡)
    PayOrderDTO pay(VocPaySaleCommand command, Long userId);
    // APP支付成功接口，我需要我们自己通过postman或者Api Fox模拟支付成功的通知(解锁等级，购买代词上限, 购买补签卡)
    String paySuccess(PaySuccessNotifyCommand command, Long userId);
    // APP获取支付结果
    Integer queryTradeStatus(Long paySaleId);
    // APP获取购买历史
    PageDTO<VocPaySaleDTO> getPaySaleHistory(Long userId, PageRequest pageRequest);
}
