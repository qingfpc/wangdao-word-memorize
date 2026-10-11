package com.cskaoyan.wordmemorize.common.aspect;


import com.alibaba.fastjson.JSON;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.dao.entity.OperateLogDO;
import com.cskaoyan.wordmemorize.dao.mapper.OperateLogMapper;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cn.dev33.satoken.SaManager.log;

@Component
@Aspect
public class OperateAspect {

    @Resource
    OperateLogMapper operateLogMapper;

    //    @Pointcut("@annotation(com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog)")
//    public void myPointCut(){}
    @Around("@annotation(operateLog)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, OperateLog operateLog) throws Throwable {
        Object result = null;
        boolean success = true;
        String dataJson = null;

        try {
            // 执行目标方法
            result = joinPoint.proceed();
            if (isFileUploadOperation(joinPoint)) {
                dataJson = buildFileUploadLogData(joinPoint.getArgs());
            } else {
                // 获取请求参数
                Object[] args = joinPoint.getArgs();

                // 根据操作类型构造 data 字段
                switch (operateLog.operateType()) {
                    case OperateLogConstant.OPERATE_ADD:
                    case OperateLogConstant.OPERATE_UPDATE:
                    case OperateLogConstant.OPERATE_UPDATE_STATUS:
                        dataJson = JSON.toJSONString(args.length > 0 ? args[0] : null);
                        break;

                    case OperateLogConstant.OPERATE_SINGLE_DELETE:
                    case OperateLogConstant.OPERATE_BATCH_DELETE:
                    case OperateLogConstant.OPERATE_EXCEL_ADD:
                        dataJson = JSON.toJSONString(result);
                        break;

                    default:
                        dataJson = "未知操作类型";
                }
            }

        } catch (Throwable throwable) {
            success = false;
            throw throwable;
        } finally {
            OperateLogDO logDO = new OperateLogDO();
            logDO.setEmployeeId(getCurrentEmployeeId());
            logDO.setDataType(operateLog.dateType());
            logDO.setOperateType(operateLog.operateType());
            logDO.setData(dataJson);
            logDO.setStatus(success ? 1 : 0);
            logDO.setCreateTime(new Timestamp(System.currentTimeMillis()));

            operateLogMapper.insert(logDO);
        }

        return result;
    }

    private boolean isFileUploadOperation(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .anyMatch(arg -> arg instanceof MultipartFile);
    }

    private String buildFileUploadLogData(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof MultipartFile)
                .findFirst()
                .map(file -> {
                    MultipartFile multipartFile = (MultipartFile) file;
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("fileName", multipartFile.getOriginalFilename());
                    fileInfo.put("size", multipartFile.getSize());
                    fileInfo.put("contentType", multipartFile.getContentType());
                    return JSON.toJSONString(fileInfo);
                })
                .orElse("{}");
    }
    private String safeJsonSerialize(Object[] args) {
        try {
            return args.length > 0 ? JSON.toJSONString(args[0]) : "{}";
        } catch (Exception e) {
            log.warn("JSON序列化失败，使用简化处理", e);
            return "{\"error\":\"参数无法序列化\"}";
        }
    }
    private Long getCurrentEmployeeId() {
        Long employeeId = StpKit.ADMIN.getLoginIdAsLong();
        return employeeId;
    }
}
