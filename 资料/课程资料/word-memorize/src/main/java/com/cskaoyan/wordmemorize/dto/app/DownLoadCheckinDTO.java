package com.cskaoyan.wordmemorize.dto.app;

import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DownLoadCheckinDTO {
    @Schema(description = "本次下载的签到页数据")
    PageDTO<UserCheckinDTO> userCheckinPage;
}
