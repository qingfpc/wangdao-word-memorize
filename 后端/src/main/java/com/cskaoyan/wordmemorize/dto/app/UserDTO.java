package com.cskaoyan.wordmemorize.dto.app;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class UserDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "等级名称")
    String levelName;
    @Schema(description = "等级对应的值")
    Integer levelValue;
    @Schema(description = "升级日期")
    LocalDate levelDate;
    @Schema(description = "头像url")
    String headImg;
    @Schema(description = "昵称")
    String nickName;
    @Schema(description = "用户在第三方登录应用中的名字")
    String thirdPartyName;
    @Schema(description = "电话号码")
    String phoneNumber;
    @Schema(description = "用户生日")
    LocalDate birthday;
    @Schema(description = "用户Email")
    String email;
    @Schema(description = "是否拥有补签卡")
    Integer hasSignCard;
    @Schema(description = "用户当前规划的每天记忆的单词量")
    Integer vocCountOfDay;

    /**
     *  用户的特权名字符串集合
     */
    @Schema(description = "用户当前所拥有的等级特权码字符串集合")
    List<String> privilegeCodes;

}
