package Trabalho_2;

import javax.swing.*;

public class Principal {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            InterfaceCorrida interfaceCorrida = new InterfaceCorrida();
            interfaceCorrida.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            interfaceCorrida.pack();
            interfaceCorrida.setVisible(true);
        });
    }
}
