package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.PayOrderDTO;
import com.cskaoyan.wordmemorize.dto.app.VocPaySaleDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.PaySuccessNotifyCommand;
import com.cskaoyan.wordmemorize.request.VocPaySaleCommand;
import com.cskaoyan.wordmemorize.service.UserPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.shiro.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/*
 *  考虑商品全部现金支付，特权全部单词上限抵扣
 */
@Tag(name = "移动端-支付模块")
@RestController
@RequestMapping("user/pay")
public class PayController {

    @Resource
    UserPayService userPayService;


    @Operation(summary = "***用户用于支付",
            description = "用于用户购买支付行为",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    ),
                    @ApiResponse(responseCode = "511",
                            description = "用户已经有补签卡，不能重复购买"
                    )
            }
    )
    @PostMapping()
    public Result<PayOrderDTO> paySale(@RequestBody VocPaySaleCommand command) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PayOrderDTO payOrderDTO = userPayService.pay(command, userId);
        return Result.ok(payOrderDTO);
    }


    @Operation(summary = "**用户用于接收支付成功的异步通知",
            description = "用户支付成功后会接受到该通知(与移动端无关)"
    )
    @PostMapping("notice")
    public String payNotify(@Parameter(name = "") @RequestBody PaySuccessNotifyCommand command) {
        try {
            return userPayService.paySuccess(command, command.getUserId());
        } catch (Exception e) {
            return "fail";
        }
    }


    @Operation(summary = "*用户用于获取支付结果",
            description = "在用户支付后，前端会轮询该接口，直到支付成功，在进行下一步, status为2表示支付成功，1表示尚未支付",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("/{paySaleId}")
    public Result<Integer> getPayStatus(@PathVariable Long paySaleId) {
        Integer status = userPayService.queryTradeStatus(paySaleId);
        return Result.ok(status);
    }

    @Operation(summary = "**用户用于查看购买历史",
            description = "当用户点击购买历史时，可以看到成功购买的商品，特权等列表",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("list")
    public Result<PageDTO<VocPaySaleDTO>> paySuccessHistory(PageRequest pageRequest) {
        Long userId = StpKit.USER.getLoginIdAsLong();
        PageDTO<VocPaySaleDTO> paySaleHistory = userPayService.getPaySaleHistory(userId, pageRequest);
        return Result.ok(paySaleHistory);
    }

}
