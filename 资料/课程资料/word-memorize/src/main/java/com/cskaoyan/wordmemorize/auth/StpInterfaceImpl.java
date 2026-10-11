package com.cskaoyan.wordmemorize.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.cskaoyan.wordmemorize.dto.app.UserDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class StpInterfaceImpl implements StpInterface {

    /*
         只需要自己实现这个方法即可
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        if ("admin".equals(loginType)) {
            // 查询后端用户的权限别名集合
            return null;
        }


        // 针对移动端用户，查询等级特权的特权码


        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
