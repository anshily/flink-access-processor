package com.example.flinkmonitorbackend.mapper;

import com.example.flinkmonitorbackend.entity.ConsecutiveWorkDays;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ConsecutiveWorkDaysMapper {
    List<ConsecutiveWorkDays> findAll();
    List<ConsecutiveWorkDays> findByEmployeeId(String employeeId);
}