package cn.microanswer.SocketDemo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * 客户端管理类
 */
public class Client extends Thread {
    private Socket socket;
    private BufferedOutputStream outputStream;
    private boolean isReady;
    private ClientListener clientListener;

    private String host;
    private int port;
    private String userNamme = "";

    Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    Client(Socket socket) {
        this.socket = socket;
        InetAddress address = this.socket.getInetAddress();
        host = address.getHostName();
        port = socket.getPort();
        isReady = true;
        userNamme = "User-" + UUID.randomUUID().toString().substring(0, 4);
    }

    public void setClientListener(ClientListener clientListener) {
        this.clientListener = clientListener;
    }

    @Override
    public void run() {
        super.run();
        if (socket == null) {
            try {
                socket = new Socket(host, port);
                isReady = true;
            } catch (Exception e) {
                if (clientListener != null) {
                    clientListener.onError(this, e);
                }
                return;
            }
        }
        if (isReady) {
            InputStream inputStream = null;

            try {
                // 获取输出流
                outputStream = new BufferedOutputStream(socket.getOutputStream());

                if (clientListener != null) {
                    clientListener.onConneted(this, host, port);
                }

                // 读取输入流，获取消息信息。
                inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                while (socket.isConnected() && !socket.isClosed()) {
                    try {
                        Msg msg = new Msg(bufferedInputStream);
                        onMsg(msg);
                    } catch (Exception e) {
                        String msg = e.getMessage();
                        if (Constant.isEmpty(msg) || msg.contains("Connection reset") || msg.contains("Socket closed")) {
                            // 因客户端或服务端强制关闭，导致链接重置或关闭。
                            System.out.println("连接被关闭");
                            break;
                        } else {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                }
            } catch (Exception e) {
                if (clientListener != null) {
                    clientListener.onError(this, e);
                } else {
                    e.printStackTrace();
                }
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (clientListener != null) {
                    clientListener.onDisConn(this);
                }
            }
        }
    }

    void onMsg(Msg msg) {
        if (clientListener != null && !socket.isClosed()) {
            clientListener.onMsg(this, msg);
        }
    }

    /**
     * 断开链接
     */
    void disConn() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setUserNamme(String userNamme) {
        this.userNamme = userNamme;
    }

    String getUserNamme() {
        return userNamme;
    }

    void sendMsg(Msg msg) {
        sendMsg(msg, null);
    }

    void sendMsg(Msg msg, SendListener sendListener) {
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
                if (sendListener != null) {
                    sendListener.onEnd(value);
                }
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

    interface ClientListener {
        void onConneted(Client client, String host, int port);

        void onDisConn(Client client);

        void onError(Client client, Exception e);

        void onMsg(Client client, Msg msg);
    }

    interface SendListener {
        // 消息发送完成回调。
        void onEnd(Msg msg);
    }

    public static Msg CreateSystemMsg(int type) {
        return CreateSystemMsg(type, String.valueOf(type));
    }

    public static Msg CreateSystemMsg(int type, String value) {
        try {
            return new Msg(type, "0", "0", value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

