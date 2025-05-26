# ---------- Stage 1: Build ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Maven files first (for dependency caching)
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# Set permissions and cache dependencies
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Now copy the source code
COPY src ./src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM openjdk:19-jdk
WORKDIR /app

# Copy the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
