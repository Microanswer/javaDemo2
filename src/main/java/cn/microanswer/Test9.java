package cn.microanswer;

public class Test9 {
    public static void main(String[] args) {

        // 判断 .java 文件名

        String name = "123t.java";
        String name1 = "Abc.java";
        String name2 = "A.Test.java";
        String name3 = ".java";

        String regl = "^[^.0-9][a-z0-9A-Z]+(.java)$";

        if (name.matches(regl)) {
            System.out.println("该文件名正确");
        }

        System.out.println(name.matches(regl));
        System.out.println(name1.matches(regl));
        System.out.println(name2.matches(regl));
        System.out.println(name3.matches(regl));

    }
}
