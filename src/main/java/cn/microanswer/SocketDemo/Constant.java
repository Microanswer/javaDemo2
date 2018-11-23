package cn.microanswer.socketdemo;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 常量类
 */
public class Constant {
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
    static final int CLIENT_WINDOW_WIDTH = 550;
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
     *
     * @param str 要判断的字符串
     * @return true 如果为空， 否则false
     */
    static boolean isEmpty(String str) {
        return Utils.isStringEmpty(str);
    }

    /**
     * 从 inputStream 读取 length 长度到 outputStream 中
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param length       长度
     */
    static void streamCopy(InputStream inputStream, OutputStream outputStream, long length) throws Exception {
        Utils.streamCopy(inputStream, outputStream, length);
    }

    /**
     * 删除目录 或 文件
     *
     * @param f 要删除的目录或文件
     */
    static void deleteFile(File f) {
        Utils.deleteFile(f);
    }


}