package model;

import java.util.ArrayList;

public class Venda {

   private ArrayList<ItemVenda> itens;

   public Venda() {
       itens = new ArrayList<>();
   }

   public void adicionarItem(ItemVenda item) {
       itens.add(item);
   }

   public double calcularTotal() {

       double total = 0;

       for(ItemVenda item:itens) {
           total += item.getSubtotal();
       }

       return total;
   }

   public ArrayList<ItemVenda> getItens() {
       return itens;
   }
}