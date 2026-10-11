package com.cskaoyan.wordmemorize.dto.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema(description = "操作日志")
public class OperateLogDTO {

    @Schema(description = "操作日志id")
    String id;
    @Schema(description = "操作员工id")
    String employeeId;
    @Schema(description = "数据类型字符串")
    String dataType;
    @Schema(description = "操作类型")
    Integer operateType;
    @Schema(description = "json数据")
    String data;
    @Schema(description = "操作结果: 0失败 1成功")
    Integer status;
    @Schema(description = "创建时间字符串")
    String createTime;
}
