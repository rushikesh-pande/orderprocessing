# Order Processing Service - Enhanced with Cancellation

Order Processing Service enhanced with order cancellation and return initiation capabilities.

## Features

### Existing Features
- ✅ Process customer orders
- ✅ Offer and promotion management
- ✅ Order status tracking
- ✅ Kafka event publishing

### New Features (Order Cancellation & Returns)
- ✅ Cancel orders before shipping
- ✅ Automatic refund initiation
- ✅ Cancellation eligibility validation
- ✅ Integration with Return Service
- ✅ Kafka event publishing for cancellations

## Technology Stack

- Java 17
- Spring Boot 3.2.2
- Spring Data JPA
- PostgreSQL
- Apache Kafka
- Maven

## New API Endpoints - Order Cancellation

### Cancel Order
```http
POST /api/v1/orders/cancel
Content-Type: application/json

{
  "orderId": "ORD-12345",
  "customerId": "CUST-001",
  "reason": "Changed my mind",
  "additionalNotes": "Found a better price elsewhere"
}
```

**Response:**
```json
{
  "orderId": "ORD-12345",
  "status": "CANCELLED",
  "message": "Order cancelled successfully",
  "cancelledAt": "2026-02-19T10:30:00",
  "refundAmount": 99.99,
  "refundStatus": "INITIATED"
}
```

### Check if Order Can Be Cancelled
```http
GET /api/v1/orders/{orderId}/can-cancel
```

**Response:**
```json
true
```

## Order Status Flow

### Cancellable Statuses:
- ✅ **CREATED** - Order just created
- ✅ **PENDING_PAYMENT** - Awaiting payment
- ✅ **PAYMENT_CONFIRMED** - Payment received
- ✅ **PROCESSING** - Being processed

### Non-Cancellable Statuses:
- ❌ **READY_TO_SHIP** - Being prepared for shipment
- ❌ **SHIPPED** - Already shipped
- ❌ **OUT_FOR_DELIVERY** - Out for delivery
- ❌ **DELIVERED** - Already delivered
- ❌ **CANCELLED** - Already cancelled

### Complete Status List:
1. CREATED
2. PENDING_PAYMENT
3. PAYMENT_CONFIRMED
4. PROCESSING
5. READY_TO_SHIP
6. SHIPPED
7. OUT_FOR_DELIVERY
8. DELIVERED
9. CANCELLED
10. RETURN_REQUESTED
11. RETURNED
12. REFUNDED
13. FAILED

## Kafka Topics

### Published Events

#### Existing Topics:
- `order.processing.offer.applied` - When offer is applied to order
- `order.processing.status.updated` - When order status changes

#### New Topics:
- `order.cancelled` - When order is cancelled

**Event Structure:**
```json
{
  "orderId": "ORD-12345",
  "customerId": "CUST-001",
  "reason": "Changed my mind",
  "refundAmount": 99.99,
  "cancelledAt": 1708337400000,
  "status": "CANCELLED"
}
```

## Business Rules

### Order Cancellation Rules:
1. **Eligibility Check:** Order must be in CREATED, PENDING_PAYMENT, PAYMENT_CONFIRMED, or PROCESSING status
2. **Automatic Refund:** Refund is automatically initiated for paid orders
3. **Event Publishing:** Cancellation event is published to notify other services
4. **Inventory Update:** (via Kafka consumer in Inventory service)
5. **Payment Reversal:** (via Kafka consumer in Payment service)

### Integration Points:

#### With Payment Service:
- Receives `order.cancelled` event
- Processes refund automatically
- Publishes `refund.processed` event

#### With Inventory Service:
- Receives `order.cancelled` event
- Releases reserved inventory
- Updates stock availability

#### With Return Service:
- For delivered orders, customer initiates return
- Return service handles the return process
- Publishes `refund.processed` after inspection

## Error Handling

### Common Errors:

**Order Not Cancellable:**
```http
HTTP 400 Bad Request
{
  "error": "Order cannot be cancelled",
  "message": "Order cannot be cancelled. It may have already been shipped or delivered."
}
```

**Order Not Found:**
```http
HTTP 404 Not Found
{
  "error": "Order not found",
  "message": "No order found with ID: ORD-12345"
}
```

## Configuration

### application.properties

```properties
# Kafka Configuration for Cancellation Events
spring.kafka.producer.properties.order.cancelled.topic=order.cancelled
spring.kafka.producer.properties.order.cancelled.partitions=3
spring.kafka.producer.properties.order.cancelled.replication-factor=2
```

## Setup Instructions

### Prerequisites

- Java 17
- Maven 3.6+
- PostgreSQL 14+
- Apache Kafka 3.0+

### Run Application

```bash
mvn clean install
mvn spring-boot:run
```

The service will start on port 8082.

## Testing Order Cancellation

### Test Scenario 1: Cancel Valid Order

```bash
curl -X POST http://localhost:8082/api/v1/orders/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-12345",
    "customerId": "CUST-001",
    "reason": "Changed my mind",
    "additionalNotes": "Found a better price"
  }'
```

### Test Scenario 2: Check Cancellation Eligibility

```bash
curl http://localhost:8082/api/v1/orders/ORD-12345/can-cancel
```

## Integration with Return Service

For orders that are already delivered or shipped, customers should use the Return Service:

```http
POST http://localhost:8084/api/v1/returns/initiate
```

The Return Service handles:
- Return request processing
- Shipping label generation
- Product inspection
- Refund processing

## Monitoring

### Key Metrics to Monitor:

1. **Cancellation Rate:** Track % of orders cancelled
2. **Cancellation Reasons:** Analyze why orders are cancelled
3. **Refund Processing Time:** Monitor refund speed
4. **Kafka Event Lag:** Ensure events are processed timely

### Logs:

```bash
# View cancellation logs
tail -f logs/orderprocessing.log | grep "cancellation"

# View Kafka event logs
tail -f logs/orderprocessing.log | grep "order.cancelled"
```

## Future Enhancements

1. **Partial Cancellation:** Cancel specific items from order
2. **Cancellation Fee:** Apply cancellation charges for specific scenarios
3. **Auto-Cancellation:** Cancel unpaid orders after timeout
4. **Cancellation Analytics:** Dashboard showing cancellation trends
5. **Instant Refund:** Integrate with payment gateway for instant refunds

## Related Services

- **CreateOrder:** https://github.com/rushikesh-pande/createorder.git
- **PaymentProcessing:** https://github.com/rushikesh-pande/paymentprocessing.git
- **OrderShipping:** https://github.com/rushikesh-pande/ordershipping.git
- **ReturnService:** https://github.com/rushikesh-pande/returns.git (New)

## Author

Auto-generated by GitHub Copilot Multi-Agent System

## Version

2.0.0 - Enhanced with Order Cancellation & Return Integration

## License

Proprietary

