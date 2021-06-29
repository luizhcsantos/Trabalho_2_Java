package Trabalho_2;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MainPanelFrame extends JFrame {

    public MainPanelFrame() {

        Container container = getContentPane();
        setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        InterfaceCorrida interfaceCorrida = new InterfaceCorrida();
        JPanel progressoPainel = new JPanel();

        container.add(interfaceCorrida);
        container.add(progressoPainel);
    }
}
