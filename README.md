# Order Processing Service

## Overview
Microservice for processing customer orders with inventory checks and validation.

## Features
- Process customer orders
- Inventory availability check
- Order validation
- Processing status tracking
- Complete error handling

## API Endpoints

### Process Order
**POST** `/api/v1/processing`

**🆕 ENHANCEMENTS:**

### Latest Offers (NEW)
**GET** `/api/v1/offers/latest` - Get all latest offers
**GET** `/api/v1/offers/applicable?orderAmount={amount}&category={category}` - Get applicable offers
**GET** `/api/v1/offers/best?orderAmount={amount}&category={category}` - Get best offer recommendation

**Enhancement Features:**
- Flash deals and limited-time offers
- Category-specific promotions
- Order amount-based offer eligibility
- Automatic best offer recommendation
- Real-time offer validation

**Request Body:**
```json
{
  "orderId": "ORD-A1B2C3D4",
  "processingNotes": "Express processing"
}
```

**Response:**
```json
{
  "orderId": "ORD-A1B2C3D4",
  "status": "COMPLETED",
  "inventoryAvailable": true,
  "inventoryCheck": "Inventory available for all items",
  "validationPassed": true,
  "validationResult": "Order validation passed",
  "processingNotes": "Express processing",
  "processedAt": "2026-02-10T15:35:00",
  "message": "Order processed successfully"
}
```

### Get Processing Status
**GET** `/api/v1/processing/{orderId}`

## Technology Stack
- Java 17
- Spring Boot 3.2.2
- PostgreSQL
- Maven

## Running the Service
```bash
mvn clean install
mvn spring-boot:run
```

