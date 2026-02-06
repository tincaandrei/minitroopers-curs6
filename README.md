
# Maintenance Task Service – Requirements

## Overview
BMW Service Engineering wants a tiny internal service that manages **Maintenance Tasks**.  
Each task refers to work done on a car identified by its **VIN (Vehicle Identification Number)**.

For now, this service does not talk to a database — everything is stored **in memory** so we can move quickly.

Later, the team will replace the in‑memory storage with a **real H2 database**, without rewriting the entire application.  
The code should be structured so this swap is possible with minimal changes.

## MaintenanceTask Object
A `MaintenanceTask` must contain:

- `vin`
- `type` (`"OIL_CHANGE"`, `"BRAKE_INSPECTION"`)
- `status` (`"NEW"`, `"IN_PROGRESS"`, `"DONE"`)
- `notes` (optional string)

## Functional Requirements
The service must support:

### Create a task
Create a new maintenance task for a given VIN.

### Update a task’s status
Change the lifecycle state of a task.

### Add or update notes
Add or modify the notes field of a task.

### Get a task by ID
Return the details for a specific task.

### List all tasks
Return all recorded tasks.

### List tasks by VIN
Return tasks associated with a particular VIN.

## How to Run the Application

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Steps to Start

1. **Build the project**
   ```bash
   mvn clean package
   ```

2. **Run the application**
   ```bash
   mvn quarkus:dev
   ```

3. **Verify the application is running**

   The service should start on `http://localhost:8080`

   Health check: `http://localhost:8080/maintenance-service/q/health`

## API Documentation & Testing

### Swagger UI

Once the application is running, access the interactive API documentation:

**Swagger UI URL:** `http://localhost:8080/maintenance-service/q/swagger-ui`

### Using Swagger UI

1. Navigate to the Swagger UI URL in your browser
2. Expand any endpoint to see details
3. Click **"Try it out"** button
4. Fill in the required parameters/body
5. Click **"Execute"** to make the API call
6. View the response below

## Homework Assignment

Congratulations! You've successfully set up and tested the Maintenance Task Service. Now it's time to enhance it with real-world improvements.

### Part 1: Replace In-Memory Storage with H2 Database

**Current State:** The application uses an in-memory `HashMap` to store maintenance tasks.

**Your Task:** Replace the in-memory implementation with a proper H2 database using Panache.

**Requirements:**
1. Add the required Quarkus Panache and H2 dependencies to `pom.xml`
2. Create a Panache entity for `MaintenanceTask`
3. Implement a Panache repository that replaces the current `InMemoryMaintenanceTasks`
4. Configure H2 database in `application.properties`
5. Ensure all existing endpoints continue to work without changes

**Hints:**
- Look into `quarkus-hibernate-orm-panache` and `quarkus-jdbc-h2`
- Use `@Entity` annotation on your domain object
- Panache repositories extend `PanacheRepository<YourEntity>`
- H2 can run in-memory for development: `jdbc:h2:mem:testdb`

**Resources:**
- [Quarkus Panache Guide](https://quarkus.io/guides/hibernate-orm-panache)
- [Quarkus H2 Guide](https://quarkus.io/guides/datasource)

---

### Part 2: Add New Maintenance Types

**Current State:** The service only supports two maintenance types: `OIL_CHANGE` and `BRAKE_INSPECTION`.

**Your Task:** Add two additional maintenance types to the system.

**New Types to Add:**
1. **Tire Service** (`TIRE_SERVICE`)
    - Includes: tire rotation, tire replacement, wheel alignment
    - Specific field: `tirePosition` (FRONT_LEFT, FRONT_RIGHT, REAR_LEFT, REAR_RIGHT, ALL)

2. **Diagnostic Scan** (`DIAGNOSTIC_SCAN`)
    - Includes: OBD-II diagnostics, error code reading
    - Specific fields: `errorCodes` (list of strings), `scannerType` (BASIC, ADVANCED)

These services are fictional, think of it as a fun exercise to model different types of maintenance tasks with their own specific fields that will be used in production in other parts of the 
application.

**Requirements:**
1. Update the `TaskType` enum
2. Create appropriate domain method for each new type (think about proper naming conventions)
3. Update the OpenAPI specification to document the new types (you should use AI)

---

### Part 3: Refactor Task Creation Logic

**Current State:** The `createTask` method in `MaintenanceTaskService` uses a `switch` statement:

```java
public Long createTask(String vin, TaskType type, String notes) {
    MaintenanceTask task = switch (type) {
        case OIL_CHANGE -> MaintenanceTask.createOilChange(vin, notes);
        case BRAKE_INSPECTION -> MaintenanceTask.createBrakeInspection(vin, notes);
    };
    // ...
}
```

**Your Task:** Refactor this code to be more maintainable and extensible.

**Problems with Current Approach:**
- Adding new task types requires modifying the service class
- Violates Open/Closed Principle (open for extension, closed for modification)
- Logic for creating different task types is scattered

**Requirements:**
1. Refactor the creation logic so adding new task types doesn't require changing `MaintenanceTaskService`
2. Each task type should have its own creation logic encapsulated in a separate component
3. The system should automatically discover and use the appropriate creator based on the task type
4. Use dependency injection to manage the lifecycle of these components

**Hints:**
- Think about how you can delegate creation to specialized components
- Consider creating an interface that all task creators implement
- Look into `Instance<T>` for working with multiple beans of the same type
- The pattern you're implementing has two parts: one for object creation, one for selecting the right creator

**Example Usage (after refactoring):**
```java
// Should work for any task type without changing this code
public Long createTask(String vin, TaskType type, String notes, Map<String, Object> additionalData) {
    // Your refactored implementation here
}
```

**Questions to Consider:**
- How will you map a `TaskType` to the correct creator?
- How will you handle additional parameters needed for `TIRE_SERVICE` and `DIAGNOSTIC_SCAN`?
- Can you make this extensible so a new intern can add a fifth task type without touching existing code?
