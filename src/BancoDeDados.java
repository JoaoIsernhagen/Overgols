import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDeDados {
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/overgols";
    private String username = "root";
    private String password = "Gyp07@o";

    public BancoDeDados(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public BancoDeDados() {

    }

    public Connection conectar() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do banco de dados não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return connection;
    }

    public void fecharConexao(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão com o banco de dados: " + e.getMessage());
            }
        }
    }
}