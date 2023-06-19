import java.sql.*;
import java.util.ArrayList;
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
            connection = bancoDeDados.conectar();

            // Criar a consulta SQL para obter os próximos jogos
            String sql = "SELECT p.RODADAS, p.DATADAPARTIDA, SUBSTRING(p.HORADAPARTIDA, 1, 5) AS HORADAPARTIDA, t1.IDNOMETIMES AS TIME_MANDANTE, t2.IDNOMETIMES AS TIME_VISITANTE " +
                    "FROM PARTIDAS p " +
                    "INNER JOIN TIMES t1 ON p.IDTIMEMANDANTE = t1.IDTIMES " +
                    "INNER JOIN TIMES t2 ON p.IDTIMEVISITANTE = t2.IDTIMES " +
                    "WHERE STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') = (" +
                    "   SELECT MIN(STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y')) FROM PARTIDAS WHERE STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y') > CURDATE()" +
                    ") " +
                    "ORDER BY STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y'), STR_TO_DATE(p.HORADAPARTIDA, '%H:%i')";



            // Preparar a consulta SQL
            PreparedStatement statement = connection.prepareStatement(sql);

            // Executar a consulta SQL
            ResultSet resultSet = statement.executeQuery();

            // Processar os resultados
            while (resultSet.next()) {
                String rodada = resultSet.getString("RODADAS");
                String dataPartida = resultSet.getString("DATADAPARTIDA");
                String horaPartida = resultSet.getString("HORADAPARTIDA");
                String timeMandante = resultSet.getString("TIME_MANDANTE");
                String timeVisitante = resultSet.getString("TIME_VISITANTE");

                // Remover o ano da data
                String dataSemAno = dataPartida.substring(0, 5); // Extrair apenas o dia e o mês (posições 0 a 4)

                // Criar uma string com as informações do jogo
                String jogo = dataSemAno + " " + timeMandante + "\n" + horaPartida + " " + timeVisitante;

                // Adicionar o jogo à lista
                jogos.add(jogo);
            }


            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao obter as próximas partidas: " + e.getMessage());
        }

        return jogos;
    }

    public List<String> calcularEImprimirProbabilidadeMaisDeUmGol() {
        List<String> resultados = new ArrayList<>();

        try (Connection conn = bancoDeDados.conectar()) {
            String sql = "SELECT p.RODADAS, p.DATADAPARTIDA, p.HORADAPARTIDA, t1.IDNOMETIMES AS TIME_MANDANTE, " +
                    "t2.IDNOMETIMES AS TIME_VISITANTE, " +
                    "(SELECT SUM(g.GOLSMANDANTE) FROM (SELECT pg.GOLSMANDANTE FROM PARTIDAS pg WHERE pg.IDTIMEMANDANTE = p.IDTIMEMANDANTE " +
                    "AND STR_TO_DATE(pg.DATADAPARTIDA, '%d/%m/%Y') < STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') " +
                    "ORDER BY STR_TO_DATE(pg.DATADAPARTIDA, '%d/%m/%Y') DESC LIMIT 19) g) AS SOMA_GOLS_MANDANTE, " +
                    "(SELECT SUM(g.GOLSVISITANTE) FROM (SELECT pg.GOLSVISITANTE FROM PARTIDAS pg WHERE pg.IDTIMEVISITANTE = p.IDTIMEVISITANTE " +
                    "AND STR_TO_DATE(pg.DATADAPARTIDA, '%d/%m/%Y') < STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') " +
                    "ORDER BY STR_TO_DATE(pg.DATADAPARTIDA, '%d/%m/%Y') DESC LIMIT 19) g) AS SOMA_GOLS_VISITANTE " +
                    "FROM PARTIDAS p INNER JOIN TIMES t1 ON p.IDTIMEMANDANTE = t1.IDTIMES " +
                    "INNER JOIN TIMES t2 ON p.IDTIMEVISITANTE = t2.IDTIMES " +
                    "WHERE STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y') = (SELECT MIN(STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y')) " +
                    "FROM PARTIDAS WHERE STR_TO_DATE(DATADAPARTIDA, '%d/%m/%Y') > CURDATE()) " +
                    "ORDER BY STR_TO_DATE(p.DATADAPARTIDA, '%d/%m/%Y'), STR_TO_DATE(p.HORADAPARTIDA, '%H:%i')";


            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    double somaGolsMandante = rs.getDouble("SOMA_GOLS_MANDANTE");
                    double somaGolsVisitante = rs.getDouble("SOMA_GOLS_VISITANTE");

                    double mediaGolsMandante = somaGolsMandante / 19;
                    double mediaGolsVisitante = somaGolsVisitante / 19;

                    double probabilidadeMaisDeUmGol = calcularProbabilidadeMaisDeUmGol(mediaGolsMandante, mediaGolsVisitante);

                    String resultado =
                            String.format("%.2f", probabilidadeMaisDeUmGol * 100) + "%";

                    resultados.add(resultado);
                    System.out.println(resultado);
                }

                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    public double calcularProbabilidadeMaisDeUmGol(double mediaMandante, double mediaVisitante) {
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



}
