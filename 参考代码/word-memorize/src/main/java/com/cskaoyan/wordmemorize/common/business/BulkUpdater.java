package com.cskaoyan.wordmemorize.common.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BulkUpdater<U extends BaseDO> implements Updater<U> {

    BaseMapper<U> mapper;


    List<U> listToUpdate;


    @Override
    public <I, V>void update(SFunction<U, I> function, I value, String insertFieldName) {

        // 1. 获取数据库中的数据
        LambdaQueryWrapper<U> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(function, value);

        if (CollectionUtils.isEmpty(listToUpdate)) {
            return;
        }

        //2. 更新或插入数据
        listToUpdate.stream().forEach(e -> {
                    if (e.getId() == null) {
                        try {
                            Class<? extends BaseDO> clz = e.getClass();
                            Field field = clz.getDeclaredField(insertFieldName);
                            field.setAccessible(true);
                            field.set(e, value);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        mapper.insert(e);
                        return;
                    }

                    mapper.updateById(e);
                });

    }


}
