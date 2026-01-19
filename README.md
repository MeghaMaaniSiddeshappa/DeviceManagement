# Devices API
Devices API is a Spring Boot REST API for persisting and managing device resources. It supports full and partial CRUD operations with domain validations, filtering, and pagination. The application is containerized and uses PostgreSQL for persistence.

## Device Domain Model
| Field          | Type      | Notes                                   |
| -------------- | --------- | --------------------------------------- |
| `id`           | UUID      | Auto-generated                          |
| `name`         | String    | Cannot be updated if `IN_USE`           |
| `brand`        | String    | Cannot be updated if `IN_USE`           |
| `state`        | Enum      | `AVAILABLE`, `IN_USE`, `INACTIVE`       |
| `creationTime` | Timestamp | Auto-set on creation, cannot be updated |


## Features
* Create, fully/partially update, fetch single/all devices
* Get devices by brand or state
* Delete devices (only if not IN_USE)
* Pagination support



##  Business Rules:
* `name` and `brand` cannot be updated if device is `IN_USE`
* Devices in `IN_USE` state cannot be deleted
* `creationTime` is immutable after creation

## API Design Highlights

* RESTful endpoints with proper HTTP status codes
* Pagination support
* Centralized exception handling
* Basic structured logging
* OpenAPI / Swagger documentation
* Enum validation: AVAILABLE, IN_USE, INACTIVE

## Technology Stack

* Java 21 / Spring Boot 3
* Spring Data JPA / Hibernate
* PostgreSQL
* Docker & Docker Compose
* Swagger/OpenAPI for API documentation
* Maven for build management
* JUnit & Mockito for testing

## Getting Started
### Prerequisites
* Docker & Docker Compose installed
* Git installed
* Java 21 installed (for running tests/builds locally)

## Running the Application
### 1. Clone the repository:
```bash 
git clone https://github.com/MeghaMaaniSiddeshappa/DeviceManagement.git
```

### 2. Navigate to the project directory and start the application using Docker Compose:
```
cd DeviceManagement/devices-api
docker-compose up --build
```

* API: http://localhost:8080/api/v1/devices
* Swagger UI: http://localhost:8080/swagger-ui.html
* PostgreSQL: localhost:5432 (user: admin, password: password, db: device_db)
* Tables are auto-created and populated with sample data.(see data.sql)



## API Endpoints

| Method | Endpoint               | Description                                   |
| ------ | ---------------------- | --------------------------------------------- |
| GET    | `/api/v1/devices`      | Fetch all devices (filters: `brand`, `state`) |
| GET    | `/api/v1/devices/{id}` | Fetch a device by ID                          |
| POST   | `/api/v1/devices`      | Create a new device                           |
| PUT    | `/api/v1/devices/{id}` | Full update                                   |
| PATCH  | `/api/v1/devices/{id}` | Partial update                                |
| DELETE | `/api/v1/devices/{id}` | Delete (only if not `IN_USE`)                 |

Pagination: ?page=0&size=10

#### Sample Request

POST /api/v1/devices
{
"name": "OptiCam Pro",
"brand": "OptiCam",
"state": "AVAILABLE"
}

#### Tests

* Unit & integration tests included
* Run tests: mvn test

#### Future Improvements:
* Authentication & Authorization
* Enhanced validation rules and test coverage
* Advanced filtering & sorting
* Caching for improved performance
* Advanced logging & monitoring
* DB normalization and indexing for performance optimization

#### Author: 

Megha Maani Siddeshappa