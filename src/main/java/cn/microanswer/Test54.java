package cn.microanswer;


import java.io.*;

public class Test54 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年04月20日 23:12:47
     */
    public static void main(String[] args) throws Exception {

        String s = HttpUtil.get("https://isluo.com/");
        System.out.println(s);

    }
}
