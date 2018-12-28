package cn.microanswer.SocketDemo;

import java.io.File;
import java.io.InputStream;

/**
 * 消息体定义
 */
class MsgBody {
    private InputStream inputStream;
    private File sendFile;
    private File receiveFile;

    /**
     * 消息体使用流的方式保存，实现任意数据的交换。
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * 用户发出文件时，通过文件构造的消息，此时文件保存到此字段，该字段对应的
     * 文件是用户文件，不是临时文件，不能随便删除。
     */
    public File getSendFile() {
        return sendFile;
    }

    /**
     * 收到大于1mb的消息时，消息内容将缓存到文件，存储到此字段
     * 如果有缓存文件，此字段将指向本消息对应的缓存文件
     */
    public File getReceiveFile() {
        return receiveFile;
    }

    void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    void setSendFile(File sendFile) {
        this.sendFile = sendFile;
    }

    void setReceiveFile(File receiveFile) {
        this.receiveFile = receiveFile;
    }
}
