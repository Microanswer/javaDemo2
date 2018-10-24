package cn.microanswer;

import java.util.Random;
import java.util.Scanner;

public class Test4 {
    public static void main () {

        /**
         * 4、键盘输入一个4位整数，如果输入非整数，提示“请输入整数”；
         *    如果是4位整数，获取百位数，如果百位数等于 小于10的随机整数，
         *    输出，“恭喜您中奖了！！”
         */

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个4位数：");
        String next = scanner.next();

        // 判断是否是 4 位数值
        while (next.length() != 4 || !next.matches("[1-4][1-4][1-4][1-4]")) {
            System.out.println("请输入整数：");
            next = scanner.next();
        }
        scanner.close();

        // 获取百位数
        int num = Integer.parseInt(String.valueOf(next.charAt(1)));

        // 获取一个 小于 10 的随机整数
        Random random = new Random();
        int radomNum = random.nextInt(10);

        if (num == radomNum) {
            System.out.println("恭喜您中奖了！！");
        } else {
            System.out.println("很遗憾您没中奖。");
        }
    }
}
