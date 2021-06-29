package Trabalho_2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

public class CarroCorrida implements Runnable {


    private final String nome;
    private int distanciaPercorrida;
    private final int distanciaTotalPercorrida;
    private static int colocacao;
    private final AtomicInteger row;
    private int mov;
    private double probQuebra;
    private double probAbastecimento;

    //private List<String> eventos;
    private DefaultTableModel tableModel;

    public CarroCorrida(String nome, int distanciaTotalPercorrida,
                        double probQuebra, double probAbastecimento,
            /*List<String> eventos,*/ DefaultTableModel tableModel) {


        this.nome = nome;
        this.distanciaTotalPercorrida = distanciaTotalPercorrida;
        if (probQuebra > 1.0 && probQuebra <= 100.0)
            this.probQuebra = probQuebra/100.0;
        if (probAbastecimento > 1.0 && probAbastecimento <= 100.0)
            this.probAbastecimento = probAbastecimento/100.0;
        //this.eventos = eventos;
        this.tableModel = tableModel;
        colocacao = 0;
        row = new AtomicInteger(0);
        mov = 0;
        distanciaPercorrida = 0;
    }

    public void carroCorrendoSituacao() {
        out.println("o carro "+nome+" moveu-se "+mov+" metros " +
                "e já percorreu "+distanciaPercorrida+" metros");
    }

    public void carrosCorrendo() {
        int MOV_MAXIMO = 50;
        mov = (int) (Math.random()* MOV_MAXIMO);
        distanciaPercorrida += mov;
        if (distanciaPercorrida > distanciaTotalPercorrida)
            distanciaPercorrida = distanciaTotalPercorrida;
    }

    public void carroDesacelerando() {
        Thread.yield();
    }

    public void colocacaoCarro() {
        synchronized (row) {
            row.set(colocacao);
            colocacao++;
            tableModel.setValueAt(nome + " é o " + colocacao + "º colocado",
                    row.intValue(),
                    1);
        }
    }

    @Override
    public void run() {
        while(distanciaPercorrida < distanciaTotalPercorrida) {
            long  l = calculaProb(probAbastecimento, probQuebra);
            if (l == 1) {
                carrosCorrendo();
            } else {
                carroDesacelerando();
                try {
                    int sleepTime = 100; // milissegundos
                    Thread.sleep(l * sleepTime);
                    SwingWorker<String, Object[]> sw = new SwingWorker<String, Object[]>() {
                        String evento = null;
                        @Override
                        protected String doInBackground() throws Exception {
                            Thread.sleep(1);
                            if (l == 2)
                                evento = nome+" parou para abastecer";
                            else if (l == 3)
                                evento = nome+" quebrou";
                            else if (l == 4)
                                evento = nome+" quebrou e aproveitou p/ abastecer";
                            return evento;
                        }

                        @Override
                        protected void done() {
                            if (evento != null)
                                tableModel.addRow(new String[]{evento});
                        }
                    };
                    sw.execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        colocacaoCarro();
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
                else if (probAbastecimento == 0) {
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
