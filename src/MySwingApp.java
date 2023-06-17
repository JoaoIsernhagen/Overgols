import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MySwingApp extends JFrame {
    public MySwingApp() {
        super("My Swing App");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(123, 112, 246)); // Violeta escuro

        // Adicionar o rótulo "Cadastro" acima da janela
        JLabel titleLabel = new JLabel("Cadastro");
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
        formPanel.setBackground(new Color(238, 130, 238)); // Violeta escuro
        formPanel.setLayout(new GridBagLayout());

        // Adicionar borda fina ao redor do formPanel
        Border formPanelBorder = BorderFactory.createLineBorder(new Color(75, 0, 130), 1, true);
        formPanel.setBorder(formPanelBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel label1 = new JLabel("Nome:");
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

        JLabel label2 = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(label2, gbc);

        JTextField textField2 = new JTextField();
        textField2.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(textField2, gbc);

        JLabel label3 = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(label3, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        JButton button = new JButton("Cadastrar");
        button.setPreferredSize(new Dimension(120, 35)); // Definindo uma largura e altura personalizadas
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 80, 10, 80);
        formPanel.add(button, gbc);

        shadowPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        mainPanel.add(shadowPanel, gbcMain);

        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Ação do botão "Cadastrar"
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui você pode adicionar a lógica para processar o cadastro

                // Exibir a mensagem de "Cadastro concluído"
                JOptionPane.showMessageDialog(MySwingApp.this, "Cadastro concluído");

                // Voltar para a tela de login
                dispose(); // Fecha a tela de cadastro
                new LoginScreen(); // Abre a tela de login novamente
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MySwingApp::new);
    }
}
