package com.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.Reservation;
import com.mapper.ReservationMapper;
import com.service.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl  extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {
}
