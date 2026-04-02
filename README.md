# 🏘️ Society Hub — Residential Society Management System

A full-stack web application to digitize and streamline the day-to-day operations of a residential housing society. It connects three types of users — **Admins**, **Residents**, and **Vendors** — on a single platform to manage complaints, maintenance bills, announcements, flat assignments, and payments.

---

## 📌 Project Structure

```
├── society-maintenance/        # React Frontend (Port: 3000)
└── societymainteinance/        # Spring Boot Backend (Port: 8080)
```

---

## 🚀 Features

### 👨‍💼 Admin
- Secure login with email and password
- Manage residents — view, search, filter by block, and delete
- Manage flats — create, assign to residents, filter by block, delete
- Manage vendors — add, edit, delete, filter by service type
- Assign vendors to resident complaints
- Create, edit, and delete society announcements
- View activity logs for all operations
- Update admin profile and password

### 🏠 Resident
- Register and login with email and password
- View assigned flat details (flat number, block, floor)
- Raise complaints with title and description
- Track complaint status (Pending → In Progress → Completed)
- Pay service charges for resolved complaints via **Razorpay**
- View maintenance bills (auto-generated after payment)
- Read society announcements
- View personal activity history
- Update profile and change password

### 🔧 Vendor
- Secure **OTP-based login** via registered email (no password required)
- View complaints assigned by admin
- Update complaint status (In Progress / Completed)
- Set service amount for completed work
- Track assigned and completed jobs

---

## 💳 Payment Flow

1. Vendor marks a complaint as completed and sets a service amount
2. Resident sees the pending payment in their dashboard
3. Resident clicks "Pay Now" — Razorpay payment gateway opens
4. On successful payment, the complaint is marked as paid
5. A maintenance bill is automatically generated and added to the resident's bills

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 19, React Router 7, Axios |
| Backend | Spring Boot 4, Java 21 |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security (CSRF disabled, CORS configured) |
| Payment | Razorpay |
| Email / OTP | Spring Mail (Gmail SMTP) |
| Validation | Spring Bean Validation |

---

## ⚙️ Setup & Installation

### Prerequisites
- Node.js (v18+)
- Java 21
- MySQL
- Maven

---

### Backend Setup

1. Create a MySQL database:
```sql
CREATE DATABASE SocietyMaintenance;
```

2. Update the config file at:
```
societymainteinance/societymainteinance/src/main/resources/application.properties
```

Fill in your actual values:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/SocietyMaintenance
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

spring.mail.username=your_email@gmail.com
spring.mail.password=your_gmail_app_password
```

3. Run the backend:
```bash
cd societymainteinance/societymainteinance
./mvnw spring-boot:run
```

Backend runs at: `http://localhost:8080`

---

### Frontend Setup

```bash
cd society-maintenance
npm install
npm start
```

Frontend runs at: `http://localhost:3000`

---

## 🗄️ Database Schema (Key Entities)

- `admins` — Admin accounts
- `residents` — Resident accounts with flat association
- `flats` — Flat details (number, block, floor)
- `vendors` — Vendor profiles with service type and availability status
- `complaints` — Complaints raised by residents, assigned to vendors
- `announcements` — Society-wide notices posted by admin
- `maintenance_bills` — Bills auto-generated after complaint payment
- `activities` — Audit log of all user actions

---

## 🔐 Authentication

| Role | Method |
|---|---|
| Admin | Email + Password |
| Resident | Email + Password |
| Vendor | Email + OTP (sent via Gmail) |

---

## 📡 API Base URL

```
http://localhost:8080
```

Key endpoint groups: `/auth`, `/residents`, `/admins`, `/vendors`, `/flats`, `/complaints`, `/announcements`, `/bills`, `/payments`, `/activities`

---

## 📸 Pages

- `/` — Landing page with features overview
- `/auth` — Login / Register
- `/admin` — Admin dashboard
- `/resident` — Resident dashboard
- `/vendor` — Vendor dashboard
- `/forgot-password` — Password recovery
- `/features`, `/about`, `/contact` — Static info pages

---

## ⚠️ Important Notes

- Never commit your real `application.properties` credentials to GitHub
- Razorpay is configured in test mode — use Razorpay test card details for payments
- Vendor OTP login requires a valid Gmail App Password (not your regular Gmail password)
