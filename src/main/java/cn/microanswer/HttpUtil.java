package cn.microanswer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okio.BufferedSink;

import java.io.IOException;
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
            __httpClient = new OkHttpClient
                    .Builder()
                    .build();
        }
        return __httpClient;
    }

    /**
     * 使用 get方法 请求某地址。
     *
     * @param url     [Y] 要请求的地址。
     * @param headers [N] 请求头。
     * @return 请求结果。
     * @throws Exception 错误。
     */
    public static String get(String url, Map<String, String> headers) throws Exception {
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
        if (response.code() != 200 || !"OK".equals(response.message())) {
            throw new Exception(url + " [" + response.code() + "] " + response.message());
        }
        return response.body().string();
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

        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(bodyBytes);
            }
        };

        builder.post(body);
        Request request = builder.build(); // 建立请求数据。
        Call call = getHttpClient().newCall(request); // 使用请求数据建立请求。
        Response response = call.execute();
        if (response.code() != 200 || !"OK".equals(response.message())) {
            throw new Exception(url + " [" + response.code() + "] " + response.message());
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
     * @param params  [N] 要转换的集合map
     * @return 结果
     * @throws Exception
     */
    public static String map2wwwUrlFormEncode(Map<String, ?> params) throws Exception {
        return map2wwwUrlFormEncode(params, CHARSET);
    }
}
