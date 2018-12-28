package cn.microanswer;

import java.util.Scanner;

public class Test10 {

    // 定义区域类。 一个区域有 一个小数 和一个 大数 组成的一个范围
    static class Area {

        int min; // 最小的数
        int max; // 最大的数

        Area(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * 判断某个数是否在该区域内。
         *
         * @param num 要判断的数字
         * @return 如果在，返回 true，否则返回 false
         */
        boolean isInArea(int num) {
            return this.min <= num && num <= this.max;
        }

        /**
         * 判断该区域是否比某个数大
         *
         * @param num 要判断的数字
         * @return 如果是，返回 true，否则返回 false
         */
        boolean isBigger(int num) {
            return this.min > num;
        }

        /**
         * 判断该区域是否比某个数小
         *
         * @param num 要判断的数
         * @return 如果是，返回 true，否则返回 false
         */
        boolean isSmaller(int num) {
            return this.max < num;
        }

        /**
         * 随机从该范围中获取一个数字
         *
         * @return 结果
         */
        int getRandom() {
            return (int) Math.round(Math.random() * (this.max - this.min + 1) + this.min);
        }
    }

    private static int getInputValue(Scanner scanner) {

        System.out.println("请输入[0-9,A-Z,a-z]中任意一个字符：");
        String str = scanner.next();

        // 修复错误，当用户输入的不是这些字符时，给出是
        while (!str.matches("[0-9A-Za-z]")) {
            System.out.println("您的输入有误，请重新输入[0-9,A-Z,a-z]中任意一个字符：");
            str = scanner.next();
        }

        return str.charAt(0);
    }

    public static void main(String[] args) {

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 定义三个区域
        Area areas[] = new Area[3];
        areas[0] = new Area('0', '9');
        areas[1] = new Area('A', 'Z');
        areas[2] = new Area('a', 'z');

        int randomIndex = (int) (Math.random() * 3);
        int randomValue = areas[randomIndex].getRandom();

        // 使用 Scanner 从键盘获取输入值。
        Scanner scanner = new Scanner(System.in);
        int userValue = getInputValue(scanner);

        // 判断用户输入是否等于随机的数。并记录用户猜测次数。
        int tryCount = 0;
        while (userValue != randomValue) {
            System.out.printf("第%d次猜测\n", ++tryCount);


            // 判断 用户输入的字符 是否和 随机的字符 在同一个区域
            if (areas[randomIndex].isInArea(userValue)) {
                // 在，则判断输入的字符是否在随机字符之前

                System.out.print("在随机产生字符之");
                if (userValue < randomValue) {
                    System.out.println("前。");
                } else {
                    System.out.println("后。");
                }

            } else {
                // 输入的字符 和 随机产生的字符 不在同一区域
                System.out.print("不在当前区域，当前区域所有的字符都位于您输入的字符之");

                if (areas[randomIndex].isSmaller(userValue)) {
                    System.out.print("前");
                } else {
                    System.out.print("后");
                }

                System.out.println("。(按0-9,A-Z,a-z排序)");

            }

            // 重新获取用户输入
            userValue = getInputValue(scanner);
        }

        System.out.printf("猜对啦，您一共猜测了 %d 次，一共花费了 %d 毫秒", ++tryCount, System.currentTimeMillis() - startTime);

        scanner.close();
    }
}
