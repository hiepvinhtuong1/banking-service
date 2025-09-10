# backend/Dockerfile
# Stage 1: build
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: create image
FROM amazoncorretto:21.0.4
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Cài đặt netcat để kiểm tra kết nối
#RUN yum update -y && yum install -y nc

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]