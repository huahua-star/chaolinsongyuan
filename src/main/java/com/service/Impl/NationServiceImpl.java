package com.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.Nation;
import com.mapper.NationMapper;
import com.service.NationService;
import org.springframework.stereotype.Service;

@Service
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements NationService {

}
