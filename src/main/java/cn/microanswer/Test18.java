package cn.microanswer;

import java.util.Scanner;

public class Test18 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("输入一个数：");
        String numStr = scanner.next();
        while (!numStr.matches("[0-9]+")) {
            System.out.println("您的输入不正确，请重新输入一个数值：");
            numStr = scanner.nextLine();
        }
        System.out.println("请输入一个组成图形的字符：");
        String chat = scanner.next();
        while (chat.length() != 1) {
            System.out.println("您必须输入一个字符，请重新输入：");
            chat = scanner.nextLine();
        }

        // 分隔符，使用空格
        String split = " ";

        int n = Integer.parseInt(numStr);
        int rowCount = 2 * n - 1;

        layoutLX(n, rowCount, split, chat);

        layoutTX(n, rowCount, split, chat);

        layoutSJX(n, rowCount, split, chat);

        layoutSJX2(5, "^");
    }

    // 输出菱形
    static void layoutLX(int n, int rowCount, String split, String chat) {
        System.out.println("输出菱形:");
        // 输出菱形.
        for (int i = 1; i <= rowCount; i++) {
            // 计算输出分隔字符的个数并输出
            int splitCount = Math.abs(i - n);
            for (int k = 0; k < (splitCount); k++) {
                System.out.print(split);
            }

            // 计算输出图形字符
            int chatCount = (n - splitCount) * 2 - 1;
            for (int k = 0; k < chatCount; k++) {
                System.out.print(chat);
            }

            System.out.println();
        }
        System.out.println();
    }

    // 输出梯形
    static void layoutTX(int n, int rowCount, String split, String chat) {
        // 输出梯形, 第 n-1 行后内内容相同
        System.out.println("输出梯形：");
        for (int i = 1; i <= rowCount; i++) {
            int f = i;
            if (f > n - 1) {
                f = n;
            }

            for (int j = 0; j < f; j++) {
                System.out.print(chat);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 生成三角形
     * @param rowCount 三角形行数
     * @param s 要组成图像的字符
     */
    static void layoutSJX2 (int rowCount, String s) {
        for (int i = 1; i <= rowCount; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print(s);
            }
            System.out.println();
        }
    }

    // 输出三角形
    static void layoutSJX(int n, int rowCount, String split, String chat) {
        System.out.println("输出三角形：");
        int t = 0;
        for (int i = 1; i <= rowCount; i++) {
            int f;
            if ( i <= n-1) {
                f = i;
                t = f;
            } else {
                f = --t;
            }
            for (int j = 0; j < f; j++) {
                System.out.print(chat);
            }
            System.out.println();
        }
        System.out.println();
    }
}
