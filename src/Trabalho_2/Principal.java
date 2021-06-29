package Trabalho_2;

import javax.swing.*;

public class Principal {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainPanelFrame mainPanelFrame = new MainPanelFrame();
            mainPanelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPanelFrame.pack();
            mainPanelFrame.setVisible(true);
        });
    }
}
