import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Campeonato {
    private BancoDeDados bancoDeDados;

    public Campeonato(BancoDeDados bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
    }

    public List<String> getYears() {
        List<String> years = new ArrayList<>();

        Connection connection = bancoDeDados.conectar();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT DISTINCT ANODOCAMPEONATO FROM CAMPEONATOS");

                while (resultSet.next()) {
                    String year = resultSet.getString("ANODOCAMPEONATO");
                    years.add(year);
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                bancoDeDados.fecharConexao(connection);
            }
        }

        return years;
    }

    public List<String> getRounds(String year) {
        List<String> rounds = new ArrayList<>();

        Connection connection = bancoDeDados.conectar();
        if (connection != null) {
            try {
                String query = "SELECT DISTINCT RODADAS FROM PARTIDAS " +
                        "INNER JOIN CAMPEONATOS ON CAMPEONATOS.ID_PARTIDAS = PARTIDAS.IDPARTIDAS " +
                        "WHERE CAMPEONATOS.ANODOCAMPEONATO = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, year);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String round = resultSet.getString("RODADAS");
                    rounds.add(round);
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                bancoDeDados.fecharConexao(connection);
            }
        }

        return rounds;
    }

    public List<String> getMatches(String year, String round) {
        List<String> matches = new ArrayList<>();

        Connection connection = bancoDeDados.conectar();
        if (connection != null) {
            try {
                String query = "SELECT PARTIDAS.DATADAPARTIDA, TIMES.IDNOMETIMES AS MANDANTE, TIMES_VISITANTE.IDNOMETIMES AS VISITANTE, PARTIDAS.GOLSMANDANTE, PARTIDAS.GOLSVISITANTE " +
                        "FROM PARTIDAS " +
                        "INNER JOIN CAMPEONATOS ON CAMPEONATOS.ID_PARTIDAS = PARTIDAS.IDPARTIDAS " +
                        "INNER JOIN TIMES ON TIMES.IDTIMES = PARTIDAS.IDTIMEMANDANTE " +
                        "INNER JOIN TIMES AS TIMES_VISITANTE ON TIMES_VISITANTE.IDTIMES = PARTIDAS.IDTIMEVISITANTE " +
                        "WHERE CAMPEONATOS.ANODOCAMPEONATO = ? AND PARTIDAS.RODADAS = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, year);
                statement.setString(2, round);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String date = resultSet.getString("DATADAPARTIDA");
                    String teamHome = resultSet.getString("MANDANTE");
                    int goalsHome = resultSet.getInt("GOLSMANDANTE");
                    int goalsAway = resultSet.getInt("GOLSVISITANTE");
                    String teamAway = resultSet.getString("VISITANTE");

                    String match = String.format("%s - %s vs %d: %d x %s", date, teamHome, goalsHome, goalsAway, teamAway);
                    matches.add(match);
                }


                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                bancoDeDados.fecharConexao(connection);
            }
        }

        return matches;
    }
}
