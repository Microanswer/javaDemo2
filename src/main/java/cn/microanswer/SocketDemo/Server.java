package cn.microanswer.SocketDemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务管理类
 */
public class Server extends Thread {

    private ServerSocket serverSocket;
    private ServerListener serverListener;
    private boolean isServerReady;

    private HashMap<String, Client> clients;

    Server() {
        clients = new HashMap<>();
        isServerReady = false;
    }

    void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    public void run() {
        super.run();
        try {
            // 新建服务
            serverSocket = new ServerSocket(Constant.SERVER_PORT);
            isServerReady = true;

            if (serverListener != null) {
                serverListener.onServerReady();
            }

            while (!serverSocket.isClosed()) {
                try {
                    Socket accept = serverSocket.accept();
                    Client client = new Client(accept);

                    // 加入统计列队
                    clients.put(client.getUserNamme(), client);

                    client.setClientListener(clientListener);
                    client.start(); // 保持对该客户端的活跃状态

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            for (Map.Entry<String, Client> entry : clients.entrySet()) {
                entry.getValue().colse();
            }

            System.out.println("服务停止");
            if (serverListener != null) {
                serverListener.onServerFinlish();
            }

        } catch (Exception e) {
            if (serverListener != null) {
                serverListener.onError(e);
            } else {
                e.printStackTrace();
            }
        }
    }

    public boolean isServerReady() {
        return isServerReady;
    }

    String getAddress() {
        // return serverSocket.getInetAddress().getHostAddress();
        return "127.0.0.1";
    }

    private Client.ClientListener clientListener = new Client.ClientListener() {
        @Override
        public void onConneted(Client client, String host, int port) {

            // 当客户端连接上，则下发客户端应该使用的名称
            client.sendMsg(Client.CreateSystemMsg(MsgHead.TYPE_SYSTEM_SETNAME, client.getUserNamme()));

            if (serverListener != null) {
                serverListener.onClientConnted(client, client.getUserNamme());
            }
        }

        @Override
        public void onDisConn(Client client) {
            clients.remove(client.getUserNamme());

            if (serverListener != null) {
                serverListener.onClientDisco(client, client.getUserNamme());
            }
        }

        @Override
        public void onError(Client client, Exception e) {
            if (serverListener != null) {
                serverListener.onClientError(e, client);
            }
        }

        @Override
        public void onMsg(Client client, Msg msg) {
            // 这个范围内的消息，属于系统消息
            if (10 < msg.msgHead.msgType && msg.msgHead.msgType <= 100) {

                // 客户端请求设置用户名
                if (msg.msgHead.msgType == MsgHead.TYPE_SYSTEM_SETNAME) {
                    String oldName = client.getUserNamme();
                    client.setUserNamme(msg.getTxtBody());
                    clients.remove(oldName);
                    clients.put(client.getUserNamme(), client);
                    if (serverListener != null) {
                        serverListener.onClientUpdate(client, client.getUserNamme());
                    }


                    // 客户端请求获取所有在线人员
                } else if (msg.msgHead.msgType == MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER) {
                    String[] clients = getClients();
                    String value = Arrays.toString(clients).replace("[", "").replace("]", "");
                    client.sendMsg(Msg.CreateSystemMsg(MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER, value), null);
                }
                // 这个范围内的消息，属于用户消息
            } else if (0 < msg.msgHead.msgType && msg.msgHead.msgType <= 10) {

                // 获取消息的目标客户
                String toName = msg.msgHead.toName;
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
    };

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
            v[i] = entry.getKey();
            i++;
        }
        return v;
    }


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
         */
        void onClientConnted(Client client, String name);

        /**
         * 有客端更新数据
         *
         * @param client 更新的
         * @param name   客户端新名称
         */
        void onClientUpdate(Client client, String name);

        /**
         * 客户端断开链接，此方法回调
         *
         * @param client 客户端
         * @param name   名字
         */
        void onClientDisco(Client client, String name);

        void onServerReady();

        void onServerFinlish();
    }
}
