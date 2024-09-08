import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateBankDatabaseAndTables {

    // Database URL (MySQL root), username and password
    static final String ROOT_DB_URL = "jdbc:mysql://localhost:3306/";  // MySQL root URL
    static final String DB_NAME = "boj";  // Your database name
    static final String USER = "root";  // Replace with your MySQL username
    static final String PASS = "7796";  // Replace with your MySQL password

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Register the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection to MySQL root
            System.out.println("Connecting to MySQL...");
            conn = DriverManager.getConnection(ROOT_DB_URL, USER, PASS);

            // Create a statement
            stmt = conn.createStatement();

            // SQL to create the database if it doesn't exist
            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(createDatabase);
            System.out.println("Database '" + DB_NAME + "' created or already exists!");

            // Now connect to the specific database 'boj'
            conn = DriverManager.getConnection(ROOT_DB_URL + DB_NAME, USER, PASS);
            stmt = conn.createStatement();

            // Create the accounts table
            String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts ("
                    + "account_number INT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "password VARCHAR(100) NOT NULL, "
                    + "balance DOUBLE DEFAULT 0"
                    + ");";
            stmt.executeUpdate(createAccountsTable);

            // Create the transactions table
            String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions ("
                    + "transaction_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "account_from INT, "
                    + "account_to INT, "
                    + "amount DOUBLE, "
                    + "transaction_date DATETIME, "
                    + "FOREIGN KEY (account_from) REFERENCES accounts(account_number), "
                    + "FOREIGN KEY (account_to) REFERENCES accounts(account_number)"
                    + ");";
            stmt.executeUpdate(createTransactionsTable);

            System.out.println("Tables created successfully in the database '" + DB_NAME + "'!");

        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Finally block to close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
