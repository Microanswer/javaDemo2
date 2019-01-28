package cn.microanswer;

import static cn.microanswer.U.print;

class Mug {
    Mug(int marker) {
        print("Mug(" + marker + ")");
    }

    void f(int marker) {
        print("f(" + marker + ")");
    }
}

class Mugs {
    Mug mug1;
    Mug mug2;

    // {   在编译成 class 文件后，这里的构造代码块的代码就会被移动到 每一个构造函数里头。
    //     就像下面的构造函数一样
    //     mug1 = new Mug(1);
    //     mug2 = new Mug(2);
    //     print("mug1 & mug2 initialized");
    // }

    Mugs() {

        // 先前构造代码块的代码在被编译成 class 后实际上代码就成了这样的了
        mug1 = new Mug(1);
        mug2 = new Mug(2);
        print("mug1 & mug2 initialized");

        print("Mugs()");
    }

    Mugs(int i) {

        // 先前构造代码块的代码在被编译成 class 后实际上代码就成了这样的了

        mug1 = new Mug(1);
        mug2 = new Mug(2);
        print("mug1 & mug2 initialized");

        print("Mugs(int)");
    }

}

public class Test51 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年01月21日 14:58:56
     */
    public static void main(String[] args) throws Exception {
        String s = HttpUtil.get("https://baidu.com", null);
        System.out.println(s);
    }

}


class U {
    public static void print(String va) {
        System.out.println(va);
    }
}