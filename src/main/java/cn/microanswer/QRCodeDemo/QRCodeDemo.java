package cn.microanswer.QRCodeDemo;

import com.swetake.util.Qrcode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class QRCodeDemo extends JFrame implements ActionListener {
    private final int WIDTH = 240;
    private final int HEIGHT = 240;
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();

    // 显示图片的Label
    private JLabel resultLabel;

    // 输入文字的地方
    private JTextField textField;

    // 生成二维码的按钮
    private JButton buttonBuild;

    // 清空生成图像的按钮
    private JButton buttonClear;

    // 保存图像的按钮
    private JButton buttonSave;

    public QRCodeDemo() {
        super("二维码测试");
        Dimension screenSize = toolkit.getScreenSize();

        setBounds(
                ((int) Math.round((screenSize.getWidth() - WIDTH) / 2f)),
                ((int) Math.round((screenSize.getWidth() - HEIGHT) / 2f)),
                WIDTH, HEIGHT
        );

        // setResizable(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        Container contentPane = getContentPane();

        resultLabel = new JLabel();
        resultLabel.setPreferredSize(new Dimension(200, 300));
        contentPane.add(resultLabel);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(155, 20));
        contentPane.add(textField);

        buttonBuild = new JButton("生成");
        buttonBuild.setActionCommand("build");
        buttonBuild.addActionListener(this);
        contentPane.add(buttonBuild);
        buttonClear = new JButton("清除");
        buttonClear.setActionCommand("clear");
        buttonClear.addActionListener(this);
        contentPane.add(buttonClear);
        buttonSave = new JButton("保存");
        buttonSave.setActionCommand("save");
        buttonSave.addActionListener(this);
        contentPane.add(buttonSave);
    }

    private java.awt.Image buildQRCode(String txt) {
        if (txt == null || txt.equals("")) {
            txt = " ";
        }

        Qrcode qrcode = new Qrcode();
        boolean[][] booleans = new boolean[0][];
        try {
            booleans = qrcode.calQrcode(txt.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BufferedImage image = new BufferedImage(booleans[0].length * 3, booleans.length * 3, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = image.getGraphics();
        Color transpart = new Color(1, 1, 1, 1f);
        for (int i = 0; i < booleans.length; i++) {
            for (int j = 0; j < booleans[i].length; j++) {
                graphics.setColor(booleans[i][j] ? Color.BLACK : transpart);
                graphics.fillRect(j * 3, i * 3, 3, 3);
            }
        }

        graphics.dispose();
        image.flush();

        return image;
    }

    public static void main(String[] args) throws Exception {
        new QRCodeDemo().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if ("save".equals(actionCommand)) {

            Icon icon = resultLabel.getIcon();
            if (icon instanceof ImageIcon) {
                ImageIcon imageIcon = (ImageIcon) icon;

                BufferedImage image = (BufferedImage) imageIcon.getImage();

                try {
                    ImageIO.write(image, "png", new File("C:\\Users\\Microanswer\\Desktop\\qrcode.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                System.out.println("成功");
            }

        } else if ("clear".equals(actionCommand)) {
            textField.setText("");
            resultLabel.setText("");
            resultLabel.setIcon(null);
        } else if ("build".equals(actionCommand)) {
            Image image = buildQRCode(textField.getText());
            resultLabel.setIcon(new ImageIcon(image));
        }
    }
}
