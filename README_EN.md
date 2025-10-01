<h1 align="center">
  <img src="https://readme-typing-svg.herokuapp.com/?font=Righteous&size=35&center=true&vCenter=true&width=500&height=70&duration=4000&color=2196F3&lines=Bank-Card-Management+💳;" />
</h1>

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen?style=for-the-badge&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=for-the-badge&logo=docker)
![Liquibase](https://img.shields.io/badge/Liquibase-DB%20Migrations-orange?style=for-the-badge&logo=liquibase)
![Security](https://img.shields.io/badge/Security-JWT%20%2B%20Spring%20Security-red?style=for-the-badge&logo=springsecurity)

</div>

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Tech Stack](#-tech-stack)
- [System Features](#-system-features)
- [Quick Start](#-quick-start)
- [API Documentation](#-api-documentation)
- [Database Migrations](#-database-migrations)
- [Conclusion and Future Plans](#-conclusion-and-future-plans)

## 🌟 Project Overview

**Bank Card Management** is a microservice for managing bank cards via REST API. The system allows creating, blocking, and managing cards, ensuring secure access and transparency of operations.

Imagine: instead of bulky banking systems and routine tasks — an elegant REST API that understands your needs. The system treats every card as a valuable asset: protects data, manages access, and provides full control over financial instruments.

Key focus areas of the project:
- **Security** — your data is reliably protected
- **Simplicity** — intuitive APIs for complex operations
- **Flexibility** — the system grows with your needs
- **Transparency** — always know what’s happening with your cards

## 🛠 Tech Stack

<div align="center">

| Layer | Technologies |
|------|------------|
| **Backend** | Java 21 • Spring Boot 3.4.0 |
| **Security** | Spring Security • JWT 0.12.6 |
| **Database** | PostgreSQL 16+ • Spring Data JPA |
| **Migrations** | Liquibase 4.25+ |
| **Documentation** | OpenAPI 3.0+ |
| **Containerization** | Docker 24.0+ • Docker Compose |
| **Build** | Maven 3.9+ |

</div>

## 🚀 System Features

### 🎯 Core Functionality
- ✅ **Full card management cycle** — from creation to archiving
- ✅ **Smart validation** — data is checked before saving
- ✅ **REST API** — clear endpoints for integrations
- ✅ **Auto-documentation** — OpenAPI spec generated automatically
- ✅ **Database migrations** — controlled and predictable schema updates
- ✅ **Docker containerization** — runs consistently everywhere

### 🛡️ Security System
- ✅ **JWT + Spring Security** — enterprise-grade authentication
- ✅ **Role-based access** — ADMIN and USER roles with different permissions
- ✅ **Data encryption** — card numbers are masked (**** **** **** 1234)

### 👥 Roles and Permissions

| Role | Capabilities | Intended For |
|------|-------------|----------------|
| 👑 ADMIN | Full control: create, block, activate, delete cards + manage users | Bank operators • System administrators |
| 👤 USER | View own cards • Account transfers • Request block • Balance monitoring | Bank clients • Cardholders |

### 💳 Card Attributes
- **🔒 Card Number** — securely encrypted, displayed masked
- **👤 Owner** — linked to a specific user
- **📅 Expiration Date** — automatic validity check
- **📊 Status** — Active • Blocked • Expired
- **💰 Balance** — real-time funds tracking

## ⚡ Quick Start

### 📋 Prerequisites

- **Docker 24.0+** and **Docker Compose** — for containerization
- **Java 21** — for local development
- **Maven 3.9+** — for project build

### 🐳 Running with Docker Compose

```bash
# Clone the repository if needed
git clone https://github.com/Karam1215/Bank_REST.git
cd Bank-Service

# Build and run everything in one command
docker compose up --build

# Or run in detached mode
docker compose up --build -d
```

## 📚 API Documentation

After starting the project, it is available in the browser:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

### Generating the OpenAPI Specification

To generate `openapi.yaml`, use the Maven command:

```bash
mvn clean compile springdoc-openapi:generate
```

## 🗄️ Database Migrations

Migrations are located in `src/main/resources/db/changelog/`.

They are executed automatically when the project starts.

### Manual Execution

```bash
mvn liquibase:update

```

## 🎯 Summary and Future Prospects

The **Bank Card Management** project provides a convenient and secure way to manage bank cards through a REST API.

### Summary
- Full card management cycle: creation, blocking, activation, deletion  
- Customer data protection and access control  
- Intuitive API documentation and database migrations  

### Future Prospects
- Adding support for new card types and payment systems  
- Expanding analytics and reporting capabilities  
- Integration with external banking services  
- Enhancing security and implementing additional authentication layers

