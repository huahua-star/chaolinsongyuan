package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.MiniRoomType;
import com.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
}
