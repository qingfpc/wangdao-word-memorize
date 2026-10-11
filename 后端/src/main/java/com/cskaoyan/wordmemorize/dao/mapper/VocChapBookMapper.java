package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.VocChapBookDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VocChapBookMapper extends BaseMapper<VocChapBookDO> {

}
