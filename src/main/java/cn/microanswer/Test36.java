package cn.microanswer;


import javax.swing.*;
import java.awt.*;

public class Test36 extends JFrame {
    private static final int WINDOW_WIDTH = 150;
    private static final int WINDOW_HEIGHT = 220;

    private Test36() {
        setTitle("扫雷");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false); // 不允许改变大小
        setBounds(500, 450, WINDOW_WIDTH, WINDOW_HEIGHT);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0, 5));

        // 菜单条
        JMenuBar mJMenuBar = new JMenuBar();
        mJMenuBar.add(new JMenu("游戏(G)"));
        mJMenuBar.add(new JMenu("帮助(H)"));
        contentPane.add(mJMenuBar, BorderLayout.NORTH);

        // 游戏区域
        MJPanel mMJPanel = new MJPanel();
        contentPane.add(mMJPanel, BorderLayout.CENTER);
    }


    // 自定义 JPanel ，方便管理个变量元素
    class MJPanel extends JPanel {

        MJPanel() {
            setLayout(new BorderLayout(0, 5));

            // 上方时间 + 笑脸 + 数量

            JPanel top = new JPanel();
            top.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

            top.add(new JLabel("000"));
            top.add(new JLabel("(^_^)"));
            top.add(new JLabel("000"));
            add(top, BorderLayout.NORTH);

            // 下方的游戏区域。 9x9的
            JPanel body = new JPanel();
            body.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            int count = 9;
            int width = WINDOW_WIDTH / count; // 计算出 9x9 的每一个小格子的大小。
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < count; j++) {
                    JButton jButton = new JButton();
                    jButton.setPreferredSize(new Dimension(width, width));
                    body.add(jButton);
                }
            }
            add(body, BorderLayout.WEST);

        }

    }

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月03日 14:23:19
     */
    public static void main(String[] args) throws Exception {

        // 设置界面风格和 window 窗口相同。
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                new Test36().setVisible(true);
            }
        });
    }
}
