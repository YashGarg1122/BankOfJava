import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BankOfJava {
    static final String DB_URL = "jdbc:mysql://localhost:3306/";
    static final String USER = "root";
    static final String PASS = "--Your MySQL Password--";
    static final String DB_NAME = "BOJ";
    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); 
             Statement stmt = conn.createStatement()) {
            
            // Create Database if not exists
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);
            System.out.println("Database created or already exists.");
            
            // Use the database
            stmt.executeUpdate("USE " + DB_NAME);
            
            // Create accounts and transactions tables if not exist
            String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                                         "account_number INT PRIMARY KEY," +  // Changed to INT for 3-digit account number
                                         "name VARCHAR(100)," +
                                         "password VARCHAR(100)," +
                                         "balance DOUBLE)";
            stmt.executeUpdate(createAccountsTable);
            
            String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                                             "transaction_id INT AUTO_INCREMENT PRIMARY KEY," +
                                             "account_from INT," +   // Adjusted to INT for 3-digit account number
                                             "account_to INT," +     // Adjusted to INT for 3-digit account number
                                             "amount DOUBLE," +
                                             "transaction_date DATETIME)";
            stmt.executeUpdate(createTransactionsTable);
            
            System.out.println("Tables created or already exist.");
            
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            
            while (running) {
                System.out.println("\n--- Bank of Java ---");
                System.out.println("1. Create Account");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        createAccount(conn, scanner);
                        break;
                    case 2:
                        login(conn, scanner);
                        break;
                    case 3:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void createAccount(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter your 3-digit account number: ");
        int accountNumber = scanner.nextInt();  // Using int for 3-digit account number
        scanner.nextLine(); // To consume the leftover newline
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        
        String insertAccountSQL = "INSERT INTO accounts (account_number, name, password, balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertAccountSQL)) {
            pstmt.setInt(1, accountNumber);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setDouble(4, balance);
            pstmt.executeUpdate();
            System.out.println("Account created successfully!");
        }
    }
    
    public static void login(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        scanner.nextLine(); // To consume the leftover newline
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        String selectAccountSQL = "SELECT * FROM accounts WHERE account_number = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectAccountSQL)) {
            pstmt.setInt(1, accountNumber);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Login successful!");
                userDashboard(conn, scanner, accountNumber);
            } else {
                System.out.println("Invalid account number or password.");
            }
        }
    }
    
    public static void userDashboard(Connection conn, Scanner scanner, int accountNumber) throws SQLException {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n--- Dashboard ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Add Balance");
            System.out.println("3. Transfer Money");
            System.out.println("4. View Transactions");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    checkBalance(conn, accountNumber);
                    break;
                case 2:
                    addBalance(conn, scanner, accountNumber);
                    break;
                case 3:
                    transferMoney(conn, scanner, accountNumber);
                    break;
                case 4:
                    viewTransactions(conn, accountNumber);
                    break;
                case 5:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    public static void checkBalance(Connection conn, int accountNumber) throws SQLException {
        String selectBalanceSQL = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectBalanceSQL)) {
            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Current Balance: " + balance);
            }
        }
    }
    
    public static void addBalance(Connection conn, Scanner scanner, int accountNumber) throws SQLException {
        System.out.print("Enter amount to add: ");
        double amount = scanner.nextDouble();
        
        String updateBalanceSQL = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateBalanceSQL)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountNumber);
            pstmt.executeUpdate();
            System.out.println("Balance updated successfully.");
        }
    }
    
    public static void transferMoney(Connection conn, Scanner scanner, int accountFrom) throws SQLException {
        System.out.print("Enter recipient account number: ");
        int accountTo = scanner.nextInt();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        
        // Check if the recipient account exists
        String checkRecipientSQL = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkRecipientSQL)) {
            pstmt.setInt(1, accountTo);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            if (count == 0) {
                System.out.println("Recipient account does not exist.");
                return;
            }
        }
        
        // Check if the account has sufficient balance
        String checkBalanceSQL = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkBalanceSQL)) {
            pstmt.setInt(1, accountFrom);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance < amount) {
                    System.out.println("Insufficient balance.");
                    return;
                }
            }
        }
        
        // Update sender's balance
        String updateSenderBalanceSQL = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSenderBalanceSQL)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountFrom);
            pstmt.executeUpdate();
        }
        
        // Update recipient's balance
        String updateRecipientBalanceSQL = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateRecipientBalanceSQL)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountTo);
            pstmt.executeUpdate();
        }
        
        // Log the transaction
        String insertTransactionSQL = "INSERT INTO transactions (account_from, account_to, amount, transaction_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertTransactionSQL)) {
            pstmt.setInt(1, accountFrom);
            pstmt.setInt(2, accountTo);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            pstmt.executeUpdate();
        }
        
        System.out.println("Money transferred successfully.");
    }    
    
    public static void viewTransactions(Connection conn, int accountNumber) throws SQLException {
        String selectTransactionsSQL = "SELECT * FROM transactions WHERE account_from = ? OR account_to = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectTransactionsSQL)) {
            pstmt.setInt(1, accountNumber);
            pstmt.setInt(2, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\n--- Transactions ---");
            while (rs.next()) {
                int accountFrom = rs.getInt("account_from");
                int accountTo = rs.getInt("account_to");
                double amount = rs.getDouble("amount");
                String date = rs.getString("transaction_date");
                
                System.out.printf("From: %d To: %d Amount: %.2f Date: %s\n", accountFrom, accountTo, amount, date);
            }
        }
    }
}
