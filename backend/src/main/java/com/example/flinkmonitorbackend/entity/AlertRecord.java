package com.example.flinkmonitorbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class AlertRecord {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("employee_id")
    private String employeeId;
    @JsonProperty("alert_date")
    private Date alertDate;
    @JsonProperty("alert_time")
    private String alertTime;
    @JsonProperty("alert_message")
    private String alertMessage;
    @JsonProperty("created_at")
    private Date createdAt;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public Date getAlertDate() {
        return alertDate;
    }
    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }
    public String getAlertTime() {
        return alertTime;
    }
    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }
    public String getAlertMessage() {
        return alertMessage;
    }
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}