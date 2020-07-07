package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Nation;
import com.entity.Race;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RaceMapper  extends BaseMapper<Race> {
}
