package com.example.flinkmonitorbackend.service;

import com.example.flinkmonitorbackend.entity.AccessRecord;
import com.example.flinkmonitorbackend.mapper.AccessRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccessRecordService {
    @Autowired
    private AccessRecordMapper accessRecordMapper;
    
    public List<AccessRecord> findAll() {
        return accessRecordMapper.findAll();
    }
    
    public List<AccessRecord> findByEmployeeId(String employeeId) {
        return accessRecordMapper.findByEmployeeId(employeeId);
    }
}