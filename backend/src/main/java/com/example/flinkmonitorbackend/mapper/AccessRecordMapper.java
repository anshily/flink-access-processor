package com.example.flinkmonitorbackend.mapper;

import com.example.flinkmonitorbackend.entity.AccessRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AccessRecordMapper {
    List<AccessRecord> findAll();
    List<AccessRecord> findByEmployeeId(String employeeId);
}