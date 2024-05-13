# Kitchensink: JavaEE -> Spring Boot 3 migration

## Introduction
The purpose of this project is to migrate a  Jakarta EE 10 web-enabled database application using JSF, CDI, EJB, JPA to a modern Java, Spring, JPA, MongoDB stack.
The original project source is available on this link: https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x/kitchensink

## Architecture
- Backend:
  - Java 21
  - Spring Boot 3.2.5
  - MongoDB 7.0.7
- Frontend:
  - Thymeleaf template engine

## How to build / start the project
There are 2 ways to build the project:
- Docker containers
- Local build and run

After successful startup the application can be checked on the following endpoints:
  - Health check: http://localhost:8080/actuator/health
  - Home page: http://localhost:8080/kitchensink

### Build and run with Docker (recommended)
- Requirements:
  - Running Docker
  - Ports are available to use: 8080, 27017
  - Internet connection (to pull required docker images)
- Start the project with the following command from project root folder:
  - `docker-compose -f docker-compose.yml up -d --build`
    - This will pull MongoDB, OpenJDK 21 images
    - Build the project within OpenJDK container
    - Trigger UnitTests during build
    - Host the built jar from container using 'dev' profile
    - Initialize MongoDB with sample Document

### Build and run locally with JDK 21 and Maven wrapper
- Requirements:
  - JDK 21 installed
  - Running MongoDB server on localhost:27017
  - Ports are available to use: 8080
- Start the project with the following command from project root folder:
  - `./mvnw clean package -DrunUnitTests=true`
  - `java -jar target/kitchensink_migrated-0.0.1-SNAPSHOT.jar`
- Notes for MongoDB connection:
  - Default profile will expect MongoDB to have authorization disabled and available on localhost:27017/test
  - If you want to override this configuration use Environmental Variables or change the profile values

## API documentation
- Home URL:
  - http://localhost:8080/kitchensink
- REST endpoints:
  - documented in project folder within open-api.json
- SpringDoc endpoints:
  - http://localhost:8080/api-docs
  - http://localhost:8080/swagger-ui.html
- Actuator endpoints:
  - http://localhost:8080/actuator
  - http://localhost:8080/actuator/health

## Regression / Acceptance testing
There is an acceptance test package that is based on the REST functionalities, behaviour of the original Kitchensink JBoss application.
In order to run the package:
- Requirements:
  - JDK 21 installed
  - Running Docker
  - Ports are available to use: 8080, 27017
- Start tests with the following command from project root :
  - `./mvnw clean test -DrunAcceptanceTests=true`

This test uses Testcontainer framework and  creates temporary Docker containers for the Spring application and MongoDB.

## Further project components
In `migration_tool` folder a `transformer` python app is located that supports JavaEE - Spring Boot 3 code conversion with ChatGPT 4. For  more details check [README_TRANSFORMER.md](migration_tool%2Ftransformer%2FREADME_TRANSFORMER.md)