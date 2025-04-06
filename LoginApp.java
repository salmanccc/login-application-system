import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class LoginApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

class LoginFrame extends JFrame {
    private JTextField usernameField, userIdField;
    private JPasswordField passwordField;
    private JCheckBox showPassword, rememberMe;
    private JButton loginButton, switchThemeButton, resetButton;
    private boolean isDarkMode = true;
    private Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);

    private Color lightBg = Color.WHITE;
    private Color lightFg = Color.BLACK;
    private Color darkBg = new Color(30, 30, 30);
    private Color darkFg = Color.WHITE;
    private Color btnColorDark = new Color(70, 130, 180); // steel blue
    private Color btnColorLight = new Color(100, 149, 237); // cornflower blue

    public LoginFrame() {
        setTitle("Login Application Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Login Here");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JLabel userIdLabel = new JLabel("User ID:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userIdLabel, gbc);

        userIdField = new JTextField(15);
        gbc.gridx = 1;
        add(userIdField, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        showPassword = new JCheckBox("Show Password");
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(showPassword, gbc);

        rememberMe = new JCheckBox("Remember Me");
        gbc.gridy = 5;
        add(rememberMe, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");
        switchThemeButton = new JButton("Switch Theme");

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(switchThemeButton);

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Load remembered credentials
        loadRememberedCredentials();

        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*');
        });

        loginButton.addActionListener(e -> {
            String userId = userIdField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (userId.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (rememberMe.isSelected()) {
                prefs.put("userId", userId);
                prefs.put("username", username);
                prefs.put("password", password);
            } else {
                prefs.remove("userId");
                prefs.remove("username");
                prefs.remove("password");
            }

            if (username.equals("admin") && password.equals("1234")) {
                JOptionPane.showMessageDialog(this,
                        "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetButton.addActionListener(e -> resetForm());

        switchThemeButton.addActionListener(e -> toggleTheme());

        toggleTheme(); // Apply initial theme

        setVisible(true);
    }

    private void loadRememberedCredentials() {
        String userId = prefs.get("userId", "");
        String username = prefs.get("username", "");
        String password = prefs.get("password", "");

        if (!userId.isEmpty() || !username.isEmpty() || !password.isEmpty()) {
            userIdField.setText(userId);
            usernameField.setText(username);
            passwordField.setText(password);
            rememberMe.setSelected(true);
        }
    }

    private void resetForm() {
        userIdField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        rememberMe.setSelected(false);
        userIdField.requestFocus();
    }

    private void toggleTheme() {
        Color bgColor = isDarkMode ? lightBg : darkBg;
        Color fgColor = isDarkMode ? lightFg : darkFg;
        Color btnColor = isDarkMode ? btnColorLight : btnColorDark;

        getContentPane().setBackground(bgColor);

        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JLabel || c instanceof JCheckBox) {
                c.setForeground(fgColor);
                c.setBackground(bgColor);
            }
            if (c instanceof JTextField || c instanceof JPasswordField) {
                c.setForeground(fgColor);
                c.setBackground(isDarkMode ? Color.WHITE : new Color(60, 60, 60));
            }
            if (c instanceof JPanel panel) {
                panel.setBackground(bgColor);
                for (Component b : panel.getComponents()) {
                    if (b instanceof JButton btn) {
                        btn.setForeground(Color.WHITE);
                        btn.setBackground(btnColor);
                    }
                }
            }
        }

        isDarkMode = !isDarkMode;
        repaint();
    }
}
