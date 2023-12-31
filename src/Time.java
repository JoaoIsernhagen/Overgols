import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A classe Time representa uma entidade de time de futebol e é responsável por obter informações relacionadas aos times do banco de dados.
 */
public class Time {
    private BancoDeDados bancoDeDados;

    /**
     * Cria uma instância da classe Time e instancia a classe BancoDeDados.
     */
    public Time() {
        bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
    }

    /**
     * Obtém os nomes dos times do banco de dados.
     * @return um ResultSet contendo os nomes dos times.
     */
    public ResultSet obterNomesDosTimes() {
        Connection connection = bancoDeDados.conectar();
        ResultSet resultSet = null;

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT IDNOMETIMES FROM TIMES";
                resultSet = statement.executeQuery(query);
            } catch (SQLException e) {
                System.out.println("Erro ao executar consulta: " + e.getMessage());
            }
        } else {
            System.out.println("Não foi possível estabelecer a conexão com o banco de dados.");
        }

        return resultSet;
    }

    /**
     * Obtém as últimas partidas de um time específico.
     * @param timeSelecionado o nome do time selecionado.
     * @return um ResultSet contendo as informações das últimas partidas do time.
     */
    public ResultSet obterUltimasPartidas(String timeSelecionado) {
        Connection connection = bancoDeDados.conectar();
        ResultSet resultSet = null;

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT P.*, T1.IDNOMETIMES AS NOME_MANDANTE, T2.IDNOMETIMES AS NOME_VISITANTE " +
                        "FROM PARTIDAS P " +
                        "INNER JOIN TIMES T1 ON P.IDTIMEMANDANTE = T1.IDTIMES " +
                        "INNER JOIN TIMES T2 ON P.IDTIMEVISITANTE = T2.IDTIMES " +
                        "WHERE T1.IDNOMETIMES = '" + timeSelecionado + "' OR T2.IDNOMETIMES = '" + timeSelecionado + "' " +
                        "ORDER BY DATADAPARTIDA DESC LIMIT 5";
                resultSet = statement.executeQuery(query);
            } catch (SQLException e) {
                System.out.println("Erro ao executar consulta: " + e.getMessage());
            }
        } else {
            System.out.println("Não foi possível estabelecer a conexão com o banco de dados.");
        }

        return resultSet;
    }
}
