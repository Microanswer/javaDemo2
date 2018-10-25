package cn.microanswer;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 使用 Socket 实现的简易聊天 Demo
 * http://microanswer.cn
 *
 * @author Microanswer 2018年10月19日22:09:21
 */
public class SocketDemo {

    /**
     * 程序入口。
     */
    public static void main(String args[]) {


        /*
         * 此 Demo 演示流程：
         *
         * 运行程序 --> 选择作为服务端还是客户端
         *
         * 选择服务端 --> 关闭选择窗口，创建服务并显示服务运行状态窗口。
         *
         * 选择客户端 --> 关闭选择窗口，创建客户端并显示客户端窗口。
         *
         */

        try {
            // 设置界面风格和 window 窗口相同。
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new OptionWindow(Toolkit.getDefaultToolkit()).setVisible(true);
    }

    /**
     * 常量类
     */
    static class Constant {
        /**
         * Microanswer 的网址
         */
        static final String MICROANSWER_CN = "http://microanswer.cn";
        /**
         * 文本消息传递过程中的编码格式
         */
        static final String CHAR_SET = "UTF-8";

        /**
         * 错误窗口宽度
         */
        static final int ERR_WINDOW_WIDTH = 260;
        /**
         * 错误窗口高度
         */
        static final int ERR_WINDOW_HEIGHT = 100;

        /**
         * 选择功能窗口的标题
         */
        static final String OPTION_WINDOW_TITLE = "请选择角色";
        /**
         * 选择功能窗口中创建服务端按钮文字
         */
        static final String OPTION_WINDOW_BUTTON1_TXT = "创建服务端";
        /**
         * 选择功能窗口中创建客户端按钮文字
         */
        static final String OPTION_WINDOW_BUTTON2_TXT = "创建客户端";
        /**
         * 选择功能窗口中说明按钮关于文字
         */
        static final String OPTION_WINDOW_ABOUT_TXT = "帮助和关于";
        /**
         * 选择功能窗口的宽度
         */
        static final int OPTION_WINDOW_WIDTH = 260;
        /**
         * 选择功能窗口的高度
         */
        static final int OPTION_WINDOW_HEIGHT = 170;

        /**
         * 服务窗口的标题
         */
        static final String SERVER_WINDOW_TITLE = "服务状态";
        /**
         * 服务窗口宽度
         */
        static final int SERVER_WINDOW_WIDTH = 220;
        /**
         * 服务窗口高度
         */
        static final int SERVER_WINDOW_HEIGHT = 220;
        /**
         * 服务所在的端口
         */
        static final int SERVER_PORT = 9456;

        /**
         * 客户端窗口标题
         */
        static final String CLIENT_WINDOW_TITLE = "客户端";
        /**
         * 客户端窗口宽度
         */
        static final int CLIENT_WINDOW_WIDTH = 400;
        /**
         * 客户端窗口高度
         */
        static final int CLIENT_WINDOW_HEIGHT = 400;

        /**
         * 配置程序工作目录。 在传输大文件时，此目录将作为程序的工作目录，接收的大文件将被存放在此目录下。
         * 请务必以 / 结尾
         */
        static final String WORK_DIR = "./work_" + System.nanoTime() + "/";

        // 一些界面上使用的常用文案
        static final String SURE = "确定";
        static final String CANCEL = "取消";
        static final String CHECKWEB = "访问网站";
        static final String TIP = "提示";
        static final String OPTION_WINDOW_ABOUT_CONTENT = "<html><p>本演示程序演示了使用 Java-Socket 实现的简单聊天系统。</p><p>支持文本消息，修改聊天人姓名，图片消息以及传送文件。</p><p>使用方法：</p><p>1、运行本程序先创建一个服务。<br/>2、再次运行本程序，选择创建客户端。<br/>3、多创建几个客户端，这些客户端即可进行聊天。<br/>4、在局域网里，你可以在其他机器上连接这台机器上的服务，ip请<br/>  输入服务端机器的局域网ip地址<br/>#注意：不能创建多个服务端，因为一个端口只能开启一个服务。</p><br/><p>制作：Microanswer<p><a>网站：" + MICROANSWER_CN + "</a><html>";
        static final String WORK_DIR_CREATE_FAIL_TIP = "工作目录创建失败，请检查读写权限。";
        static final String SERVER_WINDOW_CLOSE_TIP = "<html><p>关闭服务同时会关闭所有已连接的客户</p><p>端，每个客户端中的聊天数据将被删</p><p>除，包括图像、文件等信息。</p><p>确定要关闭吗？</p></html>";
        static final String CLIENT_LIST = "客户列表";
        static final String ERROR = "错误";
        static final String CLIENT_ERROR = "客户端" + ERROR;
        static final String CLIENT_WINDOW_CLOSE_TIP = "<html><p>退出客户端将删除所有聊天数据，包括</p><p>图像、文件等信息。你确定要退出吗？</p></html>";
        static final String SERVER_INFO = "服务器信息";
        static final String CLIENT_WINDOW_CLOSE_TALK_TIP = "<html><p>若该用户再次给你发消息，会话会自动打开。</p><p>确定要关闭会话吗？</p></html>";
        static final String ALL_ONLINE_USER = "所有在线人员";
        static final String NO_ONLINE_USER = "没有人在线";
        static final String SEND = "发送";
        static final String PIC = "图片";
        static final String FILE = "文件";
        static final String CLEAR = "清空";
        static final String CLIENT_DEFALUT_SERVER_HOST = "127.0.0.1";
        public static final String CLIENT_MODIFY_NAME_TIP = "<html><p>#注意：修改名称后，你将不能<br/>接收到已经和你聊天的别的好<br/>友发来的消息，需对方重新选<br/>择你新修改的名称后即可。</p></html>";

        /**
         * 判断字符串是否为空
         * @param str 要判断的字符串
         * @return true 如果为空， 否则false
         */
        static boolean isEmpty(String str) {
            return str == null || str.length() == 0;
        }

        /**
         * 从 inputStream 读取 length 长度到 outputStream 中
         *
         * @param inputStream  输入流
         * @param outputStream 输出流
         * @param length       长度
         */
        static void streamCopy(InputStream inputStream, OutputStream outputStream, long length) throws Exception {
            long leftSize = length; // 剩下还有多少没有读取
            int eachSize = 4096;

            byte data[] = new byte[eachSize];

            // 还有剩下的没读取，就一直读取
            while (leftSize > 0) {

                int size;

                if (leftSize >= eachSize) {
                    size = inputStream.read(data, 0, eachSize);
                    leftSize -= size;
                } else {
                    // 剩下的数据量已近较少了
                    size = inputStream.read(data, 0, (int) leftSize);
                    leftSize = 0;
                }

                outputStream.write(data, 0, size);
            }

            outputStream.flush();
        }

        /**
         * 删除目录 或 文件
         *
         * @param f 要删除的目录或文件
         */
        static void deleteFile(File f) {
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                if (files != null && files.length > 0) {
                    for (File ff : files) {
                        deleteFile(ff);
                    }
                }
            }
            boolean delete = f.delete();
            if (delete) {
                System.out.println(f.getAbsolutePath() + "删除成功");
            } else {
                System.err.println(f.getAbsolutePath() + "删除失败");
            }
        }
    }

    /**
     * 创建工作目录出错时，会使用此窗口进行提示。
     */
    static class ErrorWindow extends JFrame {
        private Toolkit toolkit;

        ErrorWindow(Toolkit toolkit, String errMsg) {
            super(Constant.ERROR);
            this.toolkit = toolkit;

            // 设置窗口大小和位置
            Dimension screenSize = this.toolkit.getScreenSize();
            setBounds(
                    (int) Math.round((screenSize.getWidth() - Constant.ERR_WINDOW_WIDTH) / 2f),
                    (int) Math.round((screenSize.getHeight() - Constant.ERR_WINDOW_HEIGHT) / 2f),
                    Constant.ERR_WINDOW_WIDTH,
                    Constant.ERR_WINDOW_HEIGHT
            );

            // 不允许拖动窗口大小
            setResizable(false);
            // 窗口关闭时不采取任何行为，在事件中处理具体的关闭事件。
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // 设置输出布局管理
            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());

            // 加入控件
            contentPane.add(new JLabel(errMsg, JLabel.CENTER), BorderLayout.CENTER);

            // 加入确定按钮 。 （使用JPanel配合FlowLayout进行居中显示）
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
            JButton jButton = new JButton(Constant.SURE);
            jButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dispose();
                }
            });
            panel.add(jButton);
            contentPane.add(panel, BorderLayout.SOUTH);

        }
    }

    /**
     * 选择功能窗口
     */
    static class OptionWindow extends JFrame {
        private Toolkit toolkit;
        private final String NEWSERVER = "newserver";
        private final String NEWCLIENT = "newclient";

        /**
         * 窗口事件监听器
         */
        private WindowAdapter windowAdapter = new WindowAdapter() {
            /**
             * 当点击窗口的关闭按钮时，此方法立刻调用。
             * 在此方法中调用 dispose() 方法，则会调用 windowClosed() 方法。
             * 否则 windowClosed() 将不会被调用。
             * @param e 事件
             */
            @Override
            public void windowClosing(WindowEvent e) {
                OptionWindow.this.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // 窗口完全关闭完成
                System.out.println("角色选择窗口已关闭");
            }
        };

        /**
         * 按钮事件监听器
         */
        private MouseAdapter buttonEventAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object source = e.getSource();
                if (source instanceof JButton) {

                    // 创建工作目录
                    File file = new File(Constant.WORK_DIR);
                    if (file.exists() || file.mkdirs()) {

                        JButton button = (JButton) source;
                        String actionCommand = button.getActionCommand();
                        if (NEWSERVER.equals(actionCommand)) {
                            // 创建服务端
                            new ServerWindow(toolkit).setVisible(true);
                        } else if (NEWCLIENT.equals(actionCommand)) {
                            // 创建客户端
                            new ClientWindow(toolkit);
                        }
                        // 关闭角色选择窗口
                        OptionWindow.this.dispose();
                    } else {
                        new ErrorWindow(toolkit, Constant.WORK_DIR_CREATE_FAIL_TIP).setVisible(true);
                    }
                }
            }
        };

        OptionWindow(Toolkit toolkit) {
            super(Constant.OPTION_WINDOW_TITLE); // 设定窗口标题

            this.toolkit = toolkit;

            // 初始化窗口位置和大小
            Dimension screenSize = this.toolkit.getScreenSize();
            setBounds(
                    (int) Math.round((screenSize.getWidth() - Constant.OPTION_WINDOW_WIDTH) / 2f),
                    (int) Math.round((screenSize.getHeight() - Constant.OPTION_WINDOW_HEIGHT) / 2f),
                    Constant.OPTION_WINDOW_WIDTH,
                    Constant.OPTION_WINDOW_HEIGHT
            );

            // 不允许改变大小
            setResizable(false);

            // 添加窗口关闭的监听事件
            addWindowListener(windowAdapter);

            // 添加两个按钮到界面并监听按钮事件
            addButtonAndLogic();

        }

        /**
         * 添加两个按钮到界面，提供用户选择角色
         */
        private void addButtonAndLogic() {
            Container contentPane = getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 17));


            JButton button1 = new JButton(Constant.OPTION_WINDOW_BUTTON1_TXT);
            button1.setActionCommand(NEWSERVER);
            button1.addMouseListener(buttonEventAdapter);
            contentPane.add(button1);
            JButton button2 = new JButton(Constant.OPTION_WINDOW_BUTTON2_TXT);
            button2.setActionCommand(NEWCLIENT);
            button2.addMouseListener(buttonEventAdapter);
            contentPane.add(button2);
            JLabel jLabel = new JLabel(Constant.OPTION_WINDOW_ABOUT_TXT);
            jLabel.setBorder(new UnderLineBorder());
            jLabel.setForeground(Color.BLUE);
            jLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            jLabel.setToolTipText(Constant.OPTION_WINDOW_ABOUT_TXT);
            jLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String options[] = new String[]{Constant.SURE, Constant.CHECKWEB};
                    int index = JOptionPane.showOptionDialog(
                            OptionWindow.this,
                            Constant.OPTION_WINDOW_ABOUT_CONTENT,
                            Constant.OPTION_WINDOW_ABOUT_TXT,
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[1]
                    );
                    if (index == 1) {
                        try {
                            Desktop.getDesktop().browse(new URI(Constant.MICROANSWER_CN));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            contentPane.add(jLabel);
        }

    }

    /**
     * 服务窗口。
     */
    static class ServerWindow extends JFrame implements Server.ServerListener {
        private Toolkit toolkit;

        /**
         * 服务器处理类
         */
        private Server server;

        /**
         * window 关闭事件处理
         */
        private WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (server.isServerReady() && server.getClientCount() > 0) {
                    String[] options = new String[]{Constant.SURE, Constant.CANCEL};
                    int index = JOptionPane.showOptionDialog(
                            ServerWindow.this,
                            Constant.SERVER_WINDOW_CLOSE_TIP,
                            Constant.TIP,
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[1]
                    );
                    if (index == 0) {
                        server.close();
                    }
                } else {
                    if (server.isServerReady()) {
                        server.close();
                    } else {
                        ServerWindow.this.dispose();
                    }
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                Task.TaskHelper.getInstance().stopAfterLastTaskFlish();
                File f = new File(Constant.WORK_DIR);
                if (f.exists()) {
                    Constant.deleteFile(f);
                }
            }
        };

        // 服务状态显示控件
        private JLabel serverStatusLabel;
        // 已连接人数显示控件
        private JLabel connectedCountLabel;
        // 已连接人的列表显示控件
        private JList<String> jList;

        ServerWindow(Toolkit toolkit) {
            super(Constant.SERVER_WINDOW_TITLE);
            this.toolkit = toolkit;

            // 初始化窗口位置和大小
            Dimension screenSize = this.toolkit.getScreenSize();
            setBounds(
                    (int) Math.round((screenSize.getWidth() - Constant.SERVER_WINDOW_WIDTH) / 2f),
                    (int) Math.round((screenSize.getHeight() - Constant.SERVER_WINDOW_HEIGHT) / 2f),
                    Constant.SERVER_WINDOW_WIDTH,
                    Constant.SERVER_WINDOW_HEIGHT
            );
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            // 不允许改变大小
            setResizable(false);

            // 绑定窗口事件
            addWindowListener(windowAdapter);

            serverStatusLabel = new JLabel("服务正在开启...");
            Container contentPane = getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
            contentPane.add(serverStatusLabel);

            initServerSocket();
        }

        private void initServerSocket() {
            server = new Server();
            server.setServerListener(this);
            server.start(); // 开启服务
        }

        @Override
        public void onServerReady() {
            serverStatusLabel.setText("<html><p>服务已开启</p><p>Host:" + server.getAddress() + "；端口: " + Constant.SERVER_PORT + "</p></html>");

            connectedCountLabel = new JLabel("当前已连接人数：0");
            getContentPane().add(connectedCountLabel);

            jList = new JList<>();
            jList.setBackground(getContentPane().getBackground());
            jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            JScrollPane scrollPane = new JScrollPane(jList);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(170, 100));
            scrollPane.setBorder(BorderFactory.createTitledBorder(Constant.CLIENT_LIST));

            getContentPane().add(scrollPane);

            invalidate();
            System.out.println("服务已开启");
        }

        @Override
        public void onServerFinlish() {
            ServerWindow.this.dispose();
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ServerWindow.this, e.getMessage(), Constant.ERROR, JOptionPane.ERROR_MESSAGE);
        }

        @Override
        public void onClientError(Exception e, Client client) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ServerWindow.this, e.getMessage(), Constant.CLIENT_ERROR, JOptionPane.ERROR_MESSAGE);
        }

        @Override
        public void onClientConnted(Client client, String name) {
            System.out.println(name + ", 已连接");
            connectedCountLabel.setText("当前已连接人数：" + server.getClientCount());
            jList.setListData(server.getClients());
            invalidate();
        }

        @Override
        public void onClientUpdate(Client client, String name) {
            jList.setListData(server.getClients());
            invalidate();
        }

        @Override
        public void onClientDisco(Client client, String name) {
            System.out.println(name + ", 已断开");
            connectedCountLabel.setText("当前已连接人数：" + server.getClientCount());
            jList.setListData(server.getClients());
            invalidate();
        }

    }

    /**
     * 服务管理类
     */
    static class Server extends Thread {

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
                client.sendMsg(Msg.CreateSystemMsg(MsgHead.TYPE_SYSTEM_SETNAME, client.getUserNamme()), null);

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

    /**
     * 客户端窗口
     */
    static class ClientWindow extends JFrame {
        private static final String ACTION_COMMAND_SEE_ALLONLINE = "seeallonline";
        private static final String ACTION_COMMAND_SEND = "send";
        private static final String ACTION_COMMAND_CLEAR = "clear";
        private static final String ACTION_COMMAND_CHOOSE_PIC = "choosepic";
        private static final String ACTION_COMMAND_CHOOSE_FILE = "choosefile";
        private Toolkit toolkit;
        private Client client;

        private JLabel userNameLabel;

        private JTabbedPane jTabbedPane;
        private ArrayList<String> talkingUsers; // 正在聊天的队列

        // 登录弹出框里面的控件
        private JDialog dialog;
        private JButton buttonLogin;

        // 查看所有在线人员的弹出框
        private JDialog allUserDialog;
        private JList<String> allUserList;
        // 修改我的名称按钮
        private JLabel modifyMyName;

        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String options[] = new String[]{Constant.SURE, Constant.CANCEL};
                int index = JOptionPane.showOptionDialog(
                        ClientWindow.this,
                        Constant.CLIENT_WINDOW_CLOSE_TIP,
                        Constant.TIP,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (index == 0) {
                    client.disConn();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                Task.TaskHelper.getInstance().stopAfterLastTaskFlish();
            }
        };

        /**
         * 界面中按钮监听器。
         */
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof Component) {
                    Component component = (Component) source;
                    if (!component.isEnabled()) {
                        return;
                    }
                }
                String actionCommand = e.getActionCommand();

                if (ACTION_COMMAND_SEE_ALLONLINE.equals(actionCommand)) {
                    // 点击查看所有人员
                    showAllOnline();
                }
            }
        };

        ClientWindow(Toolkit toolkit) {
            super(Constant.CLIENT_WINDOW_TITLE);
            this.toolkit = toolkit;

            // 初始化窗口位置和大小
            Dimension screenSize = this.toolkit.getScreenSize();
            setBounds(
                    (int) Math.round((screenSize.getWidth() - Constant.CLIENT_WINDOW_WIDTH) / 2f),
                    (int) Math.round((screenSize.getHeight() - Constant.CLIENT_WINDOW_HEIGHT) / 2f),
                    Constant.CLIENT_WINDOW_WIDTH,
                    Constant.CLIENT_WINDOW_HEIGHT
            );

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

            // 添加窗口监听。
            addWindowListener(windowAdapter);

            // 不允许改变大小
            setResizable(false);

            // 弹出连接服务端的信息录入窗口
            showConnectDialog();

            talkingUsers = new ArrayList<>();
        }

        void showConnectDialog() {

            dialog = new JDialog(ClientWindow.this, Constant.SERVER_INFO);
            dialog.setResizable(false);
            dialog.setAutoRequestFocus(true);
            dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                    ClientWindow.this.dispose();
                    new OptionWindow(Toolkit.getDefaultToolkit()).setVisible(true);
                }
            });
            Dimension screenSize = toolkit.getScreenSize();
            dialog.setBounds(
                    (int) Math.round((screenSize.getWidth() - 200) / 2f),
                    (int) Math.round((screenSize.getHeight() - 140) / 2f),
                    200,
                    140
            );
            Container contentPane = dialog.getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JLabel tipLabel = new JLabel("服务器url：");
            contentPane.add(tipLabel);
            JTextField urlField = new JTextField();
            urlField.setPreferredSize(new Dimension(80, 20));
            urlField.setText(Constant.CLIENT_DEFALUT_SERVER_HOST);
            contentPane.add(urlField);
            JLabel tipPortLabel = new JLabel("端  口 号：");
            contentPane.add(tipPortLabel);
            JTextField portField = new JTextField();
            portField.setPreferredSize(new Dimension(80, 20));
            portField.setText(Constant.SERVER_PORT + "");
            contentPane.add(portField);
            buttonLogin = new JButton(" 登入 ");
            buttonLogin.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!buttonLogin.isEnabled()) {
                        return;
                    }
                    String url = urlField.getText();
                    String port = portField.getText();
                    if (Constant.isEmpty(url) || Constant.isEmpty(port)) {
                        JOptionPane.showMessageDialog(dialog, "请输入Url和端口号", Constant.TIP, JOptionPane.WARNING_MESSAGE);
                    } else {
                        int po = Integer.parseInt(port);
                        buttonLogin.setEnabled(false);
                        initClient(url, po);
                    }
                }
            });
            contentPane.add(buttonLogin);

            dialog.setVisible(true);
        }

        /**
         * 初始化客户端
         *
         * @param url  主机地址
         * @param port 端口号
         */
        void initClient(String url, int port) {
            client = new Client(url, port);
            client.setClientListener(new MClientListener());
            client.start(); // 开启客户端
        }

        /**
         * 初始化客户端界面
         */
        void initUI() {
            Container contentPane = ClientWindow.this.getContentPane();

            // NORTH 方向显示正在和谁聊天和我的信息和查看所有在线人员按钮
            JPanel northPanel = new JPanel();
            northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

            JButton showBtn = new JButton("在线人员");
            showBtn.setActionCommand(ACTION_COMMAND_SEE_ALLONLINE);
            showBtn.addActionListener(actionListener);


            userNameLabel = new JLabel("我的名称：" + client.getUserNamme() + " ");
            userNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 0));
            modifyMyName = new JLabel("修改");
            modifyMyName.setEnabled(false); // 在服务端返回了客户端初始名称后，才可以修改
            modifyMyName.setBorder(new UnderLineBorder());
            modifyMyName.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            modifyMyName.setForeground(Color.BLUE);
            modifyMyName.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!modifyMyName.isEnabled()) {
                        return;
                    }
                    // 修改我的姓名
                    showChangeMyNameDialog();
                }
            });

            northPanel.add(showBtn);
            northPanel.add(userNameLabel);
            northPanel.add(modifyMyName);
            contentPane.add(northPanel, BorderLayout.NORTH);

            // 中间显示 tab面板，显示多个聊天界面
            jTabbedPane = new JTabbedPane();
            contentPane.add(jTabbedPane, BorderLayout.CENTER);
        }

        /**
         * 弹出修改姓名的弹出框
         */
        void showChangeMyNameDialog() {
            JDialog dialog = new JDialog(ClientWindow.this, "修改名称");
            dialog.setResizable(false);
            dialog.setAutoRequestFocus(true);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                }
            });
            dialog.setBounds(
                    Math.round((ClientWindow.this.getWidth() - 240) / 2f + getX()),
                    Math.round((ClientWindow.this.getHeight() - 160) / 2f + getY()),
                    240,
                    160
            );
            Container contentPane = dialog.getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
            JLabel tipLabel = new JLabel("新名称：");
            contentPane.add(tipLabel);
            JTextField nameFiled = new JTextField();
            nameFiled.setPreferredSize(new Dimension(100, 20));
            contentPane.add(nameFiled);
            JLabel t = new JLabel(Constant.CLIENT_MODIFY_NAME_TIP);
            t.setForeground(Color.RED);
            t.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
            t.setFont(new Font(t.getFont().getName(), t.getFont().getStyle(), t.getFont().getSize() - 1));
            contentPane.add(t);
            JButton buttonLogin = new JButton(" 修改 ");
            buttonLogin.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String name = nameFiled.getText();
                    if (Constant.isEmpty(name)) {
                        JOptionPane.showMessageDialog(dialog, "请输入新名称。", Constant.TIP, JOptionPane.WARNING_MESSAGE);
                    } else {

                        if (name.length() > 8) {
                            JOptionPane.showMessageDialog(dialog, "名称最多只能8个字。", Constant.TIP, JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // 向服务端发起更名请求， 服务端接收到后会更新服务端的数据。
                        client.sendMsg(Msg.CreateSystemMsg(MsgHead.TYPE_SYSTEM_SETNAME, name), null);
                        setLocalUserName(name);
                        dialog.dispose();
                    }
                }
            });
            contentPane.add(buttonLogin);
            dialog.setVisible(true);
        }

        /**
         * 当有人发消息过来时，此方法被调用。
         *
         * @param msg 消息内容
         */
        void onGetMsg(Msg msg) {
            String fromName = msg.msgHead.fromName;
            int index = talkingUsers.indexOf(fromName);
            if (index == -1) {
                // 还没有与这位好友的聊天面板创建，立刻创建。
                TalkPanel talkPanel = new TalkPanel(fromName);
                talkPanel.onMsg(msg);
                jTabbedPane.addTab(fromName, talkPanel);
                jTabbedPane.setTabComponentAt(jTabbedPane.indexOfComponent(talkPanel), getTabTitle(fromName));
                talkingUsers.add(fromName);
            } else {
                // 有
                TalkPanel talkPanel = (TalkPanel) jTabbedPane.getComponentAt(index);
                talkPanel.onMsg(msg);
            }
        }

        /**
         * 告知某一条消息发送成功了
         *
         * @param msgId 消息id
         */
        void onMsgSendSuccess(String msgId) {
            System.out.println("消息：" + msgId + " 发送成功");
        }

        void setLocalUserName(String name) {
            client.setUserNamme(name);
            ClientWindow.this.setTitle(Constant.CLIENT_WINDOW_TITLE + "(" + name + ")");
            if (userNameLabel != null) {
                userNameLabel.setText("我的名称：" + client.getUserNamme() + " ");
                userNameLabel.invalidate();
            }
            if (modifyMyName != null) {
                modifyMyName.setEnabled(true);
            }
        }

        /**
         * 新建聊天的对象或选择到某一个
         *
         * @param fromName 消息来自的名称
         */
        void setTalkUser(String fromName) {
            int index = talkingUsers.indexOf(fromName);
            if (index == -1) {
                // 还没有与这位好友的聊天面板创建，立刻创建。
                TalkPanel talkPanel = new TalkPanel(fromName);
                jTabbedPane.addTab(fromName, talkPanel);
                jTabbedPane.setTabComponentAt(jTabbedPane.indexOfComponent(talkPanel), getTabTitle(fromName));
                jTabbedPane.setSelectedComponent(talkPanel);
                talkingUsers.add(fromName);
            } else {
                // 有，则直接定位到对应好友
                jTabbedPane.setSelectedIndex(index);
            }
        }

        Component getTabTitle(String fromname) {
            JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            jPanel.setBackground(new Color(0, 0, 0, 0));
            JLabel jLabel = new JLabel(fromname);
            jLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 6));
            jLabel.setEnabled(false);
            jLabel.setFocusable(false);
            jPanel.add(jLabel);

            JLabel jLabel1 = new JLabel("x");
            jLabel1.setForeground(Color.RED);
            jLabel1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String ops[] = new String[]{Constant.SURE, Constant.CANCEL};
                    int i = JOptionPane.showOptionDialog(ClientWindow.this,
                            Constant.CLIENT_WINDOW_CLOSE_TALK_TIP, Constant.TIP, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, ops, ops[1]);
                    if (i == 0) {
                        int i1 = jTabbedPane.indexOfTabComponent(jPanel);
                        jTabbedPane.removeTabAt(i1);
                        talkingUsers.remove(i1);
                    }
                }
            });
            jLabel1.setFocusable(true);
            jPanel.add(jLabel1);
            return jPanel;
        }

        /**
         * 显示所有在线的客户端
         */
        void showAllOnline() {
            allUserDialog = new JDialog(ClientWindow.this, Constant.ALL_ONLINE_USER);
            allUserDialog.setResizable(false);
            allUserDialog.setAutoRequestFocus(true);
            allUserDialog.setModal(true);
            allUserDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            allUserDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    allUserDialog.dispose();
                }
            });
            allUserDialog.setBounds(
                    Math.round((ClientWindow.this.getWidth() - 100) / 2f + getX()),
                    Math.round((ClientWindow.this.getHeight() - 200) / 2f + getY()),
                    100,
                    200
            );
            allUserList = new JList<>();
            allUserList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 只能单选
            allUserList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        String selectedValue = allUserList.getSelectedValue();
                        setTalkUser(selectedValue);
                        // 关闭弹窗口
                        allUserDialog.dispose();
                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(allUserList);

            Container contentPane = allUserDialog.getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(scrollPane, BorderLayout.CENTER);

            // 向服务器发出获取所有用户信息的请求
            client.sendMsg(Msg.CreateSystemMsg(MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER, "request"), null);
        }

        /**
         * 当请求获取所有在线的人员时，服务器返回数据会调用此方法。
         *
         * @param data 所有数据，以逗号分开
         */
        void onAllOnlieUserGeted(String data) {
            if (!Constant.isEmpty(data)) {
                // 排除自己
                data = data.trim().replace(", ", ",").replace(client.getUserNamme(), "");
                String[] split = data.split(",");
                ArrayList<String> res = new ArrayList<>();
                for (String s : split) {
                    if (!Constant.isEmpty(s)) {
                        res.add(s);
                    }
                }
                if (res.size() > 0) {
                    String result[] = new String[res.size()];
                    result = res.toArray(result);
                    allUserList.setListData(result);
                    allUserList.invalidate();
                    allUserDialog.setVisible(true);
                    return;
                }
            }
            allUserDialog.dispose();
            JOptionPane.showMessageDialog(ClientWindow.this, Constant.NO_ONLINE_USER, Constant.TIP, JOptionPane.INFORMATION_MESSAGE);
        }

        /**
         * 聊天框， 一个人可以和多个人进行对话，每个人会产生一个聊天框。
         */
        class TalkPanel extends JPanel {

            // 与之谈话的对方的名称。
            String targetUserName;

            // 消息输入框
            private JTextArea textArea;
            // 聊天记录展示列表
            private ArrayList<Msg> msgs;
            private JPanel msgListPanel;
            private JScrollPane scrollPane;

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String actionCommand = e.getActionCommand();
                    if (ACTION_COMMAND_SEND.equals(actionCommand)) {
                        // 点击发送消息
                        sendMsg();
                    } else if (ACTION_COMMAND_CLEAR.equals(actionCommand)) {
                        if (textArea != null) {
                            textArea.setText("");
                        }
                    } else if (ACTION_COMMAND_CHOOSE_PIC.equals(actionCommand) || ACTION_COMMAND_CHOOSE_FILE.equals(actionCommand)) {
                        JFileChooser jfc = new JFileChooser();
                        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        jfc.setFileFilter(new FileFilter() {
                            boolean isIneedFile(File f) {
                                String name = f.getName().toLowerCase();
                                return ACTION_COMMAND_CHOOSE_FILE.equals(actionCommand) ||
                                        name.endsWith(".jpg") ||
                                        name.endsWith(".jpeg") ||
                                        name.endsWith(".png") ||
                                        name.endsWith(".bmp") ||
                                        name.endsWith(".gif");
                            }

                            @Override
                            public boolean accept(File f) {
                                return f.isDirectory() || isIneedFile(f);
                            }

                            @Override
                            public String getDescription() {
                                return ACTION_COMMAND_CHOOSE_FILE.equals(actionCommand) ? "" : "png,jpg,jpeg,gif,bmp";
                            }
                        });
                        jfc.showDialog(ClientWindow.this, Constant.SEND);
                        File selectedFile = jfc.getSelectedFile();
                        if (selectedFile != null) {
                            sendFile(selectedFile, ACTION_COMMAND_CHOOSE_PIC.equals(actionCommand));
                        }
                    }
                }
            };

            TalkPanel(String targetUserName) {
                super(new BorderLayout());

                this.targetUserName = targetUserName;
                setBackground(Color.WHITE);

                this.initUI();
            }

            void initUI() {

                // 中间显示聊天记录
                msgs = new ArrayList<>();
                msgListPanel = new JPanel(new LinearLayout());
                msgListPanel.setBackground(Color.WHITE);
                scrollPane = new JScrollPane(msgListPanel);
                scrollPane.setBackground(Color.WHITE);
                scrollPane.getVerticalScrollBar().setUnitIncrement(6);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                this.add(scrollPane, BorderLayout.CENTER);

                // SOUTH 方向显示聊天输入框
                JPanel southPanel = new JPanel();
                southPanel.setLayout(new BorderLayout());
                JPanel funBtnsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // 一排功能按钮
                JButton button2 = new JButton(Constant.PIC);
                button2.setActionCommand(ACTION_COMMAND_CHOOSE_PIC);
                button2.addActionListener(actionListener);
                funBtnsPanel.add(button2);
                JButton btn3 = new JButton(Constant.FILE);
                btn3.setActionCommand(ACTION_COMMAND_CHOOSE_FILE);
                btn3.addActionListener(actionListener);
                funBtnsPanel.add(btn3);
                southPanel.add(funBtnsPanel, BorderLayout.NORTH);
                textArea = new JTextArea();
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setForeground(new Color(20, 20, 20));
                textArea.setFont(new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), 11));
                JScrollPane jScrollPane = new JScrollPane(textArea);
                jScrollPane.setPreferredSize(new Dimension(Constant.CLIENT_WINDOW_WIDTH, 50));
                jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                southPanel.add(jScrollPane, BorderLayout.CENTER);
                JPanel sendBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                JButton button1 = new JButton(Constant.CLEAR);
                button1.setActionCommand(ACTION_COMMAND_CLEAR);
                button1.addActionListener(actionListener);
                sendBtnPanel.add(button1);
                JButton button = new JButton(Constant.SEND);
                button.setActionCommand(ACTION_COMMAND_SEND);
                button.addActionListener(actionListener);
                sendBtnPanel.add(button);
                southPanel.add(sendBtnPanel, BorderLayout.SOUTH);
                add(southPanel, BorderLayout.SOUTH);
            }

            /**
             * 发送文件
             *
             * @param f 文件
             */
            void sendFile(File f, boolean isPic) {
                try {
                    Msg msg = new Msg(isPic ? MsgHead.TYPE_PIC : MsgHead.TYPE_FILE, client.getUserNamme(), targetUserName, f);
                    client.sendMsg(msg, new Msg.SendListener() {
                        @Override
                        public void onEnd(Msg msg) {
                            onMsg(msg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            void sendMsg() {
                if (textArea != null && !Constant.isEmpty(targetUserName)) {
                    String text = textArea.getText();
                    if (!Constant.isEmpty(text) && !Constant.isEmpty(text.trim())) {
                        try {
                            Msg msg = new Msg(MsgHead.TYPE_TXT, client.getUserNamme(), targetUserName, text);
                            client.sendMsg(msg, new Msg.SendListener() {
                                @Override
                                public void onEnd(Msg msg) {
                                    textArea.setText("");
                                    onMsg(msg);
                                }
                            });
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (Constant.isEmpty(targetUserName)) {
                    JOptionPane.showMessageDialog(
                            ClientWindow.this,
                            "请先选择聊天对象",
                            Constant.TIP,
                            JOptionPane.WARNING_MESSAGE
                    );

                }
            }

            void onMsg(Msg msg) {
                msgs.add(msg);
                MsgItemView itemView = new MsgItemView(msg, msgs.size() - 1);
                msgListPanel.add(itemView);
                scrollPane.updateUI();
                scrollPane.getViewport().setViewPosition(new Point(0, Integer.MAX_VALUE));
                TalkPanel.this.grabFocus();
            }
        }

        class MClientListener implements Client.ClientListener {
            @Override
            public void onConneted(Client client, String host, int port) {
                dialog.dispose();
                initUI();
                setVisible(true);
                System.out.println("客户端连接成功啦");
            }

            @Override
            public void onDisConn(Client client) {
                // 删除工作目录
                File file = new File(Constant.WORK_DIR);
                if (file.exists()) {
                    Constant.deleteFile(file);
                }

                // 连接被关闭，关闭窗口
                ClientWindow.this.dispose();
            }

            @Override
            public void onError(Client client, Exception e) {
                if (buttonLogin != null && buttonLogin.isValid()) {
                    buttonLogin.setEnabled(true);
                }
                JOptionPane.showMessageDialog(ClientWindow.this, e.getMessage(), Constant.ERROR, JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void onMsg(Client client, Msg msg) {
                int msgType = msg.msgHead.msgType;
                // 这个范围内的消息，属于系统消息
                if (10 < msgType && msgType <= 100) {

                    // 服务器返回所有在线的成员。
                    if (msgType == MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER) {
                        String txtBody = msg.getTxtBody();
                        onAllOnlieUserGeted(txtBody);

                        // 服务器返回了此客户端的名称
                    } else if (msgType == MsgHead.TYPE_SYSTEM_SETNAME) {
                        String name = msg.getTxtBody();
                        setLocalUserName(name);
                    }

                    // 这个范围内的消息，属于用户消息
                } else if (0 < msgType && msgType <= 10) {

                    onGetMsg(msg);

                    // 通知类消息
                } else if (100 < msgType && msgType <= 200) {
                    if (msgType == MsgHead.TYPE_NOTIFY_MSG_SUCCESS) {
                        // 通知某一条消息发送成功
                        onMsgSendSuccess(msg.getTxtBody());
                    }
                }

            }
        }

        class MsgItemView extends JPanel {

            BufferedImage reduceImage(InputStream inputStream) {
                int showW = 130;
                int showH;
                try {
                    // 构造Image对象
                    BufferedImage src = javax.imageio.ImageIO.read(inputStream);
                    int width = src.getWidth();
                    int height = src.getHeight();

                    if (width > showW) {
                        showH = Math.round(130 * (height / (float) width));
                    } else {
                        showW = width;
                        showH = height;
                    }
                    // 缩小边长
                    BufferedImage tag = new BufferedImage(showW, showH, src.getType());
                    // 绘制 缩小  后的图片
                    tag.getGraphics().drawImage(src, 0, 0, tag.getWidth(), tag.getHeight(), null);
                    inputStream.close();
                    return tag;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            private Msg msg;
            private int index;
            private boolean isme;
            private SimpleDateFormat simpleDateFormat;

            MsgItemView(Msg msg, int index) {
                super(new BorderLayout());
                setBackground(Color.WHITE);
                this.index = index;
                this.msg = msg;
                this.isme = msg.msgHead.fromName.equals(client.getUserNamme());
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

                initView();
            }

            void initView() {

                JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                infoPanel.setBackground(Color.WHITE);

                // 消息发出者
                JLabel fromLabel = new JLabel("<html><b>" + (isme ? "我说" : msg.msgHead.fromName) + "</b></html>");
                fromLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                if (!isme) {
                    fromLabel.setForeground(Color.BLUE);
                }
                infoPanel.add(fromLabel);

                // 消息发出时间
                JLabel timeLabel = new JLabel(simpleDateFormat.format(new Date(msg.msgHead.createAt)));
                timeLabel.setFont(new Font(timeLabel.getFont().getName(), timeLabel.getFont().getStyle(), timeLabel.getFont().getSize() - 2));
                timeLabel.setBorder(BorderFactory.createEmptyBorder(7, 0, 0, 5));
                timeLabel.setForeground(Color.GRAY);
                infoPanel.add(timeLabel);

                add(infoPanel, BorderLayout.NORTH);


                // 文本消息
                int type = msg.msgHead.msgType;

                if (type == MsgHead.TYPE_TXT) {
                    JLabel txtLabel = new JLabel("<html><p>" + msg.getTxtBody() + "</p></html>");
                    txtLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                    add(txtLabel);
                } else if (type == MsgHead.TYPE_PIC) {
                    JLabel picLabel = new JLabel();
                    picLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                    try {
                        if (isme) {
                            picLabel.setIcon(new ImageIcon(reduceImage(new FileInputStream(msg.msgBody.userFile))));
                        } else {
                            picLabel.setIcon(new ImageIcon(reduceImage(new FileInputStream(msg.msgBody.cacheFile))));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    add(picLabel, BorderLayout.CENTER);
                } else if (type == MsgHead.TYPE_FILE) {
                    JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    filePanel.setBackground(Color.WHITE);
                    JLabel fileLabel = new JLabel();
                    fileLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                    if (isme) {
                        fileLabel.setText("发送文件<" + msg.msgHead.extra + ">");
                    } else {
                        fileLabel.setText("收到文件<" + msg.msgHead.extra + ">");
                    }
                    filePanel.add(fileLabel);

                    JLabel seeFileLabel = new JLabel("点击查看");
                    seeFileLabel.setForeground(Color.BLUE);
                    seeFileLabel.setToolTipText("在文件浏览器中查看该文件");
                    seeFileLabel.setBorder(new UnderLineBorder());
                    seeFileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    seeFileLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            try {
                                Runtime.getRuntime().exec("explorer /e,/select," + (isme ? msg.msgBody.userFile : msg.msgBody.cacheFile).getCanonicalPath());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    filePanel.add(seeFileLabel);

                    add(filePanel, BorderLayout.CENTER);
                } else {
                    JLabel txtLabel = new JLabel(msg.getTxtBody());
                    txtLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                    add(txtLabel, BorderLayout.CENTER);
                }

            }


        }
    }

    /**
     * 客户端管理类
     */
    static class Client extends Thread {
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

        void sendMsg(Msg msg, Msg.SendListener sendListener) {
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
    }

    /**
     * 消息头定义
     */
    static class MsgHead implements Serializable {

        // 用户级别消息
        static final int TYPE_TXT = 1; // 文本消息
        static final int TYPE_PIC = 2; // 图像消息
        static final int TYPE_VIDEO = 3; // 视频消息
        static final int TYPE_VOICE = 4; // 音频消息
        static final int TYPE_FILE = 5; // 文件消息

        // 系统级别消息
        static final int TYPE_SYSTEM_SETNAME = 11; // 请求设置名称
        static final int TYPE_SYSTEM_REQUEST_ALL_USER = 12;

        // 通知级别消息
        static final int TYPE_NOTIFY_MSG_SUCCESS = 101; // 通知某条消息发送成功。

        int msgType = 1;
        String fromName;
        String toName;
        String msgId;
        String extra; // 额外数据。当传输文件时，文件名称将存放在此字段中。
        long createAt;
        long contentLength;
    }

    /**
     * 消息体定义
     */
    static class MsgBody {
        /**
         * 消息体使用流的方式保存，实现任意数据的交换。
         */
        private InputStream inputStream;

        /**
         * 用户发出文件时，通过文件构造的消息，此时文件保存到此字段，该字段对应的
         * 文件是用户文件，不是临时文件，不能随便删除。
         */
        File userFile;
        /**
         * 收到大于1mb的消息时，消息内容将缓存到文件，存储到此字段
         * 如果有缓存文件，此字段将指向本消息对应的缓存文件
         */
        File cacheFile;
    }

    /**
     * 客户端与服务端消息传递协议定义。
     * <p>
     * 消息的组成：
     * <p>消息头 - 长度固定 4096</p>
     * <p>消息体 - 长度由消息头中的 contentLength 决定</p>
     * </p>
     */
    static class Msg {

        MsgHead msgHead;
        MsgBody msgBody;


        static Msg CreateSystemMsg(int type, String value) {
            try {
                return new Msg(type, "0", "0", value);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 创建一个文件消息。通过此方式传送大文件 （图片、视频、音频、文件）
         *
         * @param type     消息格式
         * @param fromName 来自
         * @param toName   发送给
         * @param file     文件
         */
        Msg(int type, String fromName, String toName, File file) throws Exception {
            this(type, fromName, toName, file.getName(), new FileInputStream(file), file.length());
            msgBody.userFile = file;
        }

        /**
         * 根据输入流创建一个流消息，既然是输入流，此构造方法可以创建任意消息内容（文本、图像、视频、音频、文件）
         *
         * @param type          消息类型
         * @param fromName      来自
         * @param toName        发送给
         * @param inputStream   输入流
         * @param contentLength 流的可读长度
         */
        Msg(int type, String fromName, String toName, String extra, InputStream inputStream, long contentLength) throws Exception {

            // 只允许 文本，图像，视频，文件，音频 使用流传播
            if (1 > type || type > 6) {
                throw new Exception("不支持的消息类型");
            }

            // 构建消息头
            MsgHead msgHead = new MsgHead();
            msgHead.msgType = type;
            msgHead.fromName = fromName;
            msgHead.toName = toName;
            msgHead.msgId = UUID.randomUUID().toString().substring(0, 6);
            msgHead.contentLength = contentLength;
            msgHead.createAt = System.currentTimeMillis();
            msgHead.extra = extra;

            // 创建消息体。
            MsgBody msgBody = new MsgBody();
            msgBody.inputStream = inputStream;

            this.msgHead = msgHead;
            this.msgBody = msgBody;
        }

        /**
         * 创建一个文本消息
         *
         * @param type     消息类型
         * @param fromName 来自
         * @param toName   发送给
         * @param value    值 内容
         */
        Msg(int type, String fromName, String toName, String value) {
            byte[] bytes = new byte[0];
            try {
                bytes = value.getBytes(Constant.CHAR_SET);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

            MsgHead msgHead = new MsgHead();
            msgHead.msgType = type;
            msgHead.fromName = fromName;
            msgHead.toName = toName;
            msgHead.msgId = UUID.randomUUID().toString().substring(0, 6);
            msgHead.contentLength = bytes.length;
            msgHead.createAt = System.currentTimeMillis();

            MsgBody msgBody = new MsgBody();
            msgBody.inputStream = inputStream;

            this.msgHead = msgHead;
            this.msgBody = msgBody;
            this.value = value;
        }

        /**
         * 从流当中读取消息内容
         *
         * @param inputStream 输入流
         * @throws Exception 在读的过程中，如果断开链接，那么此方法可能报错。
         */
        Msg(BufferedInputStream inputStream) throws Exception {
            // 读取消息头原
            // 消息头是通过对象输出流发送的，所以通过对象输入流读取
            MsgHead msgHead;
            try {

                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                msgHead = (MsgHead) objectInputStream.readObject();

            } catch (Exception e) {
                throw new Exception("Socket closed");
            }

            // 读取消息体内容
            MsgBody msgBody = new MsgBody();

            // 如果内容长度大于了 1mb 或者是文件 则将内容缓存到文件。
            if (msgHead.contentLength >= 1024 * 1024 ||
                    msgHead.msgType == MsgHead.TYPE_PIC ||
                    msgHead.msgType == MsgHead.TYPE_VIDEO ||
                    msgHead.msgType == MsgHead.TYPE_VOICE ||
                    msgHead.msgType == MsgHead.TYPE_FILE) {
                // 创建零时文件
                File tempFile = new File(Constant.WORK_DIR + msgHead.extra);
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                Constant.streamCopy(inputStream, fileOutputStream, msgHead.contentLength);
                fileOutputStream.flush();
                fileOutputStream.close();
                msgBody.cacheFile = tempFile;
                msgBody.inputStream = new FileInputStream(tempFile);
            } else {
                // 将内容直接读入内存
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Constant.streamCopy(inputStream, outputStream, msgHead.contentLength);
                outputStream.close();
                msgBody.inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            }
            this.msgHead = msgHead;
            this.msgBody = msgBody;

        }


        // 流只能读一次，所以这里做一个缓存
        String value = null;

        String getTxtBody() {
            if (msgHead.msgType == MsgHead.TYPE_TXT ||
                    (10 < msgHead.msgType && msgHead.msgType <= 200)) {
                if (value != null) {
                    return value;
                }
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    InputStreamReader reader = new InputStreamReader(msgBody.inputStream, Constant.CHAR_SET);
                    char datas[] = new char[512];
                    int size;
                    while ((size = reader.read(datas)) != -1) {
                        stringBuilder.append(datas, 0, size);
                    }
                    reader.close();
                    value = stringBuilder.toString();
                    return value;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "[解析文本消息错误：" + e.getMessage() + "]";
                }
            } else {
                return "[不是文本消息]";
            }
        }

        void write(BufferedOutputStream outputStream) throws Exception {
            write(outputStream, true);
        }

        /**
         * 输出消息
         *
         * @param outputStream 输出流
         * @param deleteCache  当传递 true 时，会检测此消息是否有缓存文件，如果有，将在消息发出后自动删除。
         */
        void write(BufferedOutputStream outputStream, boolean deleteCache) throws Exception {

            // 先输出head
            new ObjectOutputStream(outputStream).writeObject(msgHead);

            // 然后输出body
            Constant.streamCopy(msgBody.inputStream, outputStream, msgHead.contentLength);
            // 发送完毕，关闭输入流。
            msgBody.inputStream.close();

            if (deleteCache) {
                if (msgBody.cacheFile != null) {
                    msgBody.cacheFile.delete();
                }
            }
        }

        interface SendListener {
            // 消息发送完成回调。
            void onEnd(Msg msg);
        }
    }

    /**
     * 下划线
     */
    static class UnderLineBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color color = g.getColor();
            g.setColor(Color.BLUE);
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
            g.setColor(color);
        }
    }

    /**
     * 一个异步任务执行类
     */
    static class Task<P, R> {
        private ITask<P, R> iTask; // 要执行的任务
        private boolean isCancel; // 是否取消了任务
        private boolean isFinish; // 标记任务是否执行完成了
        private P param; // 任务执行的参数

        private Task(ITask<P, R> iTask) {
            this.iTask = iTask;
            this.param = this.iTask.getParam();
        }

        private void setId(int id) {
            this.iTask.setId(id);
        }

        public String getDesc() {
            return this.iTask.getDesc();
        }

        /**
         * 取消这次任务。
         * <p style="color:red">
         * 此操作本质上是阻止后台任务执行完成后调用 afterRun() 方法, 没有调用这个方法，也就相当于这次任务被取消了。
         * </p>
         */
        public void cancel() {
            if (isFinish) {
                System.out.println("Task - 任务[" + iTask.getId() + "]已经运行完成了，cancel() 方法调用的意义不大。");
            }
            this.isCancel = true;
        }

        public int getId() {
            return iTask.getId();
        }

        private R rruunn(P param) throws Exception {
            return iTask.run(param);
        }

        private void onError(Exception e) {
            if (isCancel) {
                return;
            }
            iTask.onError(e);
            this.isFinish = true;
        }

        public boolean isCancel() {
            return isCancel;
        }

        public boolean isFinish() {
            return isFinish;
        }

        private void rruunnEnd(R vaue) {
            if (isCancel) {
                return;
            }
            iTask.afterRun(vaue);
            this.isFinish = true;
        }

        /**
         * 异步任务构建使用工具
         */
        public static class TaskHelper {
            private static TaskHelper taskHelper;

            // 在一个Task还没有运行完成的时候，又有多个task需要运行的时候，会尝试建立新的线程去运行这些task
            // 该字段限定了最多创建的线程个数， 当所有线程都有task正在运行的时候，接下来需要运行的task将会被
            // 放入一个集合中，等待前面的task运行完成后，这些放入集合的task将会被运行。
            private int maxThreadCount;

            /**
             * 存放需要运行的task的集合
             */
            private Vector<Task> needRunTasks;

            private ArrayList<TaskThread> taskThreads;

            private TaskHelper(int maxThreadCount) {
                this.maxThreadCount = maxThreadCount;
                if (this.maxThreadCount < 1) {
                    this.maxThreadCount = 1;
                }
                this.taskThreads = new ArrayList<>();
                this.needRunTasks = new Vector<>();
            }

            public static TaskHelper getInstance() {
                return getInstance(10);
            }

            public static TaskHelper getInstance(int maxThreadCount) {
                if (taskHelper == null) {
                    taskHelper = new TaskHelper(maxThreadCount);
                }
                return taskHelper;
            }

            public static TaskHelper newInstance(int maxThreadCount) {
                return new TaskHelper(maxThreadCount);
            }

            // 检查并启动任务线程
            private void initThread() {

                // 还没有开启任何一个任务线程
                if (taskThreads.isEmpty()) {
                    TaskThread<Object, Object> taskThread = new TaskThread<>(needRunTasks, this);
                    taskThread.start();
                    taskThreads.add(taskThread);
                    // Log.w("Microanswer", "没有开任何线程，现在开启一个线程了。");
                } else { // 线程列队中已经开启了线程的

                    // 检查当前任务数量是否大于当前已开启的线程数量，
                    // 如果大于了，则再开启线程处理这些任务，当开启的线程
                    // 数量已经达到最大线程数量， 则不再开启新线程

                    if (needRunTasks.size() >= taskThreads.size()) {

                        // 开启新的线程处理这些较多的任务
                        if (taskThreads.size() < maxThreadCount) {
                            // Log.w("Microanswer", "全部线程：" + taskThreads.size() + ", 全部任务： " + needRunTasks.size() + ", 任务较多，新开线程了。 ");
                            TaskThread<Object, Object> taskThread = new TaskThread<>(needRunTasks, this);
                            taskThread.start();
                            taskThreads.add(taskThread);
                        } else {
                            // 不再开启线程了。
                            // Log.w("Microanswer", "任务较多，线程已全部开启。");

                        }

                    } else {
                        // 任务较少，当前开启的线程足以处理这些任务
                        // Log.w("Microanswer", "任务较少， 使用现有线程运行，现有线程数：" + taskThreads.size());
                    }

                }
            }

            /**
             * 新建一个任务，但是并不会执行。<p>
             * 通过 run()方法可以执行
             * </p>
             *
             * @param iTask
             * @param id
             * @param <P>
             * @param <R>
             * @return
             */
            public <P, R> Task<P, R> newTask(ITask<P, R> iTask, int id) {
                Task<P, R> task = new Task<>(iTask);
                task.setId(id);
                return task;
            }

            /**
             * 新建一个任务，但是并不会执行。<p>
             * 通过 run()方法可以执行
             * </p>
             *
             * @param iTask
             * @param <P>
             * @param <R>
             * @return
             */
            public <P, R> Task<P, R> newTask(ITask<P, R> iTask) {
                return newTask(iTask, needRunTasks.size() + 1);
            }

            /**
             * 执行一个异步任务
             *
             * @param task 要执行的任务
             * @param <P>  任务执行过程中参数的类型
             * @param <R>  任务执行过程中结果数据的类型
             */
            public <P, R> void run(Task<P, R> task) {
                needRunTasks.add(task);

                initThread();

                synchronized (this) {
                    try {
                        this.notifyAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            /**
             * 执行一个异步任务
             *
             * @param iTask 要执行的任务
             * @param <P>   任务执行过程中参数的类型
             * @param <R>   任务执行过程中结果数据的类型
             * @return 返回任务对象
             */
            public <P, R> Task<P, R> run(ITask<P, R> iTask) {
                Task<P, R> task = newTask(iTask);
                run(task);
                return task;
            }


            public void stopAfterLastTaskFlish() {
                if (taskThreads != null) {
                    for (TaskThread t : taskThreads) {
                        t.stopAfterLastTaskFlish();
                    }
                    taskThreads.clear();
                }
            }
        }

        /**
         * 用于执行Task的线程。
         */
        private static class TaskThread<P, R> extends Thread {
            private Object lock;

            // 标记这个线程是否正在工作中。
            private boolean isRunning = false;

            // 任务列队
            private Vector needRunTasks;

            private TaskThread(Vector needRunTasks, TaskHelper taskHelper) {
                this.needRunTasks = needRunTasks;
                this.lock = taskHelper;
            }

            @Override
            public void run() {
                // Log.i("Mic", "线程开始：" + Thread.currentThread().getName());

                while (isRunning) {
                    try {
                        final Task<P, R> task;
                        synchronized (lock) {
                            // 在集合中没有需要运行的task的时候，线程进入wait状态，该线程将运行到这里暂停
                            while (needRunTasks.isEmpty()) {
                                // Log.i("Mic", "任务集合为空，暂停：" + Thread.currentThread().getName());
                                if (!isRunning) return;
                                lock.wait();
                            }
                            task = (Task<P, R>) needRunTasks.remove(0);
                            // Log.w("Microanswer", "线程：" + getName() + ", 取出任务，剩下：" + needRunTasks.size() + "个任务");
                        }

                        // 在新的task加入的时候，会通知该线程醒过来，继续执行任务。


                        // 执行task的内容。
                        final Object[] returnData = new Object[2];
                        returnData[0] = null; // 用于保存结果
                        returnData[1] = null; // 用于保存异常
                        try {
                            returnData[0] = task.rruunn(task.param);
                        } catch (Exception e) {
                            returnData[1] = e;
                            task.onError((Exception) returnData[1]);
                        }

                        // 没有抛错，继续执行主线程通知。
                        if (returnData[1] == null) {
                            // 执行完成，通知
                            task.rruunnEnd((R) returnData[0]);
                        }

                        // Log.i("Mic", "执行完成:" + Thread.currentThread().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Log.i("Mic", "线程结束:" + Thread.currentThread().getName());
            }

            /**
             * 开启线程执行
             */
            @Override
            public synchronized void start() {
                isRunning = true;
                super.start();
            }

            /**
             * 调用该方法后，此线程将在运行完成当前的任务后终止。
             */
            private void stopAfterLastTaskFlish() {
                isRunning = false;
                try {
                    synchronized (lock) {
                        lock.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        public static abstract class ITask<P, R> {

            /**
             * 任务描述
             */
            private String desc;
            /**
             * 任务唯一标识
             */
            private int id;

            public ITask() {
            }

            public ITask(int id) {
                this.setId(id);
            }

            /**
             * 重写该方法返回参数，该方法运行在主线
             *
             * @return 返回参数
             */
            public P getParam() {
                return null;
            }

            /**
             * 子线程
             *
             * @param param 参数
             * @return 返回运行结果
             */
            abstract public R run(P param) throws Exception;

            /**
             * 主线程
             *
             * @param value 结果
             */
            public void afterRun(R value) {
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDesc() {
                return desc;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getId() {
                return id;
            }

            public void onError(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 线性布局 （此类仅适用于本项目。请不要复制到别处使用）
     */
    static class LinearLayout implements LayoutManager2 {
        static final int HORIZONTAL = 2;
        static final int VERTICAL = 1;

        private static final int MAX = 1;
        private static final int MIN = 2;
        private static final int PERFECT = 3;

        int orientation;

        LinearLayout() {
            this.orientation = VERTICAL;
        }


        @Override
        public void addLayoutComponent(Component comp, Object constraints) {
        }

        @Override
        public float getLayoutAlignmentX(Container target) {
            return 0f;
        }

        @Override
        public float getLayoutAlignmentY(Container target) {
            return 0f;
        }

        @Override
        public void invalidateLayout(Container parent) {
            int totleH = 0;
            int componentCount = parent.getComponentCount();
            for (int index = 0; index < componentCount; index++) {
                Component component = parent.getComponent(index);
                Dimension preferredSize = new Dimension(component.getPreferredSize());
                totleH += preferredSize.height;
            }
            parent.setPreferredSize(new Dimension(0, totleH));
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        public Dimension maximumLayoutSize(Container target) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public Dimension minimumLayoutSize(Container target) {
            return new Dimension(0, 0);
        }

        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(0, 0);
        }

        @Override
        public void layoutContainer(Container parent) {
            // System.out.println("layoutContainer, parent:" + parent);
            int componentCount = parent.getComponentCount();
            int width = parent.getWidth();
            int height = parent.getHeight();

            int x = 0, y = 0;
            for (int index = 0; index < componentCount; index++) {
                Component component = parent.getComponent(index);
                Dimension preferredSize = new Dimension(component.getPreferredSize());
                if (orientation == HORIZONTAL) {
                    // 水平排列
                    preferredSize.height = height;
                } else if (orientation == VERTICAL) {
                    // 垂直排列
                    preferredSize.width = width;
                }
                component.setBounds(x, y, preferredSize.width, preferredSize.height);
                if (orientation == HORIZONTAL) {
                    // 水平排列
                    x += preferredSize.width;
                } else if (orientation == VERTICAL) {
                    // 垂直排列
                    y += preferredSize.height;
                }

            }
        }
    }
}

