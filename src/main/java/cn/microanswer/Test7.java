package cn.microanswer;

import java.awt.*;
import java.io.*;

public class Test7 {

    private static Toolkit toolkit;

    public static void main(String[] args) {
        File from = new File("D:\\wark2\\java学习视频\\IO流\\05_隐藏文件.mp4");
        File to = new File("C:\\Users\\76060\\Desktop\\a\\" + from.getName());

        FileReader read = null;
        FileWriter write = null;
        try {
            read = new FileReader(from);
            write = new FileWriter(to);
            int len;
            char[] c = new char[1024];
            while ((len = read.read(c)) != -1) {
                write.write(c, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (write != null) {
                try {
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
