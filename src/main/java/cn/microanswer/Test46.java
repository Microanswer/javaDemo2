package cn.microanswer;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test46 {

    /**
     * 输出 杨辉三角为 png 图像。
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月28日 18:07:50
     */
    public static void main(String[] args) throws Exception {

        // 建立 16 行的杨辉三角
        ArrayList<long[]> longs = printYHSJ(16);

        // 输出到图像。
        printYHSJ("C:\\Users\\Microanswer\\Desktop\\yhsj.png", longs);
    }


    /**
     * 输出杨辉三角
     *
     * @param rowCount 要输出多少行。
     */
    private static ArrayList<long[]> printYHSJ(int rowCount) {
        // String format = "%3";
        ArrayList<long[]> yhsjList = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {

            // 输出空格使结果呈现等腰三角形
            // for (int j = rowCount - (i+1); j > 0; j--) {
            //     System.out.printf(format + "s", " ");
            // }

            // 每一行都可能有多个数字，所以内层循环循环每一行的内容。
            long row[] = new long[i + 1];
            for (int j = 0; j <= i; j++) {
                // 每个数字都等于这个数字肩膀上两个数字之和。

                // 不过对于边缘的数字， 肩膀上的数不完整，此处对边缘的数字判断，并直接赋值 1
                if (j == 0 || j == i) {
                    row[j] = 1;
                } else {
                    // 获取到这个数字肩上的哪一排数字。
                    long rowNumbers[] = yhsjList.get(i - 1);
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


    private static void printYHSJ(String filePath, List<long[]> yhsjList) {
        int width = 1000;
        int height = 500;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(new Color(1,1,1,1f));
        graphics.fillRect(0, 0, width, height);

        int lastRowNumCount = yhsjList.get(yhsjList.size() - 1).length;
        int eachNumWidth = width / lastRowNumCount;
        int eachNumHeight = height / yhsjList.size();

        Font font = new Font("微软雅黑", 0, Math.round(eachNumHeight / 2f));
        graphics.setFont(font);

        for (int index = 0; index < yhsjList.size(); index++) {
            long[] row = yhsjList.get(index);
            int totalRowWidth = row.length * eachNumWidth;
            int startPositionY = index * eachNumHeight;
            for (int i = 0; i < row.length; i++) {

                int startPositionX = (width - totalRowWidth) / 2;
                startPositionX += (i * eachNumWidth);

                graphics.setColor(Color.LIGHT_GRAY);
                graphics.fillRect(startPositionX+1,
                        startPositionY+1,
                        eachNumWidth-1,
                        eachNumHeight-1
                );

                String s = String.valueOf(row[i]);

                graphics.setColor(Color.BLACK);

                float fontStringWidth = SwingUtilities.computeStringWidth(graphics.getFontMetrics(), s);

                if (fontStringWidth >= eachNumWidth) {
                    graphics.setFont(new Font(
                            "微软雅黑",
                            0,
                            Math.round(eachNumWidth/s.length())
                    ));
                } else {
                    graphics.setFont(font);
                }

                int fontSize = graphics.getFont().getSize();
                fontStringWidth = SwingUtilities.computeStringWidth(graphics.getFontMetrics(), s);
                graphics.drawString(
                        s,
                        startPositionX+1 + Math.round(eachNumWidth / 2f) - Math.round(fontStringWidth/2),
                        startPositionY + Math.round(eachNumHeight / 2f) + Math.round(fontSize / 2f)
                );
                graphics.setFont(font);
            }

        }


        try {
            ImageIO.write(bufferedImage, "png", new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
