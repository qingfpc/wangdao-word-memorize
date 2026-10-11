package com.cskaoyan.wordmemorize;

import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dao.entity.UserPrivilegeUseStatisticsDO;
import com.cskaoyan.wordmemorize.dao.mapper.EmployeeMapper;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WordMemorizeApplicationTests {


    @Autowired
    EmployeeService employeeService;


    @Test
    public void testService() {
        // 造数据
        Long employeeId = 1858808795138543618L;


        EmployeeDTO employeeInfo = employeeService.getEmployeeInfo(employeeId);
    }


    @Autowired
    EmployeeMapper employeeMapper;


    @Test
    public void testMapper() {
        Long employeeId = 1858808795138543618L;
        EmployeeDO employeeInfo = employeeMapper.getEmployeeInfo(employeeId);
    }
}
