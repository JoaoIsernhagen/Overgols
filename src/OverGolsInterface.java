import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OverGolsInterface extends JFrame {
    private Partidas partidas;
    private JTextPane dadosTextPane;
    private String nomeUsuario;

    public OverGolsInterface(String username) {
        this.nomeUsuario = username;

        // Create an instance of the database and the Partidas class
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        partidas = new Partidas(bancoDeDados);

        // Configure the window
        setTitle("Over Gols");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Header
        JPanel cabecalhoPanel = new JPanel(new BorderLayout());
        cabecalhoPanel.setBackground(new Color(54, 59, 78));
        cabecalhoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Loading the image from the file
        ImageIcon logo = new ImageIcon("C:\\Users\\joaor\\eclipse-workspace\\Overgols\\imagens\\fd2623dedfa19289f8110784f36dc541.png");

        // Resizing the image to the desired size
        Image imagemRedimensionada = logo.getImage().getScaledInstance(150, 20, Image.SCALE_SMOOTH);

        // Creating a new ImageIcon with the resized image
        ImageIcon logoRedimensionado = new ImageIcon(imagemRedimensionada);

        // Creating the JLabel with the resized ImageIcon
        JLabel tituloLabel = new JLabel(logoRedimensionado);
        tituloLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cabecalhoPanel.add(tituloLabel, BorderLayout.WEST);

        // Search field
        JTextField buscarTextField = new JTextField();
        buscarTextField.setPreferredSize(new Dimension(150, 30));
        buscarTextField.setBackground(new Color(62, 63, 75));

        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buscaPanel.setBackground(new Color(54, 59, 78));
        buscaPanel.add(buscarTextField);
        cabecalhoPanel.add(buscaPanel, BorderLayout.CENTER);

        // User and logout button
        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        usuarioPanel.setBackground(new Color(54, 59, 78));
        JLabel welcomeLabel = new JLabel("Bem-vindo, " + nomeUsuario + "!");
        JButton sairButton = new JButton("Sair");
        sairButton.setBorderPainted(false);
        sairButton.setForeground(Color.WHITE);
        sairButton.setFocusPainted(false);
        sairButton.setContentAreaFilled(false);
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginScreen loginScreen = new LoginScreen();
                loginScreen.setVisible(true);
            }
        });
        usuarioPanel.add(welcomeLabel);
        usuarioPanel.add(sairButton);
        cabecalhoPanel.add(usuarioPanel, BorderLayout.EAST);

        panel.add(cabecalhoPanel, BorderLayout.NORTH);

        // JTextPane for displaying upcoming matches and probability of more than one goal
        dadosTextPane = new JTextPane();
        dadosTextPane.setEditable(false);
        dadosTextPane.setBackground(new Color(39, 43, 57));
        dadosTextPane.setForeground(Color.WHITE);
        dadosTextPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        // ScrollPane for the JTextPane
        JScrollPane scrollPane = new JScrollPane(dadosTextPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(39, 43, 57));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodapePanel.setBackground(new Color(54, 59, 78));
        JLabel rodapeLabel = new JLabel("© 2023 Over Gols. Todos os direitos reservados.");
        rodapeLabel.setForeground(Color.WHITE);
        rodapePanel.add(rodapeLabel);

        panel.add(rodapePanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(panel);

        // Load the data initially
        atualizarDados();
    }

    private void atualizarDados() {
        // Get the list of upcoming matches
        List<String> proximasPartidas = partidas.obterProximasPartidas();

        // Calculate and retrieve the probability of more than one goal
        List<String> probabilidades = partidas.calcularEImprimirProbabilidadeMaisDeUmGol();

        // Clear the previous content of the JTextPane
        dadosTextPane.setText("");

        // Add the matches to the JTextPane
        for (int i = 0; i < proximasPartidas.size(); i++) {
            String partida = proximasPartidas.get(i);
            String probabilidade = probabilidades.get(i);

            JPanel partidaPanel = createPartidaPanel(partida, probabilidade);
            dadosTextPane.insertComponent(partidaPanel);
            dadosTextPane.insertComponent(Box.createVerticalStrut(10)); // Adds vertical space between the matches
        }
    }

    private JPanel createPartidaPanel(String partida, String probabilidade) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setPreferredSize(new Dimension(800, 80));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(123, 112, 246));
        panel.setPreferredSize(new Dimension(300, 60));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(123, 112, 246));

        String[] partes = partida.split("\n");

        JLabel parte1Label = new JLabel(partes[0]);
        JLabel parte2Label = new JLabel(partes[1]);

        parte1Label.setFont(new Font("Arial", Font.BOLD, 14));
        parte1Label.setForeground(Color.DARK_GRAY);
        parte2Label.setFont(new Font("Arial", Font.BOLD, 14));
        parte2Label.setForeground(Color.DARK_GRAY);

        infoPanel.add(parte1Label);
        infoPanel.add(parte2Label);
        panel.add(infoPanel, BorderLayout.WEST);

        JLabel probabilidadeLabel = new JLabel(probabilidade);
        probabilidadeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        probabilidadeLabel.setForeground(Color.RED);
        probabilidadeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(probabilidadeLabel, BorderLayout.CENTER);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(39, 43, 57));
        centerPanel.add(panel);

        outerPanel.add(centerPanel, BorderLayout.CENTER);

        return outerPanel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            OverGolsInterface overGolsInterface = new OverGolsInterface("fulano");
            overGolsInterface.setVisible(true);
        });
    }
}
