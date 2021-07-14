package main;

import javax.print.attribute.IntegerSyntax;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterfaceCorrida extends JFrame implements ActionListener {

    private int numCarros;
    private int numVoltas;
    private double probA;
    private double probQ;
    private int numeroCorrida;
    private boolean corridaAtiva;
    private JTextField textFieldCarros, textFieldVoltas, textFieldQuebra, textFieldAbast;
    private JTable tabela;
    private JPanel painelFiguras;

    public InterfaceCorrida() {

        numCarros = 0;
        numVoltas = 0;
        probA = 0.0;
        probQ = 0.0;
        numeroCorrida = 0;
        corridaAtiva = false;
        createGUI();
    }

    private void createGUI() {

        JPanel painel = new JPanel();
        painelFiguras = new JPanel();

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

        painelFiguras.setLayout(new BoxLayout(painelFiguras, BoxLayout.Y_AXIS));
        painelFiguras.setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
        painelFiguras.setPreferredSize(new Dimension(600, 390));

        GroupLayout painelLayout = new GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
                painelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(painelLayout.createSequentialGroup()
                                .addGroup(painelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(painelLayout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addGroup(painelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelCarros)
                                                        .addComponent(labelVoltas)
                                                        .addComponent(labelQuebra)
                                                        .addComponent(labelAbast))
                                                .addGap(29, 29, 29)
                                                .addGroup(painelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(textFieldVoltas, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textFieldQuebra, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textFieldCarros, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textFieldAbast, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(painelLayout.createSequentialGroup()
                                                .addGap(53, 53, 53)
                                                .addComponent(button)))
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        painelLayout.setVerticalGroup(
                painelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(painelLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(painelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelCarros)
                                        .addComponent(textFieldCarros, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addGroup(painelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelVoltas)
                                        .addComponent(textFieldVoltas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(painelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelQuebra)
                                        .addComponent(textFieldQuebra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(painelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelAbast)
                                        .addComponent(textFieldAbast, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(42, 42, 42)
                                .addComponent(button)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[]{"Carros", "Colocação"}, 0);
        tabela = new JTable();
        tabela.setModel(defaultTableModel);
        JScrollPane barraRolagem = new JScrollPane(tabela);
        barraRolagem.setBorder(BorderFactory.createLineBorder(new Color(102, 102, 102)));
        barraRolagem.setViewportView(tabela);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(painel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(painelFiguras, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(barraRolagem, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(barraRolagem, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                                        .addComponent(painelFiguras, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(painel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pack();
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

            Component[] comps = painelFiguras.getComponents();
            if (comps.length > 0 && numeroCorrida > 0 && !corridaAtiva) {  // se o usuário já iniciou uma corrida antes, remove as imagens
                for (Component c : comps) {
                    if (c instanceof  JPanel) {
                        painelFiguras.remove(c);
                    }
                }
                painelFiguras.revalidate();
                painelFiguras.repaint();
            }
            JPanel painelCarro = new JPanel();
            painelCarro.setBackground(Color.darkGray);
            painelCarro.setSize(new Dimension(500, painelFiguras.getHeight()/numCarros));
            Border empty = BorderFactory.createEmptyBorder(1, -1, -1, -1);
            Border dashed = BorderFactory.createDashedBorder(null, 5, 5);
            Border compound = new CompoundBorder(empty, dashed);
            painelCarro.setBorder(compound); // apenas as bordas top e bottom são desenhadas com linha tracejada
            painelCarro.setLayout(null);
            painelFiguras.add(painelCarro);
            painelFiguras.revalidate();

            t = new Thread(new CarroCorrida("CARRO_" + (i + 1), DISTANCIA, probQ, probA, df, painelCarro));
            t.start();
            corridaAtiva = true;
        }
        numeroCorrida++;
        corridaAtiva = false;
    }

    static class validVerifier extends InputVerifier {

        Border bordaOriginal;

        @Override
        public boolean verify(JComponent input) {

            JTextField jtf = (JTextField) input;

            if ("numCarros".equals(jtf.getName()))
                return !jtf.getText().equals("")
                        && verificaCaracter(jtf.getText())
                        && Integer.parseInt(jtf.getText()) >= 5;

            if ("numVoltas".equals(jtf.getName()))
                return !jtf.getText().equals("")
                        && verificaCaracter(jtf.getText())
                        && Integer.parseInt(jtf.getText()) >= 10;

            if ("probQ".equals(jtf.getName()))
                return !jtf.getText().equals("")
                        && verificaCaracter(jtf.getText())
                        && (!(Double.parseDouble(jtf.getText()) < 0.0))
                        && !((Double.parseDouble(jtf.getText())) > 100.0);

            if ("probA".equals(jtf.getName()))
                return !jtf.getText().equals("")
                        && verificaCaracter(jtf.getText())
                        && (!(Double.parseDouble(jtf.getText()) < 0.0))
                        && !((Double.parseDouble(jtf.getText())) > 100.0);
            return true;
        }

        @Override
        public boolean shouldYieldFocus(JComponent source, JComponent target) {

            boolean ehValido = verify(source);

            if (!ehValido) {
                bordaOriginal = bordaOriginal == null ? source.getBorder() : bordaOriginal;
                source.setBorder(BorderFactory.createLineBorder(Color.red, 5));
            }
            else {
                if (bordaOriginal != null) {
                    source.setBorder(bordaOriginal);
                    bordaOriginal = null;
                }
            }
            return ehValido;
        }

        public boolean verificaCaracter(String c) {

            Pattern letras = Pattern.compile("[a-zA-z]");
            Pattern especiais = Pattern.compile ("[!@#$%&*()ç¹²³£¢¬ºª°/,;.₢_+=|<>?{}\\[\\]~-]");
            Pattern espaco = Pattern.compile("[ ]");

            Matcher temLetras = letras.matcher(c);
            Matcher temEspeciais = especiais.matcher(c);
            Matcher temEspaco = espaco.matcher(c);

            return !temLetras.find() && !temEspeciais.find() && !temEspaco.find();
        }
    }
}
