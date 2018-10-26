package cn.microanswer;

import java.util.Scanner;

public class Test15 {
    public static void main(String[] args) {

        System.out.println("请输入一个4位数：");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();

        // 验证并排除用户的非法输入
        while (!str.matches("[a-z0-9A-Z]{4}")) {
            System.out.println("您的输入不合法，请重新输入一个4位数：");
            str = scanner.next();
        }

        // 输入合法了
        // 交换位置， 规则： 最高位 -> 次低位； 次高位 -> 最低位

        String result =
                str.charAt(str.length() - 2) + "" +
                str.charAt(str.length() - 1) + "" +
                str.charAt(0) + ""+
                str.charAt(1);

        // 输出内容
        System.out.println("加密结果：" + result);
    }
}
