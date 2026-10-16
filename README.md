\# AegisTrack — Engineering Component Inspection Management



AegisTrack is a REST API application for registering and managing engineering components, tracking lifecycle status, and recording inspection history.



\## Technology Stack



\- \*\*Language:\*\* Java 21

\- \*\*Framework:\*\* Spring Boot 4.0.8

\- \*\*Build Tool:\*\* Apache Maven 3.10.0

\- \*\*Database:\*\* MySQL

\- \*\*Persistence:\*\* Spring Data JPA

\- \*\*API:\*\* RESTful HTTP endpoints

\- \*\*Validation:\*\* Jakarta Bean Validation

\- \*\*Testing:\*\* JUnit, Spring MVC Test, Mockito



\## Features



\- Register engineering components.

\- Retrieve all registered components.

\- Filter components by lifecycle status.

\- Retrieve component details by ID.

\- Record and retrieve component inspection history.

\- Search inspection history by result.

\- Validate inspection results and component input.

\- Return appropriate HTTP status codes for successful requests and errors.

\- Update component inspection dates and lifecycle status according to inspection outcomes.



\## API Endpoints



\### Engineering Components



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/components` | Retrieve all components |

| GET | `/api/components?status=UNDER\_REVIEW` | Filter components by status |

| GET | `/api/components/{id}` | Retrieve a component by ID |

| POST | `/api/components` | Register a component |



\### Component Inspections



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/components/{id}/inspections` | Retrieve inspection history |

| GET | `/api/components/{id}/inspections/search?result=FAIL` | Search inspection history by result |

| POST | `/api/components/{id}/inspections` | Record a new inspection |



\## Sample Requests



\### Retrieve Components



```http

GET http://localhost:8080/api/components

```



\### Register a Component



```http

POST http://localhost:8080/api/components

Content-Type: application/json

```



```json

{

&#x20; "name": "Temperature Sensor",

&#x20; "serialNumber": "AT-SENSOR-001",

&#x20; "manufacturer": "Example Instruments Ltd",

&#x20; "componentType": "SENSOR",

&#x20; "lifecycleStatus": "REGISTERED",

&#x20; "receivedDate": "2026-10-16",

&#x20; "notes": "Initial component registration"

}

```



\### Record an Inspection



```http

POST http://localhost:8080/api/components/1/inspections

Content-Type: application/json

```



```json

{

&#x20; "inspectionDate": "2026-10-16",

&#x20; "inspectorName": "Example Inspector",

&#x20; "inspectionResult": "PASS",

&#x20; "notes": "Routine inspection"

}

```



Inspection results supported by the application are `PASS`, `FAIL`, and `CONDITIONAL`.



\## HTTP Status Codes



\- `200 OK` — Successful retrieval.

\- `201 Created` — Component or inspection created successfully.

\- `400 Bad Request` — Invalid request or validation failure.

\- `404 Not Found` — Requested component does not exist.



\## Local Setup



\### Prerequisites



\- Java 21

\- MySQL

\- Apache Maven 3.10.0, or a compatible Maven installation



\### 1. Create the database



Create a MySQL database named `aegistrack`.



```sql

CREATE DATABASE aegistrack;

```



Configure the database connection in `src/main/resources/application.properties` using your local MySQL credentials. Do not commit real passwords or secrets to version control.



\### 2. Run the application



From the project directory:



```bash

mvn spring-boot:run

```



The application runs at:



`http://localhost:8080`



\### 3. Run automated tests



```bash

mvn test

```



The project has passed its current automated test suite with \*\*10 tests, 0 failures, and 0 errors\*\*.



\## Project Structure



```text

AegisTrack/

├── src/

│   ├── main/

│   │   ├── java/com/aegistrack/

│   │   │   ├── controller/

│   │   │   ├── model/

│   │   │   ├── repository/

│   │   │   └── service/

│   │   └── resources/

│   └── test/

│       └── java/com/aegistrack/

├── pom.xml

└── README.md

```



\## Project Purpose



AegisTrack demonstrates Java backend development using Spring Boot, REST API design, relational database persistence, request validation, exception handling, and automated testing.
