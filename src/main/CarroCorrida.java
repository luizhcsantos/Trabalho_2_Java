package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class CarroCorrida implements Runnable {


    private final String nome;
    private static double distanciaPercorrida;
    private final int distanciaTotalPercorrida;
    private static int colocacao;
    private final AtomicInteger row;
    private int mov;
    private double probQuebra;
    private double probAbastecimento;
    private static boolean estaDormindo;

    private final JLabel img = new JLabel();
    private final JPanel painelCarro;
    private static Insets insets;
    private static Dimension size;

    private final DefaultTableModel tableModel;

    public CarroCorrida(String nome, int distanciaTotalPercorrida, double probQuebra, double probAbastecimento, DefaultTableModel tableModel, JPanel painelCarro) {

        this.nome = nome;
        this.distanciaTotalPercorrida = distanciaTotalPercorrida;
        distanciaPercorrida = 0.0; // new AtomicInteger(0);
        colocacao = 0;
        row = new AtomicInteger(0);
        mov = 0;
        estaDormindo = true;

        if (probQuebra > 1.0 && probQuebra <= 100.0)
            this.probQuebra = probQuebra/100.0;
        if (probAbastecimento > 1.0 && probAbastecimento <= 100.0)
            this.probAbastecimento = probAbastecimento/100.0;

        this.painelCarro = painelCarro;
        this.tableModel = tableModel;

        img.setText("");
        img.setPreferredSize(new Dimension(90, 50));
        String pathname = "/resources/images/";
        String imgName = "car"+ (new Random().nextInt(6-1)+1)+".png";
        try {
            img.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(pathname.concat(imgName)))));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        painelCarro.add(img);

        insets = painelCarro.getInsets();
        size = img.getPreferredSize();
        img.setBounds(insets.left, insets.top, size.width, size.height);
    }

    public void carrosCorrendo() {
        int MOV_MAXIMO = 50;
        mov = (int) (Math.random()* MOV_MAXIMO);
        distanciaPercorrida += mov;
        if (distanciaPercorrida > distanciaTotalPercorrida) {
            distanciaPercorrida = distanciaTotalPercorrida;
        }
    }

    public void carroDesacelerando() {
        Thread.yield();
    }

    public void colocacaoCarro() {
        synchronized (row) {
            row.set(colocacao);
            colocacao++;
            tableModel.setValueAt(nome + " é o " +
                            colocacao + "º colocado",
                    row.intValue(),
                    1);
            switch (colocacao) {
                case 1 -> painelCarro.setBackground(Color.GREEN);
                case 2 -> painelCarro.setBackground(Color.BLUE);
                case 3 -> painelCarro.setBackground(Color.YELLOW);
            }
        }
    }

    @Override
    public void run() {
        while(distanciaPercorrida < distanciaTotalPercorrida) {
            long  l = calculaProb(probAbastecimento, probQuebra);
            if (l == 1) {
                carrosCorrendo();
                ProgressoWorker pw = new ProgressoWorker(distanciaTotalPercorrida, painelCarro, img);
                pw.execute();
                estaDormindo = false;
            } else {
                carroDesacelerando();
                try {
                    int sleepTime = 100; // milissegundos
                    sleep(l * sleepTime);
                    estaDormindo = true;
                    TabelaWorker tw = new TabelaWorker(l);
                    tw.execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        colocacaoCarro();
    }


    class TabelaWorker extends SwingWorker<String, Void> {

        String evento = null;
        double l;
        public TabelaWorker(double l) {
            this.l = l;
        }

        @Override
        protected String doInBackground() throws Exception {

            sleep(1);
            if (l == 2)
                evento = nome + " parou para abastecer";
            else if (l == 3)
                evento = nome + " quebrou";
            else if (l == 4)
                evento = nome + " quebrou e aproveitou p/ abastecer";
            return evento;
        }

        @Override
        protected void done() {
            if (evento != null)
                tableModel.addRow(new String[]{evento});
        }
    }

    static class ProgressoWorker extends SwingWorker<Double, Void> {

        int distanciaTotal;
        JPanel painelCarro;
        JLabel img;
        double novaPos;
        int OFFSET = 10;


        public ProgressoWorker(int distanciaTotal, JPanel painelCarro, JLabel img) {
            this.distanciaTotal = distanciaTotal;
            this.painelCarro = painelCarro;
            this.img = img;
        }


        @Override
        protected Double doInBackground() {

            double percent;
            percent = (distanciaPercorrida/distanciaTotal)*100;
            novaPos = ((painelCarro.getWidth()*percent)/distanciaTotal)*100;
            return novaPos+OFFSET;
        }

        @Override
        protected void done() {
            if (!estaDormindo) {
                img.setBounds((int) (insets.left + novaPos)-img.getWidth(), insets.top, size.width, size.height);
            }
        }
    }



    public long calculaProb(double probAbastecimento, double probQuebra) {

        double numAleatorio = new Random().nextDouble();

        if (probAbastecimento == 0 || probQuebra == 0) {
            if (probAbastecimento == 0 && probQuebra == 0)
                return 1;
            else {
                if (probQuebra == 0) {
                    if (numAleatorio >= 0.0 && numAleatorio <= probAbastecimento)
                        return 2;
                    else
                        return 1;
                }
                else {
                    if (numAleatorio >= 0.0 && numAleatorio <= probQuebra)
                        return 3;
                    else
                        return 1;
                }
            }
        }
        else if (probAbastecimento < probQuebra) {
            if (numAleatorio > 0.0 && numAleatorio <= probAbastecimento) {
                //  parada para abastecer
                // não incrementa numero de voltas 1 vez
                return 2;
            }
            else if (numAleatorio > probAbastecimento && numAleatorio <= probQuebra) {
                // parada devido a quebra
                // não incrementa numero de voltas 2 vezes
                return 3;
            }
            else {
                // completa a volta normalmente
                // acrescenta mais 1 volta à contagem do carro
                return 1;
            }
        }
        else if (probAbastecimento > probQuebra) {
            if (numAleatorio > 0.0 && numAleatorio <= probQuebra) {
                // parada devido a quebra
                // não incrementa numero de voltas 2 vezes
                return 3;
            }
            else if (numAleatorio > probQuebra && numAleatorio <= probAbastecimento) {
                // parada para abastecer
                // não incrementa numero de voltas 1 vez
                return 2;
            }
            else {
                // completa a volta normalmente
                // acrescenta mais 1 volta à contagem do carro
                return 1;
            }
        }
        else if (probAbastecimento == probQuebra) {
            if (numAleatorio > 0.0 && numAleatorio <= ( ( (1+probAbastecimento) * (1+probQuebra) ) -1)) {
                // parada devido a quebbra + parada para abastecer
                // não incrementa numero de voltas 3 vezes
                return ThreadLocalRandom.current().nextInt(5-1)+1;
            }
            else {
                // completa a volta normalmente
                // acrescenta mais 1 volta à contagem do carro
                return 1;
            }
        }
        return 1;
    }
}
