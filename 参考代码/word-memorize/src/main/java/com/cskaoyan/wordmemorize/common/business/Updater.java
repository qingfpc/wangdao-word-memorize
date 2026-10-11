package com.cskaoyan.wordmemorize.common.business;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;

import java.util.function.Function;

public interface Updater <T extends BaseDO> {

    <I, V>void update(SFunction<T, I> function, I value, String inserFieldName);
}
