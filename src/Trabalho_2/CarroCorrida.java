package Trabalho_2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

public class CarroCorrida implements Runnable, PropertyChangeListener {


    private final String nome;
    private int distanciaPercorrida;
    private final int distanciaTotalPercorrida;
    private static int colocacao;
    private final AtomicInteger row;
    private int mov;
    private double probQuebra;
    private double probAbastecimento;

    private final JPanel painelFiguras;
    private final JProgressBar progressBar;

    private final DefaultTableModel tableModel;

    public CarroCorrida(String nome, int distanciaTotalPercorrida,
                        double probQuebra, double probAbastecimento,
                        DefaultTableModel tableModel,
                        JPanel painelFiguras) {


        this.nome = nome;
        this.distanciaTotalPercorrida = distanciaTotalPercorrida;
        if (probQuebra > 1.0 && probQuebra <= 100.0)
            this.probQuebra = probQuebra/100.0;
        if (probAbastecimento > 1.0 && probAbastecimento <= 100.0)
            this.probAbastecimento = probAbastecimento/100.0;
        this.tableModel = tableModel;
        this.painelFiguras = painelFiguras;
        colocacao = 0;
        row = new AtomicInteger(0);
        mov = 0;
        distanciaPercorrida = 0;
        progressBar = new JProgressBar(0, distanciaTotalPercorrida);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        /*ProgressoWorker pw = new ProgressoWorker(distanciaPercorrida);
        pw.addPropertyChangeListener(this);
        pw.execute();*/
        painelFiguras.add(progressBar);
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
        //ProgressoWorker pw = new ProgressoWorker();
        //pw.addPropertyChangeListener(this);
        //pw.execute();
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
                    TabelaWorker tw = new TabelaWorker(l);
                    tw.execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (painelFiguras) {
                ProgressoWorker pw = new ProgressoWorker(distanciaPercorrida, distanciaTotalPercorrida);
                pw.execute();
                pw.addPropertyChangeListener(pcEvent -> {
                    if (pcEvent.getPropertyName().equals("progress")) {
                        int value = (distanciaPercorrida/distanciaTotalPercorrida)/100;
                        progressBar.setValue(value);
                    }
                    try {
                        pw.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        colocacaoCarro();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if ("progress".equals(evt.getPropertyName()))
        {
            int andamento = (Integer) evt.getNewValue();
            progressBar.setValue(andamento);
        }
    }

    class TabelaWorker extends SwingWorker<String, Object[]> {

        String evento = null;
        double l;
        public TabelaWorker(double l) {
            this.l = l;
        }

        @Override
        protected String doInBackground() throws Exception {
            Thread.sleep(1);
            if (l == 2) // L PRECISA SER ACESSADO POR ESTA CLASSE... COMO RESOLVER???
                evento = nome + " parou para abastecer";
            else if (l == 3)
                evento = nome + " quebrou";
            else if (l == 4)
                evento = nome + " quebrou e aproveitou p/ abastecer";
            return evento;
        }

        @Override
        protected void done() {
            if (evento != null) {
                tableModel.addRow(new String[]{evento});
                setProgress(distanciaPercorrida/100);
            }
        }
    }

    static class ProgressoWorker extends SwingWorker<Void, Integer> {

        int distanciaTotalPercorrida, distanciaPercorrida;

        public ProgressoWorker(int distanciaPercorrida, int distanciaTotalPercorrida) {
            this.distanciaPercorrida = distanciaPercorrida;
            this.distanciaTotalPercorrida = distanciaTotalPercorrida;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int progress = 0;
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress = (distanciaPercorrida/distanciaTotalPercorrida)/100;
                setProgress(Math.min(progress, 100));
            }
            return null;
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
