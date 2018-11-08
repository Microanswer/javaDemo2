package cn.microanswer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 创建一个新的测试类java
 */
public class NewTest {

    public static void main(String[] args) {
        String preName = "Test";
        String className = preName;
        String testSrcFileName = className + ".java";
        int index = 1;

        File testJavaDir = new File("D:/project/javaDemo2/src/main/java/cn/microanswer");
        File testJavaFile = new File(testJavaDir, testSrcFileName);

        while (testJavaFile.exists()) {
            index++;
            className = preName + index;

            // 更根据类名查看是否有这个同名的文件夹，有文件夹，也不得行。
            while (new File(testJavaDir, className).exists()) {
                index++;
                className = preName + index;
            }

            testSrcFileName = className + ".java";
            testJavaFile = new File(testJavaDir, testSrcFileName);
        }
        SimpleDateFormat s = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        String src =
                        "package cn.microanswer;\r\n" +
                        "\r\n" +
                        "\r\n" +
                        "public class " + className + " {\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "     *\r\n" +
                        "     * @param args 主参数\r\n" +
                        "     * @author Microanswer\r\n" +
                        "     * @date " + s.format(new Date()) + "\r\n" +
                        "     */\r\n" +
                        "    public static void main(String[] args) throws Exception {\r\n" +
                        "        \r\n" +
                        "        \r\n" +
                        "    }\r\n" +
                        "}\r\n";


        try {
            PrintWriter printWriter = new PrintWriter(testJavaFile, "UTF-8");
            printWriter.print(src);
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("完成。");
    }

}
