package cn.microanswer.SocketDemo;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
     * 客户端窗口
     */
    public class ClientWindow extends JFrame {
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

