package cn.microanswer;


import java.util.Scanner;

public class Test25 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月08日 12:23:58
     */
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("--------------欢迎进入游戏世界--------------");
        System.out.println("              ******************");
        System.out.println("                  人机互动猜拳游戏");
        System.out.println("              ******************");
        System.out.println();
        System.out.println("出拳规则：1.剪刀 2.石头 3.布");
        System.out.println("请选择对方角色（1：刘备 2：孙权 3：曹操）：");
        String next = scanner.next();

    }

    /**
     * 猜拳猜拳， 此处则定义一个 拳 类。
     * 石头拳、布拳、剪刀拳分别继承并实现这个抽象类。
     * 其中 isKill 要求各自实现自己的拳种可以打败那种拳。
     */
    abstract class Quan {
        /**
         * 获取结果的方法.
         * 此方法的实现要求：
         * 返回是否能够将传入的“拳”打败！
         *
         * @param quan 要判断的“拳”
         * @return 打败了返回 true，没打败返回 false
         */
        abstract boolean isKill(Quan quan);

    }

    // 石头拳
    class ShiTouQuan extends Quan {

        @Override
        boolean isKill(Quan quan) {
            // 石头拳可以打败布拳，所以，如果是布拳，则返回胜利
            return quan instanceof JianDaoQuan;
        }
    }

    // 布拳
    class BuQuan extends Quan {

        @Override
        boolean isKill(Quan quan) {
            // 布拳可以打败石头拳，所以，如果是石头拳则返回胜利。
            return quan instanceof ShiTouQuan;
        }
    }

    // 剪刀拳
    class JianDaoQuan extends Quan {

        @Override
        boolean isKill(Quan quan) {
            // 剪刀拳可以打败布拳，所以，如果是布拳则返回胜利。
            return quan instanceof BuQuan;
        }
    }

}
