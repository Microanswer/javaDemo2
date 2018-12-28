package cn.microanswer.Test13;

public class JuXing {
    private int length; // 长
    private int width; // 宽

    public JuXing(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public int getArea() {
        return length * width;
    }

    public int getZhouChang() {
        return 2 * (length + width);
    }
}