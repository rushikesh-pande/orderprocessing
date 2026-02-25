# orderprocessing

## Overview
Microservice for: **Process order: - do all necessary action for processing customer order here please give choices of latest offer to customer, publish event in kafka topic**

## Tech Stack
- Java 17
- Spring Boot 3.2.2
- Maven
- Kafka (topic: `order.processed`)

## API Endpoints
| Method | Path | Description |
|--------|------|-------------|
| POST   | /api/v1/order-processing | Create |
| GET    | /api/v1/order-processing | List all |
| GET    | /api/v1/order-processing/{id} | Get by ID |
| PUT    | /api/v1/order-processing/{id} | Update |
| DELETE | /api/v1/order-processing/{id} | Delete |

## Running
```bash
mvn spring-boot:run
```
Service runs on port **8082**

## Kafka
Topic: `order.processed`
Events: `ORDERPROCESS_CREATED`, `ORDERPROCESS_UPDATED`, `ORDERPROCESS_DELETED`
