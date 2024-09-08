```markdown
# Bank of Java

**Bank of Java** is a simple banking application implemented in Java using Swing for the GUI and MySQL for the database. It allows users to perform various banking operations such as checking balance, adding balance, transferring money, and viewing transactions.

## Features

- **Login**: Authenticate users using account number and password.
- **Check Balance**: View the current balance of the logged-in account.
- **Add Balance**: Deposit money into the account.
- **Transfer Money**: Transfer funds between accounts.
- **View Transactions**: View a history of transactions for the logged-in account.
- **Logout**: Securely log out of the application.

## Project Structure

1. **`BankOfJavaGUI.java`**: The main application code, which includes the GUI and core functionality for interacting with the database.
2. **`CreateBankDatabaseAndTables.java`**: A separate Java file used to create the database and tables required for the application.

## Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher.
- **MySQL Database**: Ensure MySQL is installed and running.
- **JDBC Driver**: MySQL Connector/J for connecting Java with MySQL.

## Setup

### 1. Create the Database and Tables

Run the `CreateBankDatabaseAndTables.java` file to set up the MySQL database and tables. This will create a database named `BOJ` and an `accounts` table with the necessary schema.

```bash
javac CreateBankDatabaseAndTables.java
java CreateBankDatabaseAndTables
```

### 2. Configure Database Connection

Update the database connection details in `BankOfJavaGUI.java`:

```java
static final String DB_URL = "jdbc:mysql://localhost:3306/BOJ";
static final String USER = "root";
static final String PASS = "your_password";
```

Replace `your_password` with your MySQL root user password.

### 3. Compile and Run the Application

Compile the `BankOfJavaGUI.java` file:

```bash
javac BankOfJavaGUI.java
```

Run the application:

```bash
java BankOfJavaGUI
```

## Usage

1. **Login**: Enter your account number and password to log in.
2. **Check Balance**: Click on the "Check Balance" button to view your current balance.
3. **Add Balance**: Click on the "Add Balance" button to deposit money into your account.
4. **Transfer Money**: Click on the "Transfer Money" button to send money to another account.
5. **View Transactions**: Click on the "View Transactions" button to see your transaction history.
6. **Logout**: Click on the "Logout" button to exit the application.

## Contributing

If you have suggestions or improvements, feel free to submit a pull request or open an issue.

## License

This project is licensed under the GPL 3.0 License.

## Contact

For any questions or feedback, please reach out to:

- **Yash Garg** - connect.yashgarg@gmail.com

## Acknowledgements

- **Swing** for building the graphical user interface.
- **MySQL** for the database management system.
- **JDBC** for database connectivity.
```
