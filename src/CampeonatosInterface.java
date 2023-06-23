import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Classe que representa a interface gráfica para seleção de campeonatos e exibição de partidas.
 */
public class CampeonatosInterface extends JFrame {

    private JComboBox<String> yearComboBox;
    private JComboBox<String> roundComboBox;
    private JTextArea matchesTextArea;

    private Campeonato campeonato;

    /**
     * Construtor da classe CampeonatosInterface.
     * Configura a janela e inicializa os componentes da interface.
     * Carrega os anos de campeonato e instancia a classe Campeonato.
     */
    public CampeonatosInterface() {
        super("Interface de Campeonatos");

        // Configurações da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        // Cria os componentes da interface
        yearComboBox = new JComboBox<>();
        roundComboBox = new JComboBox<>();
        matchesTextArea = new JTextArea(10, 30);
        JButton showMatchesButton = new JButton("Mostrar Partidas");

        // Adiciona os componentes à janela
        add(new JLabel("Ano do Campeonato:"));
        add(yearComboBox);
        add(new JLabel("Rodada:"));
        add(roundComboBox);
        add(showMatchesButton);
        add(new JScrollPane(matchesTextArea));

        // Evento do botão para mostrar as partidas
        showMatchesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedYear = (String) yearComboBox.getSelectedItem();
                String selectedRound = (String) roundComboBox.getSelectedItem();

                if (selectedYear != null && selectedRound != null) {
                    List<String> matches = campeonato.getMatches(selectedYear, selectedRound);
                    displayMatches(matches);
                }
            }
        });

        // Evento para carregar as rodadas quando um ano é selecionado
        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedYear = (String) yearComboBox.getSelectedItem();

                if (selectedYear != null) {
                    loadRounds(selectedYear);
                }
            }
        });

        // Instancia a classe Campeonato
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        campeonato = new Campeonato(bancoDeDados);

        // Carrega os anos de campeonato
        loadYears();
    }

    /**
     * Carrega os anos de campeonato no combobox.
     */
    private void loadYears() {
        // Carrega os anos de campeonato a partir do banco de dados
        List<String> years = campeonato.getYears();
        for (String year : years) {
            yearComboBox.addItem(year);
        }
    }

    /**
     * Carrega as rodadas do ano selecionado no combobox de rodadas.
     *
     * @param year Ano selecionado.
     */
    private void loadRounds(String year) {
        // Carrega as rodadas do ano selecionado a partir do banco de dados
        List<String> rounds = campeonato.getRounds(year);
        roundComboBox.removeAllItems();
        for (String round : rounds) {
            roundComboBox.addItem(round);
        }
    }

    /**
     * Exibe as partidas na área de texto.
     *
     * @param matches Lista de partidas a serem exibidas.
     */
    private void displayMatches(List<String> matches) {
        // Exibe as partidas na caixa de texto
        matchesTextArea.setText("");

        if (matches.isEmpty()) {
            matchesTextArea.setText("Nenhuma partida encontrada.");
        } else {
            for (String match : matches) {
                matchesTextArea.append(match + "\n");
            }
        }
    }

    /**
     * Método principal que inicia a interface.
     *
     * @param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CampeonatosInterface().setVisible(true);
            }
        });
    }
}
