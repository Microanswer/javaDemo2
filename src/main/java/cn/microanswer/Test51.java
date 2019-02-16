package cn.microanswer;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Test51 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年01月21日 14:58:56
     */
    public static void main(String[] args) throws Exception {


        File file = new File("C:\\Users\\Micro\\Desktop\\ts.ts");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        String urlM = "https://dadi-yun.com/20190214/193_718e53b8/800k/hls/d5ad394178d000";

        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        for (int i = 0; i <= 516; i++) {

            String index = String.valueOf(i);
            if (i < 10) {
                index = "00" + index;
            } else if (i < 100) {
                index = "0" + index;
            }

            String url = urlM +  index + ".ts";

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.get();
            Request request = builder.build(); // 建立请求数据。
            Call call = httpClient.newCall(request); // 使用请求数据建立请求。
            Response response = call.execute(); // 发起请求。

            InputStream inputStream = response.body().byteStream();

            int size = 0;
            byte[] data = new byte[1024];

            while ((size = inputStream.read(data))!= -1) {
                fileOutputStream.write(data, 0, size);
            }
            fileOutputStream.flush();
            inputStream.close();

            System.out.println(url + " success.");

        }
    }

}