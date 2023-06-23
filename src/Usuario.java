import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A classe Usuario representa um usuário do sistema.
 */
class Usuario {
    private String username;
    private String password;
    private JButton loginButton;

    /**
     * Construtor da classe Usuario.
     * @param username o nome de usuário
     * @param password a senha do usuário
     * @param loginButton o botão de login associado ao usuário
     */
    public Usuario(String username, String password, JButton loginButton) {
        this.username = username;
        this.password = password;
        this.loginButton = loginButton;
    }

    /**
     * Autentica o usuário verificando as credenciais fornecidas no banco de dados.
     */
    public void autenticar() {
        // Conecta ao banco de dados
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        Connection connection = bancoDeDados.conectar();

        if (connection != null) {
            try {
                // Executa a consulta no banco de dados para verificar as credenciais
                String query = "SELECT * FROM usuario WHERE nomeusuario = ? AND senhausuario = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Se as credenciais estiverem corretas, abre a interface principal do sistema
                    this.username = username;
                    dispose();
                    OverGolsInterface overGolsInterface = new OverGolsInterface(username);
                    overGolsInterface.setSize(800, 600);
                    overGolsInterface.setLocationRelativeTo(null);
                    overGolsInterface.setVisible(true);
                } else {
                    // Se as credenciais estiverem incorretas, exibe uma mensagem de erro
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos!", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao executar a consulta: " + ex.getMessage(), "Erro de banco de dados", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Fecha a conexão com o banco de dados
                bancoDeDados.fecharConexao(connection);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados!", "Erro de conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método privado para fechar a janela do login.
     */
    private void dispose() {
        Window window = SwingUtilities.getWindowAncestor(loginButton);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.dispose();
        }
    }

    /**
     * Atualiza a senha do usuário no banco de dados.
     * @param newPassword a nova senha do usuário
     */
    public void updatePassword(String newPassword) {
        // Conecta ao banco de dados
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        Connection connection = bancoDeDados.conectar();

        if (connection != null) {
            try {
                // Executa a atualização da senha no banco de dados
                String query = "UPDATE usuario SET senhausuario = ? WHERE nomeusuario = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, newPassword);
                statement.setString(2, username);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Se a atualização for bem-sucedida, exibe uma mensagem de sucesso
                    JOptionPane.showMessageDialog(null, "Senha alterada com sucesso!", "Troca de Senha", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Se a atualização falhar, exibe uma mensagem de erro
                    JOptionPane.showMessageDialog(null, "Erro ao alterar a senha!", "Erro de Troca de Senha", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao executar a atualização da senha: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Fecha a conexão com o banco de dados
                bancoDeDados.fecharConexao(connection);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados!", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exclui a conta do usuário do banco de dados.
     */
    public void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir sua conta?\nEssa ação não pode ser desfeita!", "Excluir Conta", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Conecta ao banco de dados
            BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
            Connection connection = bancoDeDados.conectar();

            if (connection != null) {
                try {
                    // Executa a exclusão da conta no banco de dados
                    String query = "DELETE FROM usuario WHERE nomeusuario = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, username);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Se a exclusão for bem-sucedida, exibe uma mensagem de sucesso e fecha a janela
                        JOptionPane.showMessageDialog(null, "Conta excluída com sucesso!", "Excluir Conta", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        // Se a exclusão falhar, exibe uma mensagem de erro
                        JOptionPane.showMessageDialog(null, "Erro ao excluir a conta!", "Erro de Exclusão de Conta", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao executar a exclusão da conta: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Fecha a conexão com o banco de dados
                    bancoDeDados.fecharConexao(connection);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados!", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

