package cn.microanswer;


import java.util.ArrayList;

public class Test49 {

    /**
     *
     * 泛型的理解。
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2019年01月04日 16:45:13
     */
    public static void main(String[] args) throws Exception {

    }


    /**
     * 要理解泛型，不能直接从代码入手。 你要先从白话文上理解啥是泛型。
     *
     * 举例： 有一个背包，这个背包可以装很多东西，具体这个背包装的是什么东西。不清楚。 但是我们可以在使用背包的时候
     *       告诉背包我们要装什么东西。 那么由于要告诉背包我们装什么东西。所以在程序上来说，需要有一个东西来保存我们告
     *       诉背包这是什么东西的变量，这个变量，就是俩尖括号里面的内容。
     *
     * 下面定义 背包 类， 这个‘背包’可以装任何东西， 背包就设定这个东西是 T 类型的(也可以是别的字母，不一定得是T这个字母)。
     *
     * @param <T>
     */
    static class BackPack<T/*使用T来表示背包要存放的物品类型，尖括号就是这样的语法，记住就好了*/> {

        /**
         * 既然我们有了 T 来标致物品的类型， 所以就直接用 T 来声明一个数组， 保存的东西就好方便存在这个数组里面。
         */
        T contents[];

        /**
         * 咱们的背包是有限制容量的， 通过构造函数指定这个背包最多可以放多少个物品。
         * @param maxCount
         */
        BackPack(int maxCount) {
            // 根据这个容量进行初始化数组。
            contents = (T[]) new Object[maxCount];
        }

    }
}
