package cn.microanswer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 创建一个新的测试类java
 */
public class NewTest {
    private static SimpleDateFormat s = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("参数：" + Arrays.toString(args));
            String var = ".java";
            if (args != null && args.length == 1) {
                var = "." + args[0];
            }
            makeFile(var);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Map<String, String> code = new HashMap<>();
    private static Map<String, String> preNames = new HashMap<>();

    static {
        preNames.put(".java", "Test");
        code.put(".java",
                "package cn.microanswer;\r\n" +
                        "\r\n" +
                        "\r\n" +
                        "public class %FILENAME% {\r\n" +
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
                        "}\r\n"
        );

        preNames.put(".html", "index");
        code.put(".html",
                "<!-- created by Microanswer at " + s.format(new Date()) + " -->\n" +
                        "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
                        "    <meta name=\"renderer\" content=\"webkit\">\n" +
                        "    <title>%FILENAME%</title>\n" +
                        "</head>\n" +
                        "<body>\n\n</body>\n" +
                        "</html>"
        );

        preNames.put(".js", "js");
        code.put(".js",
                "// %FILENAME%.js created by Microanswer at " + s.format(new Date()) + '\n' +
                        "\"use strict\";\n"
        );
    }

    private static void makeFile(String end) throws Exception {
        if (end == null || end.length() == 0) {
            end = ".java";
        }
        String preName = preNames.get(end);
        String fileName = preName;
        String testSrcFileName = fileName + end;
        int index = 1;

        File testJavaDir = new File("D:\\project\\javaDemo2\\src\\main\\java\\cn\\microanswer");
        File testJavaFile = new File(testJavaDir, testSrcFileName);

        while (testJavaFile.exists()) {
            index++;
            fileName = preName + index;

            // 更根据类名查看是否有这个同名的文件夹，有文件夹，也不得行。
            while (new File(testJavaDir, fileName).exists()) {
                index++;
                fileName = preName + index;
            }

            testSrcFileName = fileName + end;
            testJavaFile = new File(testJavaDir, testSrcFileName);
        }
        String src = code.get(end);

        if (null == src) {
            throw new Exception("没提供对应代码");
        }

        src = src.replaceAll("%FILENAME%", fileName);

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
