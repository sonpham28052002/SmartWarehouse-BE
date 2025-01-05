# SmartWarehouse-BE

This project is a backend application for managing a cinema ticket system built with Spring Boot. It provides functionality for ticket management, user authentication, and other services such as Redis caching and JWT authentication.

## Table of Contents

1. [Project Setup](#project-setup)
2. [Running the Project](#running-the-project)
3. [Endpoints](#endpoints)
4. [Technologies Used](#technologies-used)
5. [License](#license)

## Project Setup

### Prerequisites

Before running the project, make sure you have the following installed:

- **JDK 17** (or higher)
- **Maven** (for building and running the project)
- **MySQL** (for the database)
- **Redis** (if using Redis for caching)

## Clone the Repository
```
git clone https://github.com/your-username/SmartWarehouse-BE.git
cd SmartWarehouse-BE
```
## Running the Project Locally
**1. Build the Project**

Run the following command to build the project:
```
mvn clean install
```
**2. Run the Application**

You can then run the application with the following command:
```
mvn spring-boot:run
```
Alternatively, you can run the packaged JAR file:
```
java -jar target/${ProejctName}-0.0.1-SNAPSHOT.jar
```
## Running the Project with Docker
**1. Build the Docker compose**
```
docker-compose build
```
**2. Run the Docker compose**
```
docker-compose up -d
```

## Technologies Used
- **Spring Boot 3.x**: Backend framework
- **Spring Security**: Authentication and authorization
- **JWT**: JSON Web Tokens for securing API endpoints
- **MySQL**: Relational database for data storage
- **Redis**: In-memory caching system
- **MapStruct**: Java bean mapping framework
- **Lombok**: Reduces boilerplate code in Java classes
- **Swagger/OpenAPI**: Automatically generates API documentation
