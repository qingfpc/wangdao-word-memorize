package com.cskaoyan.wordmemorize.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
public class BaseDO {
    @TableId
   public Long id;

    @TableLogic
   public Integer isDeleted;
}
