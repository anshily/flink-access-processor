# 使用Java 17基础镜像
FROM openjdk:17-slim

# 设置工作目录
WORKDIR /app

# 复制Maven构建产物
COPY target/flink-monitor-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8082

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]