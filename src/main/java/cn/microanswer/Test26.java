package cn.microanswer;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class Test26 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月12日 10:28:30
     */
    public static void main(String[] args) throws Exception {
        printHDS(9);
    }


    // 先预存所有 1~9 这几个数每个数的 n 次方的值。
    private static HashMap<String, BigDecimal> numbersMap = null;
    private static String[] STRS = "123456789".split("");
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
        for (int i = 0; i < 10; i++) {
            numbersMap.put(String.valueOf(i), new BigDecimal(i).pow(n));
        }

        // 然后在这些数中循环排列组合，找到符合要求的花朵数。
        String[] numbers = new String[n];
        LinkedList<MyNumber> tryList = new LinkedList<>();
        int step = STEP_GOING;
        do {

            MyNumber myn = null;

            if (step == STEP_GOING) {
                myn = getNextPosition(numbers);
                if (myn == null) {
                    myn = tryList.getLast();
                }
            } else {
                myn = tryList.getLast();
            }


            if (step == STEP_GOING) {
                String s = myn.nexNumber();
                if (s == null) { // 此位置所有数都尝试过了
                    numbers[myn.index] = null;
                    tryList.removeLast();
                    step = STEP_BACK;
                } else {
                    numbers[myn.index] = s;
                    if (!tryList.contains(myn)) {
                        tryList.add(myn);
                    }
                }

            } else {
                String s = myn.nexNumber();
                if (s == null) {
                    numbers[myn.index] = null;
                    tryList.removeLast();
                    step = STEP_BACK;
                } else {
                    numbers[myn.index] = s;
                    step = STEP_GOING;
                }
            }


            // if (isHDS(numbers)) {
            //     System.out.println("找到一个花朵数：" + array2String(numbers));
            //     count++;
            // }

        } while (tryList.size() != 0);

        System.out.println(String.format("在所有%d位数的正整数中找到%d个花朵数,耗时：%d秒", n, count, ((System.currentTimeMillis() - startTime) / 1000)));
    }

    private static MyNumber getNextPosition(String[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == null) {
                return new MyNumber(i);
            }
        }
        return null;
    }

    /**
     * 判断是否花朵数
     *
     * @param numbers 数组
     * @return true 如果是， 否则返回 false
     */
    private static boolean isHDS(String[] numbers) {
        BigDecimal result = BigDecimal.ZERO;
        StringBuilder s1 = new StringBuilder();
        for (String s : numbers) {
            if (s == null) {
                return false;
            }
            result = result.add(numbersMap.get(s));
            s1.append(s);
        }
        return s1.toString().equals(result.toPlainString());
    }

    private static String array2String(Object[] array) {
        StringBuilder r = new StringBuilder();
        for (Object sa : array) {
            r.append(sa);
        }
        return r.toString();
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
            Collections.addAll(numbers, STRS);
        }

        /**
         * 获取下一个可尝试的数字。
         *
         * @return 数字。
         */
        String nexNumber() {
            if (!hasNext()) {
                return null;
            }
            return numbers.remove(0);
        }

        // 是否还有可尝试的数字
        boolean hasNext() {
            return !numbers.isEmpty();
        }

        @Override
        public String toString() {
            return "index:" + index + ", numbers:" + this.numbers;
        }
    }

}
