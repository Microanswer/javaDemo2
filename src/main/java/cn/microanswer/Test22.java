package cn.microanswer;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Test22 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月07日 10:20:52
     */
    public static void main(String[] args) throws Exception {

    	final JFrame jFrame = new JFrame("Slider Demo");
        jFrame.setLayout(null); // 不使用布局管理器，直接按坐标布局。

        // rgb 三个颜色， 就是用三个滑动条
        final JSlider[] jsliders = new JSlider[3];
        for (int i = 0; i < jsliders.length; i++) {
            jsliders[i] = new JSlider(0, 255, 0);
            jsliders[i].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    jFrame.getContentPane().setBackground(
                            new Color(
                                    jsliders[0].getValue(),
                                    jsliders[1].getValue(),
                                    jsliders[2].getValue()
                            )
                    );
                }
            });
            jsliders[i].setBounds(10, 30 * i + 10, 200, 15);
            jFrame.getContentPane().add(jsliders[i]);
        }

        jFrame.getContentPane().setBackground(new Color(0, 0, 0));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setBounds(500, 400, 240, 150);
        jFrame.setVisible(true);
    }
}
