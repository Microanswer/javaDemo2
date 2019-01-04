package cn.microanswer;

import java.util.ArrayList;
import java.util.List;

/**
 * 命题与答题。
 * <p>
 * 命题： 1、创建一个 Test48 类。 答： 第 8 行到 34 行。
 */
public class Test48 {

    public static void main(String[] args) {
    }

    // 求车牌
    private static void t1() {
        // 车牌号： 31xxxx;

        // 将后四位从 0000 开始进行计。

        for (int i = 0; i <= 9999; i++) {

            String numStr = "" + i;

            while (numStr.length() != 4) {
                numStr = "0" + numStr;
            }

            // 牌照三四位相同
            if (numStr.charAt(0) != numStr.charAt(1)) {
                // 不相同就不用算了，直接跳过
                continue;
            }

            // 牌照五六位相同
            if (numStr.charAt(2) != numStr.charAt(3)) {
                // 不相同也不用算了。
                continue;
            }

            // 这个数是一个整数的平方。 那么将该数进行开方，开出来得整数，就说明ok
            int num = Integer.parseInt(numStr);
            double sqrt = Math.sqrt(num);

            // 判断是否整数
            boolean isOk = ("" + sqrt).matches("[0-9]*(.0)$");

            if (isOk) {
                System.out.println("车牌号可能为：31" + numStr);
                System.out.println(num + "是" + sqrt + "的平方");
            }
        }
    }

    // 模拟仙草，羊，狮子，树妖。
    private static void t2() {


        // 初始 1000 颗仙草
        final List<XianCao> xiancaos = new ArrayList<>();
        for (int i = 0; i < 1000; i++) xiancaos.add(new XianCao());

        // 初始 800 只羊。
        final List<MieMie> mieMies = new ArrayList<>();
        for (int i = 0; i < 800; i++) {
            MieMie mieMie = new MieMie();
            mieMie.setOnFunListener(new Fuck.OnFunListener() {
                @Override
                public void onFun() {
                    // 此处羊可吃掉整颗仙草。
                    // 获取一个仙草让这只羊吃。
                    XianCao xianCao = null;
                    int index = 0;
                    while (xianCao == null) {
                        xianCao = xiancaos.get(index);
                        if (xianCao.length < 1f) {
                            xianCao = null;
                        }
                        index++;
                        if (index >= xiancaos.size()) {
                            // 没有草可以吃了。
                            System.out.println("草吃完了。");
                            return;
                        }
                    }

                    // 吃掉这颗
                    xianCao.setLength(0f);
                }
            });
            mieMies.add(mieMie);
        }

        // 初始 5 只狮子
        final List<Lion> lions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Lion lion = new Lion();
            lion.setOnFunListener(new Fuck.OnFunListener() {
                @Override
                public void onFun() {
                    // 此处狮子可吃一个羊
                    if (mieMies.size() > 0) {
                        mieMies.remove(0);
                    } else {
                        System.out.println("没有羊吃了");
                    }
                }
            });
            lions.add(lion);
        }

        // 初始 1 颗树
        List<Tree> trees = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Tree tree = new Tree();
            tree.setOnFunListener(new Fuck.OnFunListener() {
                @Override
                public void onFun() {
                    // 此处树让狮子不动一天
                    for (Lion l : lions) {
                        l.setCantMove(true);
                    }
                }
            });
            trees.add(tree);
        }

        // 程序以天为单位。
        // 那么我们就以天来循环。 (模拟200天)
        for (int i = 1; i <= 200; i++) {

            for (XianCao xc : xiancaos) xc.onDay(i);

            for (MieMie mieMie : mieMies) mieMie.onDay(i);

            for (Lion lion : lions) lion.onDay(i);

            for (Tree tree : trees) tree.onDay(i);

            int xiancaoCount = 0;
            for (XianCao xc : xiancaos) if (xc.length >= 1f) xiancaoCount++;

            System.out.printf(
                    "第 %3d 天，还剩下 %4d 颗仙草, %3d 只羊, %3d 只狮子，%2d 颗树\n",
                    i,
                    xiancaoCount,
                    mieMies.size(),
                    lions.size(),
                    trees.size()
            );
        }

    }

    // 定义过日子接口，所有类实现这个接口，用于达到每个实体都要处理每天自己的分工工作。
    interface Day {

        /**
         * 每一天，此方法执行一次。
         *
         * @param day 第几天的计数。
         */
        void onDay(int day);
    }

    // 仙草，此类表示一个仙草
    static class XianCao implements Day {
        float length;

        XianCao() {
            length = 0f; // 初始化此仙草还没有长出来。
        }

        @Override
        public void onDay(int day) {
            if (length >= 1f) {
                // 已经是一颗成熟的仙草了。 不用再长了。
                return;
            }

            // 过一天长半颗。
            length += 0.5f;
        }

        public void setLength(float v) {
            if (length >= 1f) {
                length = v;
            } else {
                // 仙草没有长成，不能被吃。
            }
        }
    }

    // 此类表示一个动物、数， 什么的。
    static abstract class Fuck implements Day {
        // 操作监听器。
        private OnFunListener onFunListener;

        int funDoDay; // 标记这个动物\数 操作同一个一颗吃了多少天了。

        @Override
        public void onDay(int day) {
            funDoDay++; // 每天加1

            // 达到了一次动作周期，开始执行动作，然后又重新开始周期。
            if (funDoDay == getFunMaxDay()) {

                if (onFunListener != null) {
                    onFunListener.onFun();
                }

                funDoDay = 0;
            }
        }

        abstract int getFunMaxDay();

        interface OnFunListener {
            void onFun();
        }

        public void setOnFunListener(OnFunListener onFunListener) {
            this.onFunListener = onFunListener;
        }
    }

    static class MieMie extends Fuck {
        @Override
        int getFunMaxDay() {
            return 3;
        }
    }

    static class Lion extends Fuck {

        boolean cantMove;  // 标记是否中了不动一天的魔法。

        @Override
        public void onDay(int day) {
            if (cantMove) {
                //  中了不动一天后，不进行逻辑处理，既相当于这天狮子没动。
                cantMove = false;
            } else {
                super.onDay(day);
            }
        }

        @Override
        int getFunMaxDay() {
            return 2;
        }

        public void setCantMove(boolean cantMove) {
            this.cantMove = cantMove;
        }
    }

    static class Tree extends Fuck {
        @Override
        int getFunMaxDay() {
            return 15;
        }
    }

}
