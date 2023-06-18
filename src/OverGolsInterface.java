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

        // Crie uma instância do banco de dados e da classe Partidas
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        partidas = new Partidas(bancoDeDados);


        // Configure a janela
        setTitle("Over Gols");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicializar em tela cheia
        setUndecorated(true); // Remover a barra de título (opcional)

        // Crie o painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Cabeçalho
        JPanel cabecalhoPanel = new JPanel(new BorderLayout());
        cabecalhoPanel.setBackground(Color.WHITE);
        cabecalhoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título com logotipo
        ImageIcon logo = new ImageIcon("Overgols-master/src/over1.png"); // Substitua pelo caminho do seu logotipo
        JLabel tituloLabel = new JLabel(logo);
        cabecalhoPanel.add(tituloLabel, BorderLayout.WEST);

        // Campo de busca
        JTextField buscarTextField = new JTextField();
        buscarTextField.setPreferredSize(new Dimension(200, 30));
        cabecalhoPanel.add(buscarTextField, BorderLayout.CENTER);

        // Usuário e botão de sair
        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        usuarioPanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Bem-vindo, " + nomeUsuario + "!");

        JButton sairButton = new JButton("Sair");
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela atual
                LoginScreen loginScreen = new LoginScreen();
                loginScreen.setVisible(true); // Abre a tela de login
            }
        });
        usuarioPanel.add(welcomeLabel);
        usuarioPanel.add(sairButton);
        cabecalhoPanel.add(usuarioPanel, BorderLayout.EAST);

        panel.add(cabecalhoPanel, BorderLayout.NORTH);

        // Crie o JTextPane para exibir as próximas partidas e a probabilidade de mais de um gol
        dadosTextPane = new JTextPane();
        dadosTextPane.setEditable(false);
        dadosTextPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Adicione o JTextPane a um JScrollPane para permitir a rolagem
        JScrollPane dadosScrollPane = new JScrollPane(dadosTextPane);
        panel.add(dadosScrollPane, BorderLayout.CENTER);

        // Adicione o painel à janela
        add(panel);

        // Atualize as informações na interface
        atualizarDados();
    }

    private void atualizarDados() {
        // Obtenha a lista de próximas partidas
        List<String> proximasPartidas = partidas.obterProximasPartidas();

        // Calcule e obtenha a probabilidade de mais de um gol
        List<String> probabilidades = partidas.calcularEImprimirProbabilidadeMaisDeUmGol();

        // Limpe o conteúdo anterior do JTextPane
        dadosTextPane.setText(null);

        // Adicione as partidas ao JTextPane
        StyledDocument doc = dadosTextPane.getStyledDocument();
        Style partidaStyle = doc.addStyle("PartidaStyle", null);
        Style probabilidadeStyle = doc.addStyle("ProbabilidadeStyle", null);

        try {
            // Estilo para as partidas
            StyleConstants.setAlignment(partidaStyle, StyleConstants.ALIGN_LEFT);
            StyleConstants.setFontSize(partidaStyle, 14);
            StyleConstants.setForeground(partidaStyle, Color.DARK_GRAY);
            StyleConstants.setSpaceAbove(partidaStyle, 10);

            // Estilo para as probabilidades
            StyleConstants.setAlignment(probabilidadeStyle, StyleConstants.ALIGN_RIGHT);
            StyleConstants.setBold(probabilidadeStyle, true);
            StyleConstants.setFontSize(probabilidadeStyle, 14);
            StyleConstants.setForeground(probabilidadeStyle, Color.RED);

            // Adiciona as partidas ao JTextPane
            for (int i = 0; i < proximasPartidas.size(); i++) {
                String partida = proximasPartidas.get(i);
                String probabilidade = probabilidades.get(i);
                JPanel partidaPanel = createPartidaPanel(partida, probabilidade);

                dadosTextPane.insertComponent(partidaPanel);
                doc.insertString(doc.getLength(), "\n", partidaStyle);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private JPanel createPartidaPanel(String partida, String probabilidade) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.WHITE);
        outerPanel.setPreferredSize(new Dimension(800, 120)); // Aumente a altura para 120

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(670, 100));

        // Formate a string de partida com formatação
        String formattedPartida = String.format("%s", partida);
        JLabel partidaLabel = new JLabel(formattedPartida);
        partidaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        partidaLabel.setForeground(Color.DARK_GRAY);
        partidaLabel.setPreferredSize(new Dimension(380, 50)); // Aumente a altura para 50
        panel.add(partidaLabel, BorderLayout.CENTER);

        // Formate a string de probabilidade com formatação
        String formattedProbabilidade = String.format("%s", probabilidade);
        JLabel probabilidadeLabel = new JLabel(formattedProbabilidade);
        probabilidadeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        probabilidadeLabel.setForeground(Color.RED);
        JPanel probabilidadePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        probabilidadePanel.setBackground(Color.WHITE);
        probabilidadePanel.add(probabilidadeLabel);
        probabilidadePanel.setPreferredSize(new Dimension(380, 20));
        panel.add(probabilidadePanel, BorderLayout.SOUTH);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Centralize o painel no contêiner principal
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(panel);

        // Adicione o painel centralizado ao contêiner externo
        outerPanel.add(centerPanel, BorderLayout.CENTER);

        return outerPanel;
    }

    public static void main(String[] args) {
        // Definir o estilo visual do aplicativo para o estilo do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crie a interface gráfica
        SwingUtilities.invokeLater(() -> {
            OverGolsInterface overGolsInterface = new OverGolsInterface("fulano");
            overGolsInterface.setVisible(true);
        });
    }
}
