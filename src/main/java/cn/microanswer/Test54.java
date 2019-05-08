package cn.microanswer;


import cn.microanswer.Spider.Spider_2717com;

import java.io.File;

public class Test54 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年04月20日 23:12:47
     */
    public static void main(String[] args) throws Exception {

        Spider_2717com spider1727Com = new Spider_2717com(new File("C:\\Users\\Micro\\Desktop\\testdown"));

        // Object[][] meinvtupians = spider1727Com.getImgList(1, "meinvtupian");

        // for (Object[] meinvtupian : meinvtupians) {
            spider1727Com.downloadImg(new Object[]{
                    "人体艺术美女妲己豪乳肥臀床上撩人日本旅拍图片",
                    "https://www.2717.com/ent/meinvtupian/2019/314027.html"
            });
        // }

    }
}
