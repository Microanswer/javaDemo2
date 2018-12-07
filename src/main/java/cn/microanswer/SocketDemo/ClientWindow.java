package cn.microanswer.SocketDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 客户端窗口
 */
class ClientWindow extends JFrame {
    private static final String ACTION_COMMAND_SEND = "send";
    private static final String ACTION_COMMAND_CLEAR = "clear";
    private static final String ACTION_COMMAND_CHOOSE_PIC = "choosepic";
    private static final String ACTION_COMMAND_CHOOSE_FILE = "choosefile";

    private Toolkit toolkit;
    private Client client;

    private JLabel userNameLabel;
    private JTabbedPane jTabbedPane;
    private Map<String, Room> talkingRooms; // 正在聊天的聊天室队列
    // 登录弹出框里面的控件
    private JDialog dialog;
    private JButton buttonLogin;
    // 查看所有在线人员的弹出框
    private JList<Room.Member> allUserList; // 显示所有在线的人的列表
    private Vector<Room.Member> allOnlines; // 保存所有在线的
    // 修改我的名称按钮
    private JLabel modifyMyName;

    ClientWindow(Toolkit toolkit) {
        super(Constant.CLIENT_WINDOW_TITLE);
        this.toolkit = toolkit;
        this.talkingRooms = new HashMap<>();

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
                    setVisible(false);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                Task.TaskHelper.getInstance().stopAfterLastTaskFlish();
            }
        };
        addWindowListener(windowAdapter);

        // 不允许改变大小
        setResizable(false);

        // 弹出连接服务端的信息录入窗口
        showLoginDialog();
    }

    /**
     * 显示登录服务器弹出框
     */
    private void showLoginDialog() {

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
                (int) Math.round((screenSize.getWidth() - 240) / 2f),
                (int) Math.round((screenSize.getHeight() - 140) / 2f),
                240,
                140
        );
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel tipLabel = new JLabel("服务器url：");
        contentPane.add(tipLabel);
        final JTextField urlField = new JTextField();
        urlField.setPreferredSize(new Dimension(100, 20));
        urlField.setText(Constant.CLIENT_DEFALUT_SERVER_HOST);
        contentPane.add(urlField);
        JLabel tipPortLabel = new JLabel("端  口 号：");
        contentPane.add(tipPortLabel);
        final JTextField portField = new JTextField();
        portField.setPreferredSize(new Dimension(100, 20));
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
    private void initClient(String url, int port) {
        client = new Client(url, port);
        client.setClientListener(new MClientListener());
        client.start(); // 开启客户端
    }

    /**
     * 初始化客户端界面
     */
    private void initUI() {
        Container contentPane = ClientWindow.this.getContentPane();

        // NORTH 方向显示我的信息
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        userNameLabel = new JLabel("我的名称：" + (client.getClientNamme() == null ? "初始化中..." : client.getClientNamme()) + " ");
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

        northPanel.add(userNameLabel);
        northPanel.add(modifyMyName);
        contentPane.add(northPanel, BorderLayout.NORTH);

        // 中间显示 tab面板，显示多个聊天界面
        jTabbedPane = new JTabbedPane();
        contentPane.add(jTabbedPane, BorderLayout.CENTER);

        // 右边显示所有在线人员列表
        allOnlines = new Vector<>();
        allUserList = new JList<>(allOnlines);
        allUserList.setFixedCellWidth(150);
        allUserList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                value = ((Room.Member) value).getName();
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        allUserList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    Room.Member selectedValue = allUserList.getSelectedValue();
                    createRoomWith(selectedValue);
                }
            }
        });
        JScrollPane scrollAllUserListPanel = new JScrollPane(allUserList);
        scrollAllUserListPanel.setBorder(BorderFactory.createTitledBorder(Constant.ALL_ONLINE_USER));
        contentPane.add(scrollAllUserListPanel, BorderLayout.EAST);
    }

    /**
     * 弹出修改姓名的弹出框
     */
    private void showChangeMyNameDialog() {
        final JDialog dialog = new JDialog(ClientWindow.this, "修改名称");
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
                Math.round((ClientWindow.this.getHeight() - 100) / 2f + getY()),
                240,
                100
        );
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel tipLabel = new JLabel("新名称：");
        contentPane.add(tipLabel);
        final JTextField nameFiled = new JTextField();
        nameFiled.setPreferredSize(new Dimension(140, 20));
        contentPane.add(nameFiled);
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

                    client.setClientNameId(name, null);
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
    private void onGetMsg(Msg msg) {
        String fromName = msg.getMsgHead().getFromName();
        String fromId = msg.getMsgHead().getFromClientId();
        String roomId = msg.getMsgHead().getToClientId();
        String roomName = fromName;
        Room room = new Room(roomId, roomName);
        // 如果列队中有这个聊天室，只需要拿到这个聊天室，然后向聊天室中分发这条消息即可。
        Room.Member fromMember = new Room.Member(fromId, fromName);
        if (talkingRooms.containsKey(roomId)) {
            room = talkingRooms.get(roomId);
        } else {
            // 聊天列队中没有这个聊天室，则创建出这个聊天室
            room.addMember(new Room.Member(client.getClientId(), client.getClientNamme()));
            room.addMember(fromMember);
            room.setRoomListener(new TalkPanel(room));
            talkingRooms.put(roomId, room);
        }
        // 检查聊天室中是否有发件人的信息，如果没有，则将其加入
        if (!room.getMembers().contains(fromMember)) {
            room.getMembers().add(fromMember);
        }

        room.dispatchMsg(msg);
    }

    /**
     * 和某个人创建一个聊天室。
     */
    private void createRoomWith(Room.Member... members) {
        if (members.length > 0) {
            JSONObject data = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            Collections.addAll(jsonArray, members);
            data.put("others", jsonArray);
            client.sendMsg(Client.CreateSystemMsg(MsgHead.TYPE_SYSTEM_CREATE_ROOM, client.getClientId(), data.toJSONString()));
        }
    }

    /**
     * 申请创建聊天室成功后回调。
     *
     * @param room
     */
    private void onRoomCreated(Room room) {
        if (!talkingRooms.containsKey(room.getId())) {
            room.setRoomListener(new TalkPanel(room));
            talkingRooms.put(room.getId(), room);
        } else {
            Room room1 = talkingRooms.get(room.getId());
            room1.dispatchMsg(null);
        }
    }

    /**
     * 设置我的名称
     *
     * @param name 新名称
     */
    private void setLocalUserName(String name) {
        ClientWindow.this.setTitle(Constant.CLIENT_WINDOW_TITLE + " - (" + name + ")");
        if (userNameLabel != null) {
            userNameLabel.setText("我的名称：" + name + " ");
            userNameLabel.invalidate();
        }
        if (modifyMyName != null) {
            modifyMyName.setEnabled(true);
        }
    }

    private void onUserOnLine(Room.Member info) {
        if (!allOnlines.contains(info)) {
            allOnlines.add(info);
        }
        allUserList.updateUI();
    }

    private void onUserOffLine(Room.Member member) {
        int i = allOnlines.indexOf(member);
        if (i >= 0) {
            allOnlines.remove(i);
        }

        // 查找所有的聊天室， 如果该下线的用户在某一个聊天室中，则关闭这个聊天室。
        Set<Map.Entry<String, Room>> entries = talkingRooms.entrySet();
        for (Map.Entry<String, Room> entry : entries) {
            Room value = entry.getValue();
            value.onMemberOffLine(member);
        }

        allUserList.updateUI();
    }

    // 有用户改名了。
    private void onUserRenamed(String fromClientId, String newName) {
        for (Room.Member member : allOnlines) {
            if (member.getId().equals(fromClientId)) {
                member.setName(newName);
            }
        }
        allUserList.updateUI();

        // 遍历所有的聊天室，将聊天室中的该用户也更名
        for (Map.Entry<String, Room> entry : talkingRooms.entrySet()) {
            entry.getValue().memberReNamed(fromClientId, newName);
        }
    }

    /**
     * 服务器告知所有在线的人员时，会调用此方法。
     *
     * @param res 所有数据
     */
    private void onAllOnlieUserGeted(JSONArray res) {
        if (!Utils.isListEmpty(res)) {
            // 排除自己
            for (int i = 0; i < res.size(); i++) {
                JSONObject jsonObject = res.getJSONObject(i);
                if (client.getClientId().equals(jsonObject.getString("id"))) {
                    continue;
                }
                String name = jsonObject.getString("name");
                String id = jsonObject.getString("id");
                allOnlines.add(new Room.Member(id, name));
            }

            allUserList.setListData(allOnlines);
            allUserList.updateUI();
        }
    }

    /**
     * 聊天框， 一个人可以和多个人进行对话，每个人会产生一个聊天框。
     */
    class TalkPanel extends JPanel implements Room.RoomListener {

        // 聊天室对象
        private Room room;

        // 消息输入框
        private JTextArea textArea;
        // tab 显示房间名称的地方
        private JLabel jLabel;
        // 聊天记录展示列表
        private ArrayList<Msg> msgs;
        private JPanel msgListPanel;
        private JScrollPane scrollPane;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String actionCommand = e.getActionCommand();
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

        TalkPanel(Room room) {
            super(new BorderLayout());

            this.room = room;
            setBackground(Color.WHITE);

            this.initUI();

            // 将 tab 添加到 界面
            jTabbedPane.addTab(room.getName(), this);
            jTabbedPane.setTabComponentAt(jTabbedPane.indexOfComponent(this), getTabTitle(room.getName()));
            jTabbedPane.setSelectedComponent(this);
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
                Msg msg = new Msg(
                        isPic ? MsgHead.TYPE_PIC : MsgHead.TYPE_FILE,
                        client.getClientId(),
                        client.getClientNamme(),
                        room.getId(),
                        room.getName(),
                        f);
                client.sendMsg(msg, new Client.SendListener() {
                    @Override
                    public void onEnd(Msg msg) {
                        onMsg(msg);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 发送消息框中的消息。
         */
        void sendMsg() {
            if (textArea != null) {
                String text = textArea.getText();
                if (!Constant.isEmpty(text) && !Constant.isEmpty(text.trim())) {
                    try {
                        Msg msg = new Msg(
                                MsgHead.TYPE_TXT,
                                client.getClientId(),
                                client.getClientNamme(),
                                room.getId(),
                                room.getName(),
                                text
                        );
                        client.sendMsg(msg, new Client.SendListener() {
                            @Override
                            public void onEnd(Msg msg) {
                                textArea.setText("");
                                onMsg(msg);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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

        private Component getTabTitle(String tabName) {
            final JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            jPanel.setBackground(new Color(0, 0, 0, 0));
            jLabel = new JLabel(tabName);
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
                    }
                }
            });
            jLabel1.setFocusable(true);
            jPanel.add(jLabel1);
            return jPanel;
        }

        @Override
        public void onRoomGetMsg(Room room, Msg msg) {
            Container parent = getParent();
            // 聊天框还没有显示到界面，进行显示操作。
            if (null == parent) {
                jTabbedPane.addTab(room.getName(), this);
                jTabbedPane.setTabComponentAt(jTabbedPane.indexOfComponent(this), getTabTitle(room.getName()));
                jTabbedPane.setSelectedComponent(this);
            }
            if (msg != null) {
                onMsg(msg);
            }
        }

        // 聊天室中有成员更新名称。
        @Override
        public void onMemberReNamed(Room.Member member, String oldName) {
            if (jLabel != null) {
                jLabel.setText(member.getName());
                jLabel.invalidate();

                // 不将 room 的名称修改。 room 的名称用来初始化了 tab 传入的 title 。
                // room.setName(member.getName());
            }
        }

        @Override
        public void onRoomOff(Room room) {
            // 将聊天室关闭。
            int i1 = jTabbedPane.indexOfTab(room.getName());
            jTabbedPane.removeTabAt(i1);
            // 将聊天室从列队中移除
            talkingRooms.remove(room.getId());

            // 弹出提示
            if (jTabbedPane.getSelectedIndex() == jTabbedPane.indexOfComponent(this)) {
                JOptionPane.showMessageDialog(ClientWindow.this, "对方已下线");
            }
        }
    }

    class MClientListener implements Client.ClientListener {
        @Override
        public void onConneted(Client client, String host, int port) {
            System.out.println("客户端连接成功啦");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    dialog.dispose();
                    initUI();
                    setVisible(true);
                }
            });

        }

        @Override
        public void onNamed(Client client, final String name, String id) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setLocalUserName(name);
                }
            });

            // 当服务器下发了可以使用的用户名后，发出请求获取所有在线人员的请求
            client.sendMsg(Client.CreateSystemMsg(MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER, client.getClientId()));
        }

        @Override
        public void onDisConn(Client client) {
            // 删除工作目录
            File file = new File(Constant.WORK_DIR);
            if (file.exists()) {
                Constant.deleteFile(file);
            }

            // 连接被关闭，关闭窗口
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ClientWindow.this.dispose();
                }
            });

        }

        @Override
        public void onError(final Client client, final Exception e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    if (buttonLogin != null && buttonLogin.isValid()) {
                        buttonLogin.setEnabled(true);
                    }
                    String message = e.getMessage();
                    if ("Connection refused: connect".equals(message)) {
                        message = "登入失败，指定的服务器未启动。";
                    }
                    JOptionPane.showMessageDialog(ClientWindow.this, "客户端错误：" + message, Constant.ERROR, JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        @Override
        public void onMsg(final Client client, final Msg msg) {
            int msgType = msg.getMsgHead().getMsgType();
            // 这个范围内的消息，属于用户消息
            if (0 < msgType && msgType <= 10) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        onGetMsg(msg);
                    }
                });

                // 这个范围内的消息，属于系统消息
            } else if (10 < msgType && msgType <= 100) {

                // 服务器返回所有在线的成员。
                String text = msg.getText();
                if (msgType == MsgHead.TYPE_SYSTEM_REQUEST_ALL_USER) {
                    final JSONArray objects = JSON.parseArray(text);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            onAllOnlieUserGeted(objects);
                        }
                    });

                } else if (msgType == MsgHead.TYPE_SYSTEM_CREATE_ROOM) {
                    // 服务器返回聊天室创建结果
                    if ("false".equals(text)) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                onError(client, new Exception("聊天室创建失败，必须选择聊天对象。"));
                            }
                        });
                        return;
                    }

                    // 解析返回数据
                    final Room room = JSON.parseObject(text, Room.class);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            onRoomCreated(room);
                        }
                    });
                }

                // 通知类消息
            } else if (100 < msgType && msgType <= 200) {
                String text = msg.getText();
                if (msgType == MsgHead.TYPE_NOTIFY_USER_ONLINE) {
                    JSONObject info = JSON.parseObject(text);
                    final String name = info.getString("name");
                    final String id = info.getString("id");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            onUserOnLine(new Room.Member(id, name));
                        }
                    });
                } else if (msgType == MsgHead.TYPE_NOTIFY_USER_OFFLINE) {
                    JSONObject info = JSON.parseObject(text);
                    final String name = info.getString("name");
                    final String id = info.getString("id");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            onUserOffLine(new Room.Member(id, name));
                        }
                    });
                } else if (msgType == MsgHead.TYPE_NOTIFY_USER_RENAMED) {
                    final String newName = msg.getText();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            onUserRenamed(msg.getMsgHead().getFromClientId(), newName);
                        }
                    });
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
            this.isme = msg.getMsgHead().getFromClientId().equals(client.getClientId());
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

            initView();
        }

        void initView() {

            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            infoPanel.setBackground(Color.WHITE);

            // 消息发出者
            JLabel fromLabel = new JLabel("<html><b>" + (isme ? "我说" : msg.getMsgHead().getFromName()) + "</b></html>");
            fromLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            if (!isme) {
                fromLabel.setForeground(Color.BLUE);
            }
            infoPanel.add(fromLabel);

            // 消息发出时间
            JLabel timeLabel = new JLabel(simpleDateFormat.format(new Date(msg.getMsgHead().getCreateAt())));
            timeLabel.setFont(new Font(timeLabel.getFont().getName(), timeLabel.getFont().getStyle(), timeLabel.getFont().getSize() - 2));
            timeLabel.setBorder(BorderFactory.createEmptyBorder(7, 0, 0, 5));
            timeLabel.setForeground(Color.GRAY);
            infoPanel.add(timeLabel);

            add(infoPanel, BorderLayout.NORTH);


            // 文本消息
            int type = msg.getMsgHead().getMsgType();

            if (type == MsgHead.TYPE_TXT) {
                JLabel txtLabel = new JLabel("<html><p>" + msg.getText() + "</p></html>");
                txtLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                add(txtLabel);
            } else if (type == MsgHead.TYPE_PIC) {
                JLabel picLabel = new JLabel();
                picLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                try {
                    if (isme) {
                        picLabel.setIcon(new ImageIcon(reduceImage(new FileInputStream(msg.getMsgBody().getSendFile()))));
                    } else {
                        picLabel.setIcon(new ImageIcon(reduceImage(new FileInputStream(msg.getMsgBody().getReceiveFile()))));
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
                    fileLabel.setText("发送文件<" + msg.getMsgHead().getExtra() + ">");
                } else {
                    fileLabel.setText("收到文件<" + msg.getMsgHead().getExtra() + ">");
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
                            Runtime.getRuntime().exec("explorer /e,/select," + (isme ? msg.getMsgBody().getSendFile() : msg.getMsgBody().getReceiveFile()).getCanonicalPath());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                filePanel.add(seeFileLabel);

                add(filePanel, BorderLayout.CENTER);
            } else {
                JLabel txtLabel = new JLabel(msg.getText());
                txtLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                add(txtLabel, BorderLayout.CENTER);
            }

        }


    }
}

