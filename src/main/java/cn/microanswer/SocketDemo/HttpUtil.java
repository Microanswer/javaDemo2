package cn.microanswer.SocketDemo;


/**
 * 此类依赖 fastjson 和 okhttp3 请做好依赖配置。
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
    private static String CHARSET = "UTF-8";
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json");
    public static final MediaType APPLICATION_XML = MediaType.parse("application/xml");
    public static final MediaType X_WWW_FORM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded");

    private static OkHttpClient __httpClient;

    private static OkHttpClient getHttpClient() {
        if (__httpClient == null) {
            __httpClient = new OkHttpClient.Builder()
                    .build();
        }
        return __httpClient;
    }

    /**
     * 使用 get方法 请求某地址。
     *
     * @param url     [Y] 要请求的地址
     * @param params  [N] 请求参数
     * @param headers [N] 请求头
     * @return
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        String queryString = map2wwwUrlFormEncode(params);
        if (queryString.length() > 0) {
            if (url.contains("?")) {
                url = url + "&" + queryString;
            } else {
                url = url + "?" + queryString;
            }
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        builder.get();
        Request request = builder.build(); // 建立请求数据。
        Call call = getHttpClient().newCall(request); // 使用请求数据建立请求。
        Response response = call.execute(); // 发起请求。
        if (response.code() != 200) {
            throw new Exception("get error:" + url + " [" + response.code() + "] " + response.message());
        }
        return response.body().string();
    }

    /**
     * 使用 get方法 请求某地址。
     *
     * @param url    [Y] 要请求的地址。
     * @param params [N] 请参数。
     * @return 请求结果。
     * @throws Exception 错误。
     */
    public static String get(String url, Map<String, String> params) throws Exception {
        return get(url, params, null);
    }

    /**
     * 使用 get 方法请求某地址
     *
     * @param url [Y] 请求地址。
     * @return 结果
     * @throws Exception 错误
     */
    public static String get(String url) throws Exception {
        return get(url, null);
    }

    /**
     * 使用 POST方法 请求某url
     *
     * @param url       [Y] 要请求的地址
     * @param bodyBytes [N] 数据体
     * @param headers   [N] 请求头
     * @return 结果
     * @throws Exception 错误
     */
    public static String post(String url, final MediaType contentType, final byte[] bodyBytes, Map<String, String> headers) throws Exception {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        builder.post(RequestBody.create(contentType, bodyBytes));
        Request request = builder.build(); // 建立请求数据。
        OkHttpClient httpClient = getHttpClient();
        Call call = httpClient.newCall(request); // 使用请求数据建立请求。
        Response response = call.execute();
        if (response.code() != 200) {
            throw new Exception("post error:" + url + " [" + response.code() + "] " + response.message());
        }
        return response.body().string();
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url     [Y] 要请求的 url
     * @param params  [N] 请求参数
     * @param headers [N] 请求头
     * @return 结果
     * @throws Exception 错误
     */
    public static String postFormUrlEncode(String url, Map<String, ?> params, Map<String, String> headers) throws Exception {
        return post(url, X_WWW_FORM_URLENCODED, map2wwwUrlFormEncode(params).getBytes(CHARSET), headers);
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url    [Y] 要请求的 url
     * @param params [N] 请求参数
     * @return 结果
     * @throws Exception 错误
     */
    public static String postFormUrlEncode(String url, Map<String, ?> params) throws Exception {
        return postFormUrlEncode(url, params, null);
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url     [Y] 要请求的 url
     * @param params  [N] 请求参数
     * @param headers [N] 请求头
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationJson(String url, Map<String, ?> params, Map<String, String> headers) throws Exception {
        return postApplicationJson(url, JSON.toJSONString(params), headers);
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url    [Y] 要请求的 url
     * @param params [N] 请求参数
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationJson(String url, Map<String, ?> params) throws Exception {
        return postApplicationJson(url, params, null);
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url        [Y] 要请求的 url
     * @param jsonObject [N] 请求参数
     * @param headers    [N] 请求头
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationJson(String url, JSONObject jsonObject, Map<String, String> headers) throws Exception {
        return postApplicationJson(url, jsonObject.toJSONString(), headers);
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url        [Y] 要请求的 url
     * @param jsonObject [N] 请求参数
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationJson(String url, JSONObject jsonObject) throws Exception {
        return postApplicationJson(url, jsonObject.toJSONString(), null);
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url     [Y] 要请求的 url
     * @param jsonStr [N] 请求参数
     * @param headers [N] 请求头
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationJson(String url, String jsonStr, Map<String, String> headers) throws Exception {
        return post(url, APPLICATION_JSON, jsonStr.getBytes(CHARSET), headers);
    }

    /**
     * 使用 post方法 请求某url
     *
     * @param url     [Y] 要请求的 url
     * @param jsonStr [N] 请求参数
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationJson(String url, String jsonStr) throws Exception {
        return postApplicationJson(url, jsonStr, null);
    }

    /**
     * 使用 post方法 请求某 url
     *
     * @param url     [Y] 要请求的 url
     * @param xmlStr  [N] xml字符串
     * @param headers [N] 请求头
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationXml(String url, String xmlStr, Map<String, String> headers) throws Exception {
        return post(url, APPLICATION_XML, xmlStr.getBytes(CHARSET), headers);
    }

    /**
     * 使用 post方法 请求某 url
     *
     * @param url    [Y] 要请求的 url
     * @param xmlStr [N] xml字符串
     * @return 结果
     * @throws Exception 错误
     */
    public static String postApplicationXml(String url, String xmlStr) throws Exception {
        return postApplicationXml(url, xmlStr, null);
    }

    /**
     * 上传文件到指定的 url.
     * 此方法将使用
     *
     * @param url     [Y] 地址
     * @param params  [N] 参数
     * @param headers [N] 参数
     * @param files   [N] 文件
     * @return
     * @throws Exception
     */
    public static String upload(String url, Map<String, String> params, Map<String, String> headers, File... files) throws Exception {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        final MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        if (params != null && params.size() > 0) {
            mapForEach(params, new Fun<String, String>() {
                @Override
                public void d0fun(String key, String value) {
                    multipartBodyBuilder.addFormDataPart(key, value);
                }
            });
        }

        if (files != null && files.length > 0) {
            int index = 0;
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(
                        "file" + (index > 0 ? index : ""),
                        file.getName(),
                        RequestBody.create(MediaType.parse("*/*"), file)
                );
                index = index + 1;
            }
        }

        builder.post(multipartBodyBuilder.build());
        Request request = builder.build(); // 建立请求数据。
        Call call = getHttpClient().newCall(request); // 使用请求数据建立请求。
        Response response = call.execute();
        if (response.code() != 200) {
            throw new Exception("upload error:" + url + " [" + response.code() + "] " + response.message());
        }
        return response.body().string();
    }

    /**
     * 上传文件到指定的 url.
     * 此方法将使用
     *
     * @param url    [Y] 地址
     * @param params [N] 参数
     * @param files  [N] 文件
     * @return
     * @throws Exception
     */
    public static String upload(String url, Map<String, String> params, File... files) throws Exception {
        return upload(url, params, null, files);
    }

    /**
     * 上传文件到指定的 url.
     * 此方法将使用
     *
     * @param url   [Y] 地址
     * @param files [N] 文件
     * @return
     * @throws Exception
     */
    public static String upload(String url, File... files) throws Exception {
        return upload(url, null, files);
    }

    /**
     * 下载文件.如果指定的文件已存在，将会覆盖。
     *
     * @param url       [Y] 下载路径
     * @param params    [N] 参数
     * @param headers   [N] 请求头
     * @param fileOrDir [N] 下载目录或直接对应文件。
     * @return
     */
    public static File download(String url, Map<String, String> params, Map<String, String> headers, File fileOrDir) throws Exception {
        String queryString = map2wwwUrlFormEncode(params);
        if (queryString.length() > 0) {
            if (url.contains("?")) {
                url = url + "&" + queryString;
            } else {
                url = url + "?" + queryString;
            }
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        builder.get();
        Request request = builder.build(); // 建立请求数据。
        Call call = getHttpClient().newCall(request); // 使用请求数据建立请求。
        Response response = call.execute(); // 发起请求。
        if (response.code() != 200) {
            throw new Exception("download error:" + url + " [" + response.code() + "] " + response.message());
        }

        if (fileOrDir == null || fileOrDir.isDirectory()) {

            // 处理要保存的文件路径。
            String fileName = "";

            // 从响应头获取一个文件名：
            String contentdisposition = response.header("Content-Disposition");
            if (contentdisposition != null) {
                int filenameIndex = contentdisposition.indexOf("filename");
                if (filenameIndex > -1) {
                    fileName = contentdisposition.substring(filenameIndex + 10, contentdisposition.length() - 1);
                }
            }

            // 没有获取到文件名则从url中解析文件名。
            if (fileName.length() < 1) {
                // 从 url 检索一个文件名：
                String[] split = url.split("\\?")[0].split("/");
                fileName = split[split.length - 1];
            }

            // 依然没有文件名，只有使用时间戳来命名了。
            if (fileName.length() < 1) {
                fileName = System.currentTimeMillis() + ".httputildownload";
            }

            if (fileOrDir == null) {
                // 如果没有指定，那么将文件保存到临时目录
                File tempDir = new File(System.getProperty("java.io.tmpdir"));
                fileOrDir = new File(tempDir, fileName);
            } else {
                // 指定的是目录
                fileOrDir = new File(fileOrDir, fileName);
            }
        }

        // 开始下载
        InputStream inputStream = response.body().byteStream(); // 下载输入流获取到。

        FileOutputStream outputStream = new FileOutputStream(fileOrDir);
        BufferedOutputStream bfo = new BufferedOutputStream(outputStream);

        byte[] data = new byte[1024];
        int dataSize;

        while ((dataSize = inputStream.read(data)) != -1) {
            bfo.write(data, 0, dataSize);
        }

        bfo.flush();
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return fileOrDir;
    }

    /**
     * 将 url 的文件下载到 fileOrDir 指定的路径。如果指定的文件已存在，将会覆盖。
     *
     * @param url       [Y] url下载路径
     * @param fileOrDir [N] 目录或文件保存路径，如果不传，将保存在零时目录
     * @return
     */
    public static File download(String url, File fileOrDir) throws Exception {
        return download(url, null, null, fileOrDir);
    }

    /**
     * 将 url 的文件下载到 fileOrDir 指定的路径。如果指定的文件已存在，将会覆盖。
     *
     * @param url           [Y] url下载路径
     * @param fileOrDirPath [N] 目录或文件保存路径，如果不传，将保存在零时目录
     * @return
     */
    public static File download(String url, String fileOrDirPath) throws Exception {
        return download(url, new File(fileOrDirPath));
    }

    /**
     * 下载 url 对应的文件。 文件将被保存到零时文件夹。如果指定的文件已存在，将会覆盖。
     *
     * @param url [Y] 下载路径
     * @return
     * @throws Exception
     */
    public static File download(String url) throws Exception {
        return download(url, (File) null);
    }

    /**
     * 将 map 集合转换为 www-url-form-encoded 格式的字符串。
     *
     * @param params  [N] 要转换的集合map
     * @param charSet [Y] 编码方式
     * @return 结果
     * @throws Exception
     */
    public static String map2wwwUrlFormEncode(Map<String, ?> params, String charSet) throws Exception {
        if (params == null || params.size() < 1) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        Set<? extends Map.Entry<String, ?>> entries = params.entrySet();
        for (Map.Entry<String, ?> entry : entries) {
            String value = URLEncoder.encode(entry.getValue().toString(), charSet);
            stringBuilder.append(entry.getKey()).append("=").append(value).append("&");
        }
        return stringBuilder.toString();
    }

    /**
     * 将 map 集合转换为 www-url-form-encoded 格式的字符串。。 默认使用 UTF-8 编码
     *
     * @param params [N] 要转换的集合map
     * @return 结果
     * @throws Exception
     */
    public static String map2wwwUrlFormEncode(Map<String, ?> params) throws Exception {
        return map2wwwUrlFormEncode(params, CHARSET);
    }

    private static void mapForEach(Map<String, String> map, Fun<String, String> fun) throws Exception {
        if (map == null || map.size() <= 0 || fun == null) {
            return;
        }
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            fun.d0fun(entry.getKey(), entry.getValue());
        }
    }

    // 持有2个泛型定义和一个方法的接口
    private interface Fun<A, B> {
        void d0fun(A a, B b) throws Exception;
    }
}
