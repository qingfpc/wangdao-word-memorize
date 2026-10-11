package com.cskaoyan.wordmemorize.auth;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.stp.StpLogic;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;

public class StpKit {

    /**
     * Admin 会话对象，管理 Admin 表所有账号的登录、权限认证
     */
    public static final StpLogic ADMIN = new StpLogic("admin");

    /**
     * User 会话对象，管理 User 表所有账号的登录、权限认证
     */
    public static final StpLogic USER = new StpLogic("user");

    static {
        // 设定 StpUtil 使用的 SaTokenConfig 配置参数对象
        SaTokenConfig adminConfig = new SaTokenConfig();
        adminConfig.setTokenName(CommonConstant.ADMIN_LOGIN_TOKEN_KEY);
        adminConfig.setIsReadHeader(true);
        adminConfig.setTimeout(60 * 60 * 24 * 7);
        adminConfig.setTokenStyle("uuid");
        adminConfig.setActivityTimeout(-1);
        adminConfig.setIsConcurrent(true);
        adminConfig.setIsShare(true);
        adminConfig.setIsLog(true);
        adminConfig.setTokenStyle("uuid");
        ADMIN.setConfig(adminConfig);

        // 设定 StpUserUtil 使用的 SaTokenConfig 配置参数对象
        SaTokenConfig userConfig = new SaTokenConfig();
        userConfig.setTokenName(CommonConstant.APP_LOGIN_TOKEN_KEY);
        userConfig.setTimeout(60 * 60 * 24 * 7);
        userConfig.setIsConcurrent(true);
        userConfig.setTokenStyle("uuid");
        userConfig.setActivityTimeout(-1);
        userConfig.setIsShare(true);
        userConfig.setIsLog(true);
        userConfig.setIsReadHeader(true);
        userConfig.setLogLevel("debug");
        // 更多设置 ...
        USER.setConfig(userConfig);
    }

}
