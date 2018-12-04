package cn.microanswer;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test33 {


    public static void main(String[] args) throws Exception {

        JFrame jsq = new JFrame("计算器");
        jsq.setBounds(500, 500, 300, 80);
        Container contentPane = jsq.getContentPane();
        contentPane.setLayout(new FlowLayout());

        final JTextField field1 = new JTextField();
        field1.setPreferredSize(new Dimension(50, 26));
        contentPane.add(field1);

        final JComboBox<String> jComboBox = new JComboBox<>(new String[]{"+", "-", "x", "÷"});
        contentPane.add(jComboBox);

        final JTextField field2 = new JTextField();
        field2.setPreferredSize(new Dimension(50, 26));
        contentPane.add(field2);

        final JButton dengyu = new JButton("=");
        contentPane.add(dengyu);

        final JLabel result = new JLabel();
        contentPane.add(result);

        dengyu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String num1 = field1.getText();
                String num2 = field2.getText();

                float num1f, num2f;
                try {
                    num1f = Float.parseFloat(num1); num2f = Float.parseFloat(num2);
                } catch (Exception ignore){
                    field1.setText(""); field2.setText("");
                    return;
                }
                String op = jComboBox.getSelectedItem().toString();
                // 四舍五入保留2位。
                String r;
                if ("+".equals(op)) {
                    r = String.valueOf(Math.round((num1f + num2f) * 100f) / 100f);
                } else if ("-".equals(op)) {
                    r = String.valueOf(Math.round((num1f - num2f) * 100f) / 100f);
                } else if ("x".equals(op)) {
                    r = String.valueOf(Math.round((num1f * num2f) * 100f) / 100f);
                } else {
                    r = String.valueOf(Math.round((num1f / num2f) * 100f) / 100f);
                }

                result.setText(r);
            }
        });

        jsq.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jsq.setVisible(true);
    }
}
