package cn.microanswer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Test52 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年02月18日 16:56:24
     */
    public static void main(String[] args) throws Exception {

        File file = new File("C:\\Users\\Microanswer\\Desktop\\新华字典.json");

        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);

        BufferedReader reader1 = new BufferedReader(reader);

        StringBuilder stringBuilder = new StringBuilder();

        char[] chats = new char[1024];
        int size;

        while ((size = reader1.read(chats)) != -1) {
            stringBuilder.append(chats, 0, size);
        }

        reader1.close();

        String result = stringBuilder.toString();

        JSONArray jsonArray = JSON.parseArray(result);
        int size1 = jsonArray.size();

        int index = 0;
        for (int i = 0; i < size1; i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            String more = o.getString("more");
            if (more.contains("\n姓。")) {
                index++;
                System.out.print(o.getString("word") + " ");
                if (index % 10 == 0) {
                    System.out.println();
                }
            }
        }
        System.out.println();
        System.out.println("共：" + index + "个姓。");
    }
}
