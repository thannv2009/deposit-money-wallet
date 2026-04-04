FROM openjdk:11-ea-9-jdk
WORKDIR /app

# copy file jar đã build
COPY target/wallet-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]