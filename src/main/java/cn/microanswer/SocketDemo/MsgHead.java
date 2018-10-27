package cn.microanswer.SocketDemo;

import java.io.Serializable;

/**
 * 消息头定义
 */
public class MsgHead implements Serializable {

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