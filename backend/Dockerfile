# Use official openjdk 17 image as the base image
FROM openjdk:17-jdk-alpine

# Create a new group and user named "app" to run the application with
RUN addgroup -S app && adduser -S app -G app

# Switch to the newly created user
USER app

# Copy the JAR file from the target directory to the current directory
COPY target/*.jar app.jar

# Set the entry point to run the JAR file with the java command
ENTRYPOINT ["java", "-jar", "/app.jar"]
