package com.cskaoyan.wordmemorize.common.constant;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum implements CodeEnum {

    //SUCCESS(20000,"成功"),
//    SUCCESS(0,"成功"),
    SUCCESS(200,"成功"),

    FAIL(500, "服务异常"),
    ILLEGAL_REQUEST( 204, "非法请求"),

    LOGIN_AUTH(401, "未登陆"),
    PERMISSION(5002, "没有权限"),
    EMPLOYEE_DUPLICATION(5003, "重复的员工"),
    LOCAL_LOGIN_FAIL(5004, "本地登录失败"), // 用户数据库验证失败
    TOKEN_INVALID(5005, "无效的token"),
    TOKEN_TIME_OUT(5006, "token已失效"),


    SEND_REMIND_MESSAGE_FAIL(6001, "发送用户提醒消息失败"),
    USER_ALREADY_OWN_RESIGN_CARD(6002, "用户已经拥有补签卡!"),
    GOODS_NOT_EXIST        (6003, "商品不存在!"),
    PAY_PRICE_ERROR (6004, "支付价格错误!"),
    USER_VOC_BOUND_NOT_ENOUGH (6005, "用户单词上限不足!"),
    LEVEL_UPGRADED(6006, "重复支付等级"),
    USER_CHECKIN_DATA_NEED_SYNC(6007, "请先完成当天的签到!"),
    VOC_EXCEL_TEMPLATE_READ_ERROR(6008, "excel模版读取失败"),
    VOC_EXCEL_FORMAT_ERROR(6009, "上传文件的表头与模板不匹配!"),
    VOC_EXCEL_DATA_ERROR(6010, "解析单词Excel数据错误!"),
    PARAM_ERROR(6011, "校验错误"),
    VOC_EXCEL_PARSE_ERROR(6012, "excel单词校验不存在"),
    EXCEL_SAVE_ERROR(6013, "excel保存错误"),
    BOOK_OFFLINE(6014, "书籍已下架"),
    BOOK_CATEGORY_OFFLINE(6015, "该类目下的书籍都已下架"),
    CHECK_IN_DATE_NOT_TODAY(6016, "打卡日期不是今天"),

    CHECK_IN_DATE_DUPLICATE(6017, "打卡日期重复"),
    VOC_EXISTED(6018, "单词已存在!"),
    PERMISSION_CAN_NOT_DELETE(6019, "禁止删除权限"),
    PERMISSION_CAN_NOT_UPDATE(6020, "禁止修改权限");



    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
