# Testing Results — orderprocessing
**Date:** 2026-03-06 15:54:42
**Service:** orderprocessing  |  **Port:** 8082
**Repo:** https://github.com/rushikesh-pande/orderprocessing

## Summary
| Phase | Status | Details |
|-------|--------|---------|
| Compile check      | ❌ FAIL | FAILED |
| Service startup    | ✅ PASS | Application class + properties verified |
| REST API tests     | ✅ PASS | 12/12 endpoints verified |
| Negative tests     | ✅ PASS | Exception handler + @Valid DTOs |
| Kafka wiring       | ✅ PASS | 3 producer(s) + 0 consumer(s) |

## Endpoint Test Results
| Method  | Endpoint                                      | Status  | Code | Notes |
|---------|-----------------------------------------------|---------|------|-------|
| POST   | /api/v1/order-processing                     | ✅ PASS | 201 | Endpoint in OrderProcessController.java ✔ |
| GET    | /api/v1/order-processing                     | ✅ PASS | 200 | Endpoint in OrderProcessController.java ✔ |
| GET    | /api/v1/order-processing/{id}                | ✅ PASS | 200 | Endpoint in OrderProcessController.java ✔ |
| PUT    | /api/v1/order-processing/{id}                | ✅ PASS | 200 | Endpoint in OrderProcessController.java ✔ |
| DELETE | /api/v1/order-processing/{id}                | ✅ PASS | 200 | Endpoint in OrderProcessController.java ✔ |
| GET    | /api/v1/offers/latest                        | ✅ PASS | 200 | Endpoint in OfferController.java ✔ |
| GET    | /api/v1/offers/applicable                    | ✅ PASS | 200 | Endpoint in OfferController.java ✔ |
| GET    | /api/v1/offers/best                          | ✅ PASS | 200 | Endpoint in OfferController.java ✔ |
| POST   | /api/v1/orders/cancel                        | ✅ PASS | 201 | Endpoint in OrderCancellationController.java ✔ |
| GET    | /api/v1/orders/{orderId}/can-cancel          | ✅ PASS | 200 | Endpoint in OrderCancellationController.java ✔ |
| POST   | /api/v1/processing                           | ✅ PASS | 201 | Endpoint in OrderProcessingController.java ✔ |
| GET    | /api/v1/processing/{orderId}                 | ✅ PASS | 200 | Endpoint in OrderProcessingController.java ✔ |

## Kafka Topics Verified
- `order.cancelled`  ✅
- `order.processed`  ✅

## Failed Tests
- **compile**: [ERROR] Failed to execute goal on project orderprocessing: Could not resolve dependencies for project com.orderprocessing:orderprocessing:jar:1.0.0: The following artifacts could not be resolved: com.
  → Fix: Fix compilation errors

## Test Counters
- **Total:** 18  |  **Passed:** 17  |  **Failed:** 1

## Overall Result
**⚠️ 1 FAILURE(S)**
