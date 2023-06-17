import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Time {
    private final int id;
    private final String nome;

    public Time(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static List<Time> obterTimesDoBancoDeDados() {
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        List<Time> times = new ArrayList<>();

        Connection connection = bancoDeDados.conectar();

        if (connection != null) {
            try {
                String sql = "SELECT IDTIMES, IDNOMETIMES FROM TIMES";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    int id = resultSet.getInt("IDTIMES");
                    String nome = resultSet.getString("IDNOMETIMES");
                    Time time = new Time(id, nome);
                    times.add(time);
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
            } finally {
                bancoDeDados.fecharConexao(connection);
            }
        }

        return times;
    }
}