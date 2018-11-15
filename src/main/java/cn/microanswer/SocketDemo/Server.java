package cn.microanswer.SocketDemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 服务管理类
 */
public class Server extends Thread {

    // 服务段套接字
    private ServerSocket serverSocket;

    // 服务端监听器
    private ServerListener serverListener;

    // 保存所有连接的客户端的引用
    private HashMap<String, Client> clients;

    void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    public void run() {
        super.run();
        clients = new HashMap<>();
        try {
            // 新建服务
            serverSocket = new ServerSocket(Constant.SERVER_PORT);
        } catch (Exception e) {
            e.printStackTrace();
            // 新建服务就报错，多半都是端口占用。
            onError(e);
            onServerFinlish();
            return;
        }


        try {

            onServerReady();

            while (!serverSocket.isClosed()) {
                try {
                    Socket accept = serverSocket.accept();
                    Client client = new Client(accept);
                    client.setClientListener(clientListener);
                    client.start(); // 保持对该客户端的活跃状态

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        } finally {

            // 跳出循环的时候，也就是关闭服务的时候。

            // 将所有客户端断开连接。
            for (Map.Entry<String, Client> entry : clients.entrySet()) {
                entry.getValue().colse();
            }

            onServerFinlish();
        }
    }

    String getAddress() {
        return "127.0.0.1";
    }


    int getClientCount() {
        return clients == null ? 0 : clients.size();
    }

    void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String[] getClients() {
        String[] v = new String[clients.size()];
        int i = 0;
        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            v[i] = entry.getValue().getClientNamme();
            i++;
        }
        return v;
    }

    /**
     * 获取到某客户端发送的消息时回调。
     * @param client
     * @param msg
     */
    void onClientMsg(Client client, Msg msg) {
        // 这个范围内的消息，属于系统消息
        int msgType = msg.getMsgHead().getMsgType();
        if (10 < msgType && msgType <= 100) {

            if (msgType == MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER) {
                JSONArray jsonArray = new JSONArray();
                Set<Map.Entry<String, Client>> entries = clients.entrySet();
                for (Map.Entry<String, Client> ent : entries) {
                    JSONObject jo = new JSONObject();
                    Client value = ent.getValue();
                    jo.put("name", value.getClientNamme());
                    jo.put("id", value.getClientId());
                    jsonArray.add(jo);
                }
                client.sendMsg(Client.CreateSystemMsg(MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER, jsonArray.toJSONString()));
            }
            // 这个范围内的消息，属于用户消息
        } else if (0 < msgType && msgType <= 10) {

            // 获取消息的目标客户
            String toName = msg.getMsgHead().getToName();
            Client toClient = clients.get(toName);
            if (toClient != null) {
                // 发送到目标客户
                toClient.sendMsg(msg, null);
            } else {
                // 目标客户端没找到，可能是下线了。
                System.out.println("未找到目标客户");
            }
        } else {
            // 其他未知消息
            System.out.println("未知消息");
        }
    }

    /**
     * 出现错误，此方法回调
     *
     * @param e 出错内容
     */
    void onError(Exception e) {
        if (serverListener != null) serverListener.onError(e);
    }

    /**
     * 某个客户端出现错误
     *
     * @param e      错误
     * @param client 出错的客户端
     */
    void onClientError(Exception e, Client client) {
        if (serverListener != null) serverListener.onClientError(e, client);
    }

    /**
     * 有客户端链接，此方法回调
     *
     * @param client 客户端
     */
    void onClientConnted(Client client) {

        // 当客户端连接上，则下发客户端应该使用的名称
        String name = ("用户" + (clients.size() + 1));
        String id = String.valueOf((clients.size() + 1));
        client.setClientNameId(name, id);

        clients.put(id, client);

        if (serverListener != null) serverListener.onClientConnted(client, name, id);
    }

    /**
     * 有客端更新数据
     *
     * @param client 更新的
     * @param name   客户端新名称
     */
    void onClientUpdate(Client client, String name, String id) {
        if (serverListener != null) serverListener.onClientUpdate(client, name, id);
    }

    /**
     * 客户端断开链接，此方法回调
     *
     * @param client 客户端
     */
    void onClientDisco(Client client) {
        clients.remove(client.getClientId());
        if (serverListener != null) serverListener.onClientDisco(client);
    }

    /**
     * 服务器准备完成回调
     */
    void onServerReady() {
        if (serverListener != null) serverListener.onServerReady();
    }

    /**
     * 服务结束回调
     */
    void onServerFinlish() {
        System.out.println("服务停止");
        if (serverListener != null) serverListener.onServerFinlish();
    }

    private Client.ClientListener clientListener = new Client.ClientListener() {
        @Override
        public void onConneted(Client client, String host, int port) {
            onClientConnted(client);
        }

        @Override
        public void onNamed(Client client, String clientName, String clientId) {
            onClientUpdate(client, clientName, clientId);
        }

        @Override
        public void onDisConn(Client client) {
            onClientDisco(client);
        }

        @Override
        public void onError(Client client, Exception e) {
            onClientError(e, client);
        }

        @Override
        public void onMsg(Client client, Msg msg) {
            onClientMsg(client, msg);
        }
    };

    interface ServerListener {
        /**
         * 出现错误，此方法回调
         *
         * @param e 出错内容
         */
        void onError(Exception e);

        /**
         * 某个客户端出现错误
         *
         * @param e      错误
         * @param client 出错的客户端
         */
        void onClientError(Exception e, Client client);

        /**
         * 有客户端链接，此方法回调
         *
         * @param client 客户端
         * @param name   名字
         * @param id     唯一标识
         */
        void onClientConnted(Client client, String name, String id);

        /**
         * 有客端更新数据
         *
         * @param client 更新的
         * @param name   客户端新名称
         */
        void onClientUpdate(Client client, String name, String id);

        /**
         * 客户端断开链接，此方法回调
         *
         * @param client 客户端
         */
        void onClientDisco(Client client);

        void onServerReady();

        void onServerFinlish();
    }
}
