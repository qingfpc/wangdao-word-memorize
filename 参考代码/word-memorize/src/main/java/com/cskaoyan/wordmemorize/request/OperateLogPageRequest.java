package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class OperateLogPageRequest extends PageRequest{

    @Parameter(description = "员工id")
    String employeeId;
    @Parameter(description = "日志的数据类型")
    String dataType;
}
