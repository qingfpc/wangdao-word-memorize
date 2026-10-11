package com.cskaoyan.wordmemorize.common.constant;

public class CommonConstant {

    /*
         admin
     */
    public static final Integer BATCH = 50;


    /*
         login
     */
    public static final String ADMIN_LOGIN_TOKEN_KEY = "X-Cskaoyan-Admin-Token";

    public static final String APP_LOGIN_TOKEN_KEY = "X-Cskaoyan-App-Token";
    public static final String USER_NICK_NAME_PREFIX = "cs背单词";

    public static final String REGISTER_LEVEL_NAME = "LVL0";

    public static final Integer REGISTER_LEVEL_VALUE = 0;

    public static final Integer REGISTER_VOC_BOUND = 600;

    public static final Integer REGISTER_VOC_OF_DAY = 20;
    public static final Integer DEFAULT_REMIND_MESSAGE_STATUS = 0;

    public static final String DEFAULT_REMIND_TIME = "21:30";

    public final static String LOGIN_USER_INFO = "user:info";


    /*
         review
     */
    public static final Integer USER_CHECKIN_VOC_REWARD = 1;

    public static final Integer USER_CHECKIN_TYPE_CONTINUOUS = 1;

    public static final Integer USER_CHECKIN_TYPE_ACCUMULATE = 2;

    public static final Integer REMIND_MESSAGE_BATCH = 50;

    public static final Integer REMIND_SYSTEM_STATUS_OPEN = 1;


    public static final String REMIND_MESSAGE_CONFIG_EXPR_FIRST_PARAM = "days";

    public static final String REMIND_MESSAGE_CONFIG_EXPR_SECOND_PARAM = "span";

    public static final Integer USER_REVIEW_FAMILIAR_MIN_INTERVAL = 365;

    public static final Integer USER_PRE_REVIEW_MAX_INTERVAL = 6;

    public static final Integer USER_VOC_NOT_STARTED_TYPE = 0;


    // checkin
    public static final Integer USER_CHECK_IN_NORMAL_TYPE = 1;
    public static final Integer USER_CHECK_IN_RESIGN_TYPE = 2;

    // level
    public static final Integer ACCUMULATE_CHECK_IN_TYPE_MAX_LEVEL = 6;
    public static final Integer CHECK_IN_TYPE_MAX_LEVEL = 12;

    public static final String USER_PRIVILEGE_CODE_AI_READ = "review:ai:read";



    //vocBound log
    public static final Integer USER_VOC_BOUND_CHANGE_TYPE_PAY = 1;
    public static final Integer USER_VOC_BOUND_CHANGE_TYPE_CHECKIN_REWARD = 2;

    public static final Integer USER_VOC_BOUND_CHANGE_TYPE_EXCHANGE = 3;

    public static final Integer USER_VOC_BOUND_CHANGE_TYPE_REGISTER = 4;

    public static final String USER_VOC_BOUND_DESCRIPTION_CHECKIN = "签到奖励";

    public static final String USER_VOC_BOUND_DESCRIPTION_AI_READ = "使用AI阅读";



    // pay
    public static final Integer NOT_PAY_SUCCESS = 0;

    public static final Integer PAY_SUCCESS = 1;

    public static final Integer PAY_TYPE_GOODS = 1;




    // 系统配置
    public static final Integer PAY_TYPE_USER_VOC_BOUND = 1;
    public static final Integer PAY_TYPE_USER_LEVEL = 2;
    public static final Integer PAY_TYPE_USER_RESIGN_CARD = 3;

    public static final Double USER_VOC_REV_FACTOR_INIT = 1.4;
}
