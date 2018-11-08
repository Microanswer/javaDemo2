package cn.microanswer;


public class Test23 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月07日 16:30:42
     */
    public static void main(String[] args) throws Exception {

        //     图像字符    背景字符
        String pic = "*", bg = " ";

        //  行数      列数
        int row = 8, col = 6;

        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                System.out.print((col - j < i) ? pic : bg);
            }
            System.out.println(); // 换行打印
        }

        System.out.println(1000000 * 0.0003f);
    }
}
