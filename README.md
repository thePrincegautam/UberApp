# 🚕 Uber-Style Ride Booking System

A backend **ride-booking and driver-matching system** built using **Java, Spring Boot, Redis, Apache Kafka, MySQL, and Docker**.

This project demonstrates how a real-time ride-hailing backend can be designed using a **microservices architecture**, event-driven communication, Redis geospatial capabilities, and a driver-matching algorithm.

The system handles the complete ride lifecycle — from receiving a ride request and locating nearby drivers to assigning the best driver and completing the ride.

## 🔗 Repository

**GitHub:** [github.com/thePrincegautam/UberApp](https://github.com/thePrincegautam/UberApp?utm_source=chatgpt.com)

---

# 🏗️ Architecture

The application consists of three independent Spring Boot microservices:

| Service              |   Port | Responsibility                                                                       |
| -------------------- | -----: | ------------------------------------------------------------------------------------ |
| **Location Service** | `8082` | Stores and retrieves real-time driver locations using Redis Geospatial               |
| **Ride Service**     | `8083` | Manages riders, rides, ride lifecycle, and publishes ride events                     |
| **Matching Service** | `8084` | Consumes ride events, finds nearby drivers, scores them, and assigns the best driver |

### High-Level Flow

```text
                    ┌──────────────────┐
                    │    Rider App     │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │   Ride Service   │
                    │     :8083        │
                    └────────┬─────────┘
                             │
                       ride.requested
                             │
                             ▼
                    ┌──────────────────┐
                    │      Kafka       │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │ Matching Service │
                    │     :8084        │
                    └────────┬─────────┘
                             │
                    Find Nearby Drivers
                             │
                             ▼
                    ┌──────────────────┐
                    │ Location Service │
                    │     :8082        │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │      Redis       │
                    │  Geospatial Data │
                    └────────┬─────────┘
                             │
                             ▼
                    Driver Scoring
                             │
                             ▼
                       Best Driver
                             │
                             ▼
                    ┌──────────────────┐
                    │      Kafka       │
                    │  ride.matched    │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │   Ride Service   │
                    │ Update Ride      │
                    └──────────────────┘
```

---

# ✨ Key Features

* 🚕 Ride request management
* 📍 Real-time driver location tracking
* 🌍 Redis Geospatial driver search
* ⚡ Kafka-based event-driven communication
* 🎯 Automatic driver matching
* 📊 Driver scoring based on distance and rating
* 🔄 Ride lifecycle/state management
* 🗄️ MySQL persistence
* 🔗 REST-based service-to-service communication
* 🐳 Dockerized infrastructure
* 🧩 Independent Spring Boot microservices

---

# 🛠️ Technology Stack

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Spring Web
* Lombok

### Databases & Messaging

* MySQL
* Redis
* Apache Kafka
* Apache Zookeeper

### Infrastructure

* Docker
* Docker Compose
* Maven

### Architecture

* Microservices
* Event-Driven Architecture
* REST APIs
* Redis Geospatial
* Kafka Producer/Consumer

---

# 📁 Project Structure

```text
UberApp/
│
├── location-service/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── ride-service/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── matching-service/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── docker-compose.yml
│
└── README.md
```

---

# 🚀 Getting Started

## Prerequisites

Make sure the following are installed:

* Java 17+
* Maven 3.8+
* Docker
* Docker Compose
* Git

Verify your installations:

```bash
java -version
mvn -version
docker --version
docker compose version
```

---

# 1️⃣ Clone the Repository

```bash
git clone https://github.com/thePrincegautam/UberApp.git
cd UberApp
```

---

# 2️⃣ Start Infrastructure

The project uses Docker Compose to start:

* Redis
* MySQL
* Kafka
* Zookeeper

Run:

```bash
docker compose up -d
```

Check running containers:

```bash
docker ps
```

> **Note:** Kafka may take a few seconds to become fully available after Docker starts. Wait approximately 20–30 seconds before starting the Spring Boot services.

---

# 3️⃣ Start Location Service

Open a terminal:

```bash
cd location-service
mvn spring-boot:run
```

The service will start on:

```text
http://localhost:8082
```

---

# 4️⃣ Start Ride Service

Open another terminal:

```bash
cd ride-service
mvn spring-boot:run
```

The service will start on:

```text
http://localhost:8083
```

---

# 5️⃣ Start Matching Service

Open another terminal:

```bash
cd matching-service
mvn spring-boot:run
```

The service will start on:

```text
http://localhost:8084
```

---

# 🧪 End-to-End Testing

The following flow can be used to verify the complete application.

## Step 1 — Add Driver Locations

Register a few drivers in the Location Service.

### Driver 1

```http
POST http://localhost:8082/api/v1/locations/drivers/update
```

```json
{
  "driverId": "driver:1",
  "latitude": 12.9716,
  "longitude": 77.5946
}
```

### Driver 2

```http
POST http://localhost:8082/api/v1/locations/drivers/update
```

```json
{
  "driverId": "driver:2",
  "latitude": 12.9800,
  "longitude": 77.5800
}
```

### Driver 3

```http
POST http://localhost:8082/api/v1/locations/drivers/update
```

```json
{
  "driverId": "driver:3",
  "latitude": 12.9600,
  "longitude": 77.6100
}
```

The driver locations are stored in Redis using Redis Geospatial functionality.

---

# 2️⃣ Request a Ride

Send a ride request through the Ride Service:

```http
POST http://localhost:8083/api/v1/rides/request
```

Request body:

```json
{
  "riderId": "rider:1",
  "pickupLatitude": 12.9716,
  "pickupLongitude": 77.5946,
  "pickupAddress": "MG Road, Bangalore",
  "dropLatitude": 12.9352,
  "dropLongitude": 77.6245,
  "dropAddress": "Koramangala, Bangalore"
}
```

The Ride Service creates the ride and publishes:

```text
ride.requested
```

to Kafka.

---

# 3️⃣ Driver Matching

The Matching Service consumes the `ride.requested` event.

The matching process:

```text
Ride Request
     │
     ▼
Kafka: ride.requested
     │
     ▼
Matching Service
     │
     ▼
Find Nearby Drivers
     │
     ▼
Driver Scoring
     │
     ▼
Best Driver Selected
     │
     ▼
Kafka: ride.matched
     │
     ▼
Ride Service
     │
     ▼
Ride Updated
```

The Matching Service communicates with the Location Service to find nearby drivers.

The selected driver is then associated with the ride.

---

# 4️⃣ Check Ride Status

Use the `rideId` returned from the ride request:

```http
GET http://localhost:8083/api/v1/rides/{rideId}
```

Example:

```http
GET http://localhost:8083/api/v1/rides/1
```

After successful matching, the ride should have:

```text
Status: ACCEPTED
Driver: Assigned Driver
```

---

# 5️⃣ Start the Ride

Once the driver has been assigned:

```http
PUT http://localhost:8083/api/v1/rides/{rideId}/start
```

Example:

```http
PUT http://localhost:8083/api/v1/rides/1/start
```

The ride state changes:

```text
ACCEPTED → STARTED
```

---

# 6️⃣ Complete the Ride

After the ride finishes:

```http
PUT http://localhost:8083/api/v1/rides/{rideId}/complete
```

Example:

```http
PUT http://localhost:8083/api/v1/rides/1/complete
```

The ride state changes:

```text
STARTED → COMPLETED
```

---

# 7️⃣ Get Rider History

Retrieve all rides associated with a rider:

```http
GET http://localhost:8083/api/v1/rides/rider/{riderId}
```

Example:

```http
GET http://localhost:8083/api/v1/rides/rider/rider:1
```

This can be used to verify previous and completed rides.

---

# 📍 Redis Geospatial Verification

Open the Redis CLI:

```bash
docker exec -it redis-geo redis-cli
```

### View Stored Drivers

```redis
ZRANGE drivers:locations 0 -1
```

### Check a Driver's Position

```redis
GEOPOS drivers:locations "driver:1"
```

### Calculate Distance Between Drivers

```redis
GEODIST drivers:locations "driver:1" "driver:2" km
```

### Find Nearby Drivers

```redis
GEOSEARCH drivers:locations
FROMMEMBER "driver:1"
BYRADIUS 5 km
```

These commands demonstrate how Redis Geospatial can be used for fast location-based driver discovery.

---

# 📡 Kafka Event Flow

The application uses Kafka to decouple ride creation from driver matching.

### Ride Requested

```text
Ride Service
     │
     ▼
Kafka
     │
ride.requested
     │
     ▼
Matching Service
```

### Ride Matched

```text
Matching Service
     │
     ▼
Kafka
     │
ride.matched
     │
     ▼
Ride Service
```

This allows the services to communicate asynchronously without tightly coupling the ride-request and driver-matching workflows.

---

# 🎯 Driver Matching Algorithm

The Matching Service evaluates nearby drivers using factors such as:

* Distance from pickup location
* Driver rating
* Driver availability

A weighted scoring approach is used to identify the most suitable driver.

Conceptually:

```text
Driver Score =
    Distance Weight
    +
    Rating Weight
    +
    Availability
```

The driver with the best overall score is selected for the ride.

---

# 🔗 Service Communication

The application demonstrates two major communication patterns.

### Synchronous Communication

```text
Matching Service
       │
       │ REST
       ▼
Location Service
```

Used when the Matching Service needs nearby driver information.

### Asynchronous Communication

```text
Ride Service
     │
     │ Kafka
     ▼
Matching Service
```

and:

```text
Matching Service
     │
     │ Kafka
     ▼
Ride Service
```

This demonstrates a practical combination of **REST APIs and event-driven microservice communication**.

---

# 🔄 Ride State Machine

The complete ride lifecycle is:

```text
REQUESTED
    │
    ▼
MATCHING
    │
    ▼
ACCEPTED
    │
    ▼
STARTED
    │
    ▼
COMPLETED
```

| State       | Description                               |
| ----------- | ----------------------------------------- |
| `REQUESTED` | Rider has requested a new ride            |
| `MATCHING`  | System is searching for a suitable driver |
| `ACCEPTED`  | A driver has been assigned                |
| `STARTED`   | Driver has started the ride               |
| `COMPLETED` | Ride has been completed                   |

---

# 🧩 Microservices

## 📍 Location Service

**Port:** `8082`

Responsible for:

* Driver location updates
* Storing driver coordinates
* Redis Geospatial operations
* Finding nearby drivers

```text
Driver
   │
   ▼
Location Service
   │
   ▼
Redis
```

---

## 🚕 Ride Service

**Port:** `8083`

Responsible for:

* Creating rides
* Managing ride lifecycle
* Persisting ride information
* Publishing ride events
* Processing matched-driver events
* Rider ride history

```text
Rider
  │
  ▼
Ride Service
  │
  ├──► MySQL
  │
  └──► Kafka
```

---

## 🎯 Matching Service

**Port:** `8084`

Responsible for:

* Consuming ride requests
* Finding nearby drivers
* Calculating driver scores
* Selecting the best driver
* Publishing ride-matched events

```text
Kafka
  │
  ▼
Matching Service
  │
  ▼
Location Service
  │
  ▼
Driver Selection
  │
  ▼
Kafka
```

---

# 🗄️ Data Storage

### MySQL

Used for persistent ride-related data.

Typical ride information includes:

```text
Ride
 ├── Ride ID
 ├── Rider ID
 ├── Driver ID
 ├── Pickup Location
 ├── Drop Location
 ├── Pickup Address
 ├── Drop Address
 └── Ride Status
```

### Redis

Used for fast, real-time driver location management.

```text
drivers:locations
       │
       ├── driver:1 → coordinates
       ├── driver:2 → coordinates
       └── driver:3 → coordinates
```

---

# 🛑 Stopping the Application

Stop each Spring Boot service using:

```text
Ctrl + C
```

Stop Docker infrastructure:

```bash
docker compose down
```

To remove containers and Docker volumes:

```bash
docker compose down -v
```

> ⚠️ Use `-v` carefully because it removes Docker volumes and can delete locally stored database data.

---

# 🐞 Troubleshooting

### Kafka Connection Errors

If a service cannot connect to Kafka immediately after starting Docker:

```bash
docker ps
```

Wait until Kafka and Zookeeper are fully initialized, then restart the Spring Boot services.

### Redis Connection Errors

Check Redis:

```bash
docker ps
```

Test Redis connectivity:

```bash
docker exec -it redis-geo redis-cli ping
```

Expected response:

```text
PONG
```

### MySQL Connection Errors

Verify that MySQL is running:

```bash
docker ps
```

Also verify that the database configuration in the relevant `application.properties` or `application.yml` matches the Docker configuration.

---

# 📚 Concepts Demonstrated

This project demonstrates practical implementation of:

* Java Backend Development
* Spring Boot
* Microservices Architecture
* REST API Development
* Apache Kafka
* Kafka Producers and Consumers
* Event-Driven Architecture
* Redis Geospatial
* Redis `GEOADD`
* Redis `GEOSEARCH`
* MySQL
* Spring Data JPA
* Service-to-Service Communication
* Driver Matching Algorithms
* Ride State Machines
* Docker
* Docker Compose
* Asynchronous Processing

---

# 🚀 Future Enhancements

Possible improvements for a production-level ride-booking system:

* Driver availability management
* Driver acceptance/rejection workflow
* Ride cancellation
* Fare calculation
* Surge pricing
* Payment Service
* Driver Service
* Rider Service
* API Gateway
* Service Discovery
* Authentication & Authorization
* JWT-based security
* Distributed tracing
* Centralized logging
* Circuit breakers
* Retry mechanisms
* Rate limiting
* Real-time WebSocket updates
* Kubernetes deployment
* Automated unit and integration testing

---

# 🎯 Project Objective

The objective of this project is to demonstrate how a real-world ride-hailing backend can be designed using modern Java backend technologies and distributed-system concepts.

The project focuses on:

```text
Real-Time Location
        +
Event-Driven Architecture
        +
Microservices
        +
Driver Matching
        +
Ride Lifecycle Management
```

---

# 👨‍💻 Author

**Prince Kumar Gautam**

Java Backend Developer | Spring Boot | Microservices | Kafka | Redis

### GitHub

[Prince Kumar Gautam on GitHub](https://github.com/thePrincegautam)

---

# ⭐ Support

If you find this project useful for learning **Java, Spring Boot, Microservices, Kafka, Redis, or distributed-system design**, consider giving the repository a ⭐ star.

Contributions, suggestions, and improvements are welcome.
