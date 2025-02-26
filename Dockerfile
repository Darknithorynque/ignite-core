# Use an official OpenJDK image to run the Spring Boot application
FROM openjdk:17-jdk-slim

# Copy the jar file built by Maven/Gradle into the container
COPY target/ignite-core-0.0.1-SNAPSHOT.jar ignite-core.jar

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "ignite-core.jar"]

# Expose the port the app will run on
EXPOSE 8084
