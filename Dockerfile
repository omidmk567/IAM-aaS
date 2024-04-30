FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the source code
COPY ./iam-api .

# Build the project
RUN mvn clean package -DskipTests

# Create the final image with the built JAR file
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /opt/iam-aas

# Copy the JAR file
COPY --from=build /app/target/iam-api-2.0.2.jar ./application.jar

# Expose the port
EXPOSE 8000

# Run the application
ENTRYPOINT ["java", "-jar", "./application.jar"]