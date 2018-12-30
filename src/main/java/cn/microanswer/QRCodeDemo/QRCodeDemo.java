package cn.microanswer.QRCodeDemo;

import com.swetake.util.Qrcode;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class QRCodeDemo extends JFrame implements ActionListener {
	private final int WIDTH = 300;
	private final int HEIGHT = 350;
	private final Toolkit toolkit = Toolkit.getDefaultToolkit();

	private String currentDirectoryPath;

	// 显示图片的Label
	private JLabel resultLabel;

	// 输入文字的地方
	private JTextArea textArea;

	// 生成二维码的按钮
	private JButton buttonBuild;

	// 清空生成图像的按钮
	private JButton buttonClear;

	// 保存图像的按钮
	private JButton buttonSave;

	public QRCodeDemo() {
		super("二维码生成器");
		Dimension screenSize = toolkit.getScreenSize();

		setBounds(((int) Math.round((screenSize.getWidth() - WIDTH) / 2f)),
				((int) Math.round((screenSize.getWidth() - HEIGHT) / 2f)), WIDTH, HEIGHT);

		setResizable(false);
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		initUI();
	}

	private void initUI() {
		Container contentPane = getContentPane();

		resultLabel = new JLabel();
		resultLabel.setHorizontalAlignment(JLabel.CENTER);
		resultLabel.setPreferredSize(new Dimension(220, 180));
		contentPane.add(resultLabel);

		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(220, 50));
		contentPane.add(textArea);

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

		final Qrcode qrcode = new Qrcode();
		boolean[][] booleans = new boolean[0][];
		try {
			booleans = qrcode.calQrcode(txt.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int bcot = 3;

		if (bcot < 1) {
			bcot = 1;
		}
		BufferedImage image = new BufferedImage(booleans[0].length * bcot, booleans.length * bcot,
				BufferedImage.TYPE_INT_ARGB);

		Graphics graphics = image.getGraphics();
		Color transpart = new Color(1, 1, 1, 1f);
		for (int i = 0; i < booleans.length; i++) {
			for (int j = 0; j < booleans[i].length; j++) {
				graphics.setColor(booleans[i][j] ? Color.BLACK : transpart);
				graphics.fillRect(j * bcot, i * bcot, bcot, bcot);
			}
		}

		graphics.dispose();
		image.flush();

		return image;
	}

	public static void main(String[] args) throws Exception {
		// 设置界面风格和 window 窗口相同。
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new QRCodeDemo().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if ("save".equals(actionCommand)) {
			String txt = textArea.getText();
			if (txt == null || txt.length() <= 0) {
				return;
			}
			JFileChooser jfc = null;
			if (currentDirectoryPath == null) {
				jfc = new JFileChooser();
			} else {
				jfc = new JFileChooser(currentDirectoryPath);
			}
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只能选择目录
			jfc.setDialogTitle("选择文件夹保存");
			jfc.setFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return "文件夹";
				}

				@Override
				public boolean accept(File f) {
					return f.isDirectory();
				}
			});
			jfc.showOpenDialog(QRCodeDemo.this);
			File selectedFile = jfc.getSelectedFile();
			if (selectedFile == null) {
				return;
			}
			try {
				currentDirectoryPath = selectedFile.getCanonicalPath();
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
			Icon icon = resultLabel.getIcon();
			if (icon instanceof ImageIcon) {
				ImageIcon imageIcon = (ImageIcon) icon;

				BufferedImage image = (BufferedImage) imageIcon.getImage();

				try {
					ImageIO.write(image, "png", new File(selectedFile, "qrcode.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				System.out.println("成功");
			}

		} else if ("clear".equals(actionCommand)) {
			textArea.setText("");
			resultLabel.setText("");
			resultLabel.setIcon(null);
		} else if ("build".equals(actionCommand)) {
			String txt = textArea.getText();
			if (txt == null || txt.length() <= 0) {
				return;
			}
			try {
				Image image = buildQRCode(txt);
				resultLabel.setIcon(new ImageIcon(image));
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(QRCodeDemo.this, String
						.format("<html><p>生成二维码失败：</p><p>(%s) %s</p></html>", e2.getClass().getSimpleName(), e2.getMessage()),
						"出错", JOptionPane.ERROR_MESSAGE);

				textArea.setText("");
				resultLabel.setText("");
				resultLabel.setIcon(null);
			}
		}
	}
}
