package cn.microanswer;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Test38 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年12月04日 16:58:39
     */
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(6666);

        while (true) {
            Socket accept = serverSocket.accept();
            MySocket mySocket = new MySocket(accept, "服务器");
            mySocket.start();
            Thread.sleep(2000);
            // while (true) {
            //     mySocket.send(" ... 123654as6d54w65ef112434534特付款嫁女记活动标识...");
            // }
        }

    }


}

class MySocket extends Thread {
    private Socket socket;
    private OutputStream outputStream;
    private String name;

    public MySocket(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        super.run();
        try {
            outputStream = socket.getOutputStream();

            InputStream inputStream = socket.getInputStream();

            while (true) {
                byte datas[] = new byte[1024];
                int length = inputStream.read(datas);
                System.out.println("读取到数据：" + length);
                String s = new String(datas, "UTF-8");
                System.out.println(name + " 收到消息：" + s);

                if (length != 1024) {
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    public void send(String msg) {
        try {
            byte datas[] = new byte[1024];
            byte[] bytes = msg.getBytes("UTF-8");
            System.arraycopy(bytes, 0, datas, 0, bytes.length);

            outputStream.write(datas);
            outputStream.write("我去。。。".getBytes("UTF-8"));
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
