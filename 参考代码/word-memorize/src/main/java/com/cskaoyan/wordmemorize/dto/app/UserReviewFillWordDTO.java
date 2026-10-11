package com.cskaoyan.wordmemorize.dto.app;

import com.cskaoyan.wordmemorize.dto.admin.ReviewSubTopicDTO;
import com.cskaoyan.wordmemorize.dto.admin.TopicOptionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
@Data
public class UserReviewFillWordDTO {
    @Schema(description = "选词填空题目中包含的小题")
    List<ReviewSubTopicDTO> subTopicDTOs;
    @Schema(description = "选词填空题目中包含的选项")
    TopicOptionDTO option;

}
