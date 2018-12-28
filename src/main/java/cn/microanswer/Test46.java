package cn.microanswer;


import java.util.Calendar;
import java.util.Date;

public class Test46 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月28日 18:07:50
     */
    public static void main(String[] args) throws Exception {
        String d = "43102.6097916667";


        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, 0, -1);
        calendar.add(Calendar.DAY_OF_YEAR, 43102);
        System.out.println(calendar.getTime().toLocaleString());
    }
}
