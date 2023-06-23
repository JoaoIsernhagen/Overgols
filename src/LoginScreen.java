import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * A classe LoginScreen representa a tela de login de um aplicativo.
 */
public class LoginScreen extends JFrame {
    private String username;

    /**
     * Cria uma instância da classe LoginScreen e configura a janela de login.
     */
    public LoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 600); // Define o tamanho da janela

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(49, 55, 72)); // Cor de fundo escura

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(54, 59, 78)); // Verde escuro
        formPanel.setLayout(new GridBagLayout());

        // Adiciona uma borda arredondada ao formPanel
        Border formPanelBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        formPanel.setBorder(formPanelBorder);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE); // Branco
        userLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte personalizada
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userLabel, gbc);

        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(300, 35));
        userField.setFont(new Font("Arial", Font.PLAIN, 14)); // Fonte personalizada
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(userField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE); // Branco
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte personalizada
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14)); // Fonte personalizada
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40)); // Dimensões personalizadas
        loginButton.setBackground(new Color(87, 187, 98)); // Verde claro
        loginButton.setForeground(Color.WHITE); // Branco
        loginButton.setFocusPainted(false); // Remove o destaque quando está focado
        loginButton.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte personalizada
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 10, 0); // Espaçamento personalizado
        formPanel.add(loginButton, gbc);

        JLabel registerLabel = new JLabel("Não tenho cadastro");
        registerLabel.setForeground(Color.WHITE); // Branco
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Fonte personalizada
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Fecha a tela de login
                new MySwingApp(); // Abre a tela de registro
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0); // Espaçamento personalizado
        formPanel.add(registerLabel, gbc);

        mainPanel.add(formPanel, gbc);

        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas a janela de login
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

    /**
     * Método principal que cria uma instância da classe LoginScreen.
     * @param args os argumentos de linha de comando (não são utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen();
            }
        });
    }
}
