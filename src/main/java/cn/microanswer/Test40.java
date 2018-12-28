package cn.microanswer;


import javax.swing.*;
import java.awt.*;

public class Test40 extends JFrame {

    private Test40() {
        setBounds(500, 400, 200, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JRootPane rootPane = getRootPane();
        BoxLayout boxLayout = new BoxLayout(rootPane, BoxLayout.Y_AXIS);
        rootPane.setLayout(boxLayout);
        rootPane.setOpaque(true);
        rootPane.setBackground(Color.PINK);

        Box j = Box.createHorizontalBox();
        j.setOpaque(true);
        j.setBackground(Color.WHITE);
        // j.setLayout(new BoxLayout(j, BoxLayout.Y_AXIS));
        JLabel jLabel1 = new JLabel("<html><p>12sssssssssssssssssssssssssssssssssssssssssssssssssssss3</p></html>");
        jLabel1.setPreferredSize(new Dimension(200, 0));
        j.add(jLabel1);
        j.add(new JLabel("321"));
        rootPane.add(j);

        Box f = Box.createVerticalBox();
        // f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));
        for (int i = 0; i < 20; i++) {
            JLabel jLabel = new JLabel("我的妈呀：“" + i);
            jLabel.setOpaque(true);
            jLabel.setBackground(Color.GRAY);
            f.add(jLabel);
        }
        rootPane.add(f); // asd
    }

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月12日 16:32:17
     */
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                Test40 test40 = new Test40();
                test40.setVisible(true);
            }
        });
    }
}
