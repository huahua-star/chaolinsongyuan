package com.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.RoomDescription;
import com.mapper.RoomDescriptionMapper;
import com.service.RoomDescriptionService;
import org.springframework.stereotype.Service;

@Service
public class RoomDescriptionServiceImpl extends ServiceImpl<RoomDescriptionMapper, RoomDescription> implements RoomDescriptionService {
}
