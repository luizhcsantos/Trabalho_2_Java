package Trabalho_2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceCorrida extends JFrame implements ActionListener {

    private int numCarros;
    private int numVoltas;
    private double probA;
    private double probQ;
    private JTextField textFieldCarros, textFieldVoltas, textFieldQuebra, textFieldAbast;
    private JTable tabela;
    private JPanel painelFiguras;

    public InterfaceCorrida() {

        numCarros = 0;
        numVoltas = 0;
        probA = 0.0;
        probQ = 0.0;
        createGUI();
    }

    private void createGUI() {

        JPanel painel = new JPanel();
        JPanel painelTb = new JPanel();
        painelFiguras = new JPanel();
        painel.setLayout(new GridLayout(6, 2));

        JLabel labelCarros = new JLabel("Nº carros");
        labelCarros.setToolTipText("Número mínimo de carros: 5 ");
        JLabel labelVoltas = new JLabel("Qtde voltas");
        labelVoltas.setToolTipText("Número mínimo de voltas: 10 ");
        JLabel labelQuebra = new JLabel("Probabilidade Quebra");
        labelQuebra.setToolTipText("Valores entre [0.0, 1.0] ou [0.0, 100.0]");
        JLabel labelAbast = new JLabel("Probabilidade Abastecimento");
        labelAbast.setToolTipText("Valores entre [0.0, 1.0] ou [0.0, 100.0]");

        textFieldCarros = new JTextField(5);
        textFieldCarros.setName("numCarros");
        textFieldCarros.setInputVerifier(new validVerifier());

        textFieldVoltas = new JTextField(5);
        textFieldVoltas.setName("numVoltas");
        textFieldVoltas.setInputVerifier(new validVerifier());

        textFieldQuebra = new JTextField(5);
        textFieldQuebra.setName("probQ");
        textFieldQuebra.setInputVerifier(new validVerifier());

        textFieldAbast = new JTextField(5);
        textFieldAbast.setName("probA");
        textFieldAbast.setInputVerifier(new validVerifier());

        JButton botao = new JButton("Iniciar corrida");

        painelFiguras.setLayout(new BoxLayout(painelFiguras, BoxLayout.Y_AXIS));
        painelFiguras.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        painelFiguras.setPreferredSize(new Dimension(300, 400));

        //eventos = Collections.synchronizedList(new ArrayList<>());
        DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[]{"Carros", "Colocação"}, 0);
        tabela = new JTable(defaultTableModel);
        JScrollPane barraRolagem = new JScrollPane(tabela);

        painel.add(labelCarros);
        painel.add(textFieldCarros);
        painel.add(labelVoltas);
        painel.add(textFieldVoltas);
        painel.add(labelQuebra);
        painel.add(textFieldQuebra);
        painel.add(labelAbast);
        painel.add(textFieldAbast);

        botao.addActionListener(this);
        painel.add(botao);

        painelTb.add(barraRolagem);

        painel.add(Box.createVerticalStrut(getHeight()-1));
        add(painel, BorderLayout.WEST);
        //add(Box.createVerticalStrut(getHeight()-1));
        add(painelTb, BorderLayout.EAST);
        painelTb.add(Box.createVerticalStrut(getHeight()-1));
        add(painelFiguras, BorderLayout.CENTER);
        painelFiguras.add(Box.createVerticalStrut(getHeight()-1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // cada volta equivale a 1000 millisegundos
        int distanciaVolta = 1000;
        int DISTANCIA;
        Thread t;

        DefaultTableModel df = (DefaultTableModel) tabela.getModel();
        if (df.getRowCount() > 0) {
            df.getDataVector().removeAllElements();
            df.fireTableDataChanged();
        }

        numCarros = (Integer.parseInt(textFieldCarros.getText()));
        numVoltas = (Integer.parseInt(textFieldVoltas.getText()));
        probA = (Double.parseDouble(textFieldAbast.getText()));
        probQ = (Double.parseDouble(textFieldQuebra.getText()));
        DISTANCIA = numVoltas*distanciaVolta;

        for (int i=0; i< numCarros; i++) {

            JPanel painelCarro = new JPanel();
            painelCarro.setBorder(BorderFactory.createLineBorder(Color.blue));
            painelCarro.setBackground(Color.WHITE);
            painelCarro.setPreferredSize(new Dimension(painelFiguras.getWidth()/numCarros,
                    painelFiguras.getHeight()));
            painelCarro.setLayout(null);
            painelFiguras.add(painelCarro);
            painelCarro.revalidate();
            painelFiguras.revalidate();

            t = new Thread(new CarroCorrida("CARRO_" + (i + 1), DISTANCIA, probQ, probA, df, painelCarro));
            t.start();
        }
    }

    static class validVerifier extends InputVerifier {

        Border bordaOriginal;

        @Override
        public boolean verify(JComponent input) {
            JTextField jtf = (JTextField) input;

            if ("numVoltas".equals(jtf.getName())) {
                return !jtf.getText().equals("") && Integer.parseInt(jtf.getText()) >= 10;
            }
            else if ("numCarros".equals(jtf.getName())) {
                return !jtf.getText().equals("") && Integer.parseInt(jtf.getText()) >= 5;
            }
            else if ("probQ".equals(jtf.getName())) {
                return !jtf.getText().equals("") && (!(Double.parseDouble(jtf.getText()) < 0.0))
                        && !((Double.parseDouble(jtf.getText())) > 100.0);
            }
            else if ("probA".equals(jtf.getName())) {
                return !jtf.getText().equals("") && (!(Double.parseDouble(jtf.getText()) < 0.0))
                        && !((Double.parseDouble(jtf.getText())) > 100.0);
            }
            return true;
        }

        @Override
        public boolean shouldYieldFocus(JComponent source, JComponent target) {

            boolean ehValido = verify(source);

            if (!ehValido) {
                bordaOriginal = bordaOriginal == null ? source.getBorder() : bordaOriginal;
                source.setBorder(BorderFactory.createLineBorder(Color.red, 2));
            }
            else {
                if (bordaOriginal != null) {
                    source.setBorder(bordaOriginal);
                    bordaOriginal = null;
                }
            }
            return ehValido;
        }
    }
}
