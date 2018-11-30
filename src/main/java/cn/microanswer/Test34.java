package cn.microanswer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 继承 JPanel 进行高频率绘制。 因为 JPanel 自带实现了双缓冲，可以防止高频率绘制屏幕闪烁。
 */
public class Test34 extends JPanel implements Runnable {

    private SimpleDateFormat dateFormat;

    // 保存时间字符串的
    private String timeText = "Time Loading...";

    // 字体
    private Font f;

    // 保存对更新时间线程的引用。
    private Thread mThread;

    // 标记是否持续进行时间更新。(实际就是线程里是否继续循环)
    private boolean doing = false;

    private Test34() {
        // 基本初始化内容。
        dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA);
        f = new Font("微软雅黑", Font.PLAIN, 50);

        mThread = new Thread(this);
    }

    @Override
    public void run() {
        while (doing) {

            // 获取最新的时间
            timeText = dateFormat.format(new Date());

            // 在 Java Swing 里面， 如果直接在子线程里面更新新UI，通常UI不会或者达不到更新预期的去重绘UI。
            // 所以，Swing 是提供了一套专门在UI线程提供更新UI的方法。 下面方法中的 Runnable 就会运行在
            // Swing 的UI线程中，保存UI的更新都会及时且无误的执行。
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });

            try {
                Thread.sleep(10L);
            } catch (Exception ignore) { }
        }
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(Color.BLACK);
        graphics.setFont(f);
        graphics.drawString(timeText, 25, 70);
    }

    private void start() {
        doing = true;
        mThread.start();
    }

    private void stop() {
        doing = false;
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {

                    // 构建一个窗口
                    JFrame jFrame = new JFrame("Timer");
                    jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    jFrame.setBounds(650, 400, 360, 135);
                    jFrame.setResizable(false);

                    // 构建时间Panel
                    Test34 timerPanel = new Test34();
                    jFrame.add(timerPanel);

                    // 让时间panel开始走
                    timerPanel.start();

                    // 添加窗口监听器。
                    jFrame.addWindowListener(new WindowAdapter() {

                        public void windowClosing(WindowEvent e) {
                            jFrame.setVisible(false);
                            jFrame.dispose();
                        }

                        public void windowClosed(WindowEvent e) {
                            timerPanel.stop(); // 窗口关闭，时间也别走了。
                        }
                    });

                    // 显示窗口。
                    jFrame.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}