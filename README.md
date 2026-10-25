# CustomerApp – Customer Profile REST API

## Overview
CustomerApp is a Spring Boot REST API that manages customer registration, login, and profile retrieval. It integrates with PlanApp and FriendApp through REST APIs to retrieve plan details and friend contact information.

## Technologies Used
- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- MySQL
- RestTemplate
- Resilience4j Circuit Breaker
- Maven
- Lombok
- Postman

## Features
- Register new customers.
- Authenticate customers using their phone number and password.
- Retrieve customer profiles using a phone number.
- Fetch plan details from PlanApp.
- Retrieve friend contact information from FriendApp.
- Handle profile-service failures using Resilience4j circuit breaker and fallback logic.
- Persist customer information in MySQL.

## Project Structure
```text
CustomerApp/
├── src/main/java/com/example/customer/
│   ├── api/
│   │   └── CustomerRestController.java
│   ├── entity/
│   │   ├── CustomerEntity.java
│   │   └── PlanEntity.java
│   ├── model/
│   │   ├── CustomerResponse.java
│   │   └── LoginRequest.java
│   ├── repository/
│   │   └── CustomerRepository.java
│   ├── service/
│   │   └── CustomerService.java
│   └── CustomerAppApplication.java
├── src/main/resources/
│   └── application.yaml
└── pom.xml
```

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/customer/register` | Register a customer |
| POST | `/customer/login` | Authenticate a customer |
| GET | `/customer/profile/{phoneNumber}` | Retrieve the customer's profile |

### 1. Register Customer

**Request**
```http
POST http://localhost:8083/customer/register
Content-Type: application/json
```

**Request Body**
```json
{
  "phoneNumber": 9876543210,
  "userName": "Shayan",
  "password": "yourPassword",
  "email": "shayan@example.com",
  "planId": "101"
}
```

Returns `true` if the customer is registered successfully and `false` if the phone number already exists.

### 2. Customer Login

**Request**
```http
POST http://localhost:8083/customer/login
Content-Type: application/json
```

**Request Body**
```json
{
  "phoneNumber": 9876543210,
  "password": "yourPassword"
}
```

Returns `true` when the supplied credentials match a database record; otherwise, returns `false`.

### 3. Retrieve Customer Profile

**Request**
```http
GET http://localhost:8083/customer/profile/9876543210
```

The response combines customer information with plan details and friend contact information retrieved from the other applications.

## Microservice Integration

CustomerApp communicates directly with the following services using `RestTemplate`:

| Service | URL | Purpose |
|---|---|---|
| PlanApp | `http://localhost:8081/plans/{id}` | Retrieve plan details |
| FriendApp | `http://localhost:8082/friend/{phoneNumber}` | Retrieve friend contacts |

All three applications must be running on their configured ports for complete profile information.

## Circuit Breaker
Resilience4j's `@CircuitBreaker` annotation is applied to the profile endpoint. A fallback method is provided to handle failures during profile retrieval. The fallback currently calls PlanApp again, so a PlanApp failure may also affect the fallback response.

## Database Configuration
CustomerApp uses MySQL with the `customer_schema` database and the `CUSTOMER` table.

Configure your database connection in `src/main/resources/application.yaml`. Keep database credentials out of public repositories.

## How to Run

1. Install Java 21 and MySQL.
2. Create the `customer_schema` database.
3. Configure the MySQL connection in `application.yaml`.
4. Start PlanApp on port `8081`.
5. Start FriendApp on port `8082`.
6. Start CustomerApp on port `8083`.

Run CustomerApp using Maven:

```bash
mvn spring-boot:run
```

On Windows, you can also use:

```bash
mvnw.cmd spring-boot:run
```

## Architecture
- **Controller:** Exposes REST endpoints and coordinates profile retrieval.
- **Service:** Handles customer registration, login, and lookup logic.
- **Repository:** Uses Spring Data JPA to access customer records.
- **Entity:** Maps customer data to the database.
- **RestTemplate:** Communicates with PlanApp and FriendApp.
- **Resilience4j:** Provides circuit breaker and fallback handling.

## Author
Shayan Arshi
