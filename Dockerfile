# Etapa 1: Build do JAR usando Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Criação do Container Final
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/mercadinho-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
