package com.cskaoyan.wordmemorize.dto.app;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class UserVocBoundStatisticsDTO {
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "支付购买的单词上限")
    Integer pay;
    @Schema(description = "免费获取的单词上限")
    Integer free;
    @Schema(description = "兑换使用的单词上限")
    Integer exchange;
    @Schema(description = "总的单词上限")
    Integer total;
    @Schema(description = "规划使用的单词上限")
    Integer occupied;
    @Schema(description = "可用的单词上限")
    Integer available;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocBoundStatisticsDTO that = (UserVocBoundStatisticsDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(pay, that.pay) && Objects.equals(free, that.free) && Objects.equals(exchange, that.exchange) && Objects.equals(total, that.total) && Objects.equals(occupied, that.occupied) && Objects.equals(available, that.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, pay, free, exchange, total, occupied, available);
    }
}
