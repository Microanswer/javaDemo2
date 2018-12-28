package cn.microanswer;


import java.net.Socket;

public class Test39 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月11日 15:04:37
     */
    public static void main(String[] args) throws Exception {

        Socket sc = new Socket("127.0.0.1", 6666);
        MySocket mySocket = new MySocket(sc, "客户端");
        mySocket.start();

        Thread.sleep(2000);
        while (true) {
            mySocket.send("我的天哪 123456654987 1223可是东方红电视剧版 vaksjdhsekhfksef...");
            // Thread.sleep(1000);
        }
        
    }
}
