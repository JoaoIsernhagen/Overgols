import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySwingApp extends JFrame {
    private BancoDeDados bancoDeDados;

    /**
     * Construtor da classe MySwingApp.
     * Cria uma instância da classe MySwingApp e configura a interface gráfica.
     */
    public MySwingApp() {
        super("Cadastro"); // Define o título da janela como "Cadastro"

        // Crie uma instância da classe BancoDeDados
        bancoDeDados = new BancoDeDados("root", "Gdyp07@o");

        setSize(800, 600); // Define o tamanho da janela

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(49, 55, 72)); // Define a cor de fundo como um tom escuro de cinza

        // Adicionar o rótulo "Cadastro" acima da janela
        JLabel titleLabel = new JLabel("Cadastro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE); // Define a cor do texto como branco
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Adiciona uma margem ao redor do rótulo
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(54, 59, 78)); // Define a cor de fundo como um tom escuro de verde
        formPanel.setLayout(new GridBagLayout());

        // Adicionar borda fina ao redor do formPanel
        Border formPanelBorder = BorderFactory.createLineBorder(new Color(87, 187, 98), 1, true);
        formPanel.setBorder(formPanelBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Rótulo e campo de texto para o nome
        JLabel label1 = new JLabel("Nome:");
        label1.setForeground(Color.WHITE); // Define a cor do texto como branco
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(label1, gbc);

        JTextField textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(textField1, gbc);

        // Rótulo e campo de texto para o email
        JLabel label2 = new JLabel("Email:");
        label2.setForeground(Color.WHITE); // Define a cor do texto como branco
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(label2, gbc);

        JTextField textField2 = new JTextField();
        textField2.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(textField2, gbc);

        // Rótulo e campo de senha
        JLabel label3 = new JLabel("Senha:");
        label3.setForeground(Color.WHITE); // Define a cor do texto como branco
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(label3, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        // Botão "Cadastrar"
        JButton button = new JButton("Cadastrar");
        button.setPreferredSize(new Dimension(120, 35)); // Define uma largura e altura personalizadas para o botão
        button.setBackground(new Color(87, 187, 98)); // Define a cor de fundo do botão como um tom claro de verde
        button.setForeground(Color.WHITE); // Define a cor do texto do botão como branco
        button.setFocusPainted(false); // Remove o contorno de foco quando o botão é selecionado
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 80, 10, 80); // Define as margens internas do botão
        formPanel.add(button, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Ação do botão "Cadastrar"
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenha os dados do usuário do JTextField e JPasswordField
                String nome = textField1.getText();
                String email = textField2.getText();
                String senha = new String(passwordField.getPassword());

                // Insira os dados do usuário no banco de dados
                Connection connection = bancoDeDados.conectar();
                if (connection != null) {
                    try {
                        String sql = "INSERT INTO usuario (nomeusuario, emailusuario, senhausuario) VALUES (?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, nome);
                        statement.setString(2, email);
                        statement.setString(3, senha);
                        statement.executeUpdate();
                        statement.close();
                        bancoDeDados.fecharConexao(connection);

                        // Exibir a mensagem de "Cadastro concluído"
                        JOptionPane.showMessageDialog(MySwingApp.this, "Cadastro concluído");

                        // Voltar para a tela de login
                        dispose(); // Fecha a tela de cadastro
                        new LoginScreen(); // Abre a tela de login novamente
                    } catch (SQLException ex) {
                        System.out.println("Erro ao inserir dados no banco de dados: " + ex.getMessage());
                    }
                } else {
                    System.out.println("Não foi possível estabelecer conexão com o banco de dados.");
                }
            }
        });
    }

    /**
     * Método principal para iniciar o aplicativo Swing.
     *
     * @param args os argumentos de linha de comando
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MySwingApp::new);
    }
}