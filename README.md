# Java Real Estate Backend Interview Project

This is a Spring Boot backend application for a real estate platform, designed for interview and assessment purposes. It demonstrates best practices in Java, Spring Boot, REST API design, security, and resilience patterns.

## Features
- User authentication and authorization (JWT, OAuth2)
- Agent, Client, and Listing management
- RESTful API endpoints
- Circuit Breaker and Rate Limiter (Resilience4j)
- API documentation with Swagger/OpenAPI
- Environment-specific configuration (dev, qa, prod)
- Integration and unit tests

## Tech Stack
- Java 17+
- Spring Boot
- Spring Security
- Resilience4j
- JUnit 5
- Mockito
- Maven
- Docker

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Docker (optional, for containerization)

### Build and Run

```
./mvnw clean install
./mvnw spring-boot:run
```

### Run Tests

```
./mvnw test
```

### API Documentation
After starting the application, access Swagger UI at:
```
http://localhost:8080/swagger-ui/
```

## Configuration
- Application properties are in `src/main/resources/application.properties`.
- Test properties are in `src/test/resources/application-test.properties`.
- Kubernetes deployment files are in the `overlays/` directory for different environments.

## Docker
To build and run the application in Docker:
```
docker build -t real-estate-backend .
docker run -p 8080:8080 real-estate-backend
```

## Notes
- This project is for interview/demo purposes and may use public APIs or mock data.
- For production use, review and update security, error handling, and configuration as needed.

## License
This project is provided for educational and interview purposes.
