package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import controller.VendaController;
import model.Produto;
import model.ItemVenda;
import service.Caixa;

public class TelaPDV extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtQuantidade;
    private JTextField txtDesconto;
    private JTextField txtValorPago;

    private JTextArea areaCarrinho;
    private JTextArea areaProdutosFixos;

    private JLabel lblSubtotal;
    private JLabel lblTotalEsquerdo;
    private JLabel lblTotalInferior;
    private JLabel lblTroco;

    private VendaController controller;
    private Caixa caixa;

    public TelaPDV() {
        controller = new VendaController();
        caixa = new Caixa();

        setTitle("Sistema de Frente de lOja");
        setSize(950, 650); // Tamanho
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        //  Produtos em estoque
        JPanel painelProduto = new JPanel(new BorderLayout());
        painelProduto.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Produtos em Estoque", TitledBorder.LEFT, TitledBorder.TOP));

        areaProdutosFixos = new JTextArea(4, 50);
        areaProdutosFixos.setEditable(false); // Impede do usuário alterar os valores
        painelProduto.add(new JScrollPane(areaProdutosFixos), BorderLayout.CENTER);
        add(painelProduto, BorderLayout.NORTH);

        // Divisao central do layout
        JPanel painelCentralDivisao = new JPanel(new BorderLayout(10, 10));

        // Placar de total da venda
        JPanel painelFinanceiroEsquerdo = new JPanel(new BorderLayout());
        painelFinanceiroEsquerdo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Total das compras", TitledBorder.LEFT, TitledBorder.TOP));
        painelFinanceiroEsquerdo.setPreferredSize(new Dimension(300, 300));

        lblTotalEsquerdo = new JLabel("Total: R$ 0.00", SwingConstants.CENTER);
        painelFinanceiroEsquerdo.add(lblTotalEsquerdo, BorderLayout.CENTER);
        painelCentralDivisao.add(painelFinanceiroEsquerdo, BorderLayout.WEST);

        //  Eentrada e carrinho de compras
        JPanel painelDireitoVendas = new JPanel(new BorderLayout(5, 5));

        JPanel painelCamposVenda = new JPanel(new GridLayout(2, 3, 5, 5));
        painelCamposVenda.setBorder(BorderFactory.createTitledBorder("Adicionar Item"));

        painelCamposVenda.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        painelCamposVenda.add(txtCodigo);

        JButton btnAdicionar = new JButton("Adicionar Produto");
        painelCamposVenda.add(btnAdicionar);

        painelCamposVenda.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField("1");
        painelCamposVenda.add(txtQuantidade);
        painelCamposVenda.add(new JLabel(""));

        painelDireitoVendas.add(painelCamposVenda, BorderLayout.NORTH);

        areaCarrinho = new JTextArea();
        areaCarrinho.setEditable(false);
        JScrollPane scrollCarrinho = new JScrollPane(areaCarrinho);
        scrollCarrinho.setBorder(BorderFactory.createTitledBorder("Itens da Venda Atual"));
        painelDireitoVendas.add(scrollCarrinho, BorderLayout.CENTER);

        painelCentralDivisao.add(painelDireitoVendas, BorderLayout.CENTER);
        add(painelCentralDivisao, BorderLayout.CENTER);

        //  Totais, descontos e acoes do caixa
        JPanel painelInferior = new JPanel(new BorderLayout(10, 10));
        painelInferior.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Fechamento", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel painelValoresDinheiro = new JPanel(new GridLayout(3, 4, 10, 5));

        lblSubtotal = new JLabel("Subtotal: R$ 0.00");
        painelValoresDinheiro.add(lblSubtotal);

        painelValoresDinheiro.add(new JLabel("Desconto (%):", SwingConstants.RIGHT));
        txtDesconto = new JTextField("0");
        painelValoresDinheiro.add(txtDesconto);

        JButton btnCalcularFinanceiro = new JButton("Calcular valores");
        painelValoresDinheiro.add(btnCalcularFinanceiro);

        lblTotalInferior = new JLabel("Total c/ Desc: R$ 0.00");
        painelValoresDinheiro.add(lblTotalInferior);

        painelValoresDinheiro.add(new JLabel("Total Recebido (R$):", SwingConstants.RIGHT));
        txtValorPago = new JTextField();
        painelValoresDinheiro.add(txtValorPago);
        painelValoresDinheiro.add(new JLabel(""));

        lblTroco = new JLabel("Troco: R$ 0.00");
        painelValoresDinheiro.add(lblTroco);

        painelValoresDinheiro.add(new JLabel(""));
        painelValoresDinheiro.add(new JLabel(""));
        painelValoresDinheiro.add(new JLabel(""));

        painelInferior.add(painelValoresDinheiro, BorderLayout.CENTER);

        // Botoes de controle laterais
        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton btnNovaVenda = new JButton("Nova Venda");
        JButton btnFinalizar = new JButton("Finalizar Venda");
        JButton btnCancelar = new JButton("Cancelar Venda");

        painelBotoes.add(btnNovaVenda);
        painelBotoes.add(btnFinalizar);
        painelBotoes.add(btnCancelar);

        painelInferior.add(painelBotoes, BorderLayout.EAST);
        add(painelInferior, BorderLayout.SOUTH);

        // Vinculo dos botoes com as funcoes
        btnAdicionar.addActionListener(e -> executarAdicao());
        btnCalcularFinanceiro.addActionListener(e -> aplicarFinanceiro());
        btnNovaVenda.addActionListener(e -> limparVenda());
        btnFinalizar.addActionListener(e -> finalizarVenda());
        btnCancelar.addActionListener(e -> limparVenda());

        // Carrega dados iniciais da tela
        listarProdutosEstoque();
        atualizarCarrinho();
    }

    private void executarAdicao() {
        try {
            String strCodigo = txtCodigo.getText().trim();
            String strQtd = txtQuantidade.getText().trim();

            int codigo = Integer.parseInt(strCodigo);
            int quantidade = Integer.parseInt(strQtd);

            Produto produto = controller.buscarProduto(codigo);

            if (produto != null) {
                if (produto.getEstoque() >= quantidade) {
                    controller.adicionarItem(produto, quantidade);

                    txtCodigo.setText("");
                    txtQuantidade.setText("1");

                    atualizarCarrinho();
                } else {
                    JOptionPane.showMessageDialog(this, "Estoque insuficiente! Apenas " + produto.getEstoque() + " unidades disponíveis.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Produto não cadastrado no sistema.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Os campos 'Código' e 'Quantidade' devem conter apenas números.", "ERRO DE ENTRADA", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFinanceiro() {
        try {
            String strDesconto = txtDesconto.getText().trim();
            double desconto = Double.parseDouble(strDesconto.replace(",", "."));
            controller.setDesconto(desconto);

            atualizarCarrinho();

            String strValorPago = txtValorPago.getText().trim();
            if (!strValorPago.isEmpty()) {
                double valorPago = Double.parseDouble(strValorPago.replace(",", "."));
                double total = controller.calcularTotalComDesconto();

                if (valorPago >= total) {
                    double troco = caixa.calcularTroco(valorPago, total);
                    lblTroco.setText(String.format("Troco: R$ %.2f", troco));
                } else {
                    JOptionPane.showMessageDialog(this, "O valor pago é menor que o total da compra", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Insira os valores em desconto e valor a ser pago", "ERRO DE ENTRADA", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarCarrinho() {
        areaCarrinho.setText("");

        if (controller.getVendaAtual().getItens().isEmpty()) {
            areaCarrinho.append("\nCarrinho de Compras Vazio");
        } else {
            for (ItemVenda item : controller.getVendaAtual().getItens()) {
                areaCarrinho.append(" " + item.getQuantidade() + "x  -  " + item.getProduto().getNome() +
                        "  |  Subtotal: R$ " + String.format("%.2f", item.getSubtotal()) + "\n");
            }
        }

        double subtotal = controller.getVendaAtual().calcularTotal();
        double total = controller.calcularTotalComDesconto();

        lblSubtotal.setText(String.format("Subtotal: R$ %.2f", subtotal));
        lblTotalInferior.setText(String.format("Total c/ Desc: R$ %.2f", total));
        lblTotalEsquerdo.setText(String.format("TOTAL: R$ %.2f", total));
    }

    private void listarProdutosEstoque() {
        areaProdutosFixos.setText("");
        for (Produto p : controller.getEstoque()) {
            areaProdutosFixos.append(" CÓD: " + p.getCodigo() + "  |  " + p.getNome() +
                    "  |  Preço: R$ " + String.format("%.2f", p.getPreco()) +
                    "  |  Estoque Disponível: " + p.getEstoque() + "\n");
        }
    }

    private void finalizarVenda() {
        if (controller.getVendaAtual().getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há produtos no carrinho para finalizar a venda.");
            return;
        }

        controller.finalizarVenda();
        JOptionPane.showMessageDialog(this, "Venda concluída com sucesso! O estoque foi atualizado no banco.");
        limparVenda();
    }

    private void limparVenda() {
        controller.novaVenda();
        txtCodigo.setText("");
        txtQuantidade.setText("1");
        txtDesconto.setText("0");
        txtValorPago.setText("");
        lblTroco.setText("Troco: R$ 0.00");
        atualizarCarrinho();
        listarProdutosEstoque();
    }
}