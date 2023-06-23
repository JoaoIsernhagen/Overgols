import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A classe UserOptionsInterface representa uma interface gráfica para permitir ao usuário trocar sua senha ou excluir sua conta.
 */
public class UserOptionsInterface extends JFrame {
    private String username;
    private Usuario usuario;

    /**
     * Cria uma instância da classe UserOptionsInterface com o nome de usuário fornecido.
     * @param username o nome de usuário.
     */
    public UserOptionsInterface(String username) {
        super("Trocar Senha");

        this.username = username;
        this.usuario = new Usuario(username, "", null); // Criar instância de Usuario

        setSize(400, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(49, 55, 72));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Nome do Usuário: " + username);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(nameLabel, gbc);

        JLabel newPasswordLabel = new JLabel("Nova Senha:");
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(newPasswordLabel, gbc);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setPreferredSize(new Dimension(200, 30));
        newPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(newPasswordField, gbc);

        JButton changePasswordButton = new JButton("Trocar Senha");
        changePasswordButton.setPreferredSize(new Dimension(150, 40));
        changePasswordButton.setBackground(new Color(41, 128, 185));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setFocusPainted(false);
        changePasswordButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(changePasswordButton, gbc);

        JButton deleteAccountButton = new JButton("Excluir Conta");
        deleteAccountButton.setPreferredSize(new Dimension(150, 40));
        deleteAccountButton.setBackground(new Color(192, 57, 43));
        deleteAccountButton.setForeground(Color.WHITE);
        deleteAccountButton.setFocusPainted(false);
        deleteAccountButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(deleteAccountButton, gbc);

        getContentPane().add(mainPanel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = new String(newPasswordField.getPassword());

                // Chamar o método updatePassword da instância de Usuario
                usuario.updatePassword(newPassword);
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Chamar o método deleteAccount da instância de Usuario
                usuario.deleteAccount();
            }
        });
    }

    /**
     * O método principal que cria uma instância da classe UserOptionsInterface com um nome de usuário fictício.
     * @param args os argumentos de linha de comando (não são utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserOptionsInterface("username");
            }
        });
    }
}
