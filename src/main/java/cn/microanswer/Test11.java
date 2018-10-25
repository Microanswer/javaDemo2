package cn.microanswer;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test11 {

    public static void main(String[] args) {

    }


    static class MyJFrame extends JFrame {
        MyJFrame() {
            super("LayoutManager测试");
            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.setBackground(Color.GRAY);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setBounds(20, 200, 700, 700);

            JPanel jPanel = new JPanel(new LinearLayout());
            jPanel.setBackground(Color.CYAN);


            for (int i = 0; i < 20; i++) {
                JLabel jLabel2 = new JLabel("<html><p>我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道我也不知道</p></html>");
                jLabel2.setOpaque(true);
                jLabel2.setBackground(new Color(255 - (i * 10), (i * 10), 255 - (i * 10)));
                jPanel.add(jLabel2);
            }

            JScrollPane scrollPane = new JScrollPane(jPanel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(5);
            scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            contentPane.add(scrollPane, BorderLayout.CENTER);
        }
    }

    static class LinearLayout implements LayoutManager2 {
        static final int HORIZONTAL = 2;
        static final int VERTICAL = 1;

        private static final int MAX = 1;
        private static final int MIN = 2;
        private static final int PERFECT = 3;

        int orientation;

        LinearLayout() {
            this.orientation = VERTICAL;
        }


        @Override
        public void addLayoutComponent(Component comp, Object constraints) {
        }

        @Override
        public float getLayoutAlignmentX(Container target) {
            return 0f;
        }

        @Override
        public float getLayoutAlignmentY(Container target) {
            return 0f;
        }

        @Override
        public void invalidateLayout(Container parent) {
            int totleH = 0;
            int componentCount = parent.getComponentCount();
            for (int index = 0; index < componentCount; index++) {
                Component component = parent.getComponent(index);
                Dimension preferredSize = new Dimension(component.getPreferredSize());
                totleH += preferredSize.height;
            }
            parent.setPreferredSize(new Dimension(0, totleH));
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        public Dimension maximumLayoutSize(Container target) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public Dimension minimumLayoutSize(Container target) {
            return new Dimension(0, 0);
        }

        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(0, 0);
        }

        @Override
        public void layoutContainer(Container parent) {
            // System.out.println("layoutContainer, parent:" + parent);
            int componentCount = parent.getComponentCount();
            int width = parent.getWidth();
            int height = parent.getHeight();

            int x = 0, y = 0;
            for (int index = 0; index < componentCount; index++) {
                Component component = parent.getComponent(index);
                Dimension preferredSize = new Dimension(component.getPreferredSize());
                if (orientation == HORIZONTAL) {
                    // 水平排列
                    preferredSize.height = height;
                } else if (orientation == VERTICAL) {
                    // 垂直排列
                    preferredSize.width = width;
                }
                System.out.println();
                component.setBounds(x, y, preferredSize.width, preferredSize.height);
                if (orientation == HORIZONTAL) {
                    // 水平排列
                    x += preferredSize.width;
                } else if (orientation == VERTICAL) {
                    // 垂直排列
                    y += preferredSize.height;
                }

            }
        }
    }
}
