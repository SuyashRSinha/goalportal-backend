# GoalPortal Backend

Enterprise KPI & Goal Management System Backend built using:

- Spring Boot,
- Spring Security,
- JWT Authentication,
- MySQL,
- REST APIs,

Tech Stack:
Backend:
Java,
Spring Boot,
Spring Security,
Spring Data JPA,
Hibernate,
JWT,

Database:
MySQL (Railway Hosted)

Deployment:
Railway

This backend powers the GoalPortal enterprise performance management platform.

---

# Live Backend API


https://web-production-0f7c0.up.railway.app

GitHub Repository:
https://github.com/SuyashRSinha/goalportal-backend

Features:
Authentication,
JWT Login,
Role-Based Access Control,
Password Encryption using BCrypt,


Goal Management:
Goal Creation,
Goal Approval Workflow,
Goal Rejection/Rework,
Shared Organizational KPIs,


Roles:
Employee,
Manager,
Admin / HR,


Analytics:
KPI Tracking,
Quarterly Reviews,
Performance Metrics,
Goal Progress Monitoring,

Database:
Hosted MySQL Database

Platform:
Railway MySQL

Database connection handled securely using Railway environment variables.

API Base URL:
https://web-production-0f7c0.up.railway.app

Important APIs:
Register User,
POST /auth/register,
Login,
POST /auth/login,
Goals,
GET /goals,
POST /goals,
PUT /goals/{id},
DELETE /goals/{id},
Local Setup,

Clone repository:

git clone https://github.com/SuyashRSinha/goalportal-backend.git

Move into project:

cd goalportal-backend,

Install dependencies:

./mvnw clean install

Run application:

./mvnw spring-boot:run

Environment Variables:
Configure in application.properties:

spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
jwt.secret=YOUR_SECRET_KEY
Railway Deployment

Backend deployed using:

Railway

Database hosted using:

Railway MySQL


Login Credentials:
Employee
Email: suyash@gmail.com
Password: 12345,
Manager:
Email: manager@gmail.com
Password: manager123,
Admin
Email: admin@gmail.com
Password: admin123,

Architecture
Frontend (React + Vercel)
        ↓
REST APIs
        ↓
Backend (Spring Boot + Railway)
        ↓
MySQL Database (Railway)


Developed By
Suyash Sinha (BTECH 3RD YEAR BIT MESRA, OFF CAMPUS PATNA)

License

This project is developed for educational, enterprise learning, and hackathon purposes.
