package com.cskaoyan.wordmemorize.auth;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpInterface;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.app.UserDTO;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import com.cskaoyan.wordmemorize.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    EmployeeService employeeService;

    @Resource
    UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        if(loginType.equals("admin")){

            // 从缓存中获取
            //Object cachedPermission = SaManager.getSaTokenDao().getObject("admin:permission:" + userId);
            //if (cachedPermission != null) {
            //    return (List<String>) cachedPermission;
            //}
            // 缓存中没有再从数据库中获取
            Set<String> permissionAlias = employeeService.getPermissionAlias(userId);
            List<String> permissionList = new ArrayList<>(permissionAlias);
            // 查好后，set 到缓存中
            //SaManager.getSaTokenDao().setObject("admin:permission:" + userId, permissionList, 60 * 60 * 24 * 7);
            return permissionList;
        }

//        Object cachedLevelPrivileges = SaManager.getSaTokenDao().getObject("app:level:privileges" + userId);
//        if (cachedLevelPrivileges != null) {
//            return (List<String>) cachedLevelPrivileges;
//        }

        UserDTO userInfo = userService.getUserInfo(userId);
        List<String> privilegeCodes = userInfo.getPrivilegeCodes();
        //SaManager.getSaTokenDao().setObject("app:level:privileges:" + userId, privilegeCodes, 60 * 60 * 24 * 7);
        return privilegeCodes;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
