Airbnb Backend (Spring Boot)
A production-ready REST API for an Airbnb-style booking platform built with Spring Boot, Spring Security (JWT), JPA/Hibernate, and PostgreSQL.

Features
Authentication with JWT access tokens and HttpOnly refresh cookie flow (login, refresh).

Role-based authorization with roles: GUEST, HOTEL_MANAGER.

Hotel management for managers: create, update, activate, list, delete under /admin/hotels.

Room management for managers under a hotel, plus inventory (availability/pricing) management.

Public browsing: search hotels and view hotel info.

Bookings: initialize, add guests, initiate payment, check status, cancel.

Razorpay webhook to capture payments with signature verification.

Tech Stack
Java 17+ and Spring Boot 3.x.

Spring Security 6 (stateless, JWT).

Spring Data JPA (Hibernate 6) with PostgreSQL.

ModelMapper for DTO mapping.

OpenAPI/Swagger via springdoc (dev).

Razorpay Java SDK for payment integration.

Getting Started
Prerequisites
Java 17+ and Maven.

PostgreSQL running locally (default DB: AirBnb, user: postgres, password: Tiger).

Configuration
Set application properties (src/main/resources/application.properties) :

spring.datasource.url=jdbc:postgresql://localhost:5432/AirBnb

spring.datasource.username=postgres

spring.datasource.password=Tiger

server.port=8081

server.servlet.context-path=/api/v1

jwt.secretKey=<your-secret>

razorpay.key.id=<key-id>

razorpay.key.secret=<key-secret>.

Run
mvn spring-boot:run or run AirBnbAppApplication from IDE.

Swagger UI (if enabled): /api/v1/swagger-ui/index.html.

Security and Roles
Roles: GUEST and HOTEL_MANAGER.

Access rules (via WebSecurityConfig):

/api/v1/auth/** and /api/v1/hotels/** and /api/v1/webhook/payment → permitAll.

/api/v1/admin/** → requires role HOTEL_MANAGER.

/api/v1/users/** and /api/v1/bookings/** → authenticated.

JWT is read from Authorization: Bearer <token>, and authorities are normalized to ROLE_* in JWTAuthFilter.

API Overview
Auth
POST /auth/signup → create account (default role: GUEST).

POST /auth/login → returns accessToken and sets refreshToken cookie.

POST /auth/refresh → issues a new accessToken based on HttpOnly refresh cookie.

Public Browse
POST /hotels/search → search by criteria, returns paged results.

GET /hotels/{hotelId}/info → hotel details and available info.

Manager (Admin) APIs
Hotels

POST /admin/hotels → create hotel.

GET /admin/hotels → list all.

GET /admin/hotels/{hotelId} → details.

PUT /admin/hotels/{hotelId} → update.

PATCH /admin/hotels/{hotelId}/activate → activate.

DELETE /admin/hotels/{hotelId} → delete.

Rooms

POST /admin/hotels/{hotelId}/rooms → create room.

GET /admin/hotels/{hotelId}/rooms → list rooms.

GET /admin/hotels/{hotelId}/rooms/{roomId} → room details.

PUT /admin/hotels/{hotelId}/rooms/{roomId} → update room.

DELETE /admin/hotels/{hotelId}/rooms/{roomId} → delete room.

Inventory

GET /admin/inventory/rooms/{roomId} → list availability/prices.

PATCH /admin/inventory/rooms/{roomId} → bulk update inventory.

User and Booking
User

GET /users/profile → current user profile.

PATCH /users/profile → update profile.

GET /users/myBookings → list user bookings.

Guests: GET/POST/PUT/DELETE /users/guests.

Booking

POST /bookings/init → create booking.

POST /bookings/{bookingId}/addGuests → attach guests.

POST /bookings/{bookingId}/payments → initiate payment.

GET /bookings/{bookingId}/status → check status.

POST /bookings/{bookingId}/cancel → cancel booking.

Webhook
POST /webhook/payment → validates Razorpay signature and captures payment; updates booking state accordingly.

Local Development Tips
Seed a manager account (HOTEL_MANAGER) for admin APIs:

Temporary dev endpoint: POST /api/v1/dev/promote?email=<email> to add HOTEL_MANAGER to an existing user; remove in production.

Use Postman:

Set Content-Type: application/json on all @RequestBody endpoints.

Store accessToken in a collection variable and send Authorization: Bearer {{accessToken}}.

Ensure cookies are enabled for refreshToken flow.

Logs:

spring.jpa.show-sql=true prints SQL; successful creates show INSERTs.

Project Structure
controller: REST controllers for auth, hotels, rooms, inventory, users, bookings, webhook.

securities: WebSecurityConfig, JWTAuthFilter, JWTService, AuthService.

service: Business logic for users, bookings, hotels, rooms, inventory.

repository: Spring Data JPA repositories.

dto: Request/response DTOs mapped via ModelMapper.

Environment Variables
JWT secret: jwt.secretKey

Razorpay credentials: razorpay.key.id, razorpay.key.secret

Database credentials in spring.datasource.*.

Roadmap
Add pagination to manager listings where applicable.

Add validation (Jakarta Bean Validation provider) to DTOs and request bodies.

Add test containers and integration tests for flows (auth, admin, booking, webhook).

Harden webhook security for production (IP allowlist, retries).

License
MIT
