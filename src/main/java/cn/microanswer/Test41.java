package cn.microanswer;


public class Test41 {

    static {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("我日");
    }
    static String res = wori();


    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月14日 10:45:35
     */
    public static void main(String[] args) throws Exception {
        System.out.println("我在主方法里：" + res);
    }

    public static String wori () {
        System.out.println("我在方法里日");
        return "wori";
    }
}
