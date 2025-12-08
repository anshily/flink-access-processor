package com.example.flinkmonitorbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.example.flinkmonitorbackend.mapper")
public class FlinkMonitorBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlinkMonitorBackendApplication.class, args);
    }

}