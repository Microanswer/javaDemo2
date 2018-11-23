package cn.microanswer.socketdemo;

import java.io.*;
import java.util.UUID;

/**
 * 客户端与服务端消息传递协议定义。
 * <p>
 * 消息的组成：
 * <p>消息头 - 长度固定 4096</p>
 * <p>消息体 - 长度由消息头中的 contentLength 决定</p>
 * </p>
 */
class Msg {
    // 字符编码
    private static final String CHAR_SET = Constant.CHAR_SET;
    // 缓存目录 （请以 / 结尾）
    private static final String CACHE_DIR = Constant.WORK_DIR;

    private MsgHead msgHead;
    private MsgBody msgBody;
    private String text = null; // 如果是文本消息，则此字段是文本内容。

    /**
     * 创建一个文件消息。通过此方式传送大文件 （图片、视频、音频、文件）
     *
     * @param type 消息格式
     * @param from 来自
     * @param to   发送给
     * @param file 文件
     */
    public Msg(
            int type,
            String from,
            String fromName,
            String to,
            String toName,
            File file
    ) throws Exception {
        this(type, from, fromName, to, toName, file.getName(), new FileInputStream(file), file.length());
        msgBody.setSendFile(file);
    }

    /**
     * 创建一个文本消息
     *
     * @param type         消息类型
     * @param fromClientId 来自
     * @param toClientId   发送给
     * @param text         值 内容
     */
    public Msg(
            int type,
            String fromClientId,
            String fromName,
            String toClientId,
            String toName,
            String text
    ) {
        byte[] bytes = new byte[0];
        try {
            if (text == null) text = "";
            bytes = text.getBytes(CHAR_SET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        MsgHead msgHead = new MsgHead();
        msgHead.setMsgType(type);
        msgHead.setFromName(fromName);
        msgHead.setFromClientId(fromClientId);
        msgHead.setToClientId(toClientId);
        msgHead.setToName(toName);
        msgHead.setMsgId(UUID.randomUUID().toString().substring(0, 6));
        msgHead.setContentLength(bytes.length);
        msgHead.setCreateAt(System.currentTimeMillis());

        MsgBody msgBody = new MsgBody();
        msgBody.setInputStream(inputStream);

        this.msgHead = msgHead;
        this.msgBody = msgBody;
        this.text = text;
    }

    /**
     * 根据输入流创建一个流消息，既然是输入流，此构造方法可以创建任意消息内容（文本、图像、视频、音频、文件）
     *
     * @param type          消息类型
     * @param fromClientId  来自
     * @param fromName      来自
     * @param toClientId    发送给
     * @param toName
     * @param inputStream   输入流
     * @param contentLength 流的可读长度
     */
    private Msg(
            int type,
            String fromClientId,
            String fromName,
            String toClientId,
            String toName,
            String extra,
            InputStream inputStream,
            long contentLength
    ) throws Exception {

        // 只允许 文本，图像，视频，文件，音频 使用流传播
        if (1 > type || type > 6) {
            throw new Exception("不支持的消息类型");
        }

        // 构建消息头
        MsgHead msgHead = new MsgHead();
        msgHead.setMsgType(type);
        msgHead.setFromClientId(fromClientId);
        msgHead.setFromName(fromName);
        msgHead.setToClientId(toClientId);
        msgHead.setToName(toName);
        msgHead.setMsgId(UUID.randomUUID().toString().substring(0, 6));
        msgHead.setContentLength(contentLength);
        msgHead.setCreateAt(System.currentTimeMillis());
        msgHead.setExtra(extra);

        // 创建消息体。
        MsgBody msgBody = new MsgBody();
        msgBody.setInputStream(inputStream);

        this.msgHead = msgHead;
        this.msgBody = msgBody;
    }

    /**
     * 从流当中读取消息内容.
     *
     * @param inputStream 输入流 ，这个流应该是 socket 的输入流，不能是别的。
     * @throws Exception 在读的过程中，如果断开链接，那么此方法可能报错。
     */
    Msg(InputStream inputStream) throws Exception {

        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // 读取消息头原
        // 消息头是通过对象输出流发送的，所以通过对象输入流读取
        MsgHead msgHead = (MsgHead) objectInputStream.readObject();


        // 读取消息体内容
        MsgBody msgBody = new MsgBody();
        int msgType = msgHead.getMsgType();
        // 如果内容长度大于了 1mb 或者是文件 则将内容缓存到文件。
        if (msgHead.getContentLength() >= 1024 * 1024 ||
                msgType == MsgHead.TYPE_PIC ||
                msgType == MsgHead.TYPE_VIDEO ||
                msgType == MsgHead.TYPE_VOICE ||
                msgType == MsgHead.TYPE_FILE) {
            // 创建零时文件
            File receiveFile = new File(CACHE_DIR + msgHead.getExtra());
            FileOutputStream fileOutputStream = new FileOutputStream(receiveFile);
            Constant.streamCopy(inputStream, fileOutputStream, msgHead.getContentLength());
            fileOutputStream.flush();
            fileOutputStream.close();
            msgBody.setReceiveFile(receiveFile);
        } else {
            // 数据量较小，可以将内容直接读入内存
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Constant.streamCopy(inputStream, outputStream, msgHead.getContentLength());
            outputStream.close();
            msgBody.setInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        }

        this.msgHead = msgHead;
        this.msgBody = msgBody;
    }

    public String getText() {
        if (text != null) return text;
        int msgType = msgHead.getMsgType();
        if (msgType == MsgHead.TYPE_TXT || (10 < msgType && msgType <= 200)) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                InputStreamReader reader = new InputStreamReader(msgBody.getInputStream(), CHAR_SET);
                char datas[] = new char[512];
                int size;
                while ((size = reader.read(datas)) != -1) {
                    stringBuilder.append(datas, 0, size);
                }
                reader.close();
                text = stringBuilder.toString();
                return text;
            } catch (Exception e) {
                e.printStackTrace();
                return "[解析文本消息错误：" + e.getMessage() + "]";
            }
        } else {
            return "[不是文本消息]";
        }
    }

    public MsgHead getMsgHead() {
        return msgHead;
    }

    public MsgBody getMsgBody() {
        return msgBody;
    }

    /**
     * 输出消息
     *
     * @param outputStream 输出流
     * @throws Exception 错误信息
     */
    public void write(BufferedOutputStream outputStream) throws Exception {
        write(outputStream, true);
    }

    /**
     * 输出消息
     *
     * @param outputStream      输出流
     * @param deleteReceiveFile 当传递 true 时，会检测此消息是否有缓存文件，如果有，将在消息发出后自动删除。
     */
    public void write(BufferedOutputStream outputStream, boolean deleteReceiveFile) throws Exception {

        // 先输出head
        new ObjectOutputStream(outputStream).writeObject(msgHead);

        // 然后输出body
        if (msgBody.getInputStream() == null) {
            File sendFile = msgBody.getSendFile();
            if (null == sendFile) {
                sendFile = msgBody.getReceiveFile();
            }
            msgBody.setSendFile(sendFile);
            msgBody.setInputStream(new FileInputStream(sendFile));
        }
        Constant.streamCopy(msgBody.getInputStream(), outputStream, msgHead.getContentLength());
        // 发送完毕，关闭输入流。
        msgBody.getInputStream().close();

        if (deleteReceiveFile) {
            if (msgBody.getReceiveFile() != null) {
                Constant.deleteFile(msgBody.getReceiveFile());
            }
        }

    }
}