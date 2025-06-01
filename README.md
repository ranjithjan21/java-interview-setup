# Java Real Estate Backend Interview Project

Welcome to the Java Real Estate Backend Interview Project!

This project is designed to help you verify your local development environment and demonstrate your backend skills during the interview process. The application simulates a simple real estate platform with agents, clients, and property listings, using Spring Boot, Java 17+, and Maven.

## Features

- RESTful APIs for managing Agents, Clients, and Listings
- JWT-based authentication and OAuth2 support
- Data persistence with Spring Data JPA (H2/in-memory database by default)
- DTO transformation layer
- Unit and integration tests with JUnit and Mockito
- OpenAPI/Swagger documentation

## Prerequisites

- Java 17 or higher
- Maven 3.8+

## Getting Started

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd java-interview-setup
   ```
2. **Open in your IDE:**
   Import the project as a Maven project in your preferred IDE (IntelliJ IDEA, Eclipse, VS Code, etc).
3. **Build the project:**
   ```sh
   mvn clean install
   ```
4. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The app will start on [http://localhost:8080](http://localhost:8080).
5. **Access API documentation:**
   Visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for interactive API docs.
6. **Run tests:**
   ```sh
   mvn test
   ```

## Project Structure

- `src/main/java/com.real.interview` - Main application code
  - `controller` - REST controllers
  - `entity` - JPA entities
  - `repository` - Spring Data repositories
  - `service` - Business logic
  - `transformer` - DTO/entity mappers
  - `config` - Security and app configuration
- `src/test/java/com.real.interview` - Unit and integration tests
- `src/main/resources/application.properties` - App configuration

## Authentication

- JWT authentication is enabled by default.
- OAuth2 login is supported (see `application.properties` for configuration).

## Example API Endpoints

- `GET /api/v1/agents` - List all agents
- `POST /api/v1/clients` - Create a new client
- `GET /api/v1/listings` - List all property listings

## Troubleshooting

- Ensure you are using Java 17+ (`java -version`)
- If ports are in use, stop other services or change the port in `application.properties`.
- For dependency issues, run `mvn clean install -U` to force update.

## License

This project is for interview and educational purposes only.
