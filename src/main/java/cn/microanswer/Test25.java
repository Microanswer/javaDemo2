package cn.microanswer;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Test25 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月08日 12:23:58
     */
    public static void main(String[] args) throws Exception {

        copy2Dir(new FileFilter() {
            @Override
            public boolean accept(File pathname) {

                String name = pathname.getAbsolutePath().toLowerCase();

                if (
                        name.endsWith(".jpg") ||
                                name.endsWith(".png") ||
                                name.endsWith(".gif") ||
                                name.endsWith(".bmp")
                ) {
                    if (name.endsWith(".gif")) {
                        return true;
                    }

                    try {
                        BufferedImage read = ImageIO.read(pathname);
                        if (read == null) {
                            return false;
                        }

                        boolean b = read.getHeight() > 800 && read.getWidth() > 500;
                        return b;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                return false;
            }
        }, "C:\\Users\\Microanswer\\Documents\\Tencent Files\\374288225", "D:\\杂图");

    }

    /**
     * 将 所有符合过滤器的文件从 fromDir（包括子目录） 复制到 toDir
     *
     * @param filter  过滤器
     * @param fromDir 源目录
     * @param toDir   目标目录
     */
    public static void copy2Dir(FileFilter filter, String fromDir, String toDir) {

        try {
            File fromDirFile = new File(fromDir);
            if (!fromDirFile.exists()) {
                throw new Exception("文件夹不存在");
            }
            if (!fromDirFile.isDirectory()) {
                throw new Exception("源目录不能是文件");
            }
            if (!fromDirFile.canRead()) {
                throw new Exception("无权限读取源目录");
            }

            File[] files = fromDirFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return filter.accept(pathname) || pathname.isDirectory();
                }
            });

            if (files == null) {
                return;
            }

            System.out.println("处理文件夹：" + fromDir);
            for (File f : files) {
                String canonicalPath = f.getCanonicalPath();
                if (f.isDirectory()) {
                    copy2Dir(filter, canonicalPath, toDir);
                } else {
                    System.out.println("处理文件：" + canonicalPath);
                    copy(f, new File(toDir, f.getName()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copy(File from, File to) {

        int count = 0;
        while (to.exists()) {
            count++;
            to = new File(to.getParent(), count + to.getName());
        }

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(from));
            out = new BufferedOutputStream(new FileOutputStream(to));

            byte[] data = new byte[4096];
            int size;

            while ((size = in.read(data)) != -1) {
                out.write(data, 0, size);
            }

            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
