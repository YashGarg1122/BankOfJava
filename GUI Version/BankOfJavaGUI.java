import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BankOfJavaGUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    private JPanel initialPanel;
    private JPanel createAccountPanel;
    private JPanel loginPanel;
    private JPanel dashboardPanel;

    // Dashboard Components
    private JLabel welcomeLabel;
    private int loggedInAccountNumber;

    // Database Credentials
    static final String DB_URL = "jdbc:mysql://localhost:3306/BOJ"; // Replace with your DB URL
    static final String USER = "root"; // Replace with your DB username
    static final String PASS = "7796"; // Replace with your DB password

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BankOfJavaGUI window = new BankOfJavaGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BankOfJavaGUI() {
        initialize();
    }

    private void initialize() {
        // Setup Frame
        frame = new JFrame("Bank of Java");
        frame.setBounds(100, 100, 700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Setup CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.getContentPane().add(mainPanel);

        // Initialize Panels
        initializeInitialPanel();
        initializeCreateAccountPanel();
        initializeLoginPanel();
        initializeDashboardPanel();

        // Show Initial Panel
        cardLayout.show(mainPanel, "initialPanel");
    }

    // Initialize Initial Panel with Create Account, Login, and Exit buttons
    private void initializeInitialPanel() {
        initialPanel = new JPanel();
        initialPanel.setLayout(null);
        initialPanel.setBackground(new Color(52, 152, 219));

        JLabel titleLabel = new JLabel("Welcome to Bank of Java");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(200, 50, 300, 30);
        initialPanel.add(titleLabel);

        // Create Account Button
        JButton btnCreateAccount = new JButton("Create Account");
        styleButton(btnCreateAccount);
        btnCreateAccount.setBounds(250, 150, 200, 40);
        btnCreateAccount.addActionListener(e -> cardLayout.show(mainPanel, "createAccountPanel"));
        initialPanel.add(btnCreateAccount);

        // Login Button
        JButton btnLogin = new JButton("Login");
        styleButton(btnLogin);
        btnLogin.setBounds(250, 220, 200, 40);
        btnLogin.addActionListener(e -> cardLayout.show(mainPanel, "loginPanel"));
        initialPanel.add(btnLogin);

        // Exit Button
        JButton btnExit = new JButton("Exit");
        styleButton(btnExit);
        btnExit.setBounds(250, 290, 200, 40);
        btnExit.addActionListener(e -> System.exit(0));
        initialPanel.add(btnExit);

        mainPanel.add(initialPanel, "initialPanel");
    }

    // Initialize Create Account Panel
    private void initializeCreateAccountPanel() {
        createAccountPanel = new JPanel();
        createAccountPanel.setLayout(null);
        createAccountPanel.setBackground(new Color(46, 204, 113));

        JLabel lblTitle = new JLabel("Create New Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(250, 30, 250, 30);
        createAccountPanel.add(lblTitle);

        // Account Number
        JLabel lblAccountNumber = new JLabel("Account Number:");
        lblAccountNumber.setFont(new Font("Arial", Font.PLAIN, 16));
        lblAccountNumber.setForeground(Color.WHITE);
        lblAccountNumber.setBounds(200, 100, 150, 25);
        createAccountPanel.add(lblAccountNumber);

        JTextField txtAccountNumber = new JTextField();
        txtAccountNumber.setBounds(360, 100, 200, 25);
        createAccountPanel.add(txtAccountNumber);

        // Name
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.PLAIN, 16));
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(200, 150, 150, 25);
        createAccountPanel.add(lblName);

        JTextField txtName = new JTextField();
        txtName.setBounds(360, 150, 200, 25);
        createAccountPanel.add(txtName);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(200, 200, 150, 25);
        createAccountPanel.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(360, 200, 200, 25);
        createAccountPanel.add(txtPassword);

        // Initial Balance
        JLabel lblBalance = new JLabel("Initial Balance:");
        lblBalance.setFont(new Font("Arial", Font.PLAIN, 16));
        lblBalance.setForeground(Color.WHITE);
        lblBalance.setBounds(200, 250, 150, 25);
        createAccountPanel.add(lblBalance);

        JTextField txtBalance = new JTextField();
        txtBalance.setBounds(360, 250, 200, 25);
        createAccountPanel.add(txtBalance);

        // Create Button
        JButton btnCreate = new JButton("Create");
        styleButton(btnCreate);
        btnCreate.setBounds(240, 320, 120, 40);
        btnCreate.addActionListener(e -> {
            try {
                int accountNumber = Integer.parseInt(txtAccountNumber.getText().trim());
                String name = txtName.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                double balance = Double.parseDouble(txtBalance.getText().trim());

                if (name.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                createAccount(accountNumber, name, password, balance);
                JOptionPane.showMessageDialog(frame, "Account created successfully!");
                clearCreateAccountFields(txtAccountNumber, txtName, txtPassword, txtBalance);
                cardLayout.show(mainPanel, "initialPanel");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error creating account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        createAccountPanel.add(btnCreate);

        // Back Button
        JButton btnBack = new JButton("Back");
        styleButton(btnBack);
        btnBack.setBounds(380, 320, 120, 40);
        btnBack.addActionListener(e -> {
            clearCreateAccountFields(txtAccountNumber, txtName, txtPassword, txtBalance);
            cardLayout.show(mainPanel, "initialPanel");
        });
        createAccountPanel.add(btnBack);

        mainPanel.add(createAccountPanel, "createAccountPanel");
    }

    // Initialize Login Panel
    private void initializeLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(231, 76, 60));

        JLabel lblTitle = new JLabel("Login to Your Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(240, 30, 250, 30);
        loginPanel.add(lblTitle);

        // Account Number
        JLabel lblAccountNumber = new JLabel("Account Number:");
        lblAccountNumber.setFont(new Font("Arial", Font.PLAIN, 16));
        lblAccountNumber.setForeground(Color.WHITE);
        lblAccountNumber.setBounds(200, 120, 150, 25);
        loginPanel.add(lblAccountNumber);

        JTextField txtAccountNumber = new JTextField();
        txtAccountNumber.setBounds(360, 120, 200, 25);
        loginPanel.add(txtAccountNumber);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(200, 170, 150, 25);
        loginPanel.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(360, 170, 200, 25);
        loginPanel.add(txtPassword);

        // Login Button
        JButton btnLogin = new JButton("Login");
        styleButton(btnLogin);
        btnLogin.setBounds(240, 240, 120, 40);
        btnLogin.addActionListener(e -> {
            try {
                int accountNumber = Integer.parseInt(txtAccountNumber.getText().trim());
                String password = new String(txtPassword.getPassword()).trim();

                if (login(accountNumber, password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    showDashboard();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid account number or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid account number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        loginPanel.add(btnLogin);

        // Back Button
        JButton btnBack = new JButton("Back");
        styleButton(btnBack);
        btnBack.setBounds(380, 240, 120, 40);
        btnBack.addActionListener(e -> {
            clearLoginFields(txtAccountNumber, txtPassword);
            cardLayout.show(mainPanel, "initialPanel");
        });
        loginPanel.add(btnBack);

        mainPanel.add(loginPanel, "loginPanel");
    }

    // Initialize Dashboard Panel
    private void initializeDashboardPanel() {
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(null);
        dashboardPanel.setBackground(new Color(52, 73, 94));

        // Welcome Label
        welcomeLabel = new JLabel("Welcome, Account #");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(50, 20, 600, 30);
        dashboardPanel.add(welcomeLabel);

        // Buttons
        JButton btnCheckBalance = new JButton("Check Balance");
        styleButton(btnCheckBalance);
        btnCheckBalance.setBounds(100, 80, 200, 50);
        btnCheckBalance.addActionListener(e -> checkBalance());
        dashboardPanel.add(btnCheckBalance);

        JButton btnAddBalance = new JButton("Add Balance");
        styleButton(btnAddBalance);
        btnAddBalance.setBounds(400, 80, 200, 50);
        btnAddBalance.addActionListener(e -> addBalance());
        dashboardPanel.add(btnAddBalance);

        JButton btnTransferMoney = new JButton("Transfer Money");
        styleButton(btnTransferMoney);
        btnTransferMoney.setBounds(100, 160, 200, 50);
        btnTransferMoney.addActionListener(e -> transferMoney());
        dashboardPanel.add(btnTransferMoney);

        JButton btnViewTransactions = new JButton("View Transactions");
        styleButton(btnViewTransactions);
        btnViewTransactions.setBounds(400, 160, 200, 50);
        btnViewTransactions.addActionListener(e -> viewTransactions());
        dashboardPanel.add(btnViewTransactions);

        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout);
        btnLogout.setBounds(250, 240, 200, 50);
        btnLogout.addActionListener(e -> logout());
        dashboardPanel.add(btnLogout);

        mainPanel.add(dashboardPanel, "dashboardPanel");
    }

    // Method to style buttons consistently
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(41, 128, 185));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Method to create a new account in the database
    private void createAccount(int accountNumber, String name, String password, double balance) throws SQLException {
        String insertAccountSQL = "INSERT INTO accounts (account_number, name, password, balance) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(insertAccountSQL)) {
            pstmt.setInt(1, accountNumber);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setDouble(4, balance);
            pstmt.executeUpdate();
        }
    }

    // Method to clear Create Account fields
    private void clearCreateAccountFields(JTextField accNum, JTextField name, JPasswordField pwd, JTextField balance) {
        accNum.setText("");
        name.setText("");
        pwd.setText("");
        balance.setText("");
    }

    // Method to clear Login fields
    private void clearLoginFields(JTextField accNum, JPasswordField pwd) {
        accNum.setText("");
        pwd.setText("");
    }

    // Method to handle user login
    private boolean login(int accountNumber, String password) throws SQLException {
        String selectAccountSQL = "SELECT * FROM accounts WHERE account_number = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(selectAccountSQL)) {
            pstmt.setInt(1, accountNumber);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                loggedInAccountNumber = accountNumber;
                return true;
            }
        }
        return false;
    }

    // Method to display the dashboard
    private void showDashboard() {
        welcomeLabel.setText("Welcome to your Dashboard, Account #" + loggedInAccountNumber + "!");
        cardLayout.show(mainPanel, "dashboardPanel");
    }

    // Method to check account balance
    private void checkBalance() {
        String selectBalanceSQL = "SELECT balance FROM accounts WHERE account_number = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(selectBalanceSQL)) {
            pstmt.setInt(1, loggedInAccountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                JOptionPane.showMessageDialog(frame, "Your Current Balance: $" + String.format("%.2f", balance), "Balance", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error fetching balance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add balance to account
    private void addBalance() {
        String input = JOptionPane.showInputDialog(frame, "Enter amount to add:", "Add Balance", JOptionPane.PLAIN_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String updateBalanceSQL = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement pstmt = conn.prepareStatement(updateBalanceSQL)) {
                    pstmt.setDouble(1, amount);
                    pstmt.setInt(2, loggedInAccountNumber);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Balance added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to add balance.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error updating balance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to transfer money between accounts
    private void transferMoney() {
        JPanel transferPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        transferPanel.setPreferredSize(new Dimension(400, 200));

        JLabel lblRecipient = new JLabel("Recipient Account Number:");
        JTextField txtRecipient = new JTextField();
        JLabel lblAmount = new JLabel("Amount to Transfer:");
        JTextField txtAmount = new JTextField();

        transferPanel.add(lblRecipient);
        transferPanel.add(txtRecipient);
        transferPanel.add(lblAmount);
        transferPanel.add(txtAmount);

        int result = JOptionPane.showConfirmDialog(frame, transferPanel, "Transfer Money", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int recipientAccount = Integer.parseInt(txtRecipient.getText().trim());
                double amount = Double.parseDouble(txtAmount.getText().trim());

                if (recipientAccount == loggedInAccountNumber) {
                    JOptionPane.showMessageDialog(frame, "Cannot transfer to the same account.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                performTransfer(loggedInAccountNumber, recipientAccount, amount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error during transfer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to perform the transfer operation
    private void performTransfer(int fromAccount, int toAccount, double amount) throws SQLException {
        String checkRecipientSQL = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        String checkBalanceSQL = "SELECT balance FROM accounts WHERE account_number = ?";
        String updateBalanceSQL = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        String insertTransactionSQL = "INSERT INTO transactions (account_from, account_to, amount, transaction_date) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            conn.setAutoCommit(false); // Start transaction

            // Check if recipient exists
            try (PreparedStatement pstmt = conn.prepareStatement(checkRecipientSQL)) {
                pstmt.setInt(1, toAccount);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                if (count == 0) {
                    JOptionPane.showMessageDialog(frame, "Recipient account does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    conn.rollback();
                    return;
                }
            }

            // Check sender's balance
            double currentBalance;
            try (PreparedStatement pstmt = conn.prepareStatement(checkBalanceSQL)) {
                pstmt.setInt(1, fromAccount);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    currentBalance = rs.getDouble("balance");
                    if (currentBalance < amount) {
                        JOptionPane.showMessageDialog(frame, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
                        conn.rollback();
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Sender account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    conn.rollback();
                    return;
                }
            }

            // Deduct amount from sender
            try (PreparedStatement pstmt = conn.prepareStatement(updateBalanceSQL)) {
                pstmt.setDouble(1, -amount);
                pstmt.setInt(2, fromAccount);
                pstmt.executeUpdate();
            }

            // Add amount to recipient
            try (PreparedStatement pstmt = conn.prepareStatement(updateBalanceSQL)) {
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, toAccount);
                pstmt.executeUpdate();
            }

            // Record the transaction
            try (PreparedStatement pstmt = conn.prepareStatement(insertTransactionSQL)) {
                pstmt.setInt(1, fromAccount);
                pstmt.setInt(2, toAccount);
                pstmt.setDouble(3, amount);
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            JOptionPane.showMessageDialog(frame, "Transfer successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            throw ex;
        }
    }

    // Method to view transaction history
    private void viewTransactions() {
        String selectTransactionsSQL = "SELECT * FROM transactions WHERE account_from = ? OR account_to = ? ORDER BY transaction_date DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(selectTransactionsSQL)) {
            pstmt.setInt(1, loggedInAccountNumber);
            pstmt.setInt(2, loggedInAccountNumber);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder transactions = new StringBuilder();
            transactions.append("Transaction History:\n\n");

            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                int from = rs.getInt("account_from");
                int to = rs.getInt("account_to");
                double amount = rs.getDouble("amount");
                String date = rs.getString("transaction_date");

                transactions.append(String.format("ID: %d | From: %d | To: %d | Amount: $%.2f | Date: %s\n",
                        transactionId, from, to, amount, date));
            }

            if (transactions.length() == 22) { // Only header exists
                transactions.append("No transactions found.");
            }

            JTextArea textArea = new JTextArea(transactions.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(frame, scrollPane, "Transactions", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error fetching transactions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to logout and return to initial panel
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            loggedInAccountNumber = 0;
            cardLayout.show(mainPanel, "initialPanel");
        }
    }
}
