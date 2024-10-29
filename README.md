# Bank of Java

**Bank of Java** is a command-line banking application implemented in Java using JDBC for interacting with a MySQL database. This application showcases basic banking functionalities such as account management, balance operations, money transfers, and transaction logging.

## Features

- **Database Setup**: Automatically creates a database named `BOJ` and necessary tables if they do not already exist.
- **Account Management**: Create new accounts with unique account numbers, names, passwords, and initial balances.
- **User Authentication**: Login system to access account functionalities.
- **Balance Operations**: Check and update account balances.
- **Money Transfers**: Transfer money between existing accounts with validation for sufficient balance and recipient existence.
- **Transaction History**: Logs and displays all transactions including transfers, with details on amounts, sender, recipient, and transaction dates.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Database Server
- MySQL Connector/J (JDBC driver)

### Setup

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/YashGarg1122/bankofjava.git
    cd BankOfJava
    ```

2. **Configure Database**:
    - Ensure MySQL server is running.
    - Update the `DB_URL`, `USER`, and `PASS` in the `BankOfJava.java` file with your MySQL server details.

3. **Compile and Run**:
    - Compile the Java program:
      ```bash
      javac BankOfJava.java
      ```
    - Run the program:
      ```bash
      java BankOfJava
      ```

## Usage

1. **Create an Account**:
    - Select the "Create Account" option.
    - Enter a 3-digit account number, your name, a password, and an initial balance.

2. **Login**:
    - Select the "Login" option.
    - Enter your account number and password to access your account dashboard.

3. **Account Dashboard**:
    - **Check Balance**: View your current balance.
    - **Add Balance**: Add funds to your account.
    - **Transfer Money**: Transfer money to another account. Make sure the recipient account exists.
    - **View Transactions**: See a history of all transactions involving your account.
    - **Logout**: Exit the dashboard and return to the main menu.

## Database Schema

- **`accounts` Table**:
  - `account_number` (INT): Unique account number.
  - `name` (VARCHAR(100)): Account holder's name.
  - `password` (VARCHAR(100)): Account password.
  - `balance` (DOUBLE): Current account balance.

- **`transactions` Table**:
  - `transaction_id` (INT AUTO_INCREMENT PRIMARY KEY): Unique transaction identifier.
  - `account_from` (INT): Sender's account number.
  - `account_to` (INT): Recipient's account number.
  - `amount` (DOUBLE): Amount of money transferred.
  - `transaction_date` (DATETIME): Date and time of the transaction.

## License

This project is licensed under the GPL 3 License.

## Contributing

Feel free to submit issues or pull requests. Your contributions are welcome!

## Contact

For any questions or feedback, please contact [connect.yashgarg@gmail.com](mailto:connect.yashgarg@gmail.com).
