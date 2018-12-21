package cn.microanswer;


public class Test43 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月21日 16:55:09
     */
    public static void main(String[] args) throws Exception {

        // 由于此列中的 内部类 没有static修饰。 所以只能通过 外部类的对象 获取引用并进行使用。

        // 首先创建外部类的引用。
        Test43 test43 = new Test43();

        // 使用外部类的对象来获得内部类的使用。
        内部类 lei = test43.new 内部类("hello innerClass!");

        System.out.println(lei.value);
    }



    class 内部类 {
        private String value;
        private 内部类(String value) {
            this.value = value;
        }
    }



}
