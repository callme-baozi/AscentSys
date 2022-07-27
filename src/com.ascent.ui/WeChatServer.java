// 服务器端程序
package com.ascent.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class WeChatServer {
    JFrame jf = new JFrame("Server窗口");
    final int WIDTH = 500;
    final int HEIGHT = 300;
    JTextPane textPane;

    JScrollPane jScrollPane;
    JTextField jTextField;

    BufferedReader is;
    OutputStreamWriter ow;
    JPanel jPanel;
    ServerSocket server;
    Socket socket;

    public static void main(String[] args) {
        WeChatServer we = new WeChatServer();
        we.init();
    }

    public void init() {
        textPane = new JTextPane();
        jf.setBounds(500, 500, WIDTH, HEIGHT);
        setDocs("初始化完成  " + new Date().toString(), Color.BLACK, true, 10);
        jScrollPane = new JScrollPane(textPane);  // 滚动条
        jf.add(jScrollPane,BorderLayout.CENTER);
        jPanel = new JPanel();
        jTextField =new JTextField(30);
        jTextField.addActionListener(sendListener); // 回车监听
        jPanel.add(jTextField);
        jf.add(jPanel,BorderLayout.SOUTH);
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);

        try {
            server = new ServerSocket(4700);
            socket = server.accept();
            String line;
            // 指定输入输出编码，防止编译环境导致的乱码
            is = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            ow=new OutputStreamWriter(socket.getOutputStream(),"utf-8");
            while (true) {
                line=is.readLine();
                receive("Client  ",line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive(String name, String str) {
        name = name + new Date().toString(); // 加上发送时间
        setDocs(name, Color.GREEN, false, 15);
        setDocs(str, Color.BLACK, false, 20);
        textPane.validate(); // 刷新文本区
        textPane.repaint();
    }

    public void insert(String str, AttributeSet attrSet) {
        Document doc = textPane.getDocument();
        str = "\n" + str;
        try {
            doc.insertString(doc.getLength(), str, attrSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void setDocs(String str, Color col, boolean bold, int fontSize) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet(); // 定义字体样式
        StyleConstants.setForeground(attrSet, col);
        if (bold == true) {
            StyleConstants.setBold(attrSet, true); // 粗体
        }
        StyleConstants.setFontSize(attrSet, fontSize);
        insert(str, attrSet);
    }

    ActionListener sendListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = jTextField.getText().trim();
            if("".equals(str))return;  // 清空文本框不产生监听
            System.out.println("Server:" + str);
            try {
                ow.write(str+"\n");
                ow.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            receive("Server  ", str);
            jTextField.setText("");  // 发送完毕清空文本
        }
    };
}
