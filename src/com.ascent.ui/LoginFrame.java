package com.ascent.ui;
/**
 * TODO @写明类的作用
 */

import com.ascent.bean.User;
import com.ascent.utils.UserDataClient;
import com.system.Utils.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

public class LoginFrame {
    // 用户名输入框，定义在成员变量处方便获取内容
    private JTextField userField;
    // 密码输入框
    private JPasswordField passwordField;
    // 调用getProduct方法，获取用户信息，判断与输入的信息是否相符
    private UserDataClient userDataClient;
    // 定义一个窗口，设定名字
    JFrame jf = new JFrame("AscentSys登录");
    // 登录界面的宽和高
    final int WIDTH = 500;
    final int HEIGHT = 300;

    // init初始化方法
    public void init() throws Exception {
        // 设置系统图标
        jf.setIconImage(ImageIO.read(new File("AscentSys\\images\\logo.png")));
        // 设置窗口大小和位置
        jf.setBounds((ScreenUtils.getSreenWidth() - WIDTH) / 2, (ScreenUtils.getSreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        // 创建背景组件，设置图片和大小
        BackGroundComponent bgPanel = new BackGroundComponent(ImageIO.read(new File("AscentSys\\images\\bg.jpg")));
        bgPanel.setBounds(0, 0, WIDTH, HEIGHT);
        // 组装主体
        Box vBox = Box.createVerticalBox();
        // 组装用户名
        Box userBox = Box.createHorizontalBox(); // 水平布局的Box
        JLabel userLabel = new JLabel("用户名:"); // Jlabel可以存放文字与图片
        userLabel.setForeground(Color.WHITE); // 设置文字颜色，因为背景为暗色，白色为了突出文字
        userField = new JTextField(15); // 输入框的大小 15列
        userBox.add(userLabel); // 添加各组件到Box中
        userBox.add(Box.createHorizontalStrut(10)); // 增加水平间距
        userBox.add(userField);
        // 组装密码
        Box passwordBox = Box.createHorizontalBox();
        JLabel passwordLabel = new JLabel("密    码:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField(15);
        passwordBox.add(passwordLabel);
        passwordBox.add(Box.createHorizontalStrut(10));
        passwordBox.add(passwordField);
        // 组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton loginBtn = new JButton("登录");
        JButton exitBtn = new JButton("退出");
        JButton registBtn = new JButton("注册");
        btnBox.add(loginBtn);
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(exitBtn);
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(registBtn);
        // 给按钮注册监听器
        exitBtn.addActionListener(ExitActionListener);
        loginBtn.addActionListener(LoginActionListener);
        registBtn.addActionListener(RegistActionListener);
        // 将水平box放进垂直box
        vBox.add(Box.createVerticalStrut(60)); // 增加垂直间距
        vBox.add(userBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(passwordBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);
        // 将垂直box放进bgPanel
        bgPanel.add(vBox);
        // 将bgPanel放进frame里
        jf.add(bgPanel);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 点×键退出
        jf.setResizable(false); // 不可调整窗口大小
        jf.setVisible(true); // 窗口可视化
        userDataClient=new UserDataClient(); // 要先初始化，调用默认构造函数，后边才可以调用getProduct方法

    }

    public static void main(String[] args) {
        // 在该类运行窗口，方便测试
        try {
            new LoginFrame().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 退出键事务处理
    ActionListener ExitActionListener = e -> {
        jf.setVisible(false); // 窗口设置不可见，但不是关掉了窗口
        jf.dispose(); // 回收资源
        userDataClient.closeSocKet(); // 关闭客户端服务器
    };
    // 登录键处理
    ActionListener LoginActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 定义一个map，存放所有用户信息
            HashMap<String, User>userTabel= userDataClient.getUsers();
            // 获取输入的用户名
            String inUsername = userField.getText();
            // 获取输入的密码
            String inPassword = new String(passwordField.getPassword());
            // 判断信息是否正确
            if (userTabel.containsKey(inUsername)){ // map中如果包含该用户名的key
                String password =userTabel.get(inUsername).getPassword(); // 获取对应的value：密码
                if (password.equals(inPassword)){ // 密码如果相等
                    jf.setVisible(false); // 关掉登录窗口
                    jf.dispose();
                    userDataClient.closeSocKet();
                    try {
                        User user=userTabel.get(inUsername);
                        new MainFrame().init(user); // 进入主界面
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                else{  // 密码不正确，则弹出窗口
                    JOptionPane.showMessageDialog(jf,"密码错误！");
                }
            }else { // 不包含用户名，则弹出窗口
                JOptionPane.showMessageDialog(jf,"用户不存在！");
            }
        }
    };
    // 注册键事务处理
    ActionListener RegistActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jf.setVisible(false); // 当前登录窗口不可见
            try {
                new RegistFrame(jf).init();  // 进入注册窗口
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    };
}
