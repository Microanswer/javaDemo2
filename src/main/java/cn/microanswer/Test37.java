package cn.microanswer;

import javax.swing.*;
import java.awt.*;


/**
 * 本类演示讲解边界布局和图形组件
 *
 * @author Administrator
 */
public class Test37 {
    public static void main(String[] args) {
        JFrame sweepFrame = new JFrame("扫雷");
        sweepFrame.setBounds(500, 150, 500, 500);
        sweepFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //添加组件
        //内容面板
        JPanel contentPanel = new JPanel();
        //设置面板的布局方式为边界布局
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setSize(500, 500);
        contentPanel.setBackground(Color.red);

        //菜单面板
        FlowLayout flowLayout1 = new FlowLayout();
        flowLayout1.setAlignment(FlowLayout.LEFT);
        JPanel menuPanel = new JPanel(flowLayout1);
        menuPanel.setSize(500, 50);

        //菜单面板中的标签
        JLabel gameLable = new JLabel("游戏(G)");
        JLabel helpLable = new JLabel("帮助(H)");
        menuPanel.add(gameLable);
        menuPanel.add(helpLable);

        //游戏面板
        JPanel gamePanel = new JPanel();
        gamePanel.setSize(500, 450);
        gamePanel.setBackground(Color.gray);

        gamePanel.setLayout(new BorderLayout());

        //游戏面板头部
        JPanel headPanel = new JPanel();
        headPanel.setSize(50, 100);
        gamePanel.setBackground(Color.red);
        JTextField timeJtf = new JTextField("010");
        JButton faceBun = new JButton("笑脸");
        JTextField timeJTF = new JTextField("000");
        headPanel.add(timeJtf);
        headPanel.add(faceBun);
        headPanel.add(timeJTF);


        //游戏面板尾部
        JPanel footPanel = new JPanel();
        footPanel.setSize(100, 10);
        footPanel.setBackground(Color.yellow);

        JButton btn1 = new JButton();
        JButton btn2 = new JButton();
        JButton btn3 = new JButton();
        JButton btn4 = new JButton();
        JButton btn5 = new JButton();
        footPanel.add(btn1);
        footPanel.add(btn2);
        footPanel.add(btn3);
        footPanel.add(btn4);
        footPanel.add(btn5);

        sweepFrame.add(contentPanel, BorderLayout.CENTER);
        contentPanel.add(menuPanel, BorderLayout.NORTH);
        contentPanel.add(gamePanel, BorderLayout.CENTER);
        gamePanel.add(headPanel, BorderLayout.NORTH);
        gamePanel.add(footPanel, BorderLayout.CENTER);
        sweepFrame.setResizable(false);
        sweepFrame.setVisible(true);
    }
}
