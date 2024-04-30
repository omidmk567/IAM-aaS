FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /opt/iam-aas

# Copy the JAR file
COPY ./target/iam-api-2.0.2.jar ./application.jar

# Expose the port
EXPOSE 8000

# Run the application
ENTRYPOINT ["java", "-jar", "./application.jar"]