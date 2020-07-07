package com.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.Nation;
import com.entity.Race;
import com.mapper.NationMapper;
import com.mapper.RaceMapper;
import com.service.NationService;
import com.service.RaceService;
import org.springframework.stereotype.Service;

@Service
public class RaceServiceImpl  extends ServiceImpl<RaceMapper, Race> implements RaceService {

}
