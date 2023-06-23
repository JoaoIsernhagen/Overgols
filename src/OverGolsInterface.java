import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Font;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


/**
 * Classe que representa a interface gráfica do aplicativo Over Gols.
 */
public class OverGolsInterface extends JFrame {
    private Partidas partidas;
    private JTextPane dadosTextPane;
    private String nomeUsuario;

    /**
     * Construtor da classe OverGolsInterface.
     * Cria uma instância da classe OverGolsInterface e configura a interface gráfica.
     *
     * @param username o nome do usuário logado
     */
    public OverGolsInterface(String username) {
        this.nomeUsuario = username;

        // Cria uma instância do banco de dados e da classe Partidas
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        partidas = new Partidas(bancoDeDados);

        // Configura a janela
        setTitle("Over Gols");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Cria o painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Cabeçalho
        JPanel cabecalhoPanel = new JPanel(new BorderLayout());
        cabecalhoPanel.setBackground(new Color(54, 59, 78));
        cabecalhoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Carrega a imagem do arquivo
        ImageIcon logo = new ImageIcon("C:\\Users\\joaor\\eclipse-workspace\\Overgols\\imagens\\fd2623dedfa19289f8110784f36dc541.png");

        // Redimensiona a imagem para o tamanho desejado
        Image imagemRedimensionada = logo.getImage().getScaledInstance(150, 20, Image.SCALE_SMOOTH);

        // Cria um novo ImageIcon com a imagem redimensionada
        ImageIcon logoRedimensionado = new ImageIcon(imagemRedimensionada);

        // Cria o JLabel com o ImageIcon redimensionado
        JLabel tituloLabel = new JLabel(logoRedimensionado);
        tituloLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cabecalhoPanel.add(tituloLabel, BorderLayout.WEST);

        // Botão para abrir a página de times
        JButton timesButton = new JButton("Times");
        timesButton.addActionListener(e -> {
            TimeInterface timeInterface = new TimeInterface();
            timeInterface.setVisible(true);
            timeInterface.imprimirNomesDosTimes();
        });
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buscaPanel.setBackground(new Color(54, 59, 78));
        buscaPanel.add(timesButton);
        cabecalhoPanel.add(buscaPanel, BorderLayout.CENTER);



        // Botão para abrir a página de campeonatos
        JButton campeonatosButton = new JButton("Campeonatos");
        campeonatosButton.addActionListener(e -> {
            CampeonatosInterface campeonatosInterface = new CampeonatosInterface();
            campeonatosInterface.setVisible(true);
        });
        buscaPanel.add(campeonatosButton);


        // Cria uma instância da classe Font com a fonte desejada
        Font fontePersonalizada = new Font("Novo Correio", Font.CENTER_BASELINE, 14);

        // Cria o JLabel com o texto e a fonte personalizada
        JLabel welcomeLabel = new JLabel(nomeUsuario + "!");
        welcomeLabel.setFont(fontePersonalizada);
        welcomeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Adiciona um cursor de mão para indicar que é clicável
        welcomeLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Abre a interface de troca de senha
                UserOptionsInterface changePasswordInterface = new UserOptionsInterface(nomeUsuario);
            }
        });



        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        usuarioPanel.setBackground(new Color(54, 59, 78));
        JButton sairButton = new JButton("Sair");
        usuarioPanel.add(welcomeLabel);
        usuarioPanel.add(sairButton);
        cabecalhoPanel.add(usuarioPanel, BorderLayout.EAST);
        panel.add(cabecalhoPanel, BorderLayout.NORTH);

        // JTextPane para exibir as próximas partidas e a probabilidade de mais de um gol
        dadosTextPane = new JTextPane();
        dadosTextPane.setEditable(false);
        dadosTextPane.setBackground(new Color(39, 43, 57));
        dadosTextPane.setForeground(Color.WHITE);
        dadosTextPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        // JScrollPane para o JTextPane
        JScrollPane scrollPane = new JScrollPane(dadosTextPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(39, 43, 57));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Rodapé
        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodapePanel.setBackground(new Color(54, 59, 78));
        JLabel rodapeLabel = new JLabel("© 2023 Over Gols. Todos os direitos reservados.");
        rodapeLabel.setForeground(Color.WHITE);
        rodapePanel.add(rodapeLabel);

        panel.add(rodapePanel, BorderLayout.SOUTH);

        // Adiciona o painel principal ao frame
        add(panel);

        // Carrega os dados inicialmente
        atualizarDados();
    }

    /**
     * Atualiza os dados exibidos na interface gráfica.
     */
    private void atualizarDados() {
        // Obtém a lista de próximas partidas
        List<String> proximasPartidas = partidas.obterProximasPartidas();

        // Calcula e obtém a probabilidade de mais de um gol
        List<String> probabilidades = partidas.calcularEImprimirProbabilidadeMaisDeUmGol();

        // Limpa o conteúdo anterior do JTextPane
        dadosTextPane.setText("");

        // Adiciona as partidas ao JTextPane
        for (int i = 0; i < proximasPartidas.size(); i++) {
            String partida = proximasPartidas.get(i);
            String probabilidade = probabilidades.get(i);

            JPanel partidaPanel = createPartidaPanel(partida, probabilidade);
            dadosTextPane.insertComponent(partidaPanel);
            dadosTextPane.insertComponent(Box.createVerticalStrut(10)); // Adiciona espaço vertical entre as partidas
        }
    }

    /**
     * Cria um painel para exibir uma partida e sua probabilidade de mais de um gol.
     *
     * @param partida       a descrição da partida
     * @param probabilidade a probabilidade de mais de um gol na partida
     * @return o painel criado
     */
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

    /**
     * Método principal que inicia o aplicativo Over Gols.
     *
     * @param args argumentos da linha de comando (não utilizados)
     */
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