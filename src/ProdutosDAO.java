
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public void cadastrarProduto(ProdutosDTO produto) {
        if (inserirProduto(produto)) {
            JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ArrayList<ProdutosDTO> listarProdutos() {
        return listagem;
    }

    public boolean inserirProduto(ProdutosDTO produto) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uc11", "tutor", "Tutor123!");
            String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setDouble(2, produto.getValor());
            prep.setString(3, produto.getStatus());
            prep.executeUpdate();
            System.out.println("Produto inserido com sucesso!");
            return true;
        } catch (SQLException ex) {
            System.out.println("Erro ao inserir produto: " + ex.getMessage());
            return false;
        } finally {
            fecharConexao();
        }
    }

    public ArrayList<ProdutosDTO> obterTodosOsProdutos() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uc11", "tutor", "Tutor123!");
            String sql = "SELECT * FROM produtos";
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery();

            listagem.clear();

            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));

                listagem.add(produto);
            }

            return listagem;

        } catch (SQLException ex) {
            System.out.println("Erro ao obter todos os produtos: " + ex.getMessage());
            return null;
        } finally {
            fecharConexao();
        }
    }

    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uc11", "tutor", "Tutor123!");
            String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery();

            listagem.clear();

            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));

                listagem.add(produto);
            }

            return listagem;

        } catch (SQLException ex) {
            System.out.println("Erro ao obter produtos vendidos: " + ex.getMessage());
            return null;
        } finally {
            fecharConexao();
        }
    }

    public boolean verificarVenda(int productId) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uc11", "tutor", "Tutor123!");
            String sql = "SELECT status FROM produtos WHERE id = ?";
            prep = conn.prepareStatement(sql);
            prep.setInt(1, productId);
            resultset = prep.executeQuery();

            if (resultset.next()) {
                String status = resultset.getString("status");
                return status.equals("Vendido");
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao verificar venda: " + ex.getMessage());
        } finally {
            fecharConexao();
        }
        return false;
    }

    public void venderProduto(int productId) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uc11", "tutor", "Tutor123!");
            String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
            prep = conn.prepareStatement(sql);
            prep.setInt(1, productId);
            prep.executeUpdate();
            System.out.println("Produto vendido com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao vender produto: " + ex.getMessage());
        } finally {
            fecharConexao();
        }
    }

    private void fecharConexao() {
        try {
            if (conn != null) {
                conn.close();
            }
            if (prep != null) {
                prep.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao fechar a conexão: " + ex.getMessage());
        }
    }
}
