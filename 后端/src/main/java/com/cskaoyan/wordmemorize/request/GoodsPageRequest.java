package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.util.List;

@Data
public class GoodsPageRequest extends PageRequest{

    @Parameter(description = "获取商品的的类型: 1. 单词上限 2.等级解锁 3.补签卡")
    List<Integer> types;
}
