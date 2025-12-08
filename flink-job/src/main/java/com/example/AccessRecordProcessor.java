package com.example;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public class AccessRecordProcessor {

    public static void main(String[] args) {
        // 创建TableEnvironment
        EnvironmentSettings settings = EnvironmentSettings.inBatchMode();
        TableEnvironment tableEnv = TableEnvironment.create(settings);

        // 注册MySQL源表（access_records）
        tableEnv.executeSql("""
            CREATE TABLE access_records (
                id INT,
                employee_id STRING,
                access_time TIMESTAMP,
                direction STRING,
                created_at TIMESTAMP
            ) WITH (
                'connector' = 'jdbc',
                'url' = 'jdbc:mysql://mysql:3306/access_db?useSSL=false&allowPublicKeyRetrieval=true',
                'username' = 'root',
                'password' = 'root_password',
                'table-name' = 'access_records'
            )
        """);

        // 注册MySQL结果表（stay_duration）
        tableEnv.executeSql("""
            CREATE TABLE stay_duration (
                employee_id STRING,
                start_time TIMESTAMP,
                end_time TIMESTAMP,
                duration_seconds INT,
                location STRING
            ) WITH (
                'connector' = 'jdbc',
                'url' = 'jdbc:mysql://mysql:3306/access_db?useSSL=false&allowPublicKeyRetrieval=true',
                'username' = 'root',
                'password' = 'root_password',
                'table-name' = 'stay_duration'
            )
        """);

        // 注册差旅表
        tableEnv.executeSql("""
            CREATE TABLE travel_records (
                id INT,
                employee_id STRING,
                travel_date DATE,
                reason STRING,
                created_at TIMESTAMP
            ) WITH (
                'connector' = 'jdbc',
                'url' = 'jdbc:mysql://mysql:3306/access_db?useSSL=false&allowPublicKeyRetrieval=true',
                'username' = 'root',
                'password' = 'root_password',
                'table-name' = 'travel_records'
            )
        """);

        // 注册排班表
        tableEnv.executeSql("""
            CREATE TABLE shift_schedule (
                id INT,
                employee_id STRING,
                schedule_date DATE,
                shift_type STRING,
                start_time TIME,
                end_time TIME,
                created_at TIMESTAMP
            ) WITH (
                'connector' = 'jdbc',
                'url' = 'jdbc:mysql://mysql:3306/access_db?useSSL=false&allowPublicKeyRetrieval=true',
                'username' = 'root',
                'password' = 'root_password',
                'table-name' = 'shift_schedule'
            )
        """);

        // 注册提醒表
        tableEnv.executeSql("""
            CREATE TABLE alert_records (
                employee_id STRING,
                alert_date DATE,
                alert_time TIME,
                alert_message STRING
            ) WITH (
                'connector' = 'jdbc',
                'url' = 'jdbc:mysql://mysql:3306/access_db?useSSL=false&allowPublicKeyRetrieval=true',
                'username' = 'root',
                'password' = 'root_password',
                'table-name' = 'alert_records'
            )
        """);

        // 处理逻辑1：计算停留时间
        tableEnv.executeSql("""
            INSERT INTO stay_duration
            SELECT 
                employee_id,
                start_time,
                end_time,
                TIMESTAMPDIFF(SECOND, start_time, end_time) AS duration_seconds,
                location
            FROM (
                SELECT 
                    employee_id,
                    start_time,
                    end_time,
                    CASE 
                        WHEN start_direction = 'IN' THEN 'INSIDE'
                        ELSE 'OUTSIDE'
                    END AS location
                FROM (
                    SELECT 
                        r1.employee_id,
                        r1.access_time AS start_time,
                        r2.access_time AS end_time,
                        r1.direction AS start_direction,
                        r2.direction AS end_direction
                    FROM (
                        SELECT 
                            employee_id,
                            access_time,
                            direction,
                            ROW_NUMBER() OVER (PARTITION BY employee_id ORDER BY access_time) AS row_num
                        FROM access_records
                    ) r1
                    JOIN (
                        SELECT 
                            employee_id,
                            access_time,
                            direction,
                            ROW_NUMBER() OVER (PARTITION BY employee_id ORDER BY access_time) AS row_num
                        FROM access_records
                    ) r2 ON 
                        r1.employee_id = r2.employee_id AND 
                        r1.row_num + 1 = r2.row_num
                ) paired_records
                WHERE start_direction <> end_direction
            ) calculated_duration
        """);

        // 处理逻辑2：生成提醒记录
        tableEnv.executeSql("""
            INSERT INTO alert_records
            SELECT 
                s.employee_id,
                s.schedule_date AS alert_date,
                CURRENT_TIME AS alert_time,
                CONCAT('员工 ', s.employee_id, ' 当日有排班且在门禁外时间超过30分钟且无差旅记录') AS alert_message
            FROM (
                SELECT 
                    employee_id,
                    CAST(start_time AS DATE) AS record_date,
                    SUM(CASE WHEN location = 'OUTSIDE' THEN duration_seconds ELSE 0 END) AS total_outside_seconds
                FROM stay_duration
                GROUP BY employee_id, CAST(start_time AS DATE)
                HAVING SUM(CASE WHEN location = 'OUTSIDE' THEN duration_seconds ELSE 0 END) > 1800 -- 30分钟 = 1800秒
            ) outside_time
            JOIN shift_schedule s ON 
                outside_time.employee_id = s.employee_id AND 
                outside_time.record_date = s.schedule_date
            LEFT JOIN travel_records t ON 
                outside_time.employee_id = t.employee_id AND 
                outside_time.record_date = t.travel_date
            WHERE t.id IS NULL -- 无差旅记录
        """);

        // 处理逻辑3：计算连续工作天数
        // 注册连续工作天数表
        tableEnv.executeSql("""
            CREATE TABLE consecutive_work_days (
                employee_id STRING,
                consecutive_days INT,
                start_date DATE,
                end_date DATE
            ) WITH (
                'connector' = 'jdbc',
                'url' = 'jdbc:mysql://mysql:3306/access_db?useSSL=false&allowPublicKeyRetrieval=true',
                'username' = 'root',
                'password' = 'root_password',
                'table-name' = 'consecutive_work_days'
            )
        """);

        // 计算连续工作天数 - 完整逻辑
        // 1. 确定每个员工的工作日期
        // 2. 计算相邻工作日期的间隔
        // 3. 标记连续工作的分组
        // 4. 统计每组的连续天数
        tableEnv.executeSql("""
            INSERT INTO consecutive_work_days
            SELECT
                employee_id,
                CAST(MAX(consecutive_group_days) AS INT) AS consecutive_days,
                MIN(work_date) AS start_date,
                MAX(work_date) AS end_date
            FROM (
                SELECT
                    employee_id,
                    work_date,
                    consecutive_group,
                    COUNT(*) OVER (PARTITION BY employee_id, consecutive_group ORDER BY work_date) AS consecutive_group_days
                FROM (
                    SELECT
                        employee_id,
                        work_date,
                        SUM(is_new_sequence) OVER (PARTITION BY employee_id ORDER BY work_date) AS consecutive_group
                    FROM (
                        SELECT
                            employee_id,
                            work_date,
                            LAG(work_date) OVER (PARTITION BY employee_id ORDER BY work_date) AS prev_work_date,
                            CASE
                                WHEN LAG(work_date) OVER (PARTITION BY employee_id ORDER BY work_date) IS NULL THEN 1
                                WHEN TIMESTAMPDIFF(DAY, 
                                                   CAST(LAG(work_date) OVER (PARTITION BY employee_id ORDER BY work_date) AS TIMESTAMP), 
                                                   CAST(work_date AS TIMESTAMP)) > 1 THEN 1
                                ELSE 0
                            END AS is_new_sequence
                        FROM (
                            -- 获取每个员工的工作日期（有进入记录的日期）
                            SELECT DISTINCT
                                employee_id,
                                CAST(access_time AS DATE) AS work_date
                            FROM access_records
                            WHERE direction = 'IN'
                        ) distinct_work_dates
                    ) sequence_markers
                ) grouped_sequences
            ) sequence_counts
            GROUP BY employee_id
            HAVING MAX(consecutive_group_days) > 6
        """);

        System.out.println("Access record processing completed successfully!");
    }
}