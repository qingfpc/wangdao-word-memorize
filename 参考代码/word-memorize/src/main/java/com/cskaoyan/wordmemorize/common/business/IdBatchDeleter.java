package com.cskaoyan.wordmemorize.common.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.Acceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class IdBatchDeleter<U> implements BatchDeleter <U>{

    // 一次批量操作的数量
    private final Integer BATCH;

    private BaseMapper<U> baseMapper;

    @Override
    public <P> void delete(SFunction<U, P> field, List<P> conditions) {
        List<P> batchConditions = new ArrayList<>();
        for (int i = 1; i <= conditions.size(); i++) {
            batchConditions.add(conditions.get(i - 1));
            if (i % BATCH == 0) {
                LambdaQueryWrapper<U> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(field, batchConditions);
                baseMapper.delete(wrapper);
                batchConditions.clear();
            }
        }

        if (!batchConditions.isEmpty()) {
            LambdaQueryWrapper<U> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(field, batchConditions);
            baseMapper.delete(wrapper);
        }

    }
}
