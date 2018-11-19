package cn.microanswer.socketdemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 客户端管理类
 */
public class Client extends Thread {

    // 保存套接字引用。
    private Socket socket;

    // 来自 socket 连接成功后获取到的输出流。
    private BufferedOutputStream outputStream;

    // 信息状态监听器。
    private ClientListener clientListener;

    private String clientNamme; // 客户名称。
    private String clientId; // 客户唯一标识。
    private String host; // 服务机地址。
    private int port; // 服务机端口。
    private boolean isInServer; // 标记此client是服务端持有的还是客户端持有的。

    /**
     * 使用提供的主机地址和端口号初始化一个客户端。
     *
     * @param host 主机地址
     * @param port 端口号
     */
    Client(String host, int port) {
        this.host = host;
        this.port = port;
        isInServer = false;
    }

    /**
     * 使用 已有的套接字初始化一个客户端。 此构造函数通常用于服务端accept到一个客户连接时使用。
     *
     * @param socket 套接字客户端。
     */
    Client(Socket socket) {
        this.socket = socket;
        InetAddress address = this.socket.getInetAddress();
        host = address.getHostName();
        port = socket.getPort();
        isInServer = true;
    }

    /**
     * 设置客户端监听器。
     *
     * @param clientListener 监听器
     */
    public final void setClientListener(ClientListener clientListener) {
        this.clientListener = clientListener;
    }

    @Override
    public final void run() {
        super.run();

        // 如果socket 为空， 则是通过 主机地址 和 端口号 进行初始化的，此时进行连接对应主机操作。
        if (socket == null) {
            try {
                socket = new Socket(host, port);
            } catch (Exception e) {
                // 调起错误监听
                onError(e);
                // 构建套接字的时候就出错了，以后也没法继续运行。调起生命周期结束方法。
                onDisConn();
                return;
            }
        }

        // 创建输入流引用。
        InputStream inputStream = null;

        try {
            // 获取输出流
            outputStream = new BufferedOutputStream(socket.getOutputStream());

            // 调起监听连接成功监听。
            // （可能有在 onConneted 方法中向服务端发出消息的需求，所以，此方法的调起前要初始化好输出流。）
            onConneted(host, port);

            // 循环读取输入流，获取消息信息。
            inputStream = new BufferedInputStream(socket.getInputStream());
            while (socket.isConnected() && !socket.isClosed() && !socket.isOutputShutdown()) {
                try {
                    onMsg(new Msg(inputStream));
                } catch (Exception e) {
                    String message = e.getMessage();

                    if (e instanceof EOFException) {
                        e = new Exception("Socket closed");
                        message = e.getMessage();
                    }

                    if ("socket closed".equals(message.toLowerCase())) {
                        break;
                    }

                    Exception e2 = new Exception(message);
                    onError(e2);
                }
            }
        } catch (Exception e) {
            onError(e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            onDisConn();
        }
    }

    public String getClientNamme() {
        return clientNamme;
    }

    public String getClientId() {
        return clientId;
    }

    void onError(Exception e) {
        if (clientListener != null) clientListener.onError(this, e);
    }

    void onDisConn() {
        System.out.println("客户端关闭");
        if (clientListener != null) clientListener.onDisConn(this);
    }

    void onConneted(String host, int port) {
        if (clientListener != null) {
            clientListener.onConneted(this, host, port);
        }
    }

    void onNamed() {
        if (clientListener != null) {
            clientListener.onNamed(this, clientNamme, clientId);
        }
    }

    void onMsg(Msg msg) {

        int msgType = msg.getMsgHead().getMsgType();

        // 接收到初始化客户端id和名称的消息。该消息为底层消息。不暴露给外部。
        if (msgType == MsgHead.TYPE_SYSTEM_SETNAME_ID) {
            String text = msg.getText();
            JSONObject object = JSON.parseObject(text);
            if (!Utils.isStringEmpty(object.getString("name"))) {
                clientNamme = object.getString("name");
            }
            if (!Utils.isStringEmpty(object.getString("id"))) {
                clientId = object.getString("id");
            }
            onNamed();
            return;
        }

        if (clientListener != null && !socket.isClosed()) {
            clientListener.onMsg(this, msg);
        }
    }

    /**
     * 断开链接
     */
    void disConn() {
        if (isInServer) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 向服务器发送下线申请。服务器断开该客户，而不是客户主动断开。
            sendMsg(Client.CreateSystemMsg(MsgHead.TYPE_SYSTEM_REQUEST_EXIT, clientId));
        }
    }

    final void sendMsg(Msg msg) {
        sendMsg(msg, null);
    }

    final void sendMsg(Msg msg, SendListener sendListener) {
        Task.TaskHelper.getInstance().run(new Task.ITask<Msg, Msg>() {
            @Override
            public Msg getParam() {
                return msg;
            }

            @Override
            public Msg run(Msg param) {
                try {
                    msg.write(outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return param;
            }

            @Override
            public void afterRun(Msg value) {
                super.afterRun(value);
                if (sendListener != null) sendListener.onEnd(value);

            }
        });
    }

    void colse() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClientNameId(String name, String id) {

        // 发起更名请求， 服务端接收到后会更新服务端的数据。客户端接收到会更新客户端信息。
        JSONObject data = new JSONObject();
        if (!Utils.isStringEmpty(id)) {
            data.put("id", id);
            this.clientId = id;
        }
        if (!Utils.isStringEmpty(name)) {
            data.put("name", name);
            this.clientNamme = name;
        }
        sendMsg(Client.CreateSystemMsg(MsgHead.TYPE_SYSTEM_SETNAME_ID, id, data.toJSONString()));
    }

    interface ClientListener {
        void onConneted(Client client, String host, int port);

        /**
         * 只有客户端的 client ，此回调会调用。
         *
         * @param clientName 客户端名称
         * @param clientId   客户端id
         */
        void onNamed(Client client, String clientName, String clientId);

        void onDisConn(Client client);

        void onError(Client client, Exception e);

        void onMsg(Client client, Msg msg);
    }

    interface SendListener {
        // 消息发送完成回调。
        void onEnd(Msg msg);
    }

    public static Msg CreateSystemMsg(int type, String fromClientId) {
        return CreateSystemMsg(type, fromClientId, String.valueOf(type));
    }

    public static Msg CreateSystemMsg(int type, String fromClientId, String value) {
        try {
            return new Msg(type, fromClientId, "", "", "", value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

