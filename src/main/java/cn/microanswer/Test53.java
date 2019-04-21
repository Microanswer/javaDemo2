package cn.microanswer;


import cn.microanswer.Spider.Spider;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test53 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年04月20日 15:47:30
     */
    public static void main(String[] args) throws Exception {

        Spider spider = new Spider();
        String web = "http://www.mm964.com";
        String downloadDir = "D:\\测试下载\\";

        String html = spider.getHtml(web + "/articlelist/?37.html");

        Document document = spider.parseHtml(html);

        // 选择到列表
        Elements elements = document.select(".list .listt");
        for (Element element : elements) {
            Element td_a = element.selectFirst("td a");
            String href = td_a.attr("href");
            String title = td_a.selectFirst("font").text();
            // System.out.println(title + ", " + href);

            if (href.startsWith("/")) {
                href = web + href;
            }

            // 打开内容页面
            String conetntPage = spider.getHtml(href);
            Document conetentDoc = spider.parseHtml(conetntPage);

            // 解析内容界面
            // System.out.println(conetntPage);

            // 选择图片内容div
            Elements pics = conetentDoc.select(".picContent img");

            for (int o = 0; o < pics.size(); o++) {
                Element e = pics.get(o);
                String src = e.attr("src");
                System.out.print("下载：[" + title + "][" + (o + 1) + "/" + pics.size() + "] " + src);
                spider.downloadInFile(src, downloadDir + title);
                System.out.println(" 成功");
            }
        }

        System.out.println("完成");
    }
}
