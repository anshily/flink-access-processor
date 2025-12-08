package com.example.flinkmonitorbackend.service;

import com.example.flinkmonitorbackend.entity.AlertRecord;
import com.example.flinkmonitorbackend.mapper.AlertRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlertRecordService {
    @Autowired
    private AlertRecordMapper alertRecordMapper;
    
    public List<AlertRecord> findAll() {
        return alertRecordMapper.findAll();
    }
    
    public List<AlertRecord> findByEmployeeId(String employeeId) {
        return alertRecordMapper.findByEmployeeId(employeeId);
    }
}