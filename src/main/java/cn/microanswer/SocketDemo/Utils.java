package cn.microanswer.SocketDemo;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Utils {

    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0;

    }

    /**
     * 从 inputStream 读取 length 长度到 outputStream 中
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param length       长度
     */
    static void streamCopy(InputStream inputStream, OutputStream outputStream, long length) throws Exception {
        long leftSize = length; // 剩下还有多少没有读取
        int eachSize = 4096;

        byte data[] = new byte[eachSize];

        // 还有剩下的没读取，就一直读取
        while (leftSize > 0) {

            int size;

            if (leftSize >= eachSize) {
                size = inputStream.read(data, 0, eachSize);
                leftSize -= size;
            } else {
                // 剩下的数据量已近较少了
                size = inputStream.read(data, 0, (int) leftSize);
                leftSize = 0;
            }

            outputStream.write(data, 0, size);
        }

        outputStream.flush();
    }

    /**
     * 删除目录 或 文件
     *
     * @param f 要删除的目录或文件
     */
    static void deleteFile(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File ff : files) {
                    deleteFile(ff);
                }
            }
        }
        boolean delete = f.delete();
        if (delete) {
            System.out.println(f.getAbsolutePath() + " 删除成功");
        } else {
            System.err.println(f.getAbsolutePath() + " 删除失败");
        }
    }

    public static boolean isArrayEmpty(String[] users) {
        return null == users || users.length == 0;
    }

    public static boolean isListEmpty(List res) {
        return null == res || res.size() == 0;
    }
}
