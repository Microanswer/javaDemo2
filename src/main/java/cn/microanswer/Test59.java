package cn.microanswer;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Test59 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2019年12月25日 11:38:37
     */
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(1234);

        while (!socketChannel.connect(address)){
            System.out.println("连接服务器中...");
        }
        System.out.println("连接服务器成功...");

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        socketChannel.read(byteBuffer);
        byteBuffer.rewind();

        byte[] array = byteBuffer.array();
        System.out.println("收到消息"+new String(array, UTF_8));
        socketChannel.close();
    }
}
