package com.cskaoyan.wordmemorize.dto.app;

import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayUserLevelDTO {

    @Schema(description = "当前用户已达到的等级及等级对应的特权信息")
    List<LevelPageDTO> nowLevel;
    @Schema(description = "当前用户未达到的等级及等级对应的特权信息")
    List<LevelPageDTO> upperLevel;
}
