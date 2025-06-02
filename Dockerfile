# Use a lightweight OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Copy source code
COPY src src

# Set environment variables (can be overridden at runtime)
ENV JWT_SECRET=changeme
ENV GOOGLE_CLIENT_ID=your-google-client-id
ENV GOOGLE_CLIENT_SECRET=your-google-client-secret

# Package the application
RUN ./mvnw clean package -DskipTests

# Copy the built jar to the container
RUN cp target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

