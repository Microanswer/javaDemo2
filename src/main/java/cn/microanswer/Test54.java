package cn.microanswer;


import cn.microanswer.Spider.Spider;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class Test54 {
    private Spider spider;
    private File dir;

    public Test54(File dir) {
        this.spider = new Spider();
        this.dir = dir;
    }


    /**
     * 获取图片列表数据
     *
     * @param pageIndex 页码
     * @param type      图片类型
     * @return 数组, 格式如：[["title", "href"], ["title", "href"]...]
     */
    private Object[][] getImgList(int pageIndex, String type) throws IOException {
        Response response = this.spider
                .setResponseCharSet("GB2312")
                .get("https://www.2717.com/ent/" + type + "/list_11_" + pageIndex + ".html");

        String html = this.spider.response2String(response);

        Document document = this.spider.parseHtml(html);

        Elements lis = document.select("div.MeinvTuPianBox ul li");

        Object[][] result = new Object[lis.size()][];
        for (int i = 0; i < lis.size(); i++) {
            Element li = lis.get(i);
            Element a = li.selectFirst("a");

            String title = a.attr("title");
            String href = a.attr("href");

            if (href.startsWith("/")) {
                href = response.request().url().newBuilder().encodedPath(href).build().toString();
            }

            result[i] = new Object[]{title, href};
        }

        return result;
    }

    /**
     * 下载
     *
     * @param item
     */
    public void downloadImg(Object[] item) throws IOException {
        File dir = new File(this.dir, item[0] + "");
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }

        Response response = this.spider
                .setResponseCharSet("GB2312")
                .get(item[1] + "");

        String html = this.spider.response2String(response);

        // System.out.println(html);

        Document document = this.spider.parseHtml(html);

        // 找到图片dom
        Elements imgs = document.select("#picBody p a img");

        for (Element img : imgs) {
            String src = img.attr("src");
            if (src == null || src.trim().length() == 0) {
                continue;
            }
            System.out.println("下载：" + src);
            this.spider.downloadInFile(src, dir);
        }
        // 寻找下一页按钮
        Elements a = document.select("div.page-tag.oh ul.articleV4Page li#nl a");
        if (a.isEmpty()) {
            // 没有下一页。
            System.out.println("没有下一页了");
        } else {
            // 有下一页
            String href = a.get(0).attr("href");
            HttpUrl url = response.request().url();
            href = url.newBuilder().setEncodedPathSegment(url.pathSize() - 1, href).build().toString();
            downloadImg(new Object[]{item[0], href});
        }

    }

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年04月20日 23:12:47
     */
    public static void main(String[] args) throws Exception {

        Test54 spider1727Com = new Test54(new File("C:\\Users\\Microanswer\\Desktop\\testdown"));

        Object[][] meinvtupians = spider1727Com.getImgList(1, "meinvtupian");

        for (Object[] meinvtupian : meinvtupians) {
            spider1727Com.downloadImg(meinvtupian);
        }

    }
}
