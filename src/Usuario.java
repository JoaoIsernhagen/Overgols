import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Usuario {
    private String username;
    private String password;
    private JButton loginButton;

    public Usuario(String username, String password, JButton loginButton) {
        this.username = username;
        this.password = password;
        this.loginButton = loginButton;
    }

    public void autenticar() {
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        Connection connection = bancoDeDados.conectar();
        if (connection != null) {
            try {
                String query = "SELECT * FROM usuario WHERE nomeusuario = ? AND senhausuario = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    this.username = username;
                    dispose();
                    OverGolsInterface overGolsInterface = new OverGolsInterface(username);
                    overGolsInterface.setSize(800, 600);
                    overGolsInterface.setLocationRelativeTo(null);
                    overGolsInterface.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos!", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao executar a consulta: " + ex.getMessage(), "Erro de banco de dados", JOptionPane.ERROR_MESSAGE);
            } finally {
                bancoDeDados.fecharConexao(connection);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados!", "Erro de conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dispose() {
        Window window = SwingUtilities.getWindowAncestor(loginButton);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.dispose();
        }
    }

    public void updatePassword(String newPassword) {
        BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
        Connection connection = bancoDeDados.conectar();
        if (connection != null) {
            try {
                String query = "UPDATE usuario SET senhausuario = ? WHERE nomeusuario = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, newPassword);
                statement.setString(2, username);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Senha alterada com sucesso!", "Troca de Senha", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Erro ao alterar a senha!", "Erro de Troca de Senha", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erro ao executar a atualização da senha: " + ex.getMessage(),
                        "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            } finally {
                bancoDeDados.fecharConexao(connection);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Erro ao conectar ao banco de dados!", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Tem certeza que deseja excluir sua conta?\nEssa ação não pode ser desfeita!",
                "Excluir Conta", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            BancoDeDados bancoDeDados = new BancoDeDados("root", "Gdyp07@o");
            Connection connection = bancoDeDados.conectar();
            if (connection != null) {
                try {
                    String query = "DELETE FROM usuario WHERE nomeusuario = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, username);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null,
                                "Conta excluída com sucesso!", "Excluir Conta", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Erro ao excluir a conta!", "Erro de Exclusão de Conta", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Erro ao executar a exclusão da conta: " + ex.getMessage(),
                            "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
                } finally {
                    bancoDeDados.fecharConexao(connection);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Erro ao conectar ao banco de dados!", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
