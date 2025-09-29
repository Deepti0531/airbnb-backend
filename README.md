# Airbnb Backend (Spring Boot)

A production-ready REST API for an Airbnb-style booking platform built with Spring Boot, Spring Security (JWT), JPA/Hibernate, and PostgreSQL.

## Features

- Authentication with JWT access tokens and HttpOnly refresh cookie flow (login, refresh).
- Role-based authorization with roles: GUEST and HOTEL_MANAGER.
- Hotel management for managers: create, update, activate, list, delete under `/admin/hotels`.
- Room management under a hotel, plus inventory (availability/pricing) management.
- Public browsing: search hotels and view hotel info.
- Bookings: initialize, add guests, initiate payment, check status, cancel.
- Razorpay webhook to capture payments with signature verification.

## Tech Stack

- Java 17+ and Spring Boot 3.x
- Spring Security 6 (stateless, JWT)
- Spring Data JPA (Hibernate 6) with PostgreSQL
- ModelMapper for DTO mapping
- OpenAPI/Swagger via springdoc (dev)
- Razorpay Java SDK for payment integration

## Getting Started

### Prerequisites
- Java 17+ and Maven
- PostgreSQL running locally (default DB: `AirBnb`, user: `postgres`, password: `Tiger`)

### Configuration
Configure `src/main/resources/application.properties`:
spring.application.name=airBnbApp

DB
spring.datasource.url=jdbc:postgresql://localhost:5432/AirBnb
spring.datasource.username=postgres
spring.datasource.password=Tiger

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8081
server.servlet.context-path=/api/v1

JWT
jwt.secretKey=change-me

Razorpay
razorpay.key.id=change-me
razorpay.key.secret=change-me

### Run
mvn spring-boot:run

or run AirBnbAppApplication from your IDE

(Optional) Swagger UI in dev:
/api/v1/swagger-ui/index.html

## Security and Roles

- Roles: `GUEST`, `HOTEL_MANAGER`
- Access rules:
  - `/api/v1/auth/**`, `/api/v1/hotels/**`, `/api/v1/webhook/payment` → permit all
  - `/api/v1/admin/**` → requires role `HOTEL_MANAGER`
  - `/api/v1/users/**`, `/api/v1/bookings/**` → authenticated
- JWT from `Authorization: Bearer <token>`; authorities normalized to `ROLE_*` for `hasRole(...)` checks.

## API Overview

### Auth
- `POST /auth/signup` → create account (default role: GUEST)
- `POST /auth/login` → returns `accessToken` and sets `refreshToken` cookie
- `POST /auth/refresh` → issues a new `accessToken` using HttpOnly `refreshToken`

### Public Browse
- `POST /hotels/search` → search hotels (paged)
- `GET /hotels/{hotelId}/info` → hotel details

### Manager (Admin) APIs
- Hotels
  - `POST /admin/hotels` → create hotel
  - `GET /admin/hotels` → list all
  - `GET /admin/hotels/{hotelId}` → details
  - `PUT /admin/hotels/{hotelId}` → update
  - `PATCH /admin/hotels/{hotelId}/activate` → activate
  - `DELETE /admin/hotels/{hotelId}` → delete
- Rooms
  - `POST /admin/hotels/{hotelId}/rooms` → create room
  - `GET /admin/hotels/{hotelId}/rooms` → list rooms
  - `GET /admin/hotels/{hotelId}/rooms/{roomId}` → room details
  - `PUT /admin/hotels/{hotelId}/rooms/{roomId}` → update room
  - `DELETE /admin/hotels/{hotelId}/rooms/{roomId}` → delete room
- Inventory
  - `GET /admin/inventory/rooms/{roomId}` → list availability/prices
  - `PATCH /admin/inventory/rooms/{roomId}` → bulk update inventory

### User and Booking
- User
  - `GET /users/profile`
  - `PATCH /users/profile`
  - `GET /users/myBookings`
  - Guests: `GET|POST|PUT|DELETE /users/guests`
- Booking
  - `POST /bookings/init`
  - `POST /bookings/{bookingId}/addGuests`
  - `POST /bookings/{bookingId}/payments`
  - `GET /bookings/{bookingId}/status`
  - `POST /bookings/{bookingId}/cancel`

### Webhook
- `POST /webhook/payment` → validates Razorpay signature and captures payment, then updates booking state

## Local Development Tips

- Seed a manager account (HOTEL_MANAGER) for admin APIs:
  - Temporary dev endpoint idea: `POST /api/v1/dev/promote?email=<email>` to add `HOTEL_MANAGER` to an existing user (remove for production).
- Postman:
  - Set `Content-Type: application/json` on all `@RequestBody` endpoints.
  - Store `accessToken` and send `Authorization: Bearer {{accessToken}}`.
  - Keep cookies enabled for `refreshToken`.
- Logs:
  - `spring.jpa.show-sql=true` prints SQL; successful creates show `INSERT` statements.

## Project Structure

src/main/java/com/projects/airBnbApp
├─ controller/ # REST endpoints (auth, hotels, rooms, inventory, users, bookings, webhook)
├─ securities/ # Security config, JWT filter/service, auth service
├─ service/ # Business logic (users, bookings, hotels, rooms, inventory)
├─ repository/ # Spring Data JPA repositories
├─ dto/ # Request/response DTOs (ModelMapper)
└─ entity/ # JPA entities

## Environment Variables

- `jwt.secretKey` — JWT signing secret
- `razorpay.key.id`, `razorpay.key.secret` — Razorpay credentials
- `spring.datasource.*` — database connection details

## Roadmap

- Add Jakarta Bean Validation to DTOs
- Testcontainers-based integration tests for auth/admin/booking/webhook flows
- Pagination and filtering enhancements for manager listings
- Production webhook hardening (retries, IP allowlist)

## License

MIT




