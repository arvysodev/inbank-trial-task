# Loan Decision Calculator

---

## Description

A full-stack application for evaluating loan applications based on a simplified credit scoring model.

The system calculates the maximum loan amount that can be approved for a given applicant, taking into account their credit profile, requested amount, and loan period.

The application consists of:
- Spring Boot backend implementing the decision engine
- React + Vite frontend for user interaction

---

## Decision Logic

The decision engine determines the maximum loan amount that can be approved for a customer.

### Credit Profile

A mock credit profile is used based on the provided personal code:

- `49002010965` → debt (loan always declined)
- `49002010976` → credit modifier = 100
- `49002010987` → credit modifier = 300
- `49002010998` → credit modifier = 1000

### Scoring Formula

```
credit score = (creditModifier / loanAmount) * loanPeriod
```

A loan is approved if the score is greater than or equal to 1.

### Simplified Calculation

The formula is transformed to avoid floating-point arithmetic:

```
creditModifier * loanPeriod >= loanAmount
```

This allows calculating the maximum approved loan amount for a given period:

```
maxApprovedAmount = creditModifier * loanPeriod
```

### Decision Flow

1. If the customer has active debt → reject
2. Try to calculate the maximum approved amount for the requested period
3. If no valid amount is found:
   - search for the nearest valid period (up to 60 months)
4. Return:
   - maximum approved amount
   - corresponding period

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 4
- Spring Web MVC
- Bean Validation
- MapStruct
- OpenAPI (Swagger)
- Global exception handling with ProblemDetail

### Frontend
- React
- TypeScript
- Vite
- Tailwind CSS

---

## Architecture

The backend follows a layered architecture:

- Controller - handles HTTP requests
- Service - contains business logic
- Domain - internal models
- Mapper - maps domain models to API DTOs

The frontend is a simple client application:

- Form component for user input
- API layer for communication with backend
- Result component for displaying decision

---

## Running the Project

### Backend

```bash
cd backend
./gradlew bootRun
```

Runs on:

`http://localhost:8080`

Swagger UI:

`http://localhost:8080/swagger-ui.html`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Runs on:

`http://localhost:5173`

The frontend is configured to proxy API requests to the backend.

---

## Assumptions

- Personal codes outside the predefined list are treated as invalid input
- The system always returns the maximum approved amount, not the requested amount
- If no valid amount is available for the selected period, the nearest valid period is selected
- The system does not persist any data

---

## Future Improvements

- Replace hardcoded credit profiles with database or external service
- Add authentication and user profiles
- Improve validation error responses with field-level details
- Introduce more advanced scoring logic