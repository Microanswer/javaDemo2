package cn.microanswer;


public class Test49 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年01月04日 16:45:13
     */
    public static void main(String[] args) throws Exception {

        fuckSelf(20);


    }

    private static void fuckSelf(int j8) {
        if (j8 <= 0 /*cm*/) {
            System.out.println("fuck over!");
        } else {
            fuckSelf(--j8);
        }
    }
}
