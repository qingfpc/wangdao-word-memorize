package com.cskaoyan.wordmemorize.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationOptionDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Schema(description = "ai阅读模式请求体")
public class UserVocAISituationCommand {
    @Schema(description = "模式id")
    String id;
    @Schema(description = "模式名称")
    String name;
    @Schema(description = "模式上下架状态")
    Integer status;
    @Schema(description = "模式描述")
    String description;

    @Schema(description = "模式包含的选项集合")
    List<UserVocAISituationOptionCommand> options;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocAISituationCommand that = (UserVocAISituationCommand) o;
        return Objects.equals(name, that.name) && Objects.equals(status, that.status) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, status, description);
    }
}
