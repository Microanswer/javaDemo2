package cn.microanswer.SocketDemo;

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
    public class Msg {

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
            if (msgBody.inputStream == null) {
                msgBody.inputStream = new FileInputStream(msgBody.cacheFile);
            }
            Constant.streamCopy(msgBody.inputStream, outputStream, msgHead.contentLength);
            // 发送完毕，关闭输入流。
            msgBody.inputStream.close();

            if (deleteCache) {
                if (msgBody.cacheFile != null) {
                    Constant.deleteFile(msgBody.cacheFile);
                }
            }
        }

        interface SendListener {
            // 消息发送完成回调。
            void onEnd(Msg msg);
        }
    }