package cn.microanswer.SocketDemo;

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
}

