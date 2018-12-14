package cn.microanswer;


public class Test41 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月14日 10:45:35
     */
    public static void main(String[] args) throws Exception {
        String str = "";

        str = uperFirst(str);

        System.out.println(str);
    }

    public static String uperFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
