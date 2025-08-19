#FROM maven:3.9.11-eclipse-temurin-21-noble
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM eclipse-temurin:17-alpine
#COPY --from=build /target/*.jar demo.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar","demo.jar"]

# Stage 1: Build the Spring Boot JAR
FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the pom.xml first for caching dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
