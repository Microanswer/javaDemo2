package cn.microanswer;

import java.util.Random;

public class Test20 {
    public static void main(String[] args) {

        Random random = new Random();

        // 取到 [0, 20) 的随机数
        int i = random.nextInt(20);

        System.out.println(i);

    }
}
