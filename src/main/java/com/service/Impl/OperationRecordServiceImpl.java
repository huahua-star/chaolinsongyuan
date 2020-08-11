package com.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.OperationRecord;
import com.mapper.OperationRecordMapper;
import com.service.OperationRecordService;
import org.springframework.stereotype.Service;

@Service
public class OperationRecordServiceImpl extends ServiceImpl<OperationRecordMapper, OperationRecord> implements OperationRecordService {
}
