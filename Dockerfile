# --- 1. BUILD STAGE ---
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Pre-download dependencies (caching)
RUN ./mvnw dependency:go-offline

# Copy the rest of the code
COPY src src

# Build the Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# --- 2. RUNTIME STAGE ---
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from previous stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
