package cn.microanswer;

import java.util.Scanner;

public class Test19 {

    /**
     * 程序从这里开始运行的哦。
     *
     * @author Microanswer.cn
     * @since 2018年11月3日13:52:06
     */
    public static void main(String[] args) {

        // 先提示用户输入
        System.out.println("请输入一个数：");
        Scanner scanner = new Scanner(System.in);
        String inputStr = scanner.next();

        // 校验用户输入数据的合法性(此正则表达式限定了：必须输入整数或小数格式的内容)。
        while (!inputStr.matches("[0-9]+[.]?[0-9]{0,2}")) {
            System.out.println("你的输入不合法，请重新输入：");
            inputStr = scanner.next();
        }

        // 由于上方正则表达式对最后一位是小数点的匹配是允许的，所以此处处理，如果最后一位是小数点的就去掉。
        if (inputStr.endsWith(".")) {
            inputStr = inputStr.substring(0, inputStr.length() - 1);
        }

        System.out.println("转换结果：" + transMoney(inputStr));
    }




    // 首先建立映射表。
    // 由于是将数字映射到中文，可以利用数组下标使用数字的特性定义一个较为优雅的映射表：
    private static final String[] charts = "零壹贰叁肆伍陆柒捌玖".split("");
    // 整数位的进制位名称
    private static final String[] flags = "个拾佰仟万亿兆".split("");
    // 人民币面额制称
    private static final String[] flags2 = "分角元".split("");

    //                                            个  十  百    千     万       亿         兆
    private static final long tens[] = new long[]{1, 10, 100, 1000, 10000, 100000000, 10000000000000L};

    /**
     * 将 形如： 123.5 或 123 转换为中文大写的人民币格式
     *
     * @param moneyNum 要转换的数字
     * @return 转换结果
     */
    private static String transMoney(String moneyNum) {

        // 开始解析输入并转换为中文大写格式。
        /*
         * 思考：
         * 1、当有小数点的时候，就把小数点前后分开进行转换，降低开发难度。
         */

        String[] split = moneyNum.split("\\.");
        String r;
        try {
            r = transLeft(split[0]) + flags2[2];
        } catch (Exception e) {
            return "转换错误，您输入的数字太大了。最大的数不应超过：" + Long.MAX_VALUE;
        }
        if (split.length == 2) {
            r += transRight(split[1]);
        }

        return r;
    }

    /**
     * 将小数点左边的数，即 整数 按照中文大写转换
     *
     * @param left 整数,小数点左边的数
     * @return 转换结果
     */
    private static String transLeft(String left) {

        // 由于进制位最大定义到兆位，所以，此方法处理的最大位整数为19位数。超过了的将不会处理。
        if (left.length() >= 20) {
            System.out.println("数字太大，不做处理，只能处理20位以下的数据长度。");
            return left;
        }

        // 过滤非法数字，例如： 000023 这种就会变成 23
        String numStr = String.valueOf(Long.parseLong(left));

        // 保存结果的
        StringBuilder result = new StringBuilder();

        // 使用此字段标记是否计算到了个位。 初始值为数据长度，计算一个长度减一，直到个位计算完了，长度为 0 则不再计算
        int index = tens.length - 1;
        while (index >= 0) {

            // 如果开头为 0 ，则读数零，  又如果多个 0 连续，只会读一个零
            if (numStr.startsWith("0")) {
                // 所以此处判断，如果有多个零被连续读了，只读出一个零
                if (result.lastIndexOf(charts[0]) != result.length() - 1) {
                    result.append(charts[0]);
                }

                // 读了零过后去除这一位，继续后面的读数。
                if (numStr.length() > 1) {
                    numStr = numStr.substring(1);
                }
            }

            long i = Long.parseLong(numStr) / tens[index];

            if (i >= 1) {
                // 成功的除法

                int sang = (int) i; // 获取商

                // 如果商大于等于 10 ，则递归读数
                if (sang >= 10) {
                    String sangStr = String.valueOf(sang);
                    result.append(transLeft(sangStr));

                    // 处理了前面的数则将其移除，好继续后面的数
                    numStr = numStr.replaceFirst(sangStr, "");

                } else {
                    // 商小于 10 ，返回读数
                    result.append(charts[sang]);

                    // 处理了一个数则将其移除，好继续处理后面的数
                    if (numStr.length() > 1) {
                        numStr = numStr.substring(1);
                    }
                }

                if (index > 0) { // 对于个位，通常不会再读多少 个 了。
                    result.append(flags[index]);
                }
            }

            // 降低位数计算。
            index--;
        }

        // 结尾的 0 通常不会读，如果有，去除。
        if (result.lastIndexOf(charts[0]) == result.length() - 1) {
            return result.substring(0, result.length() - 1);
        }

        return result.toString();
    }

    /**
     * 将小数点左边的数，即 小数 按照中文大写转换
     *
     * @param right 小数点右边的数
     * @return 转换结果
     */
    private static String transRight(String right) {

        // 对于小数点右边，两个都是0，直接省略
        if ("00".equals(right)) {
            return "";
        }

        // 对于小数点右边，如果末位是0，则是无意义的，可以去除。
        if (right.charAt(right.length() - 1) == '0') {
            right = right.substring(right.length() - 1);
        }

        // 对于小数点右边，十分简单，没有十位百位的说法。 且只有角分的读法。那么就直接拼接输出。
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < right.length(); i++) {
            int num = Integer.parseInt(right.substring(i, i + 1));
            result.append(num == 0 ? charts[num] : charts[num] + flags2[1 - i]); // 为啥要 1-i? 因为分在数组前头
        }

        return result.toString();
    }
}
