package com.cskaoyan.wordmemorize.common.constant;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum implements CodeEnum {

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
    USER_NOT_EXIST(6027, "用户不存在"),
    CHECK_IN_DATE_DUPLICATE(6017, "打卡日期重复"),
    VOC_EXISTED(6018, "单词已存在!"),
    PERMISSION_CAN_NOT_DELETE(6019, "禁止删除权限"),
    PERMISSION_CAN_NOT_UPDATE(6020, "禁止修改权限"),
    CHAPTER_HAS_EXISTED(6022, "章节已存在!"),
    CHAPTER_NOT_EXISTED(6023, "章节不存在!"),
    UPDATE_CHAPTER_FAIL(6024, "更新章节失败!"),
    ADD_CHAPTER_FAIL(6025, "添加章节失败!"),
    ADD_USER_VOC_REV_ERROR(6026, "添加用户单词复习数据失败!"),
    VOC_NOT_EXISTED(6021, "单词不存在!"),
    PAY_SALE_NOT_FOUND(6027, "支付订单不存在!"),
    PAY_SALE_INSERT_ERROR(6028, "支付订单插入失败!"),
    ORDER_UPDATE_FAILED(6029, "订单更新失败!"),
    PAYMENT_AMOUNT_MISMATCH(6030, "支付金额与订单金额不匹配!"),
    PAYMENT_STATUS_INVALID(6031, "支付状态无效!"),
    GOODS_NOT_FOUND(6032, "商品不存在!"),
    ORDER_STATUS_ERROR(6033, "订单状态错误!"),
    PASSWORD_LENGTH_ERROR(6034, "密码长度错误!"),
    EMPLOYEE_NOT_FOUND(6035, "员工不存在!"),
    GOODS_INFO_ERROR(6036,"商品信息错误"),
    LEVEL_PURCHASE_ORDER_ERROR(6037, "等级购买订单错误(一级一级购买)"),
    USER_HAS_REPLACEMENT_CARD(6038, "用户已经拥有补签卡!"),
    USER_HAS_SIGN_CARD(6039, "用户已经拥有签到卡!"),
    LEVEL_EXIST(6040, "等级已存在!"),
    LEVEL_NOT_EXIST(6041, "等级不存在!");







    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
