package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckStickDO;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinDO;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinStatisticsDO;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinStickDateRangeDO;
import lombok.NonNull;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserCheckinMapper extends BaseMapper<UserCheckinDO> {

}
