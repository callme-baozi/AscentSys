package com.ascent.ui;

import com.ascent.bean.User;
import com.ascent.utils.UserDataClient;
import com.system.Utils.LoginUtil;
import com.system.Utils.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class RegistFrame {
    private JFrame parentFrame;
    private UserDataClient userDataClient;


    private JTextField userField;
    private JPasswordField pswField;
    private JPasswordField sureField;

    JFrame jf = new JFrame("注册界面");
    private final int WIDTH = 500;
    private final int HEIGHT = 300;

    public RegistFrame() {
    }

    public RegistFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void init() throws Exception{
        // 居中显示
        jf.setResizable(false);
        jf.setBounds((ScreenUtils.getSreenWidth() - WIDTH) / 2, (ScreenUtils.getSreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        Box userBox = Box.createHorizontalBox();
        JLabel userLabel = new JLabel("用户名:");
        userField = new JTextField(15);
        userBox.add(userLabel);
        userBox.add(Box.createHorizontalStrut(20));
        userBox.add(userField);

        Box pswBox = Box.createHorizontalBox();
        JLabel pswLabel = new JLabel("密    码:");
        pswField = new JPasswordField(15);
        pswBox.add(pswLabel);
        pswBox.add(Box.createHorizontalStrut(20));
        pswBox.add(pswField);

        Box sureBox = Box.createHorizontalBox();
        JLabel sureLabel = new JLabel("确认密码:");
        sureField = new JPasswordField(15);
        sureBox.add(sureLabel);
        sureBox.add(Box.createHorizontalStrut(10));
        sureBox.add(sureField);

        Box btnBox = Box.createHorizontalBox();
        JButton registBtn = new JButton("注册");
        JButton backBtn = new JButton("返回登录界面");
        registBtn.addActionListener(RegistLister);
        backBtn.addActionListener(e -> {
            jf.setVisible(false);
            jf.dispose();
            try {
                parentFrame.setVisible(true);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        btnBox.add(registBtn);
        btnBox.add(Box.createHorizontalStrut(40));
        btnBox.add(backBtn);

        vBox.add(Box.createVerticalStrut(40));
        vBox.add(userBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pswBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(sureBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        JPanel jp = new JPanel();
        jp.add(vBox);

        jf.add(jp);
        jf.setVisible(true);
        userDataClient=new UserDataClient();

    }


    public static void main(String[] args) {
        try {
            new RegistFrame().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 注册键监听器
    ActionListener RegistLister = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            HashMap<String, User>userTabel= userDataClient.getUsers();
            String inUsername = userField.getText();
            String inPassword=new String(pswField.getPassword());
            String inSure=new String(sureField.getPassword());
            boolean bo=false;
            if (userTabel.containsKey(inUsername)){
                JOptionPane.showMessageDialog(jf,"用户已存在！");
            }else{
                if (!inPassword.equals(inSure)){
                    JOptionPane.showMessageDialog(jf,"两次密码不一致！");
                }else{
                    bo=userDataClient.addUser(inUsername,inPassword);
                }
            }
            if (bo){
                JOptionPane.showMessageDialog(jf,"注册成功!");
            }

        }


    };

}
