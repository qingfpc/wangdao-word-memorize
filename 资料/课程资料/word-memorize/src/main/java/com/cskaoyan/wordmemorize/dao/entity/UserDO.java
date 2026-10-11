package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDO extends BaseDO {

    /**
     *   华为登录openId
     */
    String openId;

    /**
     *  等级名称
     */
    String levelName;

    Integer levelValue;

    String headImg;

    String nickName;

    String thirdPartyName;

    String phoneNumber;

    //String statement;

    LocalDate birthday;

    String email;

    Integer hasSignCard;

    /*
         单词上限
     */
    //Integer vocBound;

    /*
         规划的每天记忆的单词量
     */
    Integer vocCountOfDay;

    LocalDateTime lastLoginTime;

    String lastLoginIp;

    LocalDate levelDate;


    /**
     *  用户的特权码集合
     */
    @TableField(exist = false)
    List<String> privilegeCodes;

}
