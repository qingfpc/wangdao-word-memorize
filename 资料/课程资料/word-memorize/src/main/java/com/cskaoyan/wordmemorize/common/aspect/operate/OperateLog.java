package com.cskaoyan.wordmemorize.common.aspect.operate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OperateLog {

    /**
     *  描述操作的数据，比如: 书籍，词汇，角色，权限
     *
     */
    String dateType();

    /**
     *  描述操作的类型:
     *  1. 新增
     *  2. 修改
     *  3. 单个删除
     *  4. 批量删除
     *  5. 修改状态
     *  6. excel导入
     *
     */
    int operateType();

}
