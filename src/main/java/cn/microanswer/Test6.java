package cn.microanswer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test6 {

    public static void main(String args[]) throws Exception{

        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader reader1 = new BufferedReader(reader);
        System.out.println("哈哈");
        String s = reader1.readLine();
        System.out.println(s);

    }

}
