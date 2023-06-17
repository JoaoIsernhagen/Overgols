import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OverGolsInterface extends JFrame {
    private JTextArea textArea;
    private Partidas partidas;
    private JPanel partidasPanel; // Adicionado o painel partidasPanel

    public OverGolsInterface() {
        // Configurações da janela
        setTitle("OverGols");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Criar painel para o cabeçalho
        JPanel cabecalhoPanel = new JPanel(new BorderLayout());
        cabecalhoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Rótulo do título
        JLabel tituloLabel = new JLabel("OverGols");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        cabecalhoPanel.add(tituloLabel, BorderLayout.WEST);

        // Campo de busca
        JTextField buscarTextField = new JTextField();
        cabecalhoPanel.add(buscarTextField, BorderLayout.CENTER);

        // Painel para o usuário e botão de sair
        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel usuarioLabel = new JLabel("Usuário: Fulano");
        JButton sairButton = new JButton("Sair");
        usuarioPanel.add(usuarioLabel);
        usuarioPanel.add(sairButton);
        cabecalhoPanel.add(usuarioPanel, BorderLayout.EAST);

        add(cabecalhoPanel, BorderLayout.NORTH);

        // Criar um painel para conter as partidas
        partidasPanel = new JPanel();
        partidasPanel.setLayout(new BoxLayout(partidasPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(partidasPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

        // Criar uma instância da classe BancoDeDados
        String root = "root";
        String password = "Gdyp07@o";
        BancoDeDados bancoDeDados = new BancoDeDados(root, password);

        // Criar uma instância da classe Partidas
        partidas = new Partidas(bancoDeDados);
    }

    public void exibirProximasPartidas() {
        // Obter a lista de próximas partidas
        List<String> proximasPartidas = partidas.obterProximasPartidas();

        // Limpar o painel de partidas
        partidasPanel.removeAll();

        // Configurar o layout do painel principal como GridBagLayout
        partidasPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5); // Adicionar espaçamento entre as caixas
        constraints.gridx = 0; // Definir a coluna como 0
        constraints.gridy = 0; // Começar na primeira linha

        // Exibir as próximas partidas
        for (String partida : proximasPartidas) {
            // Separar os campos da partida usando o caractere "|"
            String[] campos = partida.split("\\|");

            // Verificar se a partida possui pelo menos 5 campos (para evitar erros)
            if (campos.length >= 1) {
                // Criar um painel para a exibição da partida
                JPanel partidaPanel = new JPanel();
                partidaPanel.setLayout(new GridLayout(2, 2));
                partidaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                partidaPanel.setPreferredSize(new Dimension(250, 35));

                JLabel dataValorLabel = new JLabel(campos[1].trim());
                dataValorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                partidaPanel.add(dataValorLabel);

                JLabel timeCasaValorLabel = new JLabel(campos[3].trim());
                timeCasaValorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                partidaPanel.add(timeCasaValorLabel);

                JLabel horaValorLabel = new JLabel(campos[2].trim());
                horaValorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                partidaPanel.add(horaValorLabel);

                JLabel timeVisitanteValorLabel = new JLabel(campos[4].trim());
                timeVisitanteValorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                partidaPanel.add(timeVisitanteValorLabel);

                partidaPanel.setBackground(Color.LIGHT_GRAY);

                // Adicionar o painel da partida ao painel principal
                partidasPanel.add(partidaPanel, constraints);

                constraints.gridy++; // Incrementar a linha para a próxima partida
            }
        }
    }

    public static void main (String[]args){
                // Criar e exibir a interface gráfica
                SwingUtilities.invokeLater(() -> {
                    OverGolsInterface overGols = new OverGolsInterface();
                    overGols.setVisible(true);
                    overGols.exibirProximasPartidas(); // Chamar o método para exibir as partidas inicialmente
                });
            }
        }
