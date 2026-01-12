# Family Meal Mate

## Overview
FamilyMealMate is a demo backend application to manage shared household data such as family members, recipes, meal plans, and shopping lists. The project was built to demonstrate clean architecture, feature implementation and security-aware design in a realistic domain scenario.

## Features

### User and Household Management
- User registration and authentication (JWT)
- Fetch household and create new family members (unregistered users)
- New users can join an existing household during the registration process with a unique joinId

### Recipes and Ingredients
- Create and fetch recipes
- Master data: ingredients and tags loaded from CSV on startup

### Meal Plans
- Predefined meal plans for current and next week per household
- Edit meal plans by assigning recipes to days
- Templates for recurring use
- Optimistic locking to prevent simultaneous updates

### Shopping Lists
- One predefined shopping list per household
- Aggregation of ingredients from meal plans into shopping lists
- Edit shopping lists with optimistic locking

## Tech Stack
- Java 17
- Spring Boot
- Hibernate / JPA
- Liquibase 
- PostgreSQL (H2 for tests)
- MapStruct 
- Swagger / OpenAPI and Postman 
- JUnit 5, Mockito

## Getting started
1. Clone the repository
2. Configure database connection in `application.yml`
3. Run the application: ./gradlew bootRun 
4. Swagger UI: Try requests interactively http://localhost:8080/swagger-ui/index.html
5. Postman Collection: Example requests, includes JWT token automation

### Postman Collection
- Import the collection: `postman/Family Meal Mate API.postman_collection.json`
- Import the environment (optional for variables): `/postman/Localhost.postman_environment.json`

## Testing Strategy
- Integration tests to verify feature correctness
- Unit tests for pure utility components (e.g. join ID generation, ShoppingItemAggregator)
- Focus on integration tests to validate real behavior rather than mocking internal state

## Future Improvements
- Angular frontend
- Email based Household invitations
- Store recipe images
- Expand recipe search & filtering: By tags, dietary constraints, ingredients, etc.
- Admin interface: Master data CRUD endpoints
- Incident handling: Let users send feedback / error report, Admins can query and resolve them

## Notes / Limitations
- Endpoints for master data are **read-only**. No admin interface implemented
- Master data loaded from CSV on startup
- Project is focused on features over completeness as a real-life app



