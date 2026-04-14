FROM maven:3.9.9-eclipse-temurin-11 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

FROM eclipse-temurin:11-jdk

WORKDIR /app

COPY --from=build /app/deposit-api/target/deposit-api-*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]