package main;

import javax.swing.*;
import java.awt.*;

public class PainelCarro extends JPanel implements Runnable {

    Image carro;

    public PainelCarro(int w, int h) {
        setSize(new Dimension(w, h));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension tam = getSize();
        g.setColor(Color.BLACK);

        g.drawImage(carro, 0, 0 , this);
    }

    @Override
    public void run() {
        carro = new ImageIcon("C:\\Users\\lhsan\\UNESP\\2021\\1º Semestre\\Introducao a Tecnologia Java - Optativa\\Trabalhos\\Trabalho II\\red__car.png").getImage();
        repaint();
    }
}
