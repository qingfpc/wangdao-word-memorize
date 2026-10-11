package com.cskaoyan.wordmemorize.request;

import lombok.Data;

@Data
public class GoodsCategoryCommand {

    String id;

    String name;

    Integer status;

    Integer sortBy;
}
