package com.cskaoyan.wordmemorize.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.cskaoyan.wordmemorize.dao.mapper.PermissionMapper;
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



    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());


        if ("admin".equals(loginType)) {
            // 获取管理员权限
            Set<String> permissions1 = employeeService.getPermissionAlias(userId);
            return new ArrayList<>(permissions1);
        }else if ("user".equals(loginType)) {
            // 获取用户权限
            Set<String> permissions2 = userService.getPrivilegeCodesList(userId);
            return new ArrayList<>(permissions2);
        }


        return new ArrayList<>();




//        if ("admin".equals(loginType)) {
//            // 添加控制器上声明的权限
//            permissionList.add("admin:employee:search");
//            permissionList.add("admin:employee:add");
//            permissionList.add("admin:employee:update");
//            permissionList.add("admin:employee:delete");
//
//            return permissionList;
//        }else {
//            privilegeCodes.add("review:ai:read");
//            return privilegeCodes;}




    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {


        return null;
    }
}