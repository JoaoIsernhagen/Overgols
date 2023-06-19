import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen extends JFrame {
    private String username;

    public LoginScreen() {
        super("Login Screen");

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicia maximizada
        setUndecorated(true); // Remove a barra de título

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(49, 55, 72)); // Fundo escuro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(54, 59, 78)); // Verde escuro
        formPanel.setLayout(new GridBagLayout());

        // Adicionar borda arredondada ao formPanel
        Border formPanelBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        formPanel.setBorder(formPanelBorder);

        JLabel userLabel = new JLabel("Usuário:");
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

        JLabel passwordLabel = new JLabel("Senha:");
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

        JButton loginButton = new JButton("Entrar");
        loginButton.setPreferredSize(new Dimension(120, 40)); // Dimensões personalizadas
        loginButton.setBackground(new Color(87, 187, 98)); // Verde claro
        loginButton.setForeground(Color.WHITE); // Branco
        loginButton.setFocusPainted(false); // Remover destaque ao ganhar foco
        loginButton.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte personalizada
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 10, 0); // Espaçamento personalizado
        formPanel.add(loginButton, gbc);

        JLabel registerLabel = new JLabel("Ainda não sou cadastrado");
        registerLabel.setForeground(Color.WHITE); // Branco
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Fonte personalizada
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dispose(); // Fechar a tela de login
                new MySwingApp(); // Abrir a tela de cadastro
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0); // Espaçamento personalizado
        formPanel.add(registerLabel, gbc);

        mainPanel.add(formPanel, gbc);

        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fechar apenas a janela de login
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwordField.getPassword());

                // Verificar se o usuário e a senha estão corretos no banco de dados
                BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
                Connection connection = bancoDeDados.conectar();
                if (connection != null) {
                    try {
                        String query = "SELECT * FROM usuario WHERE nomeusuario = ? AND senhausuario = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, username);
                        statement.setString(2, password);
                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            // Usuário autenticado com sucesso
                            LoginScreen.this.username = username; // atribuir o nome de usuário à variável da classe
                            dispose(); // Fechar a tela de login
                            OverGolsInterface overGolsInterface = new OverGolsInterface(username); // passar o nome de usuário para a classe OverGolsInterface
                            overGolsInterface.setSize(800, 600); // Defina a largura e a altura desejadas
                            overGolsInterface.setLocationRelativeTo(null); // Centralize a janela na tela
                            overGolsInterface.setUndecorated(true); // Remover a barra de título (opcional)
                            overGolsInterface.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(LoginScreen.this, "Usuário ou senha inválidos!",
                                    "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(LoginScreen.this,
                                "Erro ao executar a consulta: " + ex.getMessage(), "Erro de banco de dados",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        bancoDeDados.fecharConexao(connection);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Erro ao conectar ao banco de dados!",
                            "Erro de conexão", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}