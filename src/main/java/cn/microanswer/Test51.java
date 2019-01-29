package cn.microanswer;

import java.io.File;

public class Test51 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年01月21日 14:58:56
     */
    public static void main(String[] args) throws Exception {
        File s = HttpUtil.download("http://amazeui.org/javascript/popover");
        System.out.println(s.getCanonicalPath());
    }

}