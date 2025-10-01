## E-Commerce Microservices Platform

# Introduction

A modern e-commerce microservices platform built with Spring Boot 3, demonstrating enterprise-grade architecture patterns including:

- **Microservices Architecture**: Independent, scalable services for products and orders
- **Centralized Configuration**: Spring Cloud Config Server for environment-specific settings
- **Inter-Service Communication**: RESTful APIs with service-to-service calls
- **Containerization**: Docker and Docker Compose for deployment
- **Database Per Service**: PostgreSQL databases for data isolation
- **Profile-Based Configuration**: Development and production environment support

## Architecture Overview

The system consists of three core services:
1. **Product Service** (Port 8081) - Manages product catalog and inventory
2. **Order Service** (Port 8082) - Handles order processing and validation
3. **Config Server** (Port 8888) - Centralized configuration management

## Technology Stack

- **Framework**: Spring Boot 3.5.6, Spring Cloud 2024.0.0
- **Database**: PostgreSQL 16
- **Build Tool**: Maven 3.x
- **Containerization**: Docker & Docker Compose
- **Java Version**: 17+

## API Testing

📋 **[Download Postman Collection](./ProjectPhase1Collection.postman_collection.json)**

Import this collection into Postman to test all API endpoints.

## Bounded context and Data Models
flowchart TB
  subgraph Product_Context["Product Context (product-service)"]
    PAPI[/"Product API"/]
    PDB[("products DB")]
  end

  subgraph Order_Context["Order Context (order-service)"]
    OAPI[/"Order API"/]
    ODB[("orders DB")]
  end

  PAPI <--> OAPI:::rest
  PAPI --- PDB
  OAPI --- ODB

  %% Product Domain Model
  subgraph Product_Domain["Product Domain Model"]
    ProductClass["
    Product
    ───────────────
    id: Long
    name: String
    price: Decimal
    stockQuantity: Int
    category: String
    createdAt: Instant
    updatedAt: Instant
    "]
  end

  %% Order Domain Model
  subgraph Order_Domain["Order Domain Model"]
    OrderClass["
    Order
    ───────────────
    id: Long
    customerId: Long
    status: OrderStatus
    total: Decimal
    createdAt: Instant
    "]
    OrderItemClass["
    OrderItem
    ───────────────
    id: Long
    productId: Long
    quantity: Int
    unitPrice: Decimal
    lineTotal: Decimal
    "]
    OrderStatus["
    «enum» OrderStatus
    ───────────────
    PENDING
    CONFIRMED
    REJECTED
    "]
  end

  %% Relationships
  OrderClass -->|"contains"| OrderItemClass
  OrderClass --- OrderStatus
  OrderItemClass -.->|ref productId| ProductClass

  classDef rest stroke-dasharray: 5 5


## Initial Configuration
1.	Apache Maven (http://maven.apache.org)
2.	Git Client (http://git-scm.com)
3.  Docker(https://www.docker.com/products/docker-desktop)

## How to Deploy and Run System

To clone and run this application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/), [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html). From your command line:

```bash
# Clone this repository
$ git clone https://github.com/slasherdrake/ecommerce-microservices

# Build config server
# Starting in the root repository
$ cd config-sever
$ mvn clean package -DskipTests

# Build product service
# Starting in the root repository
$ cd product-service
$ mvn clean package -DskipTests

# Build order service
# Starting in the root repository
$ cd order-service
$ mvn clean package -DskipTests

# Build service images and start and run containers
# Starting in the root repository
# This will run the dev profile
$ cd docker
$ docker compose up --build

# Build service images and start and run containers
# Starting in the root repository
# This will run the prod profile
$ export SPRING_PROFILES_ACTIVE=prod
docker-compose -f docker/docker-compose.yml up --build

# To stop any running containers 
# (recommended inbetween deployments)
# Starting in the root repository
$ docker-compose -f docker/docker-compose.yml down


```

