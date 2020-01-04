package cn.microanswer;


import javax.swing.*;
import java.awt.*;

public class Test57 {

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setBounds(700, 350, 400, 150);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel jl = new JLabel("Hello World.");
        jl.setHorizontalAlignment(JLabel.CENTER);
        jl.setFont(new Font("微软雅黑", Font.PLAIN, 50));
        jf.getContentPane().add(jl);

        jf.setVisible(true);
    }

    // java定义一个可变参数个数方法。
    // 要注意，可变参数必须放在形参的最后一个，且只能有一个。
    // public static int add(int... nums, String... strs) // 错误，不满足【只能有一个】
    // public static int add(int... nums, String str) // 错误，不满足【只能在最后一个】

    public static int add(int... nums) {
        // nums 是一个数组。且nums一定不为null， 但如果没传参，nums是空数组

        int result = 0;
        for (int num : nums) {
            result += num;
        }

        return result;
    }

    public static void test (){

        int r1 = add();                      // 0
        int r2 = add(1);                     // 1
        int r3 = add(1, 2, 3);               // 6
        int r4 = add(1,2,3,4,5,6,7,8,9,10);  // 55
    }

}
