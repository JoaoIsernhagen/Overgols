import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeInterface extends JFrame {
    private JTextArea textArea;
    private Time time;

    public TimeInterface() {
        setTitle("Nomes dos Times");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        time = new Time();

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Add a MouseListener to the JTextArea
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
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

    public void imprimirNomesDosTimes() {
        ResultSet resultSet = time.obterNomesDosTimes();
        textArea.setText("");

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    String nomeTime = resultSet.getString("IDNOMETIMES");
                    textArea.append(nomeTime + "\n");
                }

                resultSet.close();
            } catch (SQLException e) {
                System.out.println("Erro ao ler dados do ResultSet: " + e.getMessage());
            }
        }
    }

    public void exibirUltimasPartidas(String timeSelecionado) {
        ResultSet resultSet = time.obterUltimasPartidas(timeSelecionado);
        textArea.setText("Últimas 5 partidas de " + timeSelecionado + ":\n");

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    String partida = "Rodada: " + resultSet.getString("RODADAS") +
                            " " + resultSet.getString("DATADAPARTIDA") +
                            " " + resultSet.getString("HORADAPARTIDA") +
                            " " + resultSet.getString("NOME_MANDANTE") +
                            " " + resultSet.getString("GOLSMANDANTE") +
                            " " + resultSet.getString("NOME_VISITANTE") +
                            " " + resultSet.getString("GOLSVISITANTE");
                    textArea.append(partida + "\n");
                }

                resultSet.close();
            } catch (SQLException e) {
                System.out.println("Erro ao ler dados do ResultSet: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TimeInterface timeInterface = new TimeInterface();
            timeInterface.setVisible(true);
            timeInterface.imprimirNomesDosTimes();
        });
    }
}
