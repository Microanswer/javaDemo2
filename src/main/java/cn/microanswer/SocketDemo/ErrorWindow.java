package cn.microanswer.socketdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 创建工作目录出错时，会使用此窗口进行提示。
 */
public class ErrorWindow extends JFrame {
    private Toolkit toolkit;

    ErrorWindow(Toolkit toolkit, String errMsg) {
        super(Constant.ERROR);
        this.toolkit = toolkit;

        // 设置窗口大小和位置
        Dimension screenSize = this.toolkit.getScreenSize();
        setBounds(
                (int) Math.round((screenSize.getWidth() - Constant.ERR_WINDOW_WIDTH) / 2f),
                (int) Math.round((screenSize.getHeight() - Constant.ERR_WINDOW_HEIGHT) / 2f),
                Constant.ERR_WINDOW_WIDTH,
                Constant.ERR_WINDOW_HEIGHT
        );

        // 不允许拖动窗口大小
        setResizable(false);
        // 窗口关闭时不采取任何行为，在事件中处理具体的关闭事件。
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 设置输出布局管理
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 加入控件
        contentPane.add(new JLabel(errMsg, JLabel.CENTER), BorderLayout.CENTER);

        // 加入确定按钮 。 （使用JPanel配合FlowLayout进行居中显示）
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        JButton jButton = new JButton(Constant.SURE);
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        panel.add(jButton);
        contentPane.add(panel, BorderLayout.SOUTH);

    }
}