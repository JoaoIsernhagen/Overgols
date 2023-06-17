import java.sql.*;

public class Usuario {
    private BancoDeDados bancoDeDados;

    public Usuario() {
        bancoDeDados = new BancoDeDados("root", "Gdyp07@o"); // Substitua com o usuário e senha corretos
    }

    public boolean fazerLogin(String email, String senha) {
        Connection connection = bancoDeDados.conectar();

        if (connection != null) {
            try {
                String query = "SELECT * FROM USUARIO WHERE EMAILUSUARIO = ? AND SENHAUSUARIO = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, senha);
                ResultSet resultSet = statement.executeQuery();

                boolean loginValido = resultSet.next(); // Verifica se há algum resultado na consulta

                resultSet.close();
                statement.close();
                bancoDeDados.fecharConexao(connection);

                return loginValido;
            } catch (SQLException e) {
                System.out.println("Erro ao executar a consulta: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean cadastrarUsuario(String nome, String email, String senha) {
        Connection connection = bancoDeDados.conectar();

        if (connection != null) {
            try {
                String query = "INSERT INTO USUARIO (NOMEUSUARIO, EMAILUSUARIO, SENHAUSUARIO) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nome);
                statement.setString(2, email);
                statement.setString(3, senha);

                int rowsInserted = statement.executeUpdate();

                statement.close();
                bancoDeDados.fecharConexao(connection);

                return rowsInserted > 0; // Verifica se pelo menos uma linha foi inserida
            } catch (SQLException e) {
                System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            }
        }
        return false;
    }
}