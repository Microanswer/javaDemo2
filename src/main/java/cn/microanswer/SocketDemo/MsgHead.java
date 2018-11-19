package cn.microanswer.socketdemo;

import java.io.Serializable;

/**
 * 消息头定义
 */
class MsgHead implements Serializable {
    private static final long serialVersionUID = 102790028741581905L;

    // 用户级别消息
    public static final int TYPE_TXT = 1; // 文本消息
    public static final int TYPE_PIC = 2; // 图像消息
    public static final int TYPE_VIDEO = 3; // 视频消息
    public static final int TYPE_VOICE = 4; // 音频消息
    public static final int TYPE_FILE = 5; // 文件消息

    // 系统级别消息
    public static final int TYPE_SYSTEM_SETNAME_ID = 11; // 客户端设置名称
    public static final int TYPE_SYSTEM_REQUEST_ALL_USER = 12;
    public static final int TYPE_SYSTEM_CREATE_ROOM = 13; // 客户端请求建立聊天室。
    public static final int TYPE_SYSTEM_REQUEST_EXIT = 14; // 客户端请求退出登录。下线。

    // 通知级别消息
    public static final int TYPE_NOTIFY_MSG_SUCCESS = 101; // 通知某条消息发送成功。
    public static final int TYPE_NOTIFY_USER_ONLINE = 102; // 通知某用户上线了。
    public static final int TYPE_NOTIFY_USER_OFFLINE = 103; // 通知某用户下线了。
    public static final int TYPE_NOTIFY_USER_RENAMED = 104; // 通知某用户改名字了。

    private int msgType = 1;
    private String fromClientId;
    private String fromName;
    private String toClientId;
    private String toName;
    private String msgId;
    private String extra; // 额外数据。当传输文件时，文件名称将存放在此字段中。
    private long createAt;
    private long contentLength;

    void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    void setFromClientId(String fromClientId) {
        this.fromClientId = fromClientId;
    }

    void setToClientId(String toClientId) {
        this.toClientId = toClientId;
    }

    void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    void setExtra(String extra) {
        this.extra = extra;
    }

    void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    void setFromName(String fromName) {
        this.fromName = fromName;
    }

    void setToName(String toName) {
        this.toName = toName;
    }

    public String getFromName() {
        return fromName;
    }

    public String getToName() {
        return toName;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getFromClientId() {
        return fromClientId;
    }

    public String getToClientId() {
        return toClientId;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getExtra() {
        return extra;
    }

    public long getCreateAt() {
        return createAt;
    }

    public long getContentLength() {
        return contentLength;
    }
}