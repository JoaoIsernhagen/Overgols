import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A classe TimeInterface representa uma interface gráfica para exibir nomes de times e as últimas partidas de um time selecionado.
 */
public class TimeInterface extends JFrame {
    private JTextArea textArea;
    private Time time;
    private JButton backButton;
    private JPanel contentPanel;

    /**
     * Cria uma instância da classe TimeInterface e configura a janela da interface.
     */
    public TimeInterface() {
        setTitle("Nomes dos Times");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        time = new Time();

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Voltar");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                imprimirNomesDosTimes();
            }
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        setContentPane(contentPanel);

        // Adiciona um MouseListener ao JTextArea
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detecta o duplo clique
                    JTextArea source = (JTextArea) e.getSource();
                    int caretPosition = source.getCaretPosition();
                    int line;
                    try {
                        line = source.getLineOfOffset(caretPosition);
                        int start = source.getLineStartOffset(line);
                        int end = source.getLineEndOffset(line);
                        String selectedText = source.getText(start, end - start).trim();
                        if (!selectedText.isEmpty()) {
                            exibirUltimasPartidas(selectedText);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Imprime os nomes dos times na JTextArea.
     */
    public void imprimirNomesDosTimes() {
        ResultSet resultSet = time.obterNomesDosTimes();

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    String nomeTime = resultSet.getString("IDNOMETIMES");
                    textArea.append(nomeTime + "\n");
                }

                resultSet.close();
            } catch (SQLException e) {
                System.out.println("Erro ao ler resultado da consulta: " + e.getMessage());
            }
        } else {
            System.out.println("Não foi possível obter os nomes dos times.");
        }
    }

    /**
     * Exibe as últimas partidas do time selecionado na JTextArea.
     * @param timeSelecionado o nome do time selecionado.
     */
    public void exibirUltimasPartidas(String timeSelecionado) {
        ResultSet resultSet = time.obterUltimasPartidas(timeSelecionado);

        if (resultSet != null) {
            try {
                textArea.setText("Últimas 5 partidas de " + timeSelecionado + ":\n");

                while (resultSet.next()) {
                    String partida = "Rodada: " + resultSet.getString("RODADAS") +
                            ", Data: " + resultSet.getString("DATADAPARTIDA") +
                            ", Hora: " + resultSet.getString("HORADAPARTIDA") +
                            ", Mandante: " + resultSet.getString("NOME_MANDANTE") +
                            ", Gols Mandante: " + resultSet.getString("GOLSMANDANTE") +
                            ", Visitante: " + resultSet.getString("NOME_VISITANTE") +
                            ", Gols Visitante: " + resultSet.getString("GOLSVISITANTE");
                    textArea.append(partida + "\n");
                }

                resultSet.close();
            } catch (SQLException e) {
                System.out.println("Erro ao ler resultado da consulta: " + e.getMessage());
            }
        } else {
            System.out.println("Não foi possível obter as últimas partidas.");
        }
    }

    /**
     * O método principal que cria uma instância da classe TimeInterface e exibe a interface.
     * Também imprime os nomes dos times na JTextArea.
     * @param args os argumentos de linha de comando (não são utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TimeInterface timeInterface = new TimeInterface();
            timeInterface.setVisible(true);
            timeInterface.imprimirNomesDosTimes();
        });
    }
}
