package cn.microanswer;


import java.util.Scanner;

public class Test32 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月22日 16:30:20
     */
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        String userInputStr = "";

        // 为了防止用户乱输入，下面采取相应措施：
        while (!userInputStr.matches("[0-9]+")) {
            if (userInputStr.length() > 0) {
                System.out.println("您的输入有误。请重新输入一个数：");
            } else {
                System.out.println("请输入一个数字：");
            }
            userInputStr = scanner.next();
        }

        // 转换为用户输入的整数。
        int inputNumber = Integer.parseInt(userInputStr);

        System.out.println("你输入了：" + inputNumber);
    }
}
