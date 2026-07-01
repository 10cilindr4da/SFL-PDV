package controller;


import model.Produto;
import model.ItemVenda;
import model.Venda;
import java.util.ArrayList;

public class VendaController {

    private ArrayList<Produto> estoque;
    private Venda vendaAtual;
    private double percentualDesconto;

    public VendaController() {
        estoque = new ArrayList<>();
        vendaAtual = new Venda();
        percentualDesconto = 0.0;

        // Produtos fixados na tela inicial do usuário para escolher com seus preços, quantidade e estoques
        estoque.add(new Produto(1, "Refrigerante", 7.50, 20));
        estoque.add(new Produto(2, "Chocolate", 4.00, 35));
        estoque.add(new Produto(3, "Água", 2.50, 50));
    }

    public Produto buscarProduto(int codigo) {
        // Vai pecorrer a classe para encontrar os produtos
        for (Produto produto : estoque) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }
        return null;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        ItemVenda item = new ItemVenda(produto, quantidade);
        vendaAtual.adicionarItem(item);
    }

    public Venda getVendaAtual() {
        return vendaAtual;
    }

    public ArrayList<Produto> getEstoque() {
        return estoque;
    }

    public void setDesconto(double desconto) {
        this.percentualDesconto = desconto;
    }

    public double calcularTotalComDesconto() {
        double total = vendaAtual.calcularTotal();
        double valorDesconto = total * (percentualDesconto / 100);
        return total - valorDesconto;
    }

    public void finalizarVenda() {

        // Vai percorrer a classe de venda para baixar a quantidade dos produtos
        for (ItemVenda item : vendaAtual.getItens()) {
            Produto p = item.getProduto();
            p.baixarEstoque(item.getQuantidade());
        }
        novaVenda();
    }

    public void novaVenda() {
        vendaAtual = new Venda();
        percentualDesconto = 0.0;
    }
}