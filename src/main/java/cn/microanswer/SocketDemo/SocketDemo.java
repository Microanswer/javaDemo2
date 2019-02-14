package cn.microanswer.SocketDemo;

import javax.swing.*;
import java.awt.*;

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
    public static void main(String args[]) throws Exception {
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

             // 开启程序
              SwingUtilities.invokeAndWait(new Runnable() {
                  @Override
                  public void run() {
                       new OptionWindow(Toolkit.getDefaultToolkit()).setVisible(true);
                  }
              });

         } catch (Exception e) {
             e.printStackTrace();
         }
        System.out.println("结束");
    }
}

