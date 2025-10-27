# Database Schema

## Table: `robots`

| Column        | Type        | Constraints                     | Description |
|---------------|-------------|----------------------------------|-----------|
| `id`          | BIGINT      | PK, AUTO_INCREMENT               | Surrogate key |
| `x`           | INTEGER     | NOT NULL                         | X coordinate |
| `y`           | INTEGER     | NOT NULL                         | Y coordinate |
| `orientation` | VARCHAR(1)  | NOT NULL, {'N','S','E','W'}      | Direction |
| `lost`        | BOOLEAN     | DEFAULT FALSE                    | Lost status |
| `instructions`| VARCHAR(1000)| NOT NULL                        | Input string |
