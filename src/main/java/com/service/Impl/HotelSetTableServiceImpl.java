package com.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.HotelSetTable;
import com.mapper.HotelSetMapper;
import com.service.HotelSetTableService;
import org.springframework.stereotype.Service;

@Service
public class HotelSetTableServiceImpl extends ServiceImpl<HotelSetMapper, HotelSetTable> implements HotelSetTableService {
}
