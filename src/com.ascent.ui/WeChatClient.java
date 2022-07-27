// 客户端程序
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
import java.net.Socket;
import java.util.Date;

public class WeChatClient {
    JFrame jf = new JFrame("Client窗口");
    final int WIDTH = 500;
    final int HEIGHT = 300;
    JTextPane textPane;

    JScrollPane jScrollPane;
    JTextField jTextField;

    BufferedReader is;
    OutputStreamWriter ow;
    JPanel jPanel;
    Socket socket;

    public static void main(String[] args) {
        WeChatClient we = new WeChatClient();
        we.init();
    }

    public void init() {
        textPane = new JTextPane();
        jf.setBounds(500, 500, WIDTH, HEIGHT);
        setDocs("初始化完成  " + new Date().toString(), Color.BLACK, true, 10);
        jScrollPane = new JScrollPane(textPane);
        jf.add(jScrollPane,BorderLayout.CENTER);
        jPanel = new JPanel();
        jTextField =new JTextField(30);
        jTextField.addActionListener(sendListener);
        jPanel.add(jTextField);
        jf.add(jPanel,BorderLayout.SOUTH);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);


        try{
            socket=new Socket("10.161.126.62",4700);
            ow = new OutputStreamWriter(socket.getOutputStream(),"utf-8");
            is=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            String line;
            while(true){
                line=is.readLine();
                receive("Server  ",line);
            }
        }catch(Exception e) {
            System.out.println("Error"+e); // 异常处理
        }
    }


    public void receive(String name, String str) {
        name = name + new Date().toString();
        setDocs(name, Color.GREEN, false, 15);
        setDocs(str, Color.BLACK, false, 20);
        textPane.validate();
        textPane.repaint();
    }

    public void insert(String str, AttributeSet attrSet) {
        Document doc = textPane.getDocument();
        str = "\n" + str;
        try {
            doc.insertString(doc.getLength(), str, attrSet);
        } catch (BadLocationException e) {
            System.out.println("BadLocationException:   " + e);
        }
    }

    public void setDocs(String str, Color col, boolean bold, int fontSize) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attrSet, col);
        //颜色
        if (bold == true) {
            StyleConstants.setBold(attrSet, true);
        }//字体类型
        StyleConstants.setFontSize(attrSet, fontSize);
        //字体大小
        insert(str, attrSet);
    }

    ActionListener sendListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = jTextField.getText().trim();
            if("".equals(str))return;
            System.out.println("Client:" + str);
            try {
                ow.write(str+"\n");
                ow.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            receive("Client  ", str);
            jTextField.setText("");
        }
    };
}
