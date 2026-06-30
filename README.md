# Banking Management System

A console-based banking management system built in core Java with a MySQL backend. It supports user registration and login, opening bank accounts, and performing core banking operations — deposits, withdrawals, transfers, and balance checks — secured with a per-account security PIN.

## Features

- **User Registration & Login** — Create an account with full name, email, and password; log in with email/password.
- **Bank Account Creation** — Each user can open one bank account, secured with an auto-generated account number and a user-defined security PIN.
- **Credit Money** — Deposit funds into an account after PIN verification.
- **Debit Money** — Withdraw funds, with a balance check to prevent overdrafts.
- **Transfer Money** — Move funds from one account to another, with PIN verification and balance checks.
- **Balance Inquiry** — Check the current balance of an account after PIN verification.
- **Transaction Safety** — Credit, debit, and transfer operations use JDBC transactions (manual commit/rollback) so a partial failure doesn't leave the database in an inconsistent state.

## Tech Stack

- **Language:** Java
- **Database:** MySQL
- **Connectivity:** JDBC (`com.mysql.cj.jdbc.Driver`)
- **Interface:** Command-line (`java.util.Scanner`)

## Project Structure

```
BankingManagmentSystem/
├── BankingApp.java       # Entry point — main menu loop, DB connection setup
├── User.java             # User registration and login logic
├── Accounts.java         # Account creation and lookup logic
└── AccountManager.java   # Credit, debit, transfer, and balance check logic
```

## Database Setup

Create a MySQL database named `banking_system` with the following tables before running the app:

```sql
CREATE DATABASE banking_system;
USE banking_system;

CREATE TABLE user (
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE accounts (
    account_number BIGINT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL,
    security_pin VARCHAR(20) NOT NULL,
    FOREIGN KEY (email) REFERENCES user(email)
);
```

By default the app connects with:

```
URL:      jdbc:mysql://localhost:3306/banking_system
Username: root
Password: root
```

Update these constants at the top of `BankingApp.java` to match your local MySQL setup.

## Getting Started

### Prerequisites

- JDK 8 or higher
- MySQL Server (running locally)
- MySQL Connector/J (JDBC driver) on the classpath

### Compile

```bash
javac -cp ".:mysql-connector-j-x.x.x.jar" BankingManagmentSystem/*.java -d out
```

### Run

```bash
java -cp "out:mysql-connector-j-x.x.x.jar" BankingApp
```

> On Windows, replace `:` with `;` in the classpath.

## Usage

1. Start the application — you'll see the main menu (Register / Login / Exit).
2. **Register** a new user with your name, email, and password.
3. **Login** with your credentials.
4. If you don't have a bank account yet, you'll be prompted to open one with an initial deposit and a security PIN.
5. Once logged in, use the account menu to:
   - Debit money
   - Credit money
   - Transfer money to another account
   - Check your balance
   - Log out

## Known Limitations

- Passwords and security PINs are stored in plain text — not suitable for production use.
- One bank account per user email.
- No password/PIN hashing or input validation/sanitization beyond prepared statements.
- Single-currency, single-branch design with no transaction history logging.
