import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Classe responsável por estabelecer a conexão com um banco de dados MySQL.
 */
public class BancoDeDados {
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/overgols";
    private String username = "root";
    private String password = "Gyp07@o";
    /**
     * Construtor que permite definir o nome de usuário e a senha para a conexão.
     *
     * @param username nome de usuário para a conexão com o banco de dados
     * @param password senha para a conexão com o banco de dados
     */
    public BancoDeDados(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /**
     * Construtor padrão que utiliza o nome de usuário e a senha padrão para a conexão.
     */
    public BancoDeDados() {

    }

    /**
     * Estabelece a conexão com o banco de dados.
     *
     * @return objeto Connection que representa a conexão estabelecida
     */
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
    /**
     * Fecha a conexão com o banco de dados.
     *
     * @param connection objeto Connection que representa a conexão a ser fechada
     */
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