package cn.microanswer.Test13;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {

        // 定义一个数组，存放所有形状的面积，好方便对这个数组排序，得到各个形状的面积排序。
        int areas[] = new int[4];

        // 定义一个数组，存放所有形状的周长，好方面对这个数组排序，得到各个形状的周长排序
        int zhouchangs[] = new int[4];

        // 创建4个形状，2个长方形，2个正方形
        JuXing s1 = new JuXing(2, 3);
        JuXing s2 = new JuXing(5, 3);
        ZhengFnagXing s3 = new ZhengFnagXing(6);
        ZhengFnagXing s4 = new ZhengFnagXing(3);

        // 将各个形状的面积和周长分别放入数组。
        areas[0] = s1.getArea();
        areas[1] = s2.getArea();
        areas[2] = s3.getArea();
        areas[3] = s4.getArea();
        zhouchangs[0] = s1.getZhouChang();
        zhouchangs[1] = s2.getZhouChang();
        zhouchangs[2] = s3.getZhouChang();
        zhouchangs[3] = s4.getZhouChang();

        // 对两个数组分别排序，得到他们的面积和周长的大小排序。
        Arrays.sort(areas);
        Arrays.sort(zhouchangs);

        System.out.println("面积排序结果：" + Arrays.toString(areas));
        System.out.println("周长排序结果：" + Arrays.toString(zhouchangs));
    }

}
