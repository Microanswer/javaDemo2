package cn.microanswer;

public class Test17 {
    public static void main(String[] args) {

        // 使用while
        int sum = 0;
        int i = 0;
        while (i < 100) {
            i++;
            sum += i;
        }
        System.out.println("1+...+100=" + sum);

        // 使用 do..while
        sum = 0;
        i = 0;
        do {
            i++;
            sum += i;
        } while (i < 100);
        System.out.println("1+...+100=" + sum);

        // 使用for
        sum = 0;
        for (i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println("1+...+100=" + sum);

    }
}
