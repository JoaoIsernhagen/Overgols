import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen extends JFrame {
    private String username;

    public LoginScreen() {
        super("Login Screen");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(123, 112, 246)); // Violeta escuro


        // Adicionar o rótulo "Login" acima da janela
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(75, 0, 130)); // Azul violeta
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(titleLabel, titleGbc);

        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                int shadowSize = 10;
                int shadowOpacity = 100;
                int width = getWidth();
                int height = getHeight();
                Color shadowColor = new Color(0, 0, 0, shadowOpacity);
                g2d.setColor(shadowColor);
                g2d.fillRect(shadowSize, shadowSize, width - 2 * shadowSize, height - 2 * shadowSize);
                g2d.dispose();
            }
        };
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remover a borda

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(172, 189, 61)); // Violeta escuro
        formPanel.setLayout(new GridBagLayout());

        // Adicionar borda fina ao redor do formPanel
        Border formPanelBorder = BorderFactory.createLineBorder(new Color(0, 0, 0), 1, true);
        formPanel.setBorder(formPanelBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Usuário:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userLabel, gbc);

        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(userField, gbc);

        JLabel passwordLabel = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Entrar");
        loginButton.setPreferredSize(new Dimension(120, 35)); // Definindo uma largura e altura personalizadas
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 80, 10, 80);
        formPanel.add(loginButton, gbc);

        JLabel registerLabel = new JLabel("Ainda não sou cadastrado");
        registerLabel.setForeground(new Color(0, 0, 0)); // Azul violeta
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
        formPanel.add(registerLabel, gbc);

        shadowPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        mainPanel.add(shadowPanel, gbcMain);

        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Defina a largura e a altura desejadas
        setLocationRelativeTo(null); // Centralize a janela na tela
        setUndecorated(true); // Remover a barra de título (opcional)
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
