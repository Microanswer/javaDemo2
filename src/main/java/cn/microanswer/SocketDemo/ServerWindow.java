package cn.microanswer.SocketDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * 服务窗口。
 */
public class ServerWindow extends JFrame implements Server.ServerListener {
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
            if (server.getClientCount() > 0) {
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
                server.close();
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
        JOptionPane.showMessageDialog(ServerWindow.this, "<html><p>客户端出错：</p>" + e.getMessage() + "</html>", Constant.CLIENT_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onClientConnted(Client client, String name, String id) {
        System.out.println(name + ", 已连接");
        connectedCountLabel.setText("当前已连接人数：" + server.getClientCount());
        jList.setListData(server.getClients());
        invalidate();
    }

    @Override
    public void onClientUpdate(Client client, String name, String id) {
        jList.setListData(server.getClients());
        jList.updateUI();
    }

    @Override
    public void onClientDisco(Client client) {
        System.out.println(client.getClientNamme() + ", 已断开");
        connectedCountLabel.setText("当前已连接人数：" + server.getClientCount());
        jList.setListData(server.getClients());
        invalidate();
    }

}
