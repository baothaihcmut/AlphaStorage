# Use a base image that has JDK 11 or the version your app requires
FROM openjdk:23-jdk-slim as builder

# Set the working directory
WORKDIR /app

# Copy the jar file to the working directory
COPY target/AnphaStorage-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
