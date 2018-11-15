package cn.microanswer;


import java.net.HttpURLConnection;
import java.net.URL;

public class Test27 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月13日 11:53:38
     */
    public static void main(String[] args) throws Exception {

        String[][] urls = new String[][] {
                {"12seba" , "71eee" },
                {"63jjj"  , "61cao" },
                {"kkxxee" , "ee216" },
                {"4444kk" , "hhh52" },
                {"999abcd", "22213" },
                {"77soso" , "567pao"},
                {"5c5c5c" , "19ggg" },
                {"007pipi", "97bobo"},
                {"sebo333", "sss17" },
                {"22qqk"  , "677x"  },
                {"999258" , "42iii" },
                {"rb444"  , "34ccc" },
                {"2bbuu"  , "uy666" },
                {"2oohh"  , "93kx2" },
                {"49vv"   , "9492"  },
                {"oookkk" , "75bo"  },
                {"ccc90"  , "41xxx" },
                {""       , "22213" },
        };

        for (int i = 0; i < urls.length; i++) {
            for (int j = 0; j < urls[i].length; j++) {
                String url = urls[i][j];
                url = "http://www." + url + ".com";
                System.out.print(String.format("%25s %3s", url , (canUse(url) ? "no" : "yes")));
                System.out.print(",  ");
            }
            System.out.println();
        }
    }

    private static boolean canUse(String url){
        if (url == null || url.length() == 1) {
            return false;
        }
        try {


            URL u = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setConnectTimeout(2000);
            int responseCode = urlConnection.getResponseCode();
            urlConnection.disconnect();
            return responseCode == 200;
        }catch (Exception ignored){}
        return false;
    }
}
