package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.LevelDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface LevelMapper extends BaseMapper<LevelDO> {
    List<Map<String, Object>> selectLevelsWithGoods();

    @Select("SELECT id, level_name, level_value, check_in_days, check_in_type, " +
            "unlock_price, description, is_deleted " +
            "FROM level WHERE level_value = #{levelValue} AND is_deleted = 0 LIMIT 1")
    LevelDO selectNextLevel(@Param("levelValue") Integer levelValue);

//    @Select("SELECT id,level_name,level_value,check_in_days,check_in_type," +
//            "unlock_price,description,is_deleted FROM level WHERE is_deleted=0")
//    List<LevelDO> selectAllWithoutGoodsId();
}
