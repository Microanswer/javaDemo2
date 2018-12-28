package cn.microanswer;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.ArrayList;
import java.util.Collections;

public class Test5 {
    public static void main(String[] args) {
        System.out.println("第8题：" + fn(10));
        System.out.println("第9题：");
        fn2(10);
    }

    /**
     * 第8题
     */
    public static  double fn(int n) {
        if (n == 1) {
            return 1;
        } else {
            return 2 * fn(n - 1) + 3;
        }
    }

    /**
     * 第9题
     */
    public static void fn2(int count) {
        for (int index = 1; index <= count; index++) {
            for (int jndex = 0; jndex < index; jndex++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
