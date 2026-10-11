package com.cskaoyan.wordmemorize.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserBookStatisticsDO {
    private Integer total;
    private Integer selected;
    private Integer unSelected;
}
