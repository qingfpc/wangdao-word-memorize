package com.cskaoyan.wordmemorize.dao.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("user_voc_bound_statistics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserVocBoundStatisticsDO extends BaseDO {
    Long userId;

    Integer pay;

    Integer free;

    Integer exchange;

    Integer total;

    Integer occupied;

    Integer available;
}
