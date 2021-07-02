package Rascunho;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BarraProgresso implements ActionListener, PropertyChangeListener {

    // Declara componentes
    JLabel rotulo;
    JProgressBar progresso;
    JButton botao;

    public Container criaPainel()
    {
        // Cria painel
        JPanel painel = new JPanel();

        // Cria layout
        painel.setLayout(new BoxLayout(painel,BoxLayout.PAGE_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Cria componentes
        rotulo = new JLabel("Barra de progresso");
        progresso = new JProgressBar(0,100);
        botao = new JButton("Ok");

        // Alinha componentes
        rotulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        progresso.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adiciona propriedade a barra de progresso
        progresso.setStringPainted(true);

        // Cria evento do botão
        botao.addActionListener(this);

        // Adiciona componentes ao painel
        painel.add(rotulo);
        painel.add(Box.createVerticalStrut(10));
        painel.add(progresso);
        painel.add(Box.createVerticalStrut(10));
        painel.add(botao);

        return painel;
    }

    // Cria classe SwingWorker para a propriedade progress
    class ProcessaMenssagem extends SwingWorker<Void, Void>
    {

        @Override
        public Void doInBackground() throws Exception {

            // Progresso inicial
            setProgress(0);

            // cria contador
            for (int andamento = 0; andamento <= 100; andamento++)
            {
                // Cria um efeito de espera
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                // Atualiza o contador
                setProgress(Math.min(andamento, 100));
            }

            return null;
        }

    }

    // Evento do botão
    public void actionPerformed(ActionEvent arg0)
    {

        if (arg0.getSource() == botao)
        {
            // Cria objeto de progresso
            ProcessaMenssagem processa = new ProcessaMenssagem();
            processa.addPropertyChangeListener(this);
            processa.execute();
        }

    }

    // Atualiza a propriedade do objeto
    public void propertyChange(PropertyChangeEvent evt) {

        if ("progress" == evt.getPropertyName())
        {
            int andamento = (Integer) evt.getNewValue();
            progresso.setValue(andamento);
        }
    }

    // Cria GUI
    public static void criaGUI()
    {
        // Cria formulario principal
        JFrame formulario = new JFrame("DA - Progress Bar");
        formulario.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cria instancia da classe
        BarraProgresso barra = new BarraProgresso();

        // Adiciona painel principal
        formulario.setContentPane(barra.criaPainel());

        formulario.setSize(300,150);

        formulario.setVisible(true);
    }

    public static void main(String[] args) {

        // Thread do swing
        SwingUtilities.invokeLater(() -> criaGUI());

    }

}
