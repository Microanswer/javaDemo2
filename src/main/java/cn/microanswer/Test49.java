package cn.microanswer;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Test49 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2019年01月04日 16:45:13
     */
    public static void main(String[] args) throws Exception {

        List<Integer> nus = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 300000; i++) {
            nus.add(random.nextInt(Integer.MAX_VALUE));
        }

        long s = System.currentTimeMillis();
        Collections.sort(nus);
        System.out.println("耗时：" + (System.currentTimeMillis() - s) + "毫秒");
    }
}
