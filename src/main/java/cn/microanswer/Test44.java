package cn.microanswer;


import java.util.Arrays;
import java.util.List;

public class Test44 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月24日 10:44:05
     */
    public static void main(String[] args) throws Exception {


        // 计算 int 范围类的所有素数
        a:
        for (int i = 2; i <= Integer.MAX_VALUE; i++) {

            // 素数定义：只能能被 1 和 本身 整除的数。
            // 那么我们就要挨个判定是否只能被某个数整除。 这是最原始的方法。
            // 使用 for 循环来挨个判断

            int perfectCount = 0; // 记录刚刚被整除的次数。
            int j;
            for (j = 1; j <= i; j++) {

                if (i % j == 0) perfectCount++;

                if (perfectCount > 2) {
                    // 能够被整除超过2次以上，那一定不是素数了，可以不用继续循环计算这个数了。
                    continue a; // 调到外层循环继续执行下一个数。
                }
            }

            if (j - 1 == i) { // 为啥要 j-1? 因为 上边for循环最后一次执行的 j++ 是不需要的。这里减一就为了去除这次不必要的计算。
                // 找到一个素数
                System.out.println("找到素数：" + i);
            }
        }
    }

    public static void test(String[] args) {

        // 循环寻找 10000 以内的所有素数。。由于 1 本身不算素数，所以，从2开始循环。
        for (int i = 2; i <= 10000; i++) {

            // 素数的规则： 只能被1和自身整除的数。
            // 要表示成代码的话，可以换一种思考方式：
            // 原文：只能被1和自身整除的数 --> 换一种思考：
            // 那不就是： 只要被 2 和 本身这个数-1 之间的任何一个数整除，就不是素数了。

            // 所以先假定每个数都是素数。
            // 标记是否是素数。
            boolean flag = true;

            // 然后在判断这个数 在 2~这个数-1 之间是否有数能整除。
            for (int j = 2; j < i; j++) {

                // 如果能整除
                if (i % j == 0) {

                    // 很遗憾，只能将这个数标记为不是素数了。
                    flag = false;

                    // 此次计算结束。不再对这个数进行判断。
                    break;
                }
            }

            // 经过上面的一轮 for 判断，最初假定的 这个数是素数，成立，那么直接输出。

            // 由于 flag 本身是 boolean 类型的。 可以直接 if(flag) 也可以 if(flag == true) 效果一样的。
            if (flag) {
                System.out.println(i);
            }
        }

    }

    public static List<String> testArg(String... aegs) {

        System.out.println(Arrays.toString(aegs));

        return null;
    }

    /**
     * 集合转换成数组
     *
     * @param list 集合
     * @param <T>  返回数组类型
     * @return 结果
     */
    public static <T> T[] list2Array(List list) {

        if (list == null || list.size() == 0) {
            return (T[]) new Object[0];
        }
        T[] ara = (T[]) new Object[list.size()];
        list.toArray(ara);
        return ara;
    }
}
