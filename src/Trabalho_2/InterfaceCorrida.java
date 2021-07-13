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
        //painel.setLayout(new GridLayout(6, 2));

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

        JButton button = new JButton("Iniciar Nova Corrida");
        button.addActionListener(this);

        javax.swing.GroupLayout painelLayout = new javax.swing.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
                painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(painelLayout.createSequentialGroup()
                                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(painelLayout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelCarros)
                                                        .addComponent(labelVoltas)
                                                        .addComponent(labelQuebra)
                                                        .addComponent(labelAbast))
                                                .addGap(29, 29, 29)
                                                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(textFieldVoltas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textFieldQuebra, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textFieldCarros, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textFieldAbast, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(painelLayout.createSequentialGroup()
                                                .addGap(53, 53, 53)
                                                .addComponent(button)))
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        painelLayout.setVerticalGroup(
                painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(painelLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelCarros)
                                        .addComponent(textFieldCarros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelVoltas)
                                        .addComponent(textFieldVoltas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelQuebra)
                                        .addComponent(textFieldQuebra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelAbast)
                                        .addComponent(textFieldAbast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(42, 42, 42)
                                .addComponent(button)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[]{"Carros", "Colocação"}, 0);
        tabela = new JTable();
        //barraRolagem.setPreferredSize(new Dimension(300, 400));
        tabela.setModel(defaultTableModel);
        JScrollPane barraRolagem = new JScrollPane(tabela);
        barraRolagem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        barraRolagem.setViewportView(tabela);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(painel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(painelFiguras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(barraRolagem, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(barraRolagem, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                                        .addComponent(painelFiguras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();

        //JButton botao = new JButton("Iniciar corrida");

        painelFiguras.setLayout(new javax.swing.BoxLayout(painelFiguras, javax.swing.BoxLayout.Y_AXIS));
        painelFiguras.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        painelFiguras.setPreferredSize(new Dimension(300, 400));

        //eventos = Collections.synchronizedList(new ArrayList<>());


        /*painel.add(labelCarros);
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
        painelFiguras.add(Box.createVerticalStrut(getHeight()-1));*/
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
                    painelFiguras.getHeight()-1));
            painelCarro.setLayout(null);
            painelFiguras.add(painelCarro);
            //painelCarro.revalidate();
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
