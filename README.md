#  Loan Origination System (LOS)

A robust Spring Boot application to process, approve, and manage loan applications with agent-manager hierarchies and notification simulations.

##  Features
- Submit new loan applications
- Automated multithreaded loan processing
- Agent assignment for under-review loans
- Agent decision endpoints (Approve/Reject)
- Mock notification service (logs notifications)
- Fetch loans by status (paginated)
- Get loan status counts
- Top customers API

##  Tech Stack
Java 17 | Spring Boot | Spring Data JPA | MySQL | Lombok | JUnit & Mockito

##  Setup Instructions

1. **Clone Repository**
    ```
    git clone <your-repo-url>
    cd Loan-Origination-System
    ```

2. **Configure MySQL**
    ```
    CREATE DATABASE losDB;
    ```
   Update `application.properties`:
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/losDB
    spring.datasource.username=root
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

3. **Build & Run**
    ```
    mvn clean install
    mvn spring-boot:run
    ```
   App runs at: `http://localhost:8080`

##  API Endpoints

 **1. Submit Loan Application**  
`POST /api/v1/loans`  
Request Body:
```
{

"customerName": "Ravi Kumar",
"customerPhone": "9876543210",
"loanAmount": 250000,
"loanType": "PERSONAL"

}
```

 **2. Trigger Loan Processing**  
Method: `POST`  
URL: `/api/v1/loans/process`  
Request Body: None  
Status:  Tested – Loan processing triggered for all APPLIED loans.

 **3. Get Loans By Status (With Pagination)**  
Method: `GET`  
URL: `/api/v1/loans?status=APPROVED_BY_SYSTEM&page=0&size=5`  
Request Body: None  
Status:  Tested – Returns list of loans filtered by status with pagination.

 **4. Get Loan Status Counts**  
Method: `GET`  
URL: `/api/v1/loans/status-count`  
Request Body: None  
Status:  Tested – Returns real-time count of loans in each status.

 **5. Top Customers API**  
Method: `GET`  
URL: `/api/v1/customers/top`  
Request Body: None  
Status:  Tested – Returns top 3 customers with most approved loans.

 **6. Agent Decision Endpoint**  
Method: `PUT`  
URL: `/api/v1/agents/{agent_id}/loans/{loan_id}/decision`  
Request Body (examples):
{
"decision": "APPROVE"
}
or
{
"decision": "REJECT"
}

Status:  Tested – Agent decision recorded and loan status updated.

 **7. Create Agent (Direct DB Insert)**  
Method: `INSERT INTO DB`  
Table: `agents`  
Columns: `agent_id, name, manager_id`  
Example SQL:
INSERT INTO agents (agent_id, name) VALUES (UUID(), 'Rohan Sharma');
Status:  Tested – Agent created successfully in database.

 **Summary**  
 All endpoints have been tested for success and failure cases, ensuring:
- Clean modular architecture
- Multithreading functionality with system approval simulation
- Agent assignment and decision-making flow
- Mock notification integration
- Real-time loan status monitoring
- Top customer aggregation
