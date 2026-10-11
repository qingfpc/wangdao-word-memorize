package com.cskaoyan.wordmemorize.common.aspect.operate;

import com.alibaba.fastjson.JSON;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.dao.entity.OperateLogDO;
import com.cskaoyan.wordmemorize.dao.mapper.OperateLogMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;

@Component
@Aspect
@Slf4j
public class AdminOperateLogAspect {

    @Resource
    OperateLogMapper operateLogMapper;


    @Around(value = "@annotation(com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog)")
    public Object editsLogAspectMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperateLog annotation = method.getAnnotation(OperateLog.class);

        // 获取插入类型和插入描述
        int operateType = annotation.operateType();
        String dateType = annotation.dateType();

        Throwable ex = null;
        // 执行状态
        Integer status = 1;
        Object proceed = null;
        try {
            proceed = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            status = 0;
            log.error("操作失败{}, error{}", dateType, e.getMessage());
            throw new RuntimeException(e);
        } finally {
            // 添加日志
            addLog(joinPoint, proceed, status, operateType, dateType);
        }
        return proceed;
    }

    private void addLog(ProceedingJoinPoint joinPoint, Object result,
                        Integer methodResult, Integer operateType, String dateType) {

        // 获取用户id
        Long employeeId = StpKit.ADMIN.getLoginIdAsLong();
        // 获取方法的参数
        String data = null;
        if (operateType == OperateLogConstant.OPERATE_ADD
                || operateType == OperateLogConstant.OPERATE_UPDATE) {
            Object arg = joinPoint.getArgs()[0];
            data = JSON.toJSONString(arg);
            insertLog(data, employeeId, dateType, operateType, methodResult);
            return;
        }
        if (operateType == OperateLogConstant.OPERATE_UPDATE_STATUS) {
            Long id = (Long) joinPoint.getArgs()[0];
            Integer status = (Integer) joinPoint.getArgs()[1];
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("id", id.toString());
            paramMap.put("status", status.toString());
            data = JSON.toJSONString(paramMap);
            insertLog(data, employeeId, dateType, operateType, methodResult);
            return;
        }

        // 如果是单个删除，批量删除，excel添加，则通过返回值生成日志数据
        if (methodResult == 0) {
            // 操作失败
            if (operateType == OperateLogConstant.OPERATE_EXCEL_ADD) {
                data = dateType + "excel导入失败！";
                insertLog(data, employeeId, dateType, operateType, methodResult);
                return;
            }
            // 单个删除或者批量删除
            data = JSON.toJSONString(joinPoint.getArgs());
            insertLog(data, employeeId, dateType, operateType, methodResult);
            return;
        }

        // 操作成功
        data = JSON.toJSONString(result);
        insertLog(data, employeeId, dateType, operateType, methodResult);

    }

    private void insertLog(String data, Long employeeId, String dateType, int operateType, Integer methodResult) {
            OperateLogDO operateLogDO = new OperateLogDO();
            operateLogDO.setEmployeeId(employeeId);
            operateLogDO.setDataType(dateType);
            operateLogDO.setOperateType(operateType);
            operateLogDO.setData(data);
            operateLogDO.setStatus(methodResult);
            operateLogMapper.insert(operateLogDO);
    }

}
