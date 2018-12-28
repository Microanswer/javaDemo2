package cn.microanswer;

public class Test18 {
    public static void main(String[] args) {
        int n = 6;
        layoutLX(n, " ", "*");
        layoutTX(n, " ", "*");
        layoutSJX(n, " ", "*");
    }

    /**
     * 输出菱形
     *
     * @param n     任意输入一个数字吧！
     * @param split 分隔符号，空格符号。
     * @param chat  组成图像的符号
     */
    static void layoutLX(int n, String split, String chat) {
        int rowCount = 2 * n - 1;
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
    static void layoutTX(int n, String split, String chat) {
        int rowCount = 2 * n - 1;
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
     *
     * @param rowCount 三角形行数
     * @param s        要组成图像的字符
     */
    static void layoutSJX2(int rowCount, String s) {
        for (int i = 1; i <= rowCount; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print(s);
            }
            System.out.println();
        }
    }

    // 输出三角形
    static void layoutSJX(int n, String split, String chat) {
        int rowCount = 2 * n - 1;
        System.out.println("输出三角形：");
        int t = 0;
        for (int i = 1; i <= rowCount; i++) {
            int f;
            if (i <= n - 1) {
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
