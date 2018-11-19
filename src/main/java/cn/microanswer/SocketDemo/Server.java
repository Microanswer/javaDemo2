package cn.microanswer.socketdemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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

    // 保存所有创建的聊天室
    private HashMap<String, Room> rooms;

    void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    public void run() {
        super.run();
        clients = new HashMap<>();
        rooms = new HashMap<>();
        try {
            // 新建服务
            serverSocket = new ServerSocket(Constant.SERVER_PORT);
        } catch (Exception e) {
            // 新建服务就报错，多半都是端口占用。
            onError(e);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    onServerFinlish();
                }
            });
            return;
        }


        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    onServerReady();
                }
            });


            while (!serverSocket.isClosed()) {
                try {
                    Socket accept = serverSocket.accept();
                    Client client = new Client(accept);
                    client.setClientListener(clientListener);
                    client.start(); // 保持对该客户端的活跃状态

                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message == null) {
                        message = e.getClass().getName();
                    }
                    if (!"socket closed".equals(message)) {
                        onError(e);
                    }
                }
            }
        } catch (Exception e) {
            onError(e);
        } finally {

            // 跳出循环的时候，也就是关闭服务的时候。

            // 将所有客户端断开连接。
            for (Map.Entry<String, Client> entry : clients.entrySet()) {
                entry.getValue().colse();
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    onServerFinlish();
                }
            });
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
     *
     * @param client
     * @param msg
     */
    void onClientMsg(Client client, Msg msg) {

        // 这个范围内的消息，属于系统消息
        int msgType = msg.getMsgHead().getMsgType();
        if (10 < msgType && msgType <= 100) {

            if (msgType == MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER) {
                // 客户端请求获取所有在线人员。

                JSONArray jsonArray = new JSONArray();
                Set<Map.Entry<String, Client>> entries = clients.entrySet();
                for (Map.Entry<String, Client> ent : entries) {
                    JSONObject jo = new JSONObject();
                    Client value = ent.getValue();
                    jo.put("name", value.getClientNamme());
                    jo.put("id", value.getClientId());
                    jsonArray.add(jo);
                }
                client.sendMsg(Client.CreateSystemMsg(
                        MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER,
                        client.getClientId(),
                        jsonArray.toJSONString()
                        )
                );

            } else if (msgType == MsgHead.TYPE_SYSTEM_CREATE_ROOM) {
                // 客户端请求创建聊天室。

                // 获取请求的数据
                String text = msg.getText();
                JSONObject data = JSON.parseObject(text);
                JSONArray others = data.getJSONArray("others"); // 要加入聊天室的对方成员

                if (Utils.isListEmpty(others)) {
                    client.sendMsg(Client.CreateSystemMsg(
                            MsgHead.TYPE_SYSTEM_CREATE_ROOM,
                            client.getClientId(),
                            "false"
                    )); // 发送给客户端，聊天室创建失败。 必须要有与之聊天的其他人。
                    return;
                }

                String roomName = "";
                if (others.size() == 1) {
                    JSONObject jsonObject = others.getJSONObject(0);
                    roomName = jsonObject.getString("name");
                } else {
                    JSONObject j1 = others.getJSONObject(0);
                    JSONObject j2 = others.getJSONObject(1);
                    roomName = j1.getString("name") + "、" + j2.getString("name");
                }
                if (others.size() > 2) {
                    roomName += "等";
                }

                Room room = new Room("", roomName);
                room.setRoomListener(new ServerRoomListener());
                room.addMember(new Room.Member(client.getClientId(), client.getClientNamme()));
                for (int i = 0; i < others.size(); i++) {
                    JSONObject j = others.getJSONObject(i);
                    room.addMember(new Room.Member(j.getString("id"), j.getString("name")));
                }
                ArrayList<Room.Member> members = room.getMembers();
                room.setSignle(members.size() <= 2);
                Collections.sort(members);
                room.setId(Utils.md5(JSON.toJSONString(members)));

                // 判断这个房间是否已存在。
                boolean has = rooms.containsValue(room);
                if (!has) {
                    // 将创建好的 Room 放入管理列队
                    rooms.put(room.getId(), room);
                } else {
                    room = rooms.get(room.getId());
                }
                // 响应客户端聊天室创建成功，并返回聊天室成员。
                client.sendMsg(
                        Client.CreateSystemMsg(
                                MsgHead.TYPE_SYSTEM_CREATE_ROOM,
                                client.getClientId(),
                                JSON.toJSONString(room)
                        )
                );
            } else if (msgType == MsgHead.TYPE_SYSTEM_REQUEST_EXIT) {
                // 客户端请求下线。
                client.disConn();
            }


            // 这个范围内的消息，属于用户消息
        } else if (0 < msgType && msgType <= 10) {
            // 获取消息的目标聊天室
            String toRoomId = msg.getMsgHead().getToClientId();
            String toRoomName = msg.getMsgHead().getToName();

            Room room = rooms.get(toRoomId);
            if (room != null) {
                // 将消息分发到各个聊天室成员
                room.dispatchMsg(msg);
            } else {
                // 目标聊天室没找到。
                System.out.println("未找到目标聊天室 (roomId:" + toRoomId + ", roomName:" + toRoomName + ")");
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (serverListener != null) serverListener.onError(e);
            }
        });
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

        // 向所有已在线的人发送该用户上线信息。
        JSONObject info = new JSONObject();
        info.put("name", name);
        info.put("id", id);
        for (Map.Entry<String, Client> e : clients.entrySet()) {
            e.getValue().sendMsg(Client.CreateSystemMsg(
                    MsgHead.TYPE_NOTIFY_USER_ONLINE,
                    client.getClientId(),
                    info.toJSONString()
            ));
        }

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

        // 向所有已在线的人发送该用户下线信息。
        JSONObject info = new JSONObject();
        info.put("name", client.getClientNamme());
        info.put("id", client.getClientId());
        for (Map.Entry<String, Client> e : clients.entrySet()) {
            e.getValue().sendMsg(Client.CreateSystemMsg(
                    MsgHead.TYPE_NOTIFY_USER_OFFLINE,
                    client.getClientId(),
                    info.toJSONString()
            ));
        }

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
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    onClientConnted(client);
                }
            });
        }

        @Override
        public void onNamed(Client client, String clientName, String clientId) {
            // 告诉所有的在线人员，有人改名字了。
            for (Map.Entry<String, Client> ent : clients.entrySet()) {
                if (ent.getValue().getClientId().equals(clientId)) {
                    continue;
                }
                ent.getValue().sendMsg(Client.CreateSystemMsg(
                        MsgHead.TYPE_NOTIFY_USER_RENAMED,
                        clientId,
                        clientName
                ));
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    onClientUpdate(client, clientName, clientId);
                }
            });
        }

        @Override
        public void onDisConn(Client client) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    onClientDisco(client);
                }
            });
        }

        @Override
        public void onError(Client client, Exception e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    onClientError(e, client);
                }
            });
        }

        @Override
        public void onMsg(Client client, Msg msg) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    onClientMsg(client, msg);
                }
            });
        }
    };

    private class ServerRoomListener implements Room.RoomListener {

        @Override
        public void onRoomGetMsg(Room room, Msg msg) {
            // 获取聊天室中所有的成员然后进行成员的消息分发。 不将消息再分发给发件人。

            ArrayList<Room.Member> otherMember = room.getMembers();
            for (Room.Member m : otherMember) {
                String id = m.getId();
                if (msg.getMsgHead().getFromClientId().equals(id)) {
                    continue;
                }
                Client client = clients.get(id);
                if (client.isAlive()) {
                    client.sendMsg(msg);
                } else {
                    System.out.println("消息发送失败，" + client.getClientNamme() + " 已下线。");
                }
            }
        }
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
