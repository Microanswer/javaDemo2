package cn.microanswer;


import java.util.Arrays;
import java.util.Comparator;

public class Test31 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月22日 16:10:47
     */
    public static void main(String[] args) throws Exception {

        // 自动实现。
        String strs[] = new String[]{"B2", "C1", "D3", "C2", "D1", "A3"};
        Arrays.sort(strs);
        System.out.println("　　自动实现结果：" + Arrays.toString(strs));

        // 人工实现
        String strs2[] = new String[]{"B2", "C1", "D3", "C2", "D1", "A3"};
        Arrays.sort(strs2, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                String[] split = o1.split("");
                String[] split1 = o2.split("");
                int result1 = split[0].compareTo(split1[0]);
                if (result1 != 0) {
                    return result1;
                } else {
                    return split[1].compareTo(split1[1]);
                }
            }
        });
        System.out.println("　　人工实现结果：" + Arrays.toString(strs2));

        // 人工冒泡实现
        String strs3[] = new String[]{"B2", "C1", "D3", "C2", "D1", "A3"};
        for (int i = 0; i < strs3.length; i++) {
            for (int j = i + 1; j < strs3.length; j++) {
                String o1 = strs3[i];
                String o2 = strs3[j];
                String[] split = o1.split("");
                String[] split1 = o2.split("");
                int result1 = split[0].compareTo(split1[0]);
                if (result1 != 0) {
                    // 比较首字母得到了大小。
                    if (result1 == 1) {
                        // 交换位置。
                        String temp = strs3[j];
                        strs3[j] = strs3[i];
                        strs3[i] = temp;
                    }
                } else {
                    int result2 =  split[1].compareTo(split1[1]);
                    if (result2 == 1) {
                        // 交换位置。
                        String temp = strs3[j];
                        strs3[j] = strs3[i];
                        strs3[i] = temp;
                    }
                }
            }
        }
        System.out.println("人工冒泡实现结果：" + Arrays.toString(strs2));

    }
}
