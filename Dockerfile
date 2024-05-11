# Base image with Java 21
FROM openjdk:21-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven executable to the image's filesystem
COPY mvnw .
COPY .mvn .mvn

# Copy pom.xml and source code
COPY pom.xml .
COPY src src

# Build the application using Maven
RUN ./mvnw clean package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Set the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "target/kitchensink_migrated-0.0.1-SNAPSHOT.jar"]
