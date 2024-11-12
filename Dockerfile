# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN apt-get update && \
    apt-get install -y maven && \
    mvn dependency:resolve

# Copy the project source files
COPY src ./src

# Build and package the project
RUN mvn clean package -DskipTests

# Run the packaged .jar file
# (targeting the jar file created during packaging using `target/*.jar`)
CMD ["java", "-jar", "target/grocery-app-backend-0.0.1-SNAPSHOT.jar"]

# Expose the application port
EXPOSE 8000
