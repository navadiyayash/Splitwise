# Splitwise Clone - Expense Sharing Application (Backend Only)

A backend-only Java Spring Boot application that replicates the core functionalities of Splitwise. It allows users to create groups, add friends, share expenses, settle debts, and track group balances in real-time. The backend is fully RESTful, tested with JUnit, and deployed on AWS.

## Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3
- **Database**: MySQL (Workbench)
- **Testing**: JUnit 5 + Mockito
- **API Testing**: Postman
- **Deployment**: AWS EC2 (Ubuntu), GitHub, MySQL on RDS/local
- **Build Tool**: Maven

## Modules Implemented

| Module            | Description                                                                 |
|-------------------|-----------------------------------------------------------------------------|
| User              | Create, update, retrieve, and delete users                                  |
| Friend            | Manage friend connections between users                                     |
| Group             | Create and manage groups                                                    |
| Group Member      | Add/remove users from groups                                                |
| Expense           | Add shared expenses with payer and group                                    |
| Expense Share     | Split expenses equally/unequally among members                              |
| Settlement        | Settle up debts between users in a group                                    |
| Group Balance     | Compute and track who owes whom and how much                                |

## Project Architecture

com.example.splitwise
│
├── Controller
│   └── All REST endpoints (UserController, GroupController, etc.)
├── Service
│   └── Business logic (ExpenseService, GroupService, etc.)
├── Repository
│   └── Spring Data JPA repositories
├── Entity
│   └── JPA Entity classes (User, Group, Expense, etc.)
├── Exception
│   └── Custom exception handling

## Key Features

- Composite key enforcement for friend relationships to prevent duplicates.
- Real-time balance calculation for group expenses and settlements.
- Automated equal expense splitting using service logic.
- Custom exception handling for all modules.
- JUnit test coverage for all service and controller layers.
- Postman collection included for full API testing.

## How We Built It (Step-by-Step)

### 1. **Project Setup**

- Created Spring Boot app using [Spring Initializr](https://start.spring.io/)
- Dependencies:
  - Spring Web
  - Spring Data JPA
  - MySQL Driver
  - Lombok
  - Validation

### 2. **Database Setup**

- Created MySQL database `splitwise_db` using MySQL Workbench
- Connected MySQL with Spring Boot using `application.properties`
- Enabled `spring.jpa.hibernate.ddl-auto=update` to auto-generate schema

### 3. **Entity Design**

- Modeled entities like `User`, `Friend`, `Group`, `Expense`, `ExpenseShare`, `GroupMember`, `Settlement`, `GroupBalance`
- Added JPA annotations like `@ManyToOne`, `@OneToMany`, and `@JoinColumn`
- Used `@UniqueConstraint` for friend table to prevent duplicate records

### 4. **Service Layer**

- Implemented all core logic inside services (e.g., balance calculation, settlement logic)
- Used constructor-based dependency injection with `@Service`
- Isolated each business logic into its corresponding service

### 5. **Controller Layer**

- Built RESTful endpoints with `@RestController`
- Mapped routes like:
  - `/api/users`, `/api/groups`, `/api/expenses`, etc.
- Handled exceptions using `@ExceptionHandler`

### 6. **Testing**

- Used `@WebMvcTest` for controller tests
- Used `@MockBean` and Mockito to mock service layers
- Verified responses using `MockMvc`
- Covered edge cases (invalid input, user not found, etc.)

### 7. **Postman Testing**

- Created Postman collection for all APIs
- Validated endpoint flow (create user → add group → share expense → view balances)

### 8. **Deployment on AWS**

- Provisioned Ubuntu EC2 instance on AWS
- Installed Java 17 and MySQL server
- Cloned project repo from GitHub
- Built project using `mvn clean install`
- Ran JAR using `java -jar splitwise-0.0.1-SNAPSHOT.jar`
- Opened necessary ports in EC2 (e.g., 8080)
- Application live and accessible over public IP

## Sample Endpoints

| Method | Endpoint                                 | Description                          |
|--------|------------------------------------------|--------------------------------------|
| POST   | `/api/users`                             | Create new user                      |
| POST   | `/api/groups`                            | Create group                         |
| POST   | `/api/group-members`                     | Add member to group                  |
| POST   | `/api/expenses`                          | Create a shared expense              |
| GET    | `/api/group-balances/group/{groupId}`    | Get all balances in a group          |
| POST   | `/api/settlements`                       | Create a settlement                  |
| GET    | `/api/expenses/group/{groupId}`          | Get all expenses in a group          |

## Testing APIs with Postman

- Import the included `postman_collection.json` file (if added to repo)
- Test flow:
  1. Create users
  2. Add friends
  3. Create groups and add members
  4. Add shared expenses
  5. Check group balances
  6. Settle balances

## Running Tests

```bash

mvn test

## Future Enhancements

* Add Swagger UI for live API documentation
* Add email notifications for settlements
* Build frontend UI using React or Angular
* Add JWT-based user authentication
* Add currency support and timezone features

