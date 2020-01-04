package cn.microanswer;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Test58 {

    static ArrayList<SocketChannel> clients = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        ServerSocketChannel sskChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = sskChannel.socket();
        serverSocket.bind(new InetSocketAddress(8080));
        sskChannel.configureBlocking(false);

        while (true) {
            Thread.sleep(1000);

            SocketChannel accept = sskChannel.accept();
            if (null != accept) {
                System.out.println("客户端来了。。。");
                clients.add(accept);

                // 下发成功提示
                String hello = "你好，客户端。";
                accept.write(ByteBuffer.wrap(hello.getBytes(StandardCharsets.UTF_8)));
                System.out.println("提示消息已发送");
            }
        }

    }
}
