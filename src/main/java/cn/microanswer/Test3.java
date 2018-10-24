package cn.microanswer;

import java.util.ArrayList;
import java.util.Scanner;

public class Test3 {
    public static void main(String args[]) throws Exception {
        // 简易计算器
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> nums = new ArrayList<>();
        while (nums.size() < 2) {
            System.out.println(String.format("请输入第%d个数：", nums.size() + 1));
            try { nums.add(scanner.nextDouble()); }catch (Exception e) {
                System.out.println(String.format("输入错误，请重新输入第%d个数：", nums.size() + 1));
                scanner.next(); // 由于上面一行输出语句会让 scanner 执行一次行读取操作，所以此处手动调用。
            }
        }
        System.out.println("请输入运算符：");
        String ops = scanner.next();
        while (ops.length() < 1 || !String.valueOf(ops.charAt(0)).matches("[+\\-*x/]")) {
            System.out.println("运算符输入错误，请重新输入：");
            ops = scanner.next();
        }
        char op = ops.charAt(0);
        double result = 0;

        switch (op) {
            case '+': result = nums.get(0) + nums.get(1); break;
            case '-': result = nums.get(0) - nums.get(1); break;
            case '*':
            case 'x': result = nums.get(0) * nums.get(1); break;
            case '/': result = nums.get(0) / nums.get(1); break;
        }

        System.out.println(String.format("%fx%f=%f", nums.get(0), nums.get(1), result));
    }
}