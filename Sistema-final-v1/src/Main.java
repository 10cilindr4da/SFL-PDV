import view.TelaPDV;

public class Main {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            TelaPDV tela = 
                    new TelaPDV();

            tela.setVisible(true);

        });
    }
}