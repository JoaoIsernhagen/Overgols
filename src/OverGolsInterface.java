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
        setSize(800, 600); // Defina a largura e a altura desejadas
        setLocationRelativeTo(null); // Centralize a janela na tela
        setUndecorated(true); // Remover a barra de título (opcional)


        // Crie o painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        // Cabeçalho
        JPanel cabecalhoPanel = new JPanel(new BorderLayout());
        cabecalhoPanel.setBackground(new Color(0, 128, 0)); // Defina a cor de fundo para verde
        cabecalhoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

// Título com logotipo
        ImageIcon logo = new ImageIcon("Overgols-master/src/over1.png"); // Substitua pelo caminho do seu logotipo
        JLabel tituloLabel = new JLabel(logo);
        cabecalhoPanel.add(tituloLabel, BorderLayout.WEST);

// Campo de busca
        JTextField buscarTextField = new JTextField();
        buscarTextField.setPreferredSize(new Dimension(150, 30));
        buscarTextField.setBackground(new Color(200, 200, 200));

// Centralize o campo de busca no cabeçalho
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.setBackground(new Color(0, 128, 0)); // Defina a cor de fundo para verde
        buscaPanel.add(buscarTextField);
        cabecalhoPanel.add(buscaPanel, BorderLayout.CENTER);

// Usuário e botão de sair
        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        usuarioPanel.setBackground(new Color(0, 128, 0)); // Defina a cor de fundo para verde
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
        dadosTextPane.setBackground(Color.BLACK); // Defina o fundo como preto


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
            StyleConstants.setFontSize(partidaStyle, 1);
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
        outerPanel.setBackground(new Color(240, 240, 240));
        outerPanel.setPreferredSize(new Dimension(800, 80)); // Ajuste a largura e a altura desejadas

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 60)); // Ajuste a largura e a altura desejadas


        // Configurar GridBagConstraints para alinhar a probabilidade ao centro e à direita
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1; // Alterar para a coluna desejada
        c.gridy = 0; // Alterar para a linha desejada
        c.weightx = 1.0; // Aumenta o peso horizontal para esticar o componente na célula
        c.anchor = GridBagConstraints.FIRST_LINE_END; // Alinha o componente ao canto superior direito
        c.insets = new Insets(0, 10, 0, 0); // Adiciona margem à esquerda para alinhamento à direita

        String[] partes = partida.split("\n"); // Dividir a partida em duas partes

// Crie os JLabels para exibir as partes da partida
        JLabel parte1Label = new JLabel(partes[0]);
        JLabel parte2Label = new JLabel(partes[1]);

// Configure as propriedades dos JLabels
        parte1Label.setFont(new Font("Arial", Font.BOLD, 14));
        parte1Label.setForeground(Color.DARK_GRAY);
        parte2Label.setFont(new Font("Arial", Font.BOLD, 14));
        parte2Label.setForeground(Color.DARK_GRAY);

// Configurar a posição do texto da partida
        c.gridx = 0; // Defina a coluna desejada
        c.gridy = 0; // Defina a linha desejada
        c.anchor = GridBagConstraints.FIRST_LINE_START; // Alinha o componente ao canto superior esquerdo
        panel.add(parte1Label, c);

        c.gridx = 0; // Defina a coluna desejada
        c.gridy = 1; // Defina a linha desejada
        c.anchor = GridBagConstraints.FIRST_LINE_START; // Alinha o componente ao canto superior esquerdo
        panel.add(parte2Label, c);



        // Formate a string de probabilidade com formatação
        String formattedProbabilidade = String.format("%s", probabilidade);
        JLabel probabilidadeLabel = new JLabel(formattedProbabilidade);
        probabilidadeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        probabilidadeLabel.setForeground(Color.RED);

        // Configurar GridBagConstraints para alinhar a probabilidade à direita
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(probabilidadeLabel, c);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Centralize o painel no contêiner principal
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(0, 0, 0));
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
