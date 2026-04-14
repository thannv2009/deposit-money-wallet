# Stage 1: build project
FROM maven:3.9.9-eclipse-temurin-11 AS build

WORKDIR /app

# copy toàn bộ source từ root
COPY . .

# build từ root pom.xml
RUN mvn clean package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:11-jdk

WORKDIR /app

# lấy jar từ module deposit-api
COPY --from=build /app/deposit-api/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]