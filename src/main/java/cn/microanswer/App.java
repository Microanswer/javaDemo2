package cn.microanswer;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class App {


    public static void main(String[] args) {

        int[] arr = new int[]{3,2,5,7,6,8,0,1,4,9};
        int[] rul = new int[]{2,1,6,7,0,7,8};
        for (int index: rul) {
            System.out.print(arr[index]);
        }

    }


    private static String reverse(String src) throws Exception{
        if (src != null && src.length() > 0) {
            int length = src.length();
            char[] tar = new char[length];

            for (int from = 0; from < (length + 1) / 2; from++) {
                int end = (length - from) - 1;
                tar[from] = src.charAt(end);
                tar[end] = src.charAt(from);
            }
            return String.valueOf(tar);
        } else {
            return src;
        }
    }

    /**
     * 生成字符图片。
     * @param path 原图片路劲
     * @throws Exception
     */
    public static void createAsciiPic(final String path) throws Exception{
        final String base = "@#&$%*o!:."; // 字符串由复杂到简单

        File file = new File("C:\\Users\\Microanswer\\Desktop\\file.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
        PrintWriter writer = new PrintWriter(outputStreamWriter);
        try {
            final BufferedImage image = ImageIO.read(new File(path));
            for (int y = 0; y < image.getHeight(); y += 2) {
                for (int x = 0; x < image.getWidth(); x++) {
                    final int pixel = image.getRGB(x, y);
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    writer.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                }
                writer.println();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        writer.flush();
        fileOutputStream.close();
    }
}
