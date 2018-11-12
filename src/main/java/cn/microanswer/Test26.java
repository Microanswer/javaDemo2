package cn.microanswer;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test26 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月12日 10:28:30
     */
    public static void main(String[] args) throws Exception {


        printHDS(8);
    }


    // 先预存所有 1~9 这几个数每个数的 n 次方的值。
    private static HashMap<String, BigDecimal> numbersMap = null;
    private final static int STEP_GOING = 1;
    private final static int STEP_BACK = 2;

    /**
     * 打印指定位数的所有花朵数。
     * eg:
     * 参数传递 5，则会将所有5位整数符合花朵数规则的数全部打印。
     *
     * @param n 位数。
     */
    private static void printHDS(int n) {
        int count = 0; // 记录找到的花朵数个数。
        long startTime = System.currentTimeMillis();

        numbersMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            numbersMap.put(String.valueOf(i), new BigDecimal(i).pow(n));
        }

        // 然后在这些数中循环排列组合，找到符合要求的花朵数。
        MyNumber[] mynumbers = new MyNumber[n];
        String[] numbers = new String[n];
        int position = 0;
        int step = STEP_GOING;
        while (true) {

            MyNumber mn = null;

            if (step == STEP_GOING) {
                mn = mynumbers[position];
                if (mn == null) {
                    mn = new MyNumber(position);
                    mynumbers[position] = mn;
                }
            } else if (step == STEP_BACK) {
                mn = mynumbers[--position];
            }

            if (mn.hasNext()) {
                numbers[mn.index] = mn.nexNumber();
                step = STEP_GOING;
            } else {
                // 这个位置没有可填写的数了，回退一位。
                mn = new MyNumber(mn.index);
                mynumbers[mn.index] = mn;
                step = STEP_BACK;
            }


            if (isHDS(numbers)) {
                System.out.println("找到一个花朵数：" + array2String(numbers));
            }
        }

        // System.out.println(String.format("在所有%d位数的正整数中找到%d个花朵数,耗时：%d秒", n, count, ((System.currentTimeMillis() - startTime) / 1000)));
    }

    /**
     * 判断是否花朵数
     *
     * @param numbers 数组
     * @return true 如果是， 否则返回 false
     */
    static boolean isHDS(String[] numbers) {
        BigDecimal number = new BigDecimal(array2String(numbers));
        BigDecimal result = new BigDecimal("0");
        for (String s : numbers) {
            if (s == null) {
                return false;
            }
            result = result.add(numbersMap.get(s));
        }
        return number.compareTo(result) == 0;
    }

    static String array2String(Object[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : array) {
            stringBuilder.append(o.toString());
        }
        return stringBuilder.toString();
    }

    /**
     * 每个位置可以填写那些数字，此类作为管理类。
     */
    static class MyNumber {
        int index; // 此数字所在的位置。
        ArrayList<String> numbers;

        MyNumber(int index) {
            this.index = index;
            numbers = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                numbers.add(String.valueOf(i));
            }
        }

        /**
         * 获取下一个可尝试的数字。
         *
         * @return 数字。
         */
        String nexNumber() {
            return numbers.remove(0);
        }

        // 是否还有可尝试的数字
        boolean hasNext() {
            return numbers.size() > 0;
        }

    }

}
