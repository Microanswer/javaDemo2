package cn.microanswer;


import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test55 {

    public static void main(String[] args) throws Exception {

        // 视频文件路径。
        String videoFile = "D:/video.mp4";

        // 建立输入流
        FileInputStream fileInputStream = new FileInputStream(videoFile);

        // 准备一个小“桶”每次舀这么多数据。
        byte[] datas = new byte[1024];

        // 每次舀水的量
        int size = 0;

        // ---------------------------------------------------------------

        // 准备输出文件路径
        String targetFile = "E:/video.pm4";

        // 建立输出流
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);


        // 循环的使用“小桶”舀。
        while (size != -1) {

            // 舀
            size = fileInputStream.read(datas);

            // 舀到数据，往目标倒。
            // 因为我们使用 size 标记每次舀了多少水，所以这里写的时候，也要讲“桶里”有多少水这个信息
            // 告诉写出的位置，这样就能拿在桶里拿到正确的数据量。
            fileOutputStream.write(datas, 0, size);
        }

        // 完成操作，关闭输入流，关闭输出流
        fileInputStream.close();
        fileOutputStream.close();

        // 提示完成
        System.out.println("复制完成！");
    }
}