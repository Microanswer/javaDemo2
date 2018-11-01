package cn.microanswer;

import java.util.Scanner;

public class Test19 {

    // 首先建立映射表。
    // 由于是将数字映射到中文，可以利用数组小标使用数字的特性定义一个较为优雅的映射表：
    static String[] charts = "零壹贰叁肆伍陆柒捌玖".split("");
    // 整数位的进制位名称
    static String[] flags = "个拾佰仟万".split("");
    // 分为的进制名称
    static String[] flags2 = "分角".split("");

    public static void main(String[] args) {

        // 先提示用户输入
        System.out.println("请输入一个数");
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

        // 开始解析输入并转换为中文大写格式。
        /*
         * 思考：
         * 1、当有小数点的时候，就把小数点前后分开进行转发，降低开发难度。
         */

        System.out.println("您输入的数：" + inputStr);
    }

    /**
     * 将小数点左边的数，即 整数 按照中文大写转换
     *
     * @param left 整数,小数点左边的数
     * @return 转换结果
     */
    static String transLeft(String left) {

        // 由于进制位最大定义到万位，所以，此方法处理的最大位整数为9位数。超过了的将不会处理。
        if (left.length() > 9) {
            System.out.println("数字太大，不做处理，只能处理9位以下的数据长度。");
            return left;
        }

        int num = Integer.parseInt(left);

        // 不如使用 while 来巧妙的完成这个工作吧！
        /*
        测试数： 3201
        思路原型：
        while(还没计算到个位) {
            不停的从高位向低位计算
        }
         */

        StringBuilder stringBuilder = new StringBuilder();

        int tens[] = new int[]{1, 10, 100, 1000, 10000};

        // 使用此字段标记是否计算到了个位。 初始值数据长度，计算一个长度减一，直到个位计算完了，长度为0则不再计算
        int index = tens.length - 1;
        while (index >= 0) {

            int i = num / tens[index];

            if ( i > 1) {
                // 成功的除法

                int shang = i; // 获取商
                int yu = num % tens[index]; // 获取余数
                stringBuilder.append(transLeft(String.valueOf(shang))).append(flags[index]);
                stringBuilder.append(transLeft(String.valueOf(yu)));
            } else {
                // 不成功的除法, 说明位数不足高位，则不用处理
            }

            // 降低位数计算。
            index--;
        }


        return stringBuilder.toString();
    }

    /**
     * 将小数点左边的数，即 小数 按照中文大写转换
     *
     * @param right 小数点右边的数
     * @return 转换结果
     */
    static String transRight(String right) {

        // 对于小数点右边，如果末位是0，则是无意义的，可以去除。
        if (right.charAt(1) == '0') {
            right = String.valueOf(right.charAt(0));
        }

        // 对于小数点右边，十分简单，没有十位百位的说法。 且只有角分的读法。那么就直接拼接输出。
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < right.length(); i++) {
            int num = Integer.parseInt(right.substring(i, i + 1));
            result.append(num == 0 ? charts[num] : charts[num] + flags[4 + i]); // 为啥要 加4， 因为角分在数组后边几位。
        }

        return result.toString();
    }
}
