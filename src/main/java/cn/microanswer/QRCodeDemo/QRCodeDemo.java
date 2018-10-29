package cn.microanswer.QRCodeDemo;

import com.swetake.util.Qrcode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class QRCodeDemo {
    public static void main(String[] args) throws Exception {

        String ser = "呵呵";

        Qrcode qrcode = new Qrcode();
        boolean[][] booleans = qrcode.calQrcode(ser.getBytes("UTF-8"));

        BufferedImage image = new BufferedImage(booleans[0].length, booleans.length, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = image.getGraphics();
        for (int i = 0; i < booleans.length; i++) {
            for (int j = 0; j < booleans[i].length; j++) {
                graphics.setColor(booleans[i][j] ? Color.BLACK : Color.WHITE);
                graphics.fillRect(j, i, 1, 1);
            }
        }

        graphics.dispose();
        image.flush();

        ImageIO.write(image, "png", new File("C:\\Users\\Microanswer\\Desktop\\fxj.png"));
    }

}
