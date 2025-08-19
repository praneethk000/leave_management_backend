#FROM maven:3.9.11-eclipse-temurin-21-noble
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM eclipse-temurin:17-alpine
#COPY --from=build /target/*.jar demo.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar","demo.jar"]

# Stage 1: Build the app
FROM gradle:8.3-jdk17 AS build
WORKDIR /app

# Copy Gradle files first to leverage cache
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Fetch dependencies
RUN gradle build --no-daemon || return 0

# Copy the source code
COPY src ./src

# Build the jar
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the app
FROM eclipse-temurin:17-alpine
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
