# Weather Tracking Service

### Overview

The **Weather Tracking Service** is a Spring Boot application designed to fetch and display current weather data for US postal codes using a [weather API](https://openweathermap.org/). It supports user-specific request history and stores query details in a database for future reference.

---
### Features

1. **API Integration**
    - Integrates with a public [weather API](https://openweathermap.org/).
    - Handles API key configuration for secure communication.

2. **User Input**
    - Accepts user-provided postal codes and usernames.
    - Validate input to ensure correctness (US postal code format).

3. **Data Retrieval**
    - Fetches weather details like temperature, humidity, and conditions for the given postal code or username.
    - Handles errors gracefully with user-friendly error messages.

4. **Data Storage**
    - Saves Data in a Postgres database.
    - Maintains history for each postal code and user.

5. **Data Presentation**
    - Displays weather details in a user-friendly format.
    - Retrieves and displays request history by postal code or user.

6. **Testing**
    - Automated tests for API integration, input validation, and database operations.

---
## Getting Started

### Prerequisites
- **Java 21** or later
- **Maven 3** or later
- **Postgres** (for Main DataBase)
- **H2** (for Test DataBase)
- **Docker** (for containerization)
---

### Setup and Installation

1. **Clone the Repository**
```shell
    git clone https://github.com/Samira1462/weather-tracking-microservice.git
```
2. **clean package**
```shell
    mvn clean package -DskipTests=true
```
3. **Install database service**
```shell
    docker compose --file docker-compose.yml --project-name weather-service up --build -d postgres pgadmin
```
4. **Install all services**
```shell
    docker compose --file docker-compose.yml --project-name weather-service up --build -d
```
5. **API Documentation**
   
   The API endpoints are documented using Swagger and are accessible when the application is running.

```text
   http://localhost:8080/swagger-ui/index.html
```
