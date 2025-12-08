CREATE TABLE IF NOT EXISTS access_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    access_time DATETIME NOT NULL,
    direction VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stay_duration (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    duration_seconds INT NOT NULL,
    location VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建差旅表
CREATE TABLE IF NOT EXISTS travel_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    travel_date DATE NOT NULL,
    reason VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建排班表
CREATE TABLE IF NOT EXISTS shift_schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    schedule_date DATE NOT NULL,
    shift_type VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建提醒表
CREATE TABLE IF NOT EXISTS alert_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    alert_date DATE NOT NULL,
    alert_time TIME NOT NULL,
    alert_message VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建连续工作天数表
CREATE TABLE IF NOT EXISTS consecutive_work_days (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    consecutive_days INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data for testing
-- 插入更多门禁记录
INSERT INTO access_records (employee_id, access_time, direction) VALUES
('EMP001', '2024-01-01 08:00:00', 'IN'),
('EMP001', '2024-01-01 12:00:00', 'OUT'),
('EMP001', '2024-01-01 13:00:00', 'IN'),
('EMP001', '2024-01-01 18:00:00', 'OUT'),
('EMP002', '2024-01-01 09:00:00', 'IN'),
('EMP002', '2024-01-01 11:30:00', 'OUT'),
('EMP002', '2024-01-01 14:00:00', 'IN'),
('EMP002', '2024-01-01 17:00:00', 'OUT'),
('EMP003', '2024-01-01 08:30:00', 'IN'),
('EMP003', '2024-01-01 10:00:00', 'OUT'),
('EMP003', '2024-01-01 10:45:00', 'IN'),
('EMP003', '2024-01-01 18:30:00', 'OUT'),
('EMP004', '2024-01-01 09:15:00', 'IN'),
('EMP004', '2024-01-01 12:00:00', 'OUT'),
('EMP004', '2024-01-01 13:30:00', 'IN'),
('EMP004', '2024-01-01 16:45:00', 'OUT');

-- 插入排班记录
INSERT INTO shift_schedule (employee_id, schedule_date, shift_type, start_time, end_time) VALUES
('EMP001', '2024-01-01', 'FULL_DAY', '08:00:00', '18:00:00'),
('EMP002', '2024-01-01', 'FULL_DAY', '09:00:00', '17:00:00'),
('EMP003', '2024-01-01', 'FULL_DAY', '08:30:00', '18:30:00'),
('EMP004', '2024-01-01', 'FULL_DAY', '09:00:00', '17:00:00');

-- 插入差旅记录
INSERT INTO travel_records (employee_id, travel_date, reason) VALUES
('EMP001', '2024-01-02', '客户拜访'),
('EMP003', '2024-01-01', '培训');
