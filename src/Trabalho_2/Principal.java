package Trabalho_2;

import javax.swing.*;
import java.util.logging.Logger;

public class Principal {

    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(InterfaceCorrida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            InterfaceCorrida interfaceCorrida = new InterfaceCorrida();
            interfaceCorrida.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            interfaceCorrida.pack();
            interfaceCorrida.setVisible(true);
        });
    }
}
