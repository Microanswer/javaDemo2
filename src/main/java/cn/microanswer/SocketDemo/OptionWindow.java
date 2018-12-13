package cn.microanswer.SocketDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URI;

/**
 * 选择功能窗口
 */
public class OptionWindow extends JFrame {
    private Toolkit toolkit;
    private final String NEWSERVER = "newserver";
    private final String NEWCLIENT = "newclient";

    /**
     * 窗口事件监听器
     */
    private WindowAdapter windowAdapter = new WindowAdapter() {
        /**
         * 当点击窗口的关闭按钮时，此方法立刻调用。
         * 在此方法中调用 dispose() 方法，则会调用 windowClosed() 方法。
         * 否则 windowClosed() 将不会被调用。
         * @param e 事件
         */
        @Override
        public void windowClosing(WindowEvent e) {
            OptionWindow.this.dispose();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            // 窗口完全关闭完成
            System.out.println("角色选择窗口已关闭");
        }
    };

    /**
     * 按钮事件监听器
     */
    private MouseAdapter buttonEventAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof JButton) {

                // 创建工作目录
                File file = new File(Constant.WORK_DIR);
                if (file.exists() || file.mkdirs()) {

                    JButton button = (JButton) source;
                    String actionCommand = button.getActionCommand();
                    if (NEWSERVER.equals(actionCommand)) {
                        // 创建服务端
                        new ServerWindow(toolkit).setVisible(true);
                    } else if (NEWCLIENT.equals(actionCommand)) {
                        // 创建客户端
                        new ClientWindow(toolkit);
                    }
                    // 关闭角色选择窗口
                    OptionWindow.this.dispose();
                } else {
                    new ErrorWindow(toolkit, Constant.WORK_DIR_CREATE_FAIL_TIP).setVisible(true);
                }
            }
        }
    };

    OptionWindow(Toolkit toolkit) {
        super(Constant.OPTION_WINDOW_TITLE); // 设定窗口标题

        this.toolkit = toolkit;

        // 初始化窗口位置和大小
        Dimension screenSize = this.toolkit.getScreenSize();
        setBounds(
                (int) Math.round((screenSize.getWidth() - Constant.OPTION_WINDOW_WIDTH) / 2f),
                (int) Math.round((screenSize.getHeight() - Constant.OPTION_WINDOW_HEIGHT) / 2f),
                Constant.OPTION_WINDOW_WIDTH,
                Constant.OPTION_WINDOW_HEIGHT
        );

        // 不允许改变大小
        setResizable(false);

        // 添加窗口关闭的监听事件
        addWindowListener(windowAdapter);

        // 添加两个按钮到界面并监听按钮事件
        addButtonAndLogic();

    }

    /**
     * 添加两个按钮到界面，提供用户选择角色
     */
    private void addButtonAndLogic() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 17));


        JButton button1 = new JButton(Constant.OPTION_WINDOW_BUTTON1_TXT);
        button1.setActionCommand(NEWSERVER);
        button1.addMouseListener(buttonEventAdapter);
        contentPane.add(button1);
        JButton button2 = new JButton(Constant.OPTION_WINDOW_BUTTON2_TXT);
        button2.setActionCommand(NEWCLIENT);
        button2.addMouseListener(buttonEventAdapter);
        contentPane.add(button2);
        JLabel jLabel = new JLabel(Constant.OPTION_WINDOW_ABOUT_TXT);
        jLabel.setBorder(new UnderLineBorder());
        jLabel.setForeground(Color.BLUE);
        jLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel.setToolTipText(Constant.OPTION_WINDOW_ABOUT_TXT);
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String options[] = new String[]{Constant.SURE, Constant.CHECKWEB};
                int index = JOptionPane.showOptionDialog(
                        OptionWindow.this,
                        Constant.OPTION_WINDOW_ABOUT_CONTENT,
                        Constant.OPTION_WINDOW_ABOUT_TXT,
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[1]
                );
                if (index == 1) {
                    try {
                        Desktop.getDesktop().browse(new URI(Constant.MICROANSWER_CN));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        contentPane.add(jLabel);
        contentPane.add(new JLabel("v2.0"));
    }

}