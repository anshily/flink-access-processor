package com.example.flinkmonitorbackend.service;

import com.example.flinkmonitorbackend.entity.ConsecutiveWorkDays;
import com.example.flinkmonitorbackend.mapper.ConsecutiveWorkDaysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConsecutiveWorkDaysService {
    @Autowired
    private ConsecutiveWorkDaysMapper consecutiveWorkDaysMapper;
    
    public List<ConsecutiveWorkDays> findAll() {
        return consecutiveWorkDaysMapper.findAll();
    }
    
    public List<ConsecutiveWorkDays> findByEmployeeId(String employeeId) {
        return consecutiveWorkDaysMapper.findByEmployeeId(employeeId);
    }
}