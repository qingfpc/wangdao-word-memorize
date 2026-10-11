package com.cskaoyan.wordmemorize;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dao.entity.ReviewTopicDO;
import com.cskaoyan.wordmemorize.dao.entity.VocDO;
import com.cskaoyan.wordmemorize.dao.mapper.EmployeeMapper;
import com.cskaoyan.wordmemorize.dao.mapper.ReviewTopicMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocMapper;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.VocCommand;
import com.cskaoyan.wordmemorize.request.VocPageRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import com.cskaoyan.wordmemorize.service.VocService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Autowired
    VocService vocService;

    @Test
    public void testUpdateVoc_InvalidId() {
        // 准备测试数据
        VocCommand command = new VocCommand();
        command.setId("invalidId");
        command.setContent("testContent");
        command.setInterpretations(Collections.emptyList());
        command.setExamples(Collections.emptyList());
        command.setRelations(Collections.emptyList());

        // 执行测试并验证异常
        assertThrows(BusinessException.class, () -> vocService.updateVoc(command));
    }




}
