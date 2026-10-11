package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.OperateLogDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.OperateLogPageRequest;

public interface AdminLogService {
    /*
          分页查询系统操作日志(后台操作日志)
     */
    PageDTO<OperateLogDTO> getOperateLogPage(OperateLogPageRequest request);
}
