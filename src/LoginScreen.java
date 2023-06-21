import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class LoginScreen extends JFrame {
    private String username;

    public LoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 600); // Define the size of the window

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(49, 55, 72)); // Dark background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(54, 59, 78)); // Dark green
        formPanel.setLayout(new GridBagLayout());

        // Add rounded border to formPanel
        Border formPanelBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        formPanel.setBorder(formPanelBorder);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE); // White
        userLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Custom font
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userLabel, gbc);

        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(300, 35));
        userField.setFont(new Font("Arial", Font.PLAIN, 14)); // Custom font
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(userField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE); // White
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Custom font
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14)); // Custom font
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40)); // Custom dimensions
        loginButton.setBackground(new Color(87, 187, 98)); // Light green
        loginButton.setForeground(Color.WHITE); // White
        loginButton.setFocusPainted(false); // Remove highlight when focused
        loginButton.setFont(new Font("Arial", Font.BOLD, 14)); // Custom font
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 10, 0); // Custom spacing
        formPanel.add(loginButton, gbc);

        JLabel registerLabel = new JLabel("Not registered yet?");
        registerLabel.setForeground(Color.WHITE); // White
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Custom font
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the login screen
                new MySwingApp(); // Open the registration screen
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0); // Custom spacing
        formPanel.add(registerLabel, gbc);

        mainPanel.add(formPanel, gbc);

        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the login window
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwordField.getPassword());

                Usuario telaUsuario = new Usuario(username, password, loginButton);
                telaUsuario.autenticar();
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen();
            }
        });
    }
}