package cn.microanswer.Spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.EventListener;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * 网络爬虫类。 此类模拟了浏览器的请求。
 * <p>
 * 此类的正常运转，你需要加入下面的依赖：
 * 1、alibaba的FastJson。
 * 2、OkHttp3.
 * 3、jsoup
 * </p>
 */
public class Spider {

    private static OkHttpClient okHttpClient;

    private SpiderConfig spiderConfig;

    // 一个 host对应一个 CookieHandle ，所以使用map来保存、
    private Map<String, CookieHandle> cookieHandleMap;

    public Spider() {
        this(null);
    }

    public Spider(SpiderConfig spiderConfig) {
        if (null == spiderConfig) spiderConfig = newConfig();
        this.spiderConfig = spiderConfig;
        this.cookieHandleMap = new HashMap<>();
    }

    private void logDebug(String msg) {
        // System.out.println("[DEBUG] " + msg);
    }

    private void logInfo(String msg) {
        //System.out.println("[INFO] " + msg);
    }

    private void logWarn(String msg) {
        // System.out.println("[WARN] " + msg);
    }

    private void logError(String msg, Exception e) {
        // System.out.println("[ERROR] " + msg);
        e.printStackTrace();
    }

    // 单列， 让多个Spider实例适用同一个http请求客户端。
    private static OkHttpClient getOkHttpClient(Spider spider) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .eventListener(spider.new MyEventListener())
                    .addNetworkInterceptor(spider.new MyCookieFixInterceptor())
                    .build();
        }
        return okHttpClient;
    }

    // 设置响应数据的编码方式。
    public Spider setResponseCharSet(String charSet) {
        this.spiderConfig.responseCharset = charSet;
        return this;
    }

    // 打开url
    public Response open(String url, String method, RequestBody requestBody, List<Cookie> cookies, Map<String, String> headers) throws IOException {

        logDebug(method + " 地址：" + url);
        HttpUrl httpUrl = HttpUrl.parse(url);

        Request.Builder builder = new Request.Builder();
        // 请求地址。
        builder.url(httpUrl);

        Map<String, String> innerHeader = getInnerHeader(httpUrl);
        if (!isEmpty(innerHeader) && !isEmpty(headers)) innerHeader.putAll(headers);
        // 添加请求头
        if (!isEmpty(innerHeader)) {
            for (Map.Entry<String, String> hd : innerHeader.entrySet()) {
                builder.addHeader(hd.getKey(), hd.getValue());
            }
        }

        // 添加cookie
        CookieHandle cookieHandle = getCookieHandle(httpUrl.host());
        cookieHandle.handleCookies(cookies);

        // 添加请求方法 和请求数据体。
        builder.method(method, requestBody);

        Request request = builder.build();

        // logDebug("提交请求头：" + JSON.toJSONString(request.headers().toMultimap()));

        OkHttpClient okHttpClient = getOkHttpClient(this);

        Call call = okHttpClient.newCall(request);

        // 执行请求
        return call.execute();
    }

    public Response open(String url, String method, RequestBody requestBody, List<Cookie> cookies) throws IOException {
        return open(url, method, requestBody, cookies, null);
    }

    public Response open(String url, String method, RequestBody requestBody) throws IOException {
        return open(url, method, requestBody, null);
    }

    public String getHtml(String url, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        Response response = get(url, cookies, headers);
        return response2String(response);
    }

    public String getHtml(String url, List<Cookie> cookies) throws IOException {
        return getHtml(url, cookies, null);
    }

    public String getHtml(String url) throws IOException {
        return getHtml(url, null);
    }

    public Response get(String url, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        return open(url, "GET", null, cookies, headers);
    }

    public Response get(String url, List<Cookie> cookies) throws IOException {
        return get(url, cookies, null);
    }

    public Response get(String url) throws IOException {
        return get(url, null);
    }

    public Response post(String url, RequestBody body, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        return open(url, "POST", body, cookies, headers);
    }

    public Response post(String url, RequestBody body, List<Cookie> cookies) throws IOException {
        return post(url, body, cookies, null);
    }

    public Response post(String url, RequestBody body) throws IOException {
        return post(url, body, null);
    }

    public Response postXwwwUrlEncode(String url, Map<String, String> body, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        return post(url, RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), map2wwwUrlFormEncode(body)), cookies, headers);
    }

    public Response postXwwwUrlEncode(String url, Map<String, String> body, List<Cookie> cookies) throws IOException {
        return postXwwwUrlEncode(url, body, cookies, null);
    }

    public Response postXwwwUrlEncode(String url, Map<String, String> body) throws IOException {
        return postXwwwUrlEncode(url, body, null);
    }

    public Response postJson(String url, JSONObject jsonObject, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        return post(url, RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonObject.toJSONString()), cookies, headers);
    }

    public Response postJson(String url, JSONObject jsonObject, List<Cookie> cookies) throws IOException {
        return postJson(url, jsonObject, cookies, null);
    }

    public Response postJson(String url, JSONObject jsonObject) throws IOException {
        return postJson(url, jsonObject, null);
    }

    // 下载文件到内存中，返回文件内容流。
    public InputStream downloadInMem(String url, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        Response response = get(url, cookies, headers);
        return response.body().byteStream();
    }

    public InputStream downloadInMem(String url, List<Cookie> cookies) throws IOException {
        return downloadInMem(url, cookies, null);
    }

    public InputStream downloadInMem(String url) throws IOException {
        return downloadInMem(url, null);
    }

    // 下载文件到指定文件。
    public File downloadInFile(String url, File dir, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("目录:" + dir.getCanonicalPath() + " 创建失败。");
            }
        }
        Response response = get(url, cookies, headers);

        String fileName = "";

        // 获取文件名。
        // 1、 先从header里面获取。
        String contentDisposition = response.header("Content-Disposition");
        if (!isEmpty(contentDisposition) && contentDisposition.contains("filename=")) {
            fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9);
        }
        // 2、再冲从url上取
        if (isEmpty(fileName)) {
            String[] split = url.split("/");
            fileName = split[split.length - 1];
        }
        // 3、再随机生成
        if (isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString().replaceAll("-", "");
        }


        InputStream inputStream = response.body().byteStream();
        File file = new File(dir, fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            byte[] data = new byte[1024];
            int dataSize = 0;

            while ((dataSize = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, dataSize);
            }

            fileOutputStream.flush();

        } catch (IOException e) {
            logError("下载文件出错", e);
        }
        return file;
    }

    public File downloadInFile(String url, String dir, List<Cookie> cookies, Map<String, String> headers) throws IOException {
        return downloadInFile(url, new File(dir), cookies, headers);
    }

    public File downloadInFile(String url, File dir, List<Cookie> cookies) throws IOException {
        return downloadInFile(url, dir, cookies, null);
    }

    public File downloadInFile(String url, String dir, List<Cookie> cookies) throws IOException {
        return downloadInFile(url, new File(dir), cookies, null);
    }

    public File downloadInFile(String url, File dir) throws IOException {
        return downloadInFile(url, dir, null);
    }

    public File downloadInFile(String url, String dir) throws IOException {
        return downloadInFile(url, new File(dir), null);
    }

    // 解析 html 文档
    public Document parseHtml(String html) {
        return Jsoup.parse(html);
    }

    // 从响应中获取字符串内容。此方法处理了响应如果是压缩格式的内容。
    public String response2String(Response response) throws IOException {
        String contentEncoding = response.header("Content-Encoding");
        String charset = null;

        // 尝试从header中获取到编码方式
        String contentType = response.header("Content-Type");
        if (!isEmpty(contentType)) {
            final String CS = "charset=";
            final String CS2 = "CharSet=";
            if (contentType.contains(CS) || contentType.contains(CS2)) {
                charset = contentType.substring(contentType.indexOf(CS) + 8);
            }
        }
        if (isEmpty(charset)) charset = spiderConfig.responseCharset; // spiderConfig.responseCharset; // 默认编码使用UTF-8

        ResponseBody body = response.body();
        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            // 内容压缩了，需要进行解压。
            InputStream inputStream = body.byteStream();

            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

            return reader2String(new BufferedReader(new InputStreamReader(gzipInputStream, charset)));

        } else {
            // 没有压缩，直接获取。
            return body.string();
        }
    }

    // 返回内部处理了的请求头部分。
    private Map<String, String> getInnerHeader(HttpUrl httpUrl) {
        String host = httpUrl.host();

        Map<String, String> innerHeaders = new HashMap<>();
        innerHeaders.put("Accept", spiderConfig.accept);
        innerHeaders.put("Accept-Encoding", spiderConfig.acceptEncoding);
        innerHeaders.put("Accept-Language", spiderConfig.acceptLanguage);
        innerHeaders.put("User-Agent", spiderConfig.userAgent);
        innerHeaders.put("Connection", spiderConfig.connection);
        innerHeaders.put("Host", host);

        return innerHeaders;
    }

    // 判断 cookie 在 集合中哪个位置。
    private int indexOfCookie(List<Cookie> list, Cookie cookie) {
        for (int i = 0; i < list.size(); i++) {
            Cookie cookie1 = list.get(i);
            if (cookie1.name().equalsIgnoreCase(cookie.name())) {
                return i;
            }
        }
        return -1;
    }

    // 将cookie转换成能提交的格式。
    private String cookies2Str(List<Cookie> cookies) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cookie c : cookies) {
            stringBuilder.append(c.name()).append("=").append(c.value()).append(";");
        }
        return stringBuilder.toString();
    }

    // 返回当前适用的cookie处理器。
    private CookieHandle getCookieHandle(String host) {
        CookieHandle cookieHandle = cookieHandleMap.get(host);
        if (cookieHandle == null) {
            cookieHandle = new CookieHandle(host);
            cookieHandleMap.put(host, cookieHandle);
        }
        return cookieHandle;
    }

    // 创建一个新的默认配置。
    private static SpiderConfig newConfig() {
        SpiderConfig spdf = new SpiderConfig();

        spdf.responseCharset = "UTF-8";
        spdf.accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
        spdf.acceptEncoding = "gzip, deflate";
        spdf.acceptLanguage = "zh-CN,zh;q=0.9,en;q=0.8";
        spdf.connection = "keep-alive";
        spdf.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36";

        return spdf;
    }

    // 工具方法 - 将 map 集合转换为 www-url-form-encoded 格式的字符串。
    private static String map2wwwUrlFormEncode(Map<String, ?> params, String charSet) {
        if (params == null || params.size() < 1) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        Set<? extends Map.Entry<String, ?>> entries = params.entrySet();
        for (Map.Entry<String, ?> entry : entries) {
            String value = null;
            try {
                value = URLEncoder.encode(entry.getValue().toString(), charSet);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append(entry.getKey()).append("=").append(value).append("&");
        }
        return stringBuilder.toString();
    }

    // 工具方法 - 将 map 集合转换为 www-url-form-encoded 格式的字符串。
    private static String map2wwwUrlFormEncode(Map<String, ?> params) {
        return map2wwwUrlFormEncode(params, "UTF-8");
    }

    // 工具方法 - 从reader中读出字符串。
    private static String reader2String(Reader reader) throws IOException {
        char[] data = new char[512];
        int dataSize = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((dataSize = reader.read(data)) != -1) {
            stringBuilder.append(data, 0, dataSize);
        }
        reader.close();
        return stringBuilder.toString();
    }

    // 工具方法 - 判断是否为空
    private static boolean isEmpty(Object v) {
        if (v == null) return true;
        if (v instanceof String) {
            return ((String) v).length() == 0;
        } else if (v instanceof Collection) {
            return ((Collection) v).isEmpty();
        } else if (v instanceof Map) {
            return ((Map) v).isEmpty();
        } else {
            return false;
        }
    }

    // 工具方法 - 将字符串第一个字符大写。
    private static String uperFirst(String v) {
        if (isEmpty(v)) return v;
        char c = v.charAt(0);
        c = Character.toUpperCase(c);
        if (v.length() == 1) {
            return Character.toString(c);
        }
        return c + v.substring(1);
    }

    // 工具方法 - 将字符串保存到文件。
    private void string2file(String str, File f) {
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        BufferedWriter writer = null;

        try {
            FileOutputStream out = new FileOutputStream(f);
            writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            char[] data = new char[1024];
            int dataSize = 0;

            StringReader reader = new StringReader(str);
            while ((dataSize = reader.read(data)) != -1) {
                writer.write(data, 0, dataSize);
            }

            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 工具方法 - 读取文件内容返回。
    private static String file2String(File f) {
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        BufferedReader reader = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            return reader2String(reader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    // 爬虫类配置文件。
    public static class SpiderConfig {
        public String userAgent;
        public String accept;
        public String acceptEncoding;
        public String acceptLanguage;
        public String connection;
        public String responseCharset;
    }

    // 处理 cookie 的类。
    private class CookieHandle {
        private File CookieDir = new File("./cookies/");

        // 本cookie处理器对应的host
        private String host;
        private List<Cookie> cookies;


        private CookieHandle(String host) {
            this.host = host;
            cookies = readCookieFromFile();
        }

        // 根据某 url， 获取适用于此url的所有cookie。
        List<Cookie> getCookies(HttpUrl httpUrl) {
            List<Cookie> cookieList = new ArrayList<>();
            for (Cookie cookie : cookies) {
                if (cookie.matches(httpUrl)) {
                    if (cookie.expiresAt() > System.currentTimeMillis()) {
                        cookieList.add(cookie);
                    }
                }
            }
            return cookieList;
        }

        // 保存返回的所有cookie
        void handleCookies(List<Cookie> cookieList) {
            if (isEmpty(cookieList)) return;
            logDebug("保存cookies：" + cookies2Str(cookieList));
            for (Cookie c : cookieList) {
                int i = indexOfCookie(this.cookies, c);
                if (i == -1) {
                    this.cookies.add(c);
                } else {
                    this.cookies.set(i, c);
                }
            }
        }

        // 将cookie保存到文件。
        private void saveCookie2File() {
            File cookieFile = new File(CookieDir, host.replace(":", ".") + ".cookie");
            JSONArray jsonArray = new JSONArray();
            for (Cookie c : cookies) {
                JSONObject obj = new JSONObject();
                obj.put("path", c.path());
                obj.put("expiresAt", c.expiresAt());
                obj.put("persistent", c.persistent());
                obj.put("httpOnly", c.httpOnly());
                obj.put("hostOnly", c.hostOnly());
                obj.put("hashCode", c.hashCode());
                obj.put("domain", c.domain());
                obj.put("value", c.value());
                obj.put("name", c.name());
                obj.put("secure", c.secure());
                jsonArray.add(obj);
            }
            string2file(jsonArray.toJSONString(), cookieFile);
        }

        // 从文件读取cookie
        private List<Cookie> readCookieFromFile() {
            File cookieFile = new File(CookieDir, host.replace(":", ".") + ".cookie");
            if (!cookieFile.exists()) {
                return new ArrayList<>();
            } else {
                String jsonStr = file2String(cookieFile);
                JSONArray objects = JSON.parseArray(jsonStr);
                List<Cookie> cookieList = new ArrayList<>();
                for (int i = 0; i < objects.size(); i++) {
                    JSONObject obj = objects.getJSONObject(i);

                    boolean secure = obj.getBooleanValue("secure");
                    boolean hostOnly = obj.getBooleanValue("hostOnly");
                    boolean httpOnly = obj.getBooleanValue("httpOnly");
                    String domain = obj.getString("domain");
                    String path = obj.getString("path");
                    String name = obj.getString("name");
                    String value = obj.getString("value");
                    long expiresAt = obj.getLongValue("expiresAt");


                    Cookie.Builder builder = new Cookie.Builder();
                    if (secure) builder.secure();
                    if (hostOnly) builder.hostOnlyDomain(domain);
                    if (httpOnly) builder.httpOnly();
                    builder.expiresAt(expiresAt);
                    builder.path(path);
                    builder.domain(domain);
                    builder.value(value);
                    builder.name(name);

                    cookieList.add(builder.build());
                }
                return cookieList;
            }

        }
    }

    // 修复重定向时不会将cookie一起提交的问题。
    private class MyCookieFixInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl url = request.url();
            // System.out.println("打开地址：" + url.toString());

            // 每次请求都将cookie带上。
            CookieHandle cookieHandle = getCookieHandle(url.host());
            List<Cookie> cookies = cookieHandle.getCookies(url);
            String cookieStr = cookies2Str(cookies);
            Request newRequest = request.newBuilder()
                    .addHeader("Cookie", cookieStr)
                    .header("Host", url.host())
                    .build();
            // Headers headers = newRequest.headers();
            // Set<String> names = headers.names();
            // System.out.println("提交请求头：");
            // for (String name: names) {
            //     System.out.println(name+":" + headers.values(name));
            // }
            // System.out.println(1);
            return chain.proceed(newRequest);
        }
    }

    // 请求事件监听器。
    private class MyEventListener extends EventListener {
        @Override
        public void responseHeadersEnd(Call call, Response response) {
            // 保存请求返回的cookie什么的
            // System.out.println("responseHeadersEnd");
            HttpUrl url = response.request().url();
            Headers headers = response.headers();
            // System.out.println("返回头：");
            // for (String name: headers.names()                 ) {
            //     System.out.println(name + ":" + headers.values(name));
            // }
            List<Cookie> cookieList = Cookie.parseAll(url, headers);
            CookieHandle cookieHandle = getCookieHandle(url.host());
            cookieHandle.handleCookies(cookieList);
        }

        @Override
        public void callEnd(Call call) {
            // 每次请求完成，保存一次cookie到文件。
            String host = call.request().url().host();
            CookieHandle cookieHandle = getCookieHandle(host);
            cookieHandle.saveCookie2File();
        }
    }
}
