package cn.microanswer;


import javax.swing.*;
import java.awt.*;

public class Test57 {

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setBounds(700, 350, 400, 150);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel jl = new JLabel("Hello World.");
        jl.setHorizontalAlignment(JLabel.CENTER);
        jl.setFont(new Font("微软雅黑", Font.PLAIN, 50));
        jf.getContentPane().add(jl);

        jf.setVisible(true);
    }

}
