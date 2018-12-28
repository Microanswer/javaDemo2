package cn.microanswer;


public class Test21 {
    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月31日 09:52:48
     */
    public static void main(String[] args) throws Exception {
        /*
         * 计算序列： 1/2, 2/3, 3/5, 5/8 ... 的和。 前 20 项的
         */
        System.out.println("前20项的和：" + doQuestion(20));

    }

    /**
     * 计算 数列 1/2, 2/3, 3/5, 5/8 ... 的和
     *
     * @param count 要计算前多少项。
     * @return 结果。
     */
    private static float doQuestion(int count) {

        // 分子与分母的关系：
        // 后一个分数的分子是前一个分数的分母；
        // 后一个分数的分母是前一个分数的分子 + 分母；
        // 那么，定义出首项的分子分母，即可循环向后计算。
        float z = 1f, m = 2f;

        float t; // 零时变量。

        int didCount = 1; // 记录已经计算的个数。
        float result = z / m; // 保存所有相加的结果。
        // 已经计算的次数还没达到count， 则继续计算。使用 while 循环，再好不过。
        while (count > didCount) {
            // 后续项，先计算出分子和分母。
            // 怎么计算？根据上方分子与分母的关系。
            t = z;
            z = m;
            m = t + m;
            // 累加结果
            result += z / m;
            didCount++;
        }
        return result;
    }
}
