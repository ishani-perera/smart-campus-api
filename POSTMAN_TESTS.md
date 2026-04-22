# Postman Demo Checklist

Use these requests in your video demo.

1. GET `/api/v1`
2. GET `/api/v1/rooms`
3. POST `/api/v1/rooms`
4. GET `/api/v1/rooms/{roomId}`
5. DELETE empty room → `204`
6. DELETE `LIB-301` → `409`
7. POST invalid sensor roomId → `422`
8. POST valid sensor → `201`
9. GET `/api/v1/sensors?type=CO2`
10. GET `/api/v1/sensors/{sensorId}/readings`
11. POST reading → `201`
12. GET sensor again and show `currentValue` updated
13. POST reading to `OCC-001` → `403`
14. POST wrong content type → `415`
15. GET invalid route → `404`
