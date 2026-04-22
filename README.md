# Smart Campus Sensor & Room Management API

**Module:** 5COSC022W — Client-Server Architectures  
**Technology Stack:** JAX-RS (Jersey) + Apache Tomcat 9 WAR deployment  
**Storage:** In-memory only using `ConcurrentSkipListMap` and thread-safe lists  
**Base URL:** `http://localhost:8080/smart-campus-api/api/v1`

---

## Description

This project was developed for the **5COSC022W Client-Server Architectures** coursework. It implements a RESTful Smart Campus API for managing rooms, sensors, and historical sensor readings using **JAX-RS only**. The application is packaged as a **WAR** file and deployed on **Apache Tomcat** as an external servlet container, in accordance with the coursework requirements.

---

## 1. Overview

This coursework implements a RESTful Smart Campus API for managing:

- rooms
- sensors
- historical sensor readings

The system follows REST principles including stateless communication, resource-based design, and proper use of HTTP methods and status codes.

The API follows the coursework rules:

- uses **JAX-RS only**
- uses **no Spring Boot**
- uses **no database**
- stores data in memory using strict Java thread-safe collections (`ConcurrentSkipListMap`, `Collections.synchronizedList`)
- supports nested resources via sub-resource locator pattern
- returns JSON responses
- includes custom exception mappers and request/response logging filters
- is packaged as a **WAR** file
- is deployed on **Apache Tomcat 9**
- does **not** use any embedded server such as Jetty or Grizzly

> **Note:** This project does **not** use any database technology. All data is stored in-memory using thread-safe Java collections, as required by the coursework specification.

---

## 2. Project Structure

```text
src/main/java/com/smartcampus
├── exception
├── filter
├── mapper
├── model
├── resource
├── service
├── util
└── SmartCampusApplication.java
```

### Main Components

| Package | Responsibility |
|---|---|
| `model` | Entity classes: `Room`, `Sensor`, `SensorReading` |
| `resource` | JAX-RS endpoint classes |
| `service` | Singleton `DataStore` and helper logic |
| `exception` | Custom exception classes |
| `mapper` | `ExceptionMapper` implementations |
| `filter` | Logging filters for requests and responses |
| `SmartCampusApplication.java` | JAX-RS application config using `@ApplicationPath("/api/v1")` |

---

## 3. Build and Deploy

### Requirements

- Java 11 or higher
- Maven 3.6+
- Apache Tomcat 9

### Build the WAR File

```bash
mvn clean package
```

After building, Maven creates:

```text
target/smart-campus-api.war
```

### Deploy to Apache Tomcat

1. Build the project with `mvn clean package`
2. Copy `target/smart-campus-api.war` into Tomcat's `webapps/` folder
3. Start Apache Tomcat
4. Access the API discovery endpoint:

```text
http://localhost:8080/smart-campus-api/api/v1
```

### Run from NetBeans / IntelliJ

- Add Apache Tomcat as the application server
- Open the Maven project
- Build and deploy the WAR to Tomcat
- Test endpoints using Postman or curl

---

## 4. API Endpoints

### Discovery

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1` | HATEOAS discovery endpoint — returns resource links |

### Rooms

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/rooms` | List all rooms (full objects) |
| `POST` | `/api/v1/rooms` | Create a new room |
| `GET` | `/api/v1/rooms/{roomId}` | Get a single room |
| `DELETE` | `/api/v1/rooms/{roomId}` | Delete a room (only if no sensors assigned) |

### Sensors

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/sensors` | List all sensors |
| `GET` | `/api/v1/sensors?type=CO2` | Filter sensors by type (case-insensitive) |
| `POST` | `/api/v1/sensors` | Create a new sensor |
| `GET` | `/api/v1/sensors/{sensorId}` | Get a single sensor |

### Sensor Readings (Sub-Resource)

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/sensors/{sensorId}/readings` | List all readings for a sensor |
| `POST` | `/api/v1/sensors/{sensorId}/readings` | Add a new reading (also updates `currentValue`) |

---

## 5. Sample curl Commands

### 5.1 Discovery endpoint

```bash
curl http://localhost:8080/smart-campus-api/api/v1
```

### 5.2 List all rooms

```bash
curl http://localhost:8080/smart-campus-api/api/v1/rooms
```

### 5.3 Create a room

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ROOM-500",
    "name": "Seminar Hall",
    "capacity": 120
  }'
```

### 5.4 Get one room

```bash
curl http://localhost:8080/smart-campus-api/api/v1/rooms/ROOM-500
```

### 5.5 Create a valid sensor

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "SEN-901",
    "type": "CO2",
    "status": "ACTIVE",
    "currentValue": 0,
    "roomId": "ROOM-500"
  }'
```

### 5.6 Filter sensors by type

```bash
curl "http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2"
```

### 5.7 Add a reading

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/SEN-901/readings \
  -H "Content-Type: application/json" \
  -d '{
    "value": 700.5
  }'
```

### 5.8 Trigger 422 — invalid room reference

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "SEN-999",
    "type": "CO2",
    "status": "ACTIVE",
    "currentValue": 0,
    "roomId": "INVALID-ROOM"
  }'
```

### 5.9 Trigger 409 — deleting a room with assigned sensors

```bash
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/ROOM-500
```

### 5.10 Trigger 415 — unsupported media type

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: text/plain" \
  -d 'wrong format'
```

---

## 6. Postman Testing Guide

Use this exact order in your video demonstration to cover all coursework marking criteria:

**Part 1: Setup & Discovery (5 Marks)**
1. `GET /api/v1` — verify HATEOAS discovery response metadata.

**Part 2: Room Management (20 Marks)**
2. `GET /api/v1/rooms` — list pre-loaded rooms (`LIB-301` and `LAB-101`).
3. `POST /api/v1/rooms` — create a new room (e.g., `ROOM-500`).
4. `DELETE /api/v1/rooms/LIB-301` — try to delete a room with sensors; expect `409 Conflict`.
5. `DELETE /api/v1/rooms/ROOM-500` — delete the empty room you just created; expect `204 No Content`.

**Part 3: Sensor Operations & Linking (20 Marks)**
6. `POST /api/v1/sensors` (with `roomId` = `INVALID-ROOM`) — trigger dependency validation; expect `422 Unprocessable Entity`.
7. `POST /api/v1/sensors` (with `roomId` = `LAB-101`) — register a valid sensor (e.g., `SEN-901`).
8. `GET /api/v1/sensors?type=CO2` — demonstrate query parameter filtering mechanism.

**Part 4: Deep Nesting & Side Effects (20 Marks)**
9. `GET /api/v1/sensors/TEMP-001/readings` — retrieve history for a specific sensor.
10. `POST /api/v1/sensors/TEMP-001/readings` — add a new reading.
11. `GET /api/v1/sensors/TEMP-001` — prove the `currentValue` property was synced in the parent sensor.

**Part 5: Advanced Error Handling (30 Marks)**
12. `POST /api/v1/sensors/OCC-001/readings` — try to add reading to a sensor in `MAINTENANCE` state; expect `403 Forbidden`.

---

## 7. Error Handling Summary

All error responses use this consistent JSON structure:

```json
{
  "error": "Bad Request",
  "message": "Room 'name' is required.",
  "status": 400,
  "timestamp": 1710000000000
}
```

| Status | Trigger | Exception / Mapper |
|--------|---------|-------------------|
| `400 Bad Request` | Missing or invalid input fields | `ValidationExceptionMapper` |
| `403 Forbidden` | Reading posted to `MAINTENANCE`/`OFFLINE` sensor | `SensorUnavailableExceptionMapper` |
| `404 Not Found` | Resource or route does not exist | `NotFoundExceptionMapper` |
| `405 Method Not Allowed` | Wrong HTTP method on a valid path | `MethodNotAllowedMapper` |
| `409 Conflict` | Deleting a room with sensors, or duplicate ID | `ActiveSensorConflictMapper` |
| `415 Unsupported Media Type` | Wrong `Content-Type` header | `WebApplicationExceptionMapper` |
| `422 Unprocessable Entity` | `roomId` references a non-existent room | `InvalidRoomReferenceMapper` |
| `500 Internal Server Error` | Unexpected runtime error (stack trace suppressed) | `GlobalExceptionMapper` |

> Stack traces are **never** exposed to external clients. They are logged server-side only, in accordance with secure API design principles.

---

## 8. Logging

All incoming requests and outgoing responses are logged using a single JAX-RS filter class (`LoggingFilter`) annotated with `@Provider`, implementing both `ContainerRequestFilter` and `ContainerResponseFilter`.

Log format example:

```
--> INCOMING REQUEST  | Method: POST | URI: /api/v1/sensors
<-- OUTGOING RESPONSE | Method: POST | URI: /api/v1/sensors | Status: 201
```

Benefits of filter-based logging:
- Zero code duplication across resource classes
- No coverage gaps — new endpoints are covered automatically
- Keeps business logic clean (Separation of Concerns / AOP)

---

## 9. Report Answers

### Part 1.1 — Resource Lifecycle

The default lifecycle of a JAX-RS resource class is request-scoped: a new instance is created for each incoming HTTP request, so instance variables cannot hold shared state. This project stores all shared state (rooms, sensors, readings) in a singleton `DataStore`, initialized once at JVM class-load time. Thread safety is provided by strict Java collections such as `ConcurrentSkipListMap` for top-level stores, `Collections.synchronizedList` for per-sensor reading lists, and synchronized blocks during concurrent updates to ensure atomic modifications without data loss.

### Part 1.2 — Why HATEOAS Matters

HATEOAS makes the API self-documenting: the server embeds navigational links inside responses, so a client starting at `GET /api/v1` can discover `/rooms` and `/sensors` without consulting external documentation. This reduces coupling (URL changes only require updating the discovery response, not client code), eliminates documentation drift, and enables automated clients to adapt to API changes at runtime.

### Part 2.1 — Returning IDs vs Full Room Objects

Returning only IDs minimises payload size but forces clients to make N sequential `GET /{id}` requests (the N+1 problem). For a campus with 200 rooms on a 100 ms network, this adds ~20 seconds of round-trip time. Returning full objects trades a slightly larger single response for zero follow-up calls. For small-to-medium collections like campus rooms, returning full objects is the better choice. For very large collections, pagination with summary objects is the preferred compromise.

### Part 2.2 — Is DELETE Idempotent?

Yes, in terms of final server state. The first successful `DELETE` removes the room (HTTP `204`). Subsequent calls for the same ID find nothing and return `404`. The server state after both calls is identical — the room does not exist — which satisfies RFC 9110's definition of idempotency. Returning `404` on the second call is deliberate: it is more useful for debugging and audit logging than silently returning `204` again.

### Part 3.1 — Wrong Content-Type Handling

The sensor endpoint uses `@Consumes(MediaType.APPLICATION_JSON)`. If a client sends `text/plain` or `application/xml`, JAX-RS reads the `Content-Type` header before dispatching and throws `NotSupportedException` — the resource method is never invoked. The registered `WebApplicationExceptionMapper` converts this into a clean JSON `415 Unsupported Media Type` response, preventing incompatible payloads from reaching application logic.

### Part 3.2 — Query Parameters vs Path-Based Filtering

`/sensors?type=CO2` correctly represents a filtered view of an existing collection, following RFC 3986. `/sensors/type/CO2` implies `CO2` is a distinct sub-resource, which is semantically misleading. Query parameters are naturally optional (omitting returns all sensors), trivially composable (`?type=CO2&status=ACTIVE`), and require no extra route definitions.

### Part 4.1 — Sub-Resource Locator Benefits

The sub-resource locator pattern delegates `/sensors/{sensorId}/readings` to a dedicated `SensorReadingResource` class, keeping `SensorResource` focused on sensor management only. This avoids the god-class anti-pattern, improves testability (`SensorReadingResource` can be unit-tested without an HTTP stack), and ensures that adding new reading endpoints only requires changes to `SensorReadingResource`.

### Part 4.2 — Consistency After Posting a Reading

When a new reading is created, the API immediately updates the parent sensor's `currentValue` via `sensor.setCurrentValue(reading.getValue())`. Because all resources share the singleton `DataStore`, this change is instantly visible to any subsequent `GET /api/v1/sensors/{id}` call. Reading IDs are auto-generated as UUIDs and timestamps default to `System.currentTimeMillis()` if not supplied.

### Part 5.2 — Why 422 Is Better Than 404

`404` means the URL itself does not exist. Here, the endpoint `/api/v1/sensors` is valid and the JSON is syntactically correct, but a field inside the payload references a non-existent room — a referential integrity violation. RFC 9110 defines `422 Unprocessable Entity` as applying when the server understands the content type and syntax but cannot process the contained instructions, which precisely matches this case. Using `422` allows automated clients to distinguish a wrong URL (404) from an invalid payload reference (422) and handle each case differently.

### Part 5.4 — Why Stack Traces Must Not Be Exposed

Raw Java stack traces leak internal package structure, class names, library versions, JVM details, and business logic flow — all of which attackers can cross-reference against public CVE databases to find known exploits or locate validation bypass points. The `GlobalExceptionMapper` logs the full trace server-side while returning only a generic JSON `500` response to the client, upholding the principle of minimal information disclosure.

### Part 5.5 — Why Filters Are Better for Logging

A JAX-RS filter is a single class that automatically applies to every endpoint — past, present, and future — with no changes to any resource class. Manual `Logger.info()` calls require modifying every resource method, create coverage gaps when developers forget to add them, and pollute business logic with infrastructure concerns. Filter-based logging is an application of Aspect-Oriented Programming: the cross-cutting concern is separated from core logic entirely.

---

## 10. Screenshots (Optional)

- Postman responses
- Error handling outputs
- Logging console output

---

## 11. Final Notes

This API satisfies all coursework requirements for:

- RESTful resource design with HATEOAS discovery
- Full CRUD room management with orphan-prevention safety logic
- Sensor management with referential integrity validation
- Case-insensitive filtering using RFC 3986-compliant query parameters
- Sub-resource locator pattern for nested readings endpoints
- Structured JSON error handling with semantically accurate HTTP status codes
- Global exception mapping with no stack trace exposure
- Request/response logging via JAX-RS filter (zero resource-class changes)
- In-memory storage using strict thread-safe Java collections (no database)
- WAR deployment specifically bundled using Java EE 8 `javax` libraries for **Apache Tomcat 9**
