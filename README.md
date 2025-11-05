## E-Commerce Microservices Platform

# Introduction

A modern e-commerce microservices platform built with Spring Boot 3, demonstrating enterprise-grade architecture patterns including:

- **Microservices Architecture**: Independent, scalable services for products and orders
- **API Gateway**: Spring Cloud Gateway for routing and load balancing
- **Service Discovery**: Netflix Eureka for service registration and discovery
- **Authentication & Authorization**: Keycloak OAuth2/JWT integration
- **Circuit Breaker**: Resilience4j for fault tolerance and resilience
- **Centralized Configuration**: Spring Cloud Config Server for environment-specific settings
- **Inter-Service Communication**: RESTful APIs with JWT token forwarding
- **Containerization**: Docker and Docker Compose for deployment
- **Database Per Service**: PostgreSQL databases for data isolation
- **Profile-Based Configuration**: Development and production environment support

## Architecture Overview

The system consists of the following services:
1. **API Gateway** (Port 8072) - Routes requests and handles load balancing
2. **Eureka Server** (Port 8070) - Service discovery and registration
3. **Keycloak** (Port 8080) - Authentication and authorization server
4. **Config Server** (Port 8888) - Centralized configuration management
5. **Product Service** (Port 8081) - Manages product catalog and inventory
6. **Order Service** (Port 8082) - Handles order processing with circuit breaker resilience

## Technology Stack

- **Framework**: Spring Boot 3.5.6, Spring Cloud 2024.0.0
- **API Gateway**: Spring Cloud Gateway
- **Service Discovery**: Netflix Eureka
- **Authentication**: Keycloak OAuth2/OpenID Connect
- **Circuit Breaker**: Resilience4j
- **Database**: PostgreSQL 16
- **Build Tool**: Maven 3.x
- **Containerization**: Docker & Docker Compose
- **Java Version**: 17+

## Key Features

### Authentication & Security
- **Keycloak Integration**: OAuth2/JWT authentication with role-based access control
- **JWT Token Forwarding**: Seamless token propagation between microservices
- **Role-Based Authorization**: ADMIN and USER roles with different permissions

### Resilience & Fault Tolerance
- **Circuit Breaker**: Resilience4j implementation in Order Service for Product Service calls
- **Fallback Mechanisms**: Graceful degradation when services are unavailable
- **Timeout Handling**: Configurable timeouts for service-to-service communication

### Service Discovery & Routing
- **Eureka Service Registry**: Automatic service registration and discovery
- **API Gateway**: Centralized routing with load balancing capabilities
- **Health Checks**: Built-in health monitoring for all services

## API Testing

📋 **[Download Postman Collection](./ProjectPhase1Collection.postman_collection.json)**

Import this collection into Postman to test all API endpoints.

## Initial Configuration
1.	Apache Maven (http://maven.apache.org)
2.	Git Client (http://git-scm.com)
3.  Docker(https://www.docker.com/products/docker-desktop)

## How to Deploy and Run System

To clone and run this application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/), [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), and [Docker](https://www.docker.com/products/docker-desktop). From your command line:

```bash
# Clone this repository
$ git clone https://github.com/slasherdrake/ecommerce-microservices

# Build all services
# Starting in the root repository
$ cd config-server
$ mvn clean package -DskipTests

$ cd ../eureka-server
$ mvn clean package -DskipTests

$ cd ../gateway-server
$ mvn clean package -DskipTests

$ cd ../product-service
$ mvn clean package -DskipTests

$ cd ../order-service
$ mvn clean package -DskipTests

# Build and start all services with Docker Compose
# Starting in the root repository
# This will run the dev profile
$ cd docker
$ docker compose up --build

# KEYCLOAK instructions
# When the keycloak service is booted up, go to http://localhost:8080/auth
# Login with admin/admin
# Create new realm called drake-realm and paste import the realm-export.json from the docker folder

# For production profile
$ export SPRING_PROFILES_ACTIVE=prod
$ docker-compose -f docker/docker-compose.yml up --build

# To stop all containers
$ docker-compose -f docker/docker-compose.yml down
```

## Service URLs

- **API Gateway**: http://localhost:8072
- **Eureka Dashboard**: http://localhost:8070
- **Keycloak Admin**: http://localhost:8080/auth/admin (admin/admin)
- **Config Server**: http://localhost:8888
- **Product Service**: http://localhost:8081 (via Gateway: http://localhost:8072/product-service)
- **Order Service**: http://localhost:8082 (via Gateway: http://localhost:8072/order-service)

## Authentication

### Keycloak Setup
1. Access Keycloak at http://localhost:8080/auth
2. Login with admin/admin
3. Create users in the `drake-realm` realm
4. Assign roles: `ADMIN` or `USER`
5. Use the generated JWT tokens for API requests

### API Authentication
All API requests require a valid JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

