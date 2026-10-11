package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.app.PayOrderDTO;
import com.cskaoyan.wordmemorize.dto.app.VocPaySaleDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.PaySuccessNotifyCommand;
import com.cskaoyan.wordmemorize.request.VocPaySaleCommand;
import com.cskaoyan.wordmemorize.service.UserPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/pay")
public class UserPayController {

    @Autowired
    UserPayService  userPayService;
    @PostMapping("")
    public Result<PayOrderDTO> pay(@RequestBody VocPaySaleCommand  command) {
        long userId = StpKit.USER.getLoginIdAsLong();
        PayOrderDTO payOrderDTO = userPayService.pay(command, userId);
        return Result.ok(payOrderDTO);
    }
    @PostMapping("notice")
    public String payNotice(@RequestBody PaySuccessNotifyCommand  command) {
        long userId = command.getUserId();
        String paySuccess = userPayService.paySuccess(command, userId);
        return paySuccess;
    }
    @GetMapping("{paySaleId}")
    public Result<Integer> getPayResult(@PathVariable("paySaleId") Long paySaleId)
    {
        Integer status = userPayService.queryTradeStatus(paySaleId);
        return Result.ok(status);
    }
    @GetMapping("list")
    public Result<PageDTO<VocPaySaleDTO>> getPaySaleHistory(PageRequest pageRequest)
    {
        long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<VocPaySaleDTO> paySaleHistory = userPayService.getPaySaleHistory(userId, pageRequest);
        return Result.ok(paySaleHistory);
    }
}