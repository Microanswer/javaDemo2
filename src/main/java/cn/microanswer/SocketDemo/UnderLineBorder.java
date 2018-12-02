package cn.microanswer.SocketDemo;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * 下划线
 */
public class UnderLineBorder extends AbstractBorder {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color color = g.getColor();
        g.setColor(Color.BLUE);
        g.drawLine(x, y + height - 1, x + width, y + height - 1);
        g.setColor(color);
    }
}