package cn.microanswer;

import java.util.LinkedList;

public class Test2 implements Comparable<Test2>{
    public static void main(String args[]) {
        /**
         * 将牌一次从上到下编号为1~n，
         * 之后将第一张牌放到最后，将第二张牌扔掉，将第三张牌放到最后，将第四张牌扔掉，
         * 依次类推，                          知道纸牌还至少剩下2张停止操作，输出删除纸牌的编号。
         */
        LinkedList<Integer> pokers = new LinkedList<>();
        while (pokers.size() < 6) pokers.add(pokers.size() + 1); // 初始化 50 张。

        // 开始操作：
        boolean fuck = true;
        System.out.print("移除：");
        while (pokers.size() > 2) {
            if (fuck) {
                pokers.addLast(pokers.removeFirst());
            } else {
                int removedNum = pokers.removeFirst();
                System.out.print(removedNum + ",");
            }
            fuck = !fuck;
        }
        System.out.println();
        System.out.println("结果:" + pokers.toString());
    }

    @Override
    public int compareTo(Test2 o) {
        return 0;
    }
}
