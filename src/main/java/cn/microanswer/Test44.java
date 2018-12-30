package cn.microanswer;


import java.util.Arrays;
import java.util.List;

public class Test44 {

    public static void main(String[] args) {

        printBoomInfo(new String[][]{
                {"*","^","^","^"},
                {"^","^","^","^"},
                {"^","*","^","^"},
                {"^","^","^","^"}
        });
    }

    /**
     * 打印扫雷信息。
     * eg:
     * 输入：
     * *^^^
     * ^^^^
     * ^*^^
     * ^^^^
     * 将输出：100 2210 110 1110
     *
     * @param array
     */
    private static void printBoomInfo(String[][] array) {
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {

                // 如果这个位置本身是雷，则不输出。(这是根据示列的，示列上雷的位置没有输出，如果要输出，只要注释这2行即可)
                String s = array[row][col];
                if ("*".equals(s)) continue;


                // 获取这个位置周围的所有元素。
                String[] boomAtPosition = getBoomAtPosition(array, row, col);
                // 获取这些原生中的炸弹个数并输出这个个数。
                System.out.print(getBoomCount(boomAtPosition));
            }
            System.out.print(" ");
        }

    }

    /**
     * 获取某个位置 周围所有的雷。
     *
     * @param array    要从哪个数组中获取
     * @param rowIndex 行号
     * @param colIndex 列号
     * @return 结果。
     */
    private static String[] getBoomAtPosition(String[][] array, int rowIndex, int colIndex) {

        // 每个位置周围都有 8 个相邻的区域。 但是边界处除外。
        // 故，此处先判断边界处。但是如何优雅的判断是否处于边界了呢？
        // 方案一：可以写if语句挨个判断。
        // 方案二：可以先不考虑是否在边界，全部按不在边界考虑，然后再根据是否下标越界处理。（要下标越界肯定就是边界了。）

        // 使用 方案二。

        // 先得出所有位置的定位。
        int[][] positions = {
                {rowIndex - 1, colIndex - 1}, {rowIndex - 1, colIndex}, {rowIndex - 1, colIndex + 1},
                {rowIndex,     colIndex - 1},                           {rowIndex,     colIndex + 1},
                {rowIndex + 1, colIndex - 1}, {rowIndex + 1, colIndex}, {rowIndex + 1, colIndex + 1},
        };

        // 然后将这些位置对应的字符放在数组中。

        // 但是由于可能上方的位置存在越界现象(越 array 的界)，所以判断出上方越界的下标,然后取没有越界的。
        String[] result = new String[8]; // 定义结果数组， 固定 8 的长度。
        for (int i = 0; i < positions.length; i++) {

            int mRowIndex = positions[i][0];
            int mColIndex = positions[i][1];

            if (mRowIndex < 0 || mColIndex < 0 || mRowIndex >= array.length || mColIndex >= array[0].length) {
                // 这个里面的属于越界的。不加入计算。
                // 不加入计算的情况，咱们使用 null 来占位数组。
                result[i] = null;
            } else {
                result[i] = array[mRowIndex][mColIndex]; // 取具体的内容。
            }
        }

        return result;
    }

    /**
     * 获取某个位置的结果信息中有多少个炸弹。
     * @param boomsAtLocation 通过 getBoomAtPosition() 方法得到的结果。
     * @return 炸弹个数。
     */
    private static int getBoomCount(String[] boomsAtLocation) {
        int count = 0;
        for (int i = 0; i < boomsAtLocation.length; i++) {
            String s = boomsAtLocation[i];
            if ("*".equals(s)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月24日 10:44:05
     */
    public static void main3(String[] args) throws Exception {


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
