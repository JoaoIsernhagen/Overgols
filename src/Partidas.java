import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Partidas {
    private final BancoDeDados bancoDeDados;
    private Connection connection;

    public Partidas(BancoDeDados bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
    }

    public List<String> obterProximasPartidas() {
        List<String> jogos = new ArrayList<>();

        try {
            Connection connection = bancoDeDados.conectar();
            LocalDate dataAtual = LocalDate.now();

            // Criar a consulta SQL para obter os próximos jogos, ordenados por data e hora em ordem crescente
            String sql = "SELECT RODADAS, DATADAPARTIDA, HORADAPARTIDA, t1.IDNOMETIMES AS TIME_MANDANTE, t2.IDNOMETIMES AS TIME_VISITANTE " +
                    "FROM PARTIDAS p " +
                    "INNER JOIN TIMES t1 ON p.IDTIMEMANDANTE = t1.IDTIMES " +
                    "INNER JOIN TIMES t2 ON p.IDTIMEVISITANTE = t2.IDTIMES " +
                    "WHERE STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y') = (" +
                    "   SELECT MIN(STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y')) FROM PARTIDAS " +
                    "   WHERE STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y') >= STR_TO_DATE(?, '%d/%m/%Y')" +
                    ") " +
                    "AND STR_TO_DATE(HORADAPARTIDA, '%H:%i') >= STR_TO_DATE(?, '%H:%i') " +
                    "ORDER BY STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y'), STR_TO_DATE(HORADAPARTIDA, '%H:%i')";

            // Preparar a consulta SQL
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            statement.setString(2, "00:00"); // A partir da meia-noite

            // Executar a consulta SQL
            ResultSet resultSet = statement.executeQuery();

            // Processar os resultados
            while (resultSet.next()) {
                String rodada = resultSet.getString("RODADAS");
                String dataPartida = resultSet.getString("DATADAPARTIDA");
                String horaPartida = resultSet.getString("HORADAPARTIDA");
                String timeMandante = resultSet.getString("TIME_MANDANTE");
                String timeVisitante = resultSet.getString("TIME_VISITANTE");

                // Criar uma string com as informações do jogo
                String jogo = "Rodada " + rodada + " | Data: " + dataPartida + " | Hora: " + horaPartida + "|" + timeMandante + "|" + timeVisitante;
                // Adicionar o jogo à lista
                jogos.add(jogo);
            }

            resultSet.close();
            statement.close();
            connection.close();

            // Ordenar a lista de jogos usando o comparador personalizado
            jogos.sort(new PartidaComparator());
        } catch (SQLException e) {
            System.out.println("Erro ao obter as próximas partidas: " + e.getMessage());
        }

        return jogos;
    }

    private static class PartidaComparator implements Comparator<String> {
        @Override
        public int compare(String partida1, String partida2) {
            String[] partes1 = partida1.split("\\|");
            String[] partes2 = partida2.split("\\|");

            // Obter as informações de data e hora das partidas
            String data1String = partes1[1].replace("Data: ", "").trim();
            String data2String = partes2[1].replace("Data: ", "").trim();

            String hora1 = partes1[2].trim();
            String hora2 = partes2[2].trim();

            // Converter as strings de data em objetos LocalDate
            LocalDate data1 = LocalDate.parse(data1String, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate data2 = LocalDate.parse(data2String, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Comparar as datas
            int resultado = data1.compareTo(data2);
            if (resultado == 0) {
                // Se as datas forem iguais, comparar as horas em ordem crescente
                resultado = hora1.compareTo(hora2);
            }

            return resultado;
        }
    }

    public int obterGolsUltimosJogos(String time, boolean mandante) throws SQLException {
        String sql = "SELECT t1.IDNOMETIMES AS TIME_MANDANTE, t2.IDNOMETIMES AS TIME_VISITANTE, p.IDPARTIDAS, p.DATADAPARTIDA, " +
                "SUM(p.GOLSMANDANTE) AS TOTAL_GOLSMANDANTE, SUM(p.GOLSVISITANTE) AS TOTAL_GOLSVISITANTE " +
                "FROM PARTIDAS p " +
                "INNER JOIN TIMES t1 ON p.IDTIMEMANDANTE = t1.IDTIMES " +
                "INNER JOIN TIMES t2 ON p.IDTIMEVISITANTE = t2.IDTIMES " +
                "WHERE ((t1.IDNOMETIMES = ? AND ?) OR (t2.IDNOMETIMES = ? AND NOT ?)) " +
                "AND STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') < STR_TO_DATE(?, '%d/%m/%Y') " +
                "GROUP BY t1.IDNOMETIMES, t2.IDNOMETIMES, p.IDPARTIDAS, p.DATADAPARTIDA " +
                "ORDER BY STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') DESC " +
                "LIMIT 19";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, time);
        statement.setBoolean(2, mandante);
        statement.setString(3, time);
        statement.setBoolean(4, mandante);
        statement.setString(5, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        ResultSet resultSet = statement.executeQuery();

        int totalGols = 0;
        while (resultSet.next()) {
            int idPartida = resultSet.getInt("IDPARTIDAS");
            String dataPartida = resultSet.getString("DATADAPARTIDA");
            int golsMandante = resultSet.getInt("TOTAL_GOLSMANDANTE");
            int golsVisitante = resultSet.getInt("TOTAL_GOLSVISITANTE");
            String timeMandante = resultSet.getString("TIME_MANDANTE");
            String timeVisitante = resultSet.getString("TIME_VISITANTE");

            if (mandante) {
                totalGols += golsMandante;
            } else {
                totalGols += golsVisitante;
            }
        }

        resultSet.close();
        statement.close();

        return totalGols;
    }

    public void calcularGolsUltimosJogos(List<String> proximasPartidas) {
        try {
            connection = bancoDeDados.conectar();

            for (String partida : proximasPartidas) {
                String[] times = partida.split("\\|");
                if (times.length >= 5) {
                    String timeMandante = times[3].trim();
                    String timeVisitante = times[4].trim();

                    int golsMandante = obterGolsUltimosJogos(timeMandante, true);
                    int golsVisitante = obterGolsUltimosJogos(timeVisitante, false);

                    double mediaGolsMandante = (double) golsMandante / 19;
                    double mediaGolsVisitante = (double) golsVisitante / 19;

                    double probabilidadeMaisDeUmGol = calcularProbabilidadeMaisDeUmGol(mediaGolsMandante, mediaGolsVisitante);
                    double probabilidadeMaisDeUmGolEmPorcentagem = probabilidadeMaisDeUmGol * 100;

                    System.out.printf("Probabilidade de mais de um gol na partida %s: %.2f%%\n", partida, probabilidadeMaisDeUmGolEmPorcentagem);

                } else {
                    System.out.println("Formato de partida inválido: " + partida);
                }
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao calcular gols dos últimos jogos: " + e.getMessage());
        }
    }
    public static double calcularProbabilidadeMaisDeUmGol(double mediaMandante, double mediaVisitante) {
        double probabilidadeMandanteZeroGol = Math.exp(-mediaMandante) * Math.pow(mediaMandante, 0) / factorial(0);
        double probabilidadeMandanteUmGol = Math.exp(-mediaMandante) * Math.pow(mediaMandante, 1) / factorial(1);
        double probabilidadeVisitanteZeroGol = Math.exp(-mediaVisitante) * Math.pow(mediaVisitante, 0) / factorial(0);
        double probabilidadeVisitanteUmGol = Math.exp(-mediaVisitante) * Math.pow(mediaVisitante, 1) / factorial(1);

        double probabilidadeMenosDeUmGol = probabilidadeMandanteZeroGol * probabilidadeVisitanteZeroGol;
        double probabilidadeExatamenteUmGol = probabilidadeMandanteUmGol * probabilidadeVisitanteZeroGol + probabilidadeMandanteZeroGol * probabilidadeVisitanteUmGol;

        double probabilidadeMaisDeUmGol = 1 - probabilidadeMenosDeUmGol - probabilidadeExatamenteUmGol;

        return probabilidadeMaisDeUmGol;
    }

    public static int factorial(int n) {
        if (n == 0 || n == 1)
            return 1;
        else
            return n * factorial(n - 1);
    }

    public void calcularGolsUltimosConfrontos(List<String> proximasPartidas) {
        try {
            connection = bancoDeDados.conectar();

            for (String partida : proximasPartidas) {
                String[] times = partida.split("\\|");
                if (times.length >= 5) {
                    String timeMandante = times[3].trim();
                    String timeVisitante = times[4].trim();

                    int golsUltimosConfrontos = obterGolsUltimosConfrontos(timeMandante, timeVisitante);

                    double mediaGolsPartida = (double) golsUltimosConfrontos / 6; // Assuming 6 previous matches
                    double probabilidadeMaisDeUmGol = calcularProbabilidadeMaisDeUmGol(mediaGolsPartida);
                    System.out.printf("Probabilidade de mais de um gol no próximo jogo entre %s e %s: %.2f%%\n", timeMandante, timeVisitante, probabilidadeMaisDeUmGol * 100);

                } else {
                    System.out.println("Formato de partida inválido: " + partida);
                }
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao calcular gols dos últimos confrontos: " + e.getMessage());
        }
    }

    private int obterGolsUltimosConfrontos(String timeMandante, String timeVisitante) {
        try {
            String sql = "SELECT p.DATADAPARTIDA, t1.IDNOMETIMES AS MANDANTE, p.GOLSMANDANTE, t2.IDNOMETIMES AS VISITANTE, p.GOLSVISITANTE " +
                    "FROM PARTIDAS p " +
                    "INNER JOIN TIMES t1 ON p.IDTIMEMANDANTE = t1.IDTIMES " +
                    "INNER JOIN TIMES t2 ON p.IDTIMEVISITANTE = t2.IDTIMES " +
                    "WHERE ((t1.IDNOMETIMES = ? AND t2.IDNOMETIMES = ?) OR (t1.IDNOMETIMES = ? AND t2.IDNOMETIMES = ?)) " +
                    "AND STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') < CURDATE() " +
                    "ORDER BY STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') DESC " +
                    "LIMIT 6";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, timeMandante);
            statement.setString(2, timeVisitante);
            statement.setString(3, timeVisitante);
            statement.setString(4, timeMandante);

            ResultSet resultSet = statement.executeQuery();

            int totalGols = 0;

            while (resultSet.next()) {
                int golsMandante = resultSet.getInt("GOLSMANDANTE");
                int golsVisitante = resultSet.getInt("GOLSVISITANTE");

                totalGols += golsMandante + golsVisitante;
            }

            resultSet.close();
            statement.close();

            return totalGols;
        } catch (SQLException e) {
            System.out.println("Erro ao obter gols dos últimos confrontos: " + e.getMessage());
            return 0;
        }
    }

    public static double calcularProbabilidadeMaisDeUmGol(double mediaGolsPartida) {
        double probabilidadeZeroGol = Math.exp(-mediaGolsPartida) * Math.pow(mediaGolsPartida, 0) / factorial(0);
        double probabilidadeUmGol = Math.exp(-mediaGolsPartida) * Math.pow(mediaGolsPartida, 1) / factorial(1);

        double probabilidadeMenosDeUmGol = probabilidadeZeroGol;
        double probabilidadeExatamenteUmGol = probabilidadeUmGol;

        double probabilidadeMaisDeUmGol = 1 - probabilidadeMenosDeUmGol - probabilidadeExatamenteUmGol;

        return probabilidadeMaisDeUmGol;
    }

    public static int factorial2(int n) {
        if (n == 0 || n == 1)
            return 1;
        else
            return n * factorial(n - 1);
    }
}