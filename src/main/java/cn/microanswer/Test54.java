package cn.microanswer;


import cn.microanswer.Spider.Spider;

public class Test54 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2019年04月20日 23:12:47
     */
    public static void main(String[] args) throws Exception {

        Spider spider = new Spider();
        String html = spider.getHtml("http://issue.cpic.com.cn/ecar/view/");
//        String html = spider.getHtml("https://www.baidu.com");

        System.out.println(html);

    }
}
