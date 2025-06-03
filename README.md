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
- GitHub Copilot (GenAI-assisted development)

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
docker build -t learning/springboot-app:latest .
docker run -p 8080:8080 learning/springboot-app:latest
```

## AWS EKS Deployment Guide

Below are the enhanced steps to deploy this Spring Boot application to AWS EKS:

### 1. AWS CLI & IAM Setup
- Configure AWS CLI with your IAM user credentials:
  ```sh
  aws configure
  ```

### 2. Build & Push Docker Image to ECR
- Authenticate Docker to your ECR registry:
  ```sh
  aws ecr get-login-password --region <your-region> | docker login --username AWS --password-stdin <your-account-id>.dkr.ecr.<your-region>.amazonaws.com
  ```
- Tag your Docker image:
  ```sh
  docker tag learning/springboot-app:latest <your-account-id>.dkr.ecr.<your-region>.amazonaws.com/learning/springboot-app:latest
  ```
- Push the image to ECR:
  ```sh
  docker push <your-account-id>.dkr.ecr.<your-region>.amazonaws.com/learning/springboot-app:latest
  ```

### 3. Create EKS Cluster
- Create a new EKS cluster (ensure your IAM user has the necessary EKS and EC2 permissions):
  ```sh
  eksctl create cluster --name my-cluster --region ap-southeast-2 --nodes 1
  ```

### 4. Configure kubectl for EKS
- Update your kubeconfig to use the new cluster:
  ```sh
  aws eks --region ap-southeast-2 update-kubeconfig --name my-cluster
  ```
- Verify nodes:
  ```sh
  kubectl get nodes
  ```

### 5. Create Kubernetes Secrets
- Store sensitive data (e.g., JWT secret, OAuth credentials) as Kubernetes secrets:
  ```sh
  kubectl create secret generic springboot-secrets \
    --from-literal=JWT_SECRET=<your_jwt_secret> \
    --from-literal=GOOGLE_CLIENT_ID=<your_google_client_id> \
    --from-literal=GOOGLE_CLIENT_SECRET=<your_google_client_secret>
  ```
- Verify the secret:
  ```sh
  kubectl get secret springboot-secrets -o yaml
  ```

### 6. Deploy to EKS
- Apply your deployment and service manifests:
  ```sh
  kubectl apply -f overlays/prod/deployment.yml
  kubectl apply -f overlays/prod/service.yml
  ```
- Check pods and services:
  ```sh
  kubectl get pods
  kubectl get svc springboot-app-service
  ```
- (Optional) Describe a pod for troubleshooting:
  ```sh
  kubectl describe pod <pod-name>
  ```

### 7. Cleanup
- To delete the EKS cluster and all resources:
  ```sh
  eksctl delete cluster --name my-cluster --region ap-southeast-2
  ```

## Notes
- This project is for interview/demo purposes and may use public APIs or mock data.
- For production use, review and update security, error handling, and configuration as needed.

## License
This project is provided for educational and interview purposes.
