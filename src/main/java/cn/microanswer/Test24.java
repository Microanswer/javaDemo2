package cn.microanswer;


import java.util.ArrayList;
import java.util.Scanner;

public class Test24 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月08日 09:20:10
     */
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        int yuanjia = 5000;
        System.out.println("请输入您的出行月份:1~12：");
        int yuefen = input.nextInt();
        if (yuefen > 4 && yuefen < 10) {
            System.out.println("请问你选择头等舱还是经济舱？头等舱请输入1，经济舱请输入2");
            if (input.nextInt() == 1) {
                System.out.println("您的票价为：" + (yuanjia * 0.9));
            } else {
                System.out.println("您的票价为：" + yuanjia * 0.8);
            }
        } else {
            System.out.println("请问你选择头等舱还是经济舱？头等舱请输入1,经济舱请输入2");
            if (input.nextInt() == 1) {
                System.out.println("您的票价为：" + yuanjia * 0.5);
            } else {
                System.out.println("您的票价为：" + yuanjia * 0.4);
            }
        }

        input.close();

    }
}
