package cn.microanswer.Spider;


import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2717.com的网站图片爬虫
 */
public class Spider_2717com {
    private Spider spider;
    private File dir;

    public Spider_2717com(File dir) {
        this.spider = new Spider();
        this.dir = dir;
    }


    /**
     * 获取图片列表数据
     *
     * @param pageIndex 页码
     * @param site      图片站点
     * @return 数组, 格式如：[["title", "href"], ["title", "href"]...]
     */
    public Object[][] getImgList(int pageIndex, Site site) throws IOException {
        Response response = this.spider
                .setResponseCharSet("GB2312")
                .get("https://www.2717.com/" + site.siteGroup.getPath() + site.getPath() + site.getUrl().replace("{page}", pageIndex + ""));

        String html = this.spider.response2String(response);

        Document document = this.spider.parseHtml(html);

        Elements lis = document.select(site.getListSelector());

        Object[][] result = new Object[lis.size()][];
        for (int i = 0; i < lis.size(); i++) {
            Element li = lis.get(i);
            Element a = li.selectFirst(site.getaInLiSelector());

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
     * 新建一个在2717.com网站下的站点分组。
     *
     * @param name 名称
     * @param path 路径
     */
    public static SiteGroup newSiteGroup(String name, String path) {
        return new SiteGroup(name, path);
    }

    public static class SiteMap {

        public static String SITE_NAME_SHEHUIBAITAI = "社会百态";
        public static String SITE_NAME_FENGJINGSHEYING = "风景图片";
        public static String SITE_NAME_GAOXIAOQUTU = "搞笑图片";
        public static String SITE_NAME_DONGWUSHIJIE = "动物图片";
        public static String SITE_NAME_CHEBIAO = "汽车标志";

        private static SiteGroup WORD_GROUP = newSiteGroup("图看世界", "word/");

        static {

            WORD_GROUP.addSite(new Site(
                    SITE_NAME_SHEHUIBAITAI, "shehuibaitai/",
                    "list_5_{page}.html", WORD_GROUP,
                    ".list_article_left_list ul.list_article_left li", "a",
                    ".contentV3_body img", ""));

            WORD_GROUP.addSite(new Site(
                    SITE_NAME_FENGJINGSHEYING, "fengjingsheying/",
                    "list_6_{page}.html", WORD_GROUP,
                    "", "",
                    "", ""));

            WORD_GROUP.addSite(new Site(
                    SITE_NAME_GAOXIAOQUTU, "gaoxiaoqutu/",
                    "list_7_{page}.html", WORD_GROUP,
                    "", "",
                    "", ""));

            WORD_GROUP.addSite(new Site(SITE_NAME_DONGWUSHIJIE, "dongwushijie/",
                    "list_8_{page}.html", WORD_GROUP,
                    "", "",
                    "", ""));

            WORD_GROUP.addSite(new Site(
                    SITE_NAME_CHEBIAO, "chebiao/",
                    "index.html", WORD_GROUP,
                    "", "",
                    "", ""));

        }


        // private SiteGroup ENT_GROUP = newSiteGroup("娱乐周边", "ent/");
        // private SiteGroup BEAUTIFUL_GROUP = newSiteGroup("唯美图片", "beautiful/");
        // private SiteGroup GAME_GROUP = newSiteGroup("游戏动漫", "game/");
        // private SiteGroup OTHER_GROUP = newSiteGroup("其他图片", "qita/");
        // private SiteGroup NONE_GROUP = newSiteGroup("无分组", "");
    }

    /**
     * 图片组别
     */
    public static class Site {
        private String name;
        private String path;
        private String url;
        private SiteGroup siteGroup;
        private String listSelector;
        private String aInLiSelector;
        private String imgSelector;
        private String nextSelector;

        private Site(
                String name, String path,
                String url, SiteGroup siteGroup,
                String listSelector, String aInLiSelector,
                String imgSelector, String nextSelector
        ) {
            this.name = name;
            this.path = path;
            this.url = url;

            if (siteGroup == this) {
                throw new RuntimeException("站点所在组别不能是站点本身。");
            }

            this.siteGroup = siteGroup;
            this.listSelector = listSelector;
            this.aInLiSelector = aInLiSelector;
            this.imgSelector = imgSelector;
            this.nextSelector = nextSelector;
        }

        public void setSiteGroup(SiteGroup siteGroup) {
            this.siteGroup = siteGroup;
        }

        public SiteGroup getSiteGroup() {
            return siteGroup;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public String getUrl() {
            return url;
        }

        public String getListSelector() {
            return listSelector;
        }

        public String getaInLiSelector() {
            return aInLiSelector;
        }

        public String getImgSelector() {
            return imgSelector;
        }

        public String getNextSelector() {
            return nextSelector;
        }
    }

    public static class SiteGroup extends Site {
        private List<Site> sites;

        private SiteGroup(String name, String path) {
            super(name, path, null, null, null,null,null,null);
            sites = new ArrayList<>();
        }

        private SiteGroup(String name, String path, Site site) {
            this(name, path);
            if (site != null) {
                this.sites.add(site);
            }
        }

        private SiteGroup(String name, String path, List<Site> sites) {
            this(name, path);
            this.sites.addAll(sites);
        }

        public void addSite(Site site) {
            this.sites.add(site);
        }

        public Site getSiteBySiteName(String name) {
            if (sites == null) {
                return null;
            }
            for (Site s : sites) {
                if (s.name.equals(name)) {
                    return s;
                }
            }
            return null;
        }
    }
}
