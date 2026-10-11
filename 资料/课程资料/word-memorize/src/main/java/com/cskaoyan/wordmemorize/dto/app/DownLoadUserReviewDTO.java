package com.cskaoyan.wordmemorize.dto.app;

import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "下载用户的所有单词复习数据")
public class DownLoadUserReviewDTO {
    @Schema(description = "用户已选单词(复习)数据")
    PageDTO<UserVocRevDTO> userVocRevPage;
    @Schema(description = "用户以天为单位的复习统计数据数据")
    PageDTO<UserReviewStatisticsDTO> userReviewStatisticsPage;
}
