package cn.microanswer;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test46 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月28日 18:07:50
     */
    public static void main(String[] args) throws Exception {
        ArrayList<int[]> ints = printYHSJ(10);
        printYHSJ("C:\\Users\\Microanswer\\Desktop\\yhsj.png", ints);
    }


    /**
     * 输出杨辉三角
     *
     * @param rowCount 要输出多少行。
     */
    private static ArrayList<int[]> printYHSJ(int rowCount) {
        // String format = "%3";
        ArrayList<int[]> yhsjList = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {

            // 输出空格使结果呈现等腰三角形
            // for (int j = rowCount - (i+1); j > 0; j--) {
            //     System.out.printf(format + "s", " ");
            // }

            // 每一行都可能有多个数字，所以内层循环循环每一行的内容。
            int row[] = new int[i + 1];
            for (int j = 0; j <= i; j++) {
                // 每个数字都等于这个数字肩膀上两个数字之和。

                // 不过对于边缘的数字， 肩膀上的数不完整，此处对边缘的数字判断，并直接赋值 1
                if (j == 0 || j == i) {
                    row[j] = 1;
                } else {
                    // 获取到这个数字肩上的哪一排数字。
                    int rowNumbers[] = yhsjList.get(i - 1);
                    row[j] = rowNumbers[j - 1] + rowNumbers[j];
                }
                // System.out.printf(format + "d", row[j]);
                // System.out.printf(format + "s", " ");
            }
            yhsjList.add(row);
            // System.out.println();
        }
        return yhsjList;
    }


    private static void printYHSJ(String filePath, List<int[]> yhsjList) {
        int width = 1000;
        int height = 1000;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("微软雅黑", 0, 25));
        int lastRowNumCount = yhsjList.get(yhsjList.size() - 1).length;
        int eachNumWidth = width / lastRowNumCount;
        int eachNumHeight = 40;


        for (int index = 0; index < yhsjList.size(); index++) {
            int[] row = yhsjList.get(index);
            int totalRowWidth = row.length * eachNumWidth;
            int startPositionY = index * eachNumHeight;
            for (int i = 0; i < row.length; i++) {
                int startPositionX = (width - totalRowWidth) / 2;
                startPositionX += (i * eachNumWidth);
                graphics.drawRect(startPositionX, startPositionY, eachNumWidth, eachNumHeight);
                String s = String.valueOf(row[i]);
                graphics.drawString(s, startPositionX + (eachNumWidth / 2) - ((graphics.getFont().getSize() * s.length()) / 2), startPositionY + (eachNumHeight / 2) + graphics.getFont().getSize()/2);
            }

        }


        try {
            ImageIO.write(bufferedImage, "png", new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
