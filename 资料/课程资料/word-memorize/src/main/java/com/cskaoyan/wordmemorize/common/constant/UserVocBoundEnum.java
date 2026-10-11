package com.cskaoyan.wordmemorize.common.constant;

import lombok.Getter;

@Getter
public enum UserVocBoundEnum {

    USER_VOC_BOUND_LOG_TYPE_CHARGE(1, "购买"),

    USER_VOC_BOUND_LOG_TYPE_CHECKIN(2, "签到奖励"),

    USER_VOC_BOUND_LOG_TYPE_CONSUME(3, "兑换"),

    USER_VOC_BOUND_LOG_REGISTER(4, "新人注册");

    private Integer value;

    private String description;

    private UserVocBoundEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
