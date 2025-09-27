FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

COPY . /build

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
