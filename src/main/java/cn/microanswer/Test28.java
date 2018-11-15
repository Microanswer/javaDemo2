package cn.microanswer;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test28 {

    static String map2String(Map<String, String> map) {
        Set<Map.Entry<String, String>> entries = map.entrySet();
        StringBuilder resBuilder = new StringBuilder("<data>");
        for (Map.Entry<String, String> en : entries) {
            resBuilder.append("<").append(en.getKey()).append(">");
            resBuilder.append(en.getValue());
            resBuilder.append("</").append(en.getKey()).append(">");
        }
        return resBuilder.append("</data>").toString();
    }

    public static void main(String[] args) {

        Map<String, String> data = new HashMap<>();
        data.put("name", "哈哈");
        data.put("id", "123");
        System.out.println(map2String(data));
    }
}
