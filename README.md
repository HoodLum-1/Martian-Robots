
# Martian Robots
## Red Badger Coding Challenge – Full Implementation

### Problem Statement
The Martian Robots challenge involves simulating the movement of robots on a rectangular grid on Mars. Robots process sequences of instructions:

- **L** = Turn Left 90°
- **R** = Turn Right 90°
- **F** = Move Forward one grid point

If a robot moves off the grid, it is lost, but leaves a "scent" at its last position. Subsequent robots will ignore any F command that would take them off the same edge if a scent exists.

---

### Tech Stack

| Technology          | Purpose                          |
|---------------------|----------------------------------|
| Spring Boot 3.2.0   | Backend framework                |
| Java 21             | Runtime & modern syntax          |
| Lombok              | Reduce boilerplate               |
| Thymeleaf           | Server-side templating           |
| Material UI (CDN)   | Modern, responsive UI            |
| Spring Data JPA     | Persistence                      |
| H2 Database         | In-memory DB (dev)               |
| Maven               | Build & dependency management    |
| JUnit 5 + Mockito   | Testing                          |
| SpringDoc OpenAPI   | API documentation                |

---

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Quick Start

```bash
# 1. Clone the repo
git clone https://github.com/HoodLum-1/Martian-Robots.git
cd Martian-Robots

# 2. Build & Run
mvn spring-boot:run

````

## Access

| Service | URL |
|---------|-----|
| Web App | http://localhost:5000 |
| H2 Console | http://localhost:5000/h2-console |
| Swagger UI | http://localhost:5000/swagger-ui.html |
| API Docs (JSON) | http://localhost:5000/v3/api-docs |

### H2 Console Settings
- **JDBC URL**: `jdbc:h2:mem:Martian_Robots`
- **Username**: `sa`
- **Password**: (leave blank)

## Key Features

| Feature | Description |
|---------|-------------|
| Robot Simulation Engine | Full movement, scent, and loss logic |
| Scent Persistence | Robots avoid repeated loss |
| Input Validation | Client + server side |
| Modern UI | Material Design, responsive, interactive |
| REST API | JSON endpoint with OpenAPI docs |
| Persistence | Robots saved to H2 DB |
| Sample Inputs | 3 preloaded test cases |
| Full Test Coverage | Unit + integration tests |

## Sample Inputs

| # | Grid | Start | Instructions | Expected Output |
|---|------|-------|--------------|-----------------|
| 1 | 5 3 | 1 1 E | RFRFRFRF | 1 1 E |
| 2 | 5 3 | 3 2 N | FRRFLLFFRRFLL | 3 3 N LOST |
| 3 | 5 3 | 0 3 W | LLFFFLFLFL | 2 3 S |

Click any sample in the UI to auto-fill the form.


## API Endpoints

### Web Interface
| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | Main UI with form |
| POST | `/process` | Process via HTML form |

### REST API

```http
GET /api/process?
  gridMaxX=5&
  gridMaxY=3&
  startX=1&
  startY=1&
  startOrientation=E&
  instructions=RFRFRFRF
  ```

**Response:**

```json
[
  {
    "x": 1,
    "y": 1,
    "orientation": "E",
    "lost": false
  }
]
```
**Full docs:** [`openapi.yaml`](docs/openapi.yaml)

# Running Tests
```bash
mvn test
```

# Database Schema
See: [docs/schema.md](docs/schema.md)

## Table: `robots`

| Column        | Type         | Constraints              | Description          |
|---------------|--------------|--------------------------|----------------------|
| `id`          | BIGINT       | PK, AUTO                 |                      |
| `x`           | INTEGER      | NOT NULL                 | X coord              |
| `y`           | INTEGER      | NOT NULL                 | Y coord              |
| `orientation` | VARCHAR(1)   | NOT NULL                 | N/S/E/W              |
| `lost`        | BOOLEAN      | DEFAULT false            |                      |
| `instructions`| VARCHAR(1000)| NOT NULL                 |                      |

**Index:** `idx_robot_position (x, y, orientation)`

---

# OpenAPI Documentation

**Live Swagger UI:** [http://localhost:5000/swagger-ui.html](http://localhost:5000/swagger-ui.html)

**Static YAML Contract:** `docs/openapi.yaml`  

### Includes:
- Full request/response schemas
- Validation rules
- Example values
- Error responses

---

# Modern Java 21 Features Used

```java


// Records (immutable DTOs)
public record RobotInput(@Min(1) int gridMaxX, ...) {}

// Pattern matching in switch
case 'F' -> moveForward(current)

// var in locals
var result = service.process(...);

// Text blocks, sealed classes, etc.
```

# UI/UX Highlights

- **Material Design via CDN**
- **Responsive grid layout**
- **Interactive sample loader**
- **Success/Error cards**
- **Live validation**
- **Mobile-friendly**

# Scalability & Extensibility

| Feature                  | Ready For                     |
|--------------------------|-------------------------------|
| Multiple robots          | Add `List<RobotInput>` endpoint |
| Persistent grid/scent    | Store Grid in DB              |
| WebSocket live tracking  | Real-time robot paths         |
| React/Vue frontend       | Decouple UI                   |
| PostgreSQL               | Production DB                 |
