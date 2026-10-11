package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

@Data
public class LevelPrivilegePageRequest extends PageRequest{
    @Parameter(description = "等级id")
    String levelId;
}
