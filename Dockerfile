# Use a Maven image to build the project
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy the pom and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a JDK image to run the app
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Start from OpenJDK base image
FROM openjdk:17-jdk-slim

# Set work directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the project
RUN ./mvnw clean install

# Run the application
CMD ["java", "-jar", "target/mhetre-kitchen-backend-0.0.1-SNAPSHOT.jar"]
