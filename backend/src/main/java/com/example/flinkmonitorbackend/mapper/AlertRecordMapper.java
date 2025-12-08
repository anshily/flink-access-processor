package com.example.flinkmonitorbackend.mapper;

import com.example.flinkmonitorbackend.entity.AlertRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AlertRecordMapper {
    List<AlertRecord> findAll();
    List<AlertRecord> findByEmployeeId(String employeeId);
}