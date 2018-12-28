package cn.microanswer;


import java.util.Arrays;
import java.util.List;

public class Test45 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月28日 15:43:22
     */
    public static void main(String[] args) throws Exception {

        // numbers 数组里面保存的数都是基本类型的0
        int [] numbers = new int[6];

        // 将数组中的所有位置都填写0
        Arrays.fill(numbers, 0);

        // 将数组转换为集合。
        List<int[]> ints = Arrays.asList(numbers);

        // 得到的结果并不似我们预期的 集合里面 是数字， 而成了：一个数组的集合。

        // 所以此代码返回 false。 因为此集合里面的元素是 int[] 类型的。 传递一个 0 进去判断是否存在，是肯定不存在。
        boolean result = ints.contains(0); // false


    }
}
