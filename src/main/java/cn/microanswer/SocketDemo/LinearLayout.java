package cn.microanswer.SocketDemo;

import java.awt.*;

/**
 * 线性布局 （此类仅适用于本项目。请不要复制到别处使用）
 */
public class LinearLayout implements LayoutManager2 {
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