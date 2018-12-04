package cn.microanswer;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.Vector;

public class Test29 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月19日 15:15:45
     */
    public static void main(String[] args) throws Exception {
        JFrame j = new JFrame("测试");
        j.setBounds(200, 200, 200, 300);
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container contentPane = j.getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        final Vector<User> users = new Vector<>();
        final JList<User> userJList = new JList<>(users);
        JScrollPane scrollPane = new JScrollPane(userJList);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton jButton = new JButton("添加");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User u = new User();
                        u.name = String.valueOf(Math.random());
                        u.id = UUID.randomUUID().toString();
                        users.add(u);
                        userJList.updateUI();
                    }
                }).start();
            }
        });
        contentPane.add(jButton, BorderLayout.SOUTH);

        j.setVisible(true);
    }

    static class User {
        String name;
        String id;

        @Override
        public String toString() {
            return name;
        }
    }
}
