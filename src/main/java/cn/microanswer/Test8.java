package cn.microanswer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 编写一个图形用户界面程序，包含两个按钮，
 * 一个信息标签（label）和一个显示面板，
 * 两个按钮分别为“掷色子”和“移动”，
 * 在显示面板中显示一个小人（用小圆以及线绘制），
 * 随机设定小人的初始位置，当点击 “掷色子” 按钮，
 * 随机产生移动信息（上移，下移，左移，右移，移动几步）,
 * 并显示在信息标签中，点击移动，按照产生的移动信息，让小人进行移动
 */
public class Test8 {
    public static void main(String[] args) {

        // 设置外观样式为 window 默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MFrame().setVisible(true);


    }


    static class MFrame extends JFrame {
        static final int WIDTH = 400;
        static final int HEIGHT = 400;

        // 一个信息标签（label）
        JLabel txtLabel;

        // 一个显示面板
        MPanel mPanel;

        // 两个按钮分别为“掷色子”和“移动”
        JButton btnThrow;
        JButton btnMove;

        // 工具类
        Toolkit toolkit;

        // 随机数
        Random random;

        // 保存要移动的方案
        ArrayList<Step> steps;

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object source = e.getSource();
                if (source == btnMove) {
                    startMove(steps);
                } else if (source == btnThrow) {
                    startThrow();
                }
            }
        };

        MFrame() {
            super("扔筛子");
            toolkit = Toolkit.getDefaultToolkit();

            // 初始化窗口位置和大小：
            Dimension screenSize = toolkit.getScreenSize();
            setBounds(
                    ((int) Math.round((screenSize.getWidth() - WIDTH) / 2f)),
                    ((int) Math.round((screenSize.getHeight() - HEIGHT) / 2f)),
                    WIDTH,
                    HEIGHT
            );

            // 不允许改变大小：
            setResizable(false);

            // 点击窗口关闭按钮不采取任何事情，使用监听器处理
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

            // 设置窗口关闭监听器
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MFrame.this.dispose();
                    System.exit(0);
                }
            });

            //
            random = new Random();

            initUI();
        }

        /**
         * 初始化界面组件
         */
        void initUI() {

            Container contentPane = getContentPane();

            // 设置布局管理器, 使用东西南北布局。
            contentPane.setLayout(new BorderLayout());

            // 信息 Label 放在顶部，也就是 North 方向
            txtLabel = new JLabel("点击【掷筛子】按钮进行掷筛子");
            txtLabel.setHorizontalAlignment(JLabel.CENTER);
            txtLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            contentPane.add(txtLabel, BorderLayout.NORTH);

            // 显示面板放在中间
            mPanel = new MPanel();
            contentPane.add(mPanel, BorderLayout.CENTER);

            // 两个按钮放在最下边，也就是 SOUTH 方向
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
            btnThrow = new JButton("掷筛子");
            btnThrow.addMouseListener(mouseAdapter);
            btnMove = new JButton("移动");
            btnMove.addMouseListener(mouseAdapter);
            panel.add(btnThrow);
            panel.add(btnMove);
            contentPane.add(panel, BorderLayout.SOUTH);

        }

        /**
         * 开始掷筛子
         */
        void startThrow() {
            steps = new ArrayList<>();

            // 一共要移动多少次？ 随机好了 (4 ~ 10) 次
            int size = random.nextInt(6 + 1) + 4;

            for (int i = 0; i < size; i++) {
                steps.add(Step.getRandomStep(random));
            }

            // 掷筛子结果显示到界面
            displayStep(steps);
        }

        /**
         * 显示掷筛子结果
         *
         * @param steps 结果集
         */
        void displayStep(ArrayList<Step> steps) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<html><p>掷筛子结果：");
            for (Step s : steps) {
                stringBuilder.append(Step.WAY_TXTS[s.way]).append(s.count).append("步，");
            }
            stringBuilder.substring(0, stringBuilder.length() - 1);
            String result = stringBuilder.append("</p></html>").toString();
            txtLabel.setText(result);
        }

        /**
         * 执行移动
         *
         * @param steps 移动命令
         */
        void startMove(ArrayList<Step> steps) {
            if (steps == null || steps.size() < 1) {
                JOptionPane.showMessageDialog(MFrame.this, "请先点掷筛子获取移动命令", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            mPanel.startMove(steps);
        }
    }

    static class MPanel extends JPanel {

        Jack jack;
        boolean isMoving; // 标记是否正在移动，如果正在移动的时候，又发起一个移动命令，则提示。

        MPanel() {
            setBackground(Color.GRAY);
            Random r = new Random();

            // 初始化人
            jack = new Jack(r.nextInt(100) + 100, r.nextInt(100) + 100);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            // 绘制人
            jack.paint(g);
        }

        public void startMove(final ArrayList<Step> steps) {
            if (isMoving) {
                JOptionPane.showMessageDialog(this, "请等待移动完成", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 使用一个 timer 进行延时移动
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isMoving = true;
                    for (Step s : steps) {
                        for (int i = 0; i < s.count; i++) {
                            jack.move(s);
                            repaint();

                            try {
                                Thread.sleep(300);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    isMoving = false;
                }
            }, 0);
        }
    }

    /**
     * 模拟小人。 这个小人叫 Jack。
     * 以小人的头部作为整个人的坐标参考点中心。
     */
    static class Jack {

        int x, y;
        Color color;

        int headLength = 10; // 头的直径

        int xs[], ys[];

        Jack(int x, int y) {
            this.x = x;
            this.y = y;

            // 小人默认颜色
            color = new Color(0, 255, 255);

            xs = new int[10];
            ys = new int[10];
        }

        /**
         * 根据小人坐标绘制小人。
         *
         * @param g
         */
        void paint(Graphics g) {
            Color color = g.getColor();
            g.setColor(this.color);


            // 绘制头部
            g.fillOval(x, y, headLength, headLength);

            // 绘制身体
            xs[0] = x + (headLength / 2);
            ys[0] = y + (headLength);

            xs[1] = xs[0];
            ys[1] = ys[0] + 4;

            xs[2] = xs[1] - 5;
            ys[2] = ys[1];

            xs[3] = xs[1] + 5;
            ys[3] = ys[2];

            xs[4] = xs[1];
            ys[4] = ys[1];

            xs[5] = xs[4];
            ys[5] = ys[4] + 6;

            xs[6] = xs[5] + 5;
            ys[6] = ys[5] + 5;

            xs[7] = xs[5];
            ys[7] = ys[5];

            xs[8] = xs[7] - 5;
            ys[8] = ys[7] + 5;

            xs[9] = xs[5];
            ys[9] = ys[5];
            g.drawPolygon(xs, ys, 10);

            g.setColor(color);
        }

        /**
         * 根据 命令执行移动
         *
         * @param s 命令
         */
        public void move(Step s) {
            int l = 10;
            switch (s.way) {
                case Step.LEFT:
                    x -= l;break;
                case Step.TOP:
                    y -= l;break;
                case Step.RIGHT:
                    x += l;break;
                case Step.BOTTOM:
                    y += l;break;
                default:
            }
        }
    }

    /**
     * 保存移动的步数和方向
     */
    static class Step {
        static final String WAY_TXTS[] = new String[]{"", "左移", "上移", "右移", "下移"};
        static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

        // 随机参数一步
        static Step getRandomStep(Random random) {
            Step s = new Step();
            s.way = random.nextInt(4) + 1; // 1 ~ 4
            s.count = random.nextInt(4) + 2; // 2 ~ 5
            return s;
        }

        int way; // 方向
        int count; // 步数

    }
}
