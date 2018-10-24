package cn.microanswer;

import javax.swing.*;
import java.awt.*;

public class Test11 {

    public static void main(String[] args) {
        new MyJFrame().setVisible(true);
    }


    static class MyJFrame extends JFrame {
        MyJFrame() {
            super("LayoutManager测试");
            Container contentPane = getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            contentPane.setBackground(Color.GRAY);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setBounds(200, 200, 700, 700);

            JPanel jPanel = new JPanel(new LinearLayout());
            jPanel.setBackground(Color.CYAN);
            jPanel.setPreferredSize(new Dimension(100, 0));

            JLabel jLabel = new JLabel("<html><p>哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈</p></html>");
            jLabel.setOpaque(true);
            jLabel.setBackground(Color.RED);
            jPanel.add(jLabel);


            JLabel jLabel2 = new JLabel("<html><p>呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵</p></html>");
            jLabel2.setOpaque(true);
            jLabel2.setBackground(Color.PINK);
            jPanel.add(jLabel2);

            contentPane.add((jPanel), BorderLayout.CENTER);
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
            return 0.5f;
        }

        @Override
        public float getLayoutAlignmentY(Container target) {
            return 0.5f;
        }

        /**
         * <pre>
         *     此方法详解：
         *     界面上的视图调用 invalidate() 或则界面在初次打开时，此方法被调起。
         *
         *     所以，此方法应该完成的任务：
         *     1、计算出所有子控件的合适大小。
         *     2、根据所有子控件的合适大小，计算出此容器需要的大小才能容纳这些子控件。
         *     3、将容器的大小计算出来后，别忘了设置给这个容器。
         *
         *     怎么计算子控件的大小？
         *     很简单，调用控件的 validate() 方法即可完成对子控件的大小计算。
         *     当 validate() 执行后，你就可以从子控件上 get...Size() 了
         *
         * </pre>
         *
         * @param target
         */
        @Override
        public void invalidateLayout(Container target) {
            System.out.println("invalidateLayout, target:" + target);
            int componentCount = target.getComponentCount();
            for (int index = 0; index < componentCount; index++) {
                // if (componentCount)
            }
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        private Dimension calculatesContainerSize(Container parent, int which) {
            // 循环遍历子控件，根据子控件的大小，确定容器的最佳大小。
            int width = 0;
            int height = 0;

            System.out.println("preferredLayoutSize, parent:" + parent);
            int componentCount = parent.getComponentCount();
            for (int index = 0; index < componentCount; index++) {
                Component component = parent.getComponent(index);
                Dimension size = null;
                if (which == MAX) {
                    size = component.getMaximumSize();
                } else if (which == MIN) {
                    size = component.getMinimumSize();
                } else if (which == PERFECT) {
                    size = component.getPreferredSize();
                }
                if (size != null) {
                    if (orientation == HORIZONTAL) {
                        // 水平方向排列
                        // 宽度就按照每个控件的宽度累加
                        width += size.width;

                        // 高度使用最大的那个控件的高度
                        if (height < size.height) {
                            height = size.height;
                        }
                    } else if (orientation == VERTICAL) {
                        // 垂直方向排列
                        // 高度就按照每个控件高度累加
                        height += size.height;

                        // 宽度就是用最大的那个控件的宽度
                        if (width < size.width) {
                            width = size.width;
                        }
                    }

                } else {
                    System.out.println("发现子控件的最佳大小未设置。");
                }
            }
            Dimension dimension = new Dimension(width, height);
            if (which == MAX) {
                parent.setMaximumSize(dimension);
            } else if (which == MIN) {
                parent.setMinimumSize(dimension);
            } else if (which == PERFECT) {
                parent.setPreferredSize(dimension);
            }
            return dimension;
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return calculatesContainerSize(parent, PERFECT);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return calculatesContainerSize(parent, MIN);
        }

        @Override
        public Dimension maximumLayoutSize(Container target) {
            return calculatesContainerSize(target, MAX);
        }

        @Override
        public void layoutContainer(Container parent) {
            System.out.println("layoutContainer, parent:" + parent);
            int componentCount = parent.getComponentCount();
            int width = parent.getWidth();
            int height = parent.getHeight();

            int x = 0, y = 0;
            for (int index = 0; index < componentCount; index++) {
                Component component = parent.getComponent(index);
                Dimension preferredSize = new Dimension(component.getPreferredSize());
                if (orientation == HORIZONTAL) {
                    // 水平排列
                    if (height < preferredSize.height) {
                        preferredSize.height = height;
                    }

                } else if (orientation == VERTICAL) {
                    // 垂直排列

                    if (width < preferredSize.width) {
                        preferredSize.width = width;
                    }
                }
                component.setPreferredSize(preferredSize);
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
