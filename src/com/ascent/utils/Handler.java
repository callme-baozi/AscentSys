package com.ascent.utils;

import java.io.*;
import java.net.*;
import java.util.*;

import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 这个类是socket连接的处理器 例如：
 * <pre>
 * Handler aHandler = new Handler(clientSocket, myProductDataAccessor);
 * aHandler.start();
 * </pre>
 * @author ascent
 * @version 1.0
 */
public class Handler extends Thread implements ProtocolPort {

    protected Socket clientSocket;

    protected ObjectOutputStream outputToClient;

    protected ObjectInputStream inputFromClient;

    protected ProductDataAccessor myProductDataAccessor;

    protected boolean done;

    /**
     * 带两个参数的构造方法
     * @param theClientSocket 客户端Socket对象
     * @param theProductDataAccessor 处理商品数据的对象
     * @throws IOException 构造对象时可能发生IOException异常
     */
    public Handler(Socket theClientSocket,ProductDataAccessor theProductDataAccessor) throws IOException {
        clientSocket = theClientSocket;
        outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
        inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
        myProductDataAccessor = theProductDataAccessor;
        done = false;
    }

    /**
     * 执行多线程的run()方法，处理客户端发送的命令
     */
    @Override
    public void run() {

        try {
            while (!done) { // done为false时启动while循环

                log("等待命令...");

                int opCode = inputFromClient.readInt();
                log("opCode = " + opCode);

                switch (opCode) {
                    case ProtocolPort.OP_GET_PRODUCT_CATEGORIES: // 100
                        opGetProductCategories();
                        break;
                    case ProtocolPort.OP_GET_PRODUCTS: // 101
                        opGetProducts();
                        break;
                    case ProtocolPort.OP_GET_USERS: //102
                        opGetUsers();
                        break;
                    case ProtocolPort.OP_ADD_USERS: //103
                        opAddUser();
                        break;
                    case ProtocolPort.OP_GET_ALL_PRODUCTS: // 104
                        opGetAllProducts();
                        break;
                    case ProtocolPort.UPDATA:
                        opUpdata();
                    default:
                        System.out.println("错误代码");
                }
            }
        } catch (IOException exc) {
            log(exc);
        }
    }

    /**
     * 返回用户信息
     */
    private void opGetUsers() {
        try {
            HashMap<String,User> userTable = myProductDataAccessor.getUsers();
            outputToClient.writeObject(userTable);
            outputToClient.flush();
        } catch (IOException exe) {
            log("发生异常：" + exe);
        }
    }

    /**
     * 返回分类名称
     */
    protected void opGetProductCategories() {
        try {
            ArrayList<String> categoryList = myProductDataAccessor.getCategories();
            outputToClient.writeObject(categoryList);// 写入客户端
            outputToClient.flush();
            log("发出 " + categoryList.size() + " 类别信息到客户端");
        } catch (IOException exc) {
            log("发生异常:  " + exc);
        }
    }

    /**
     * 返回某个分类名称的所有商品
     */
    protected void opGetProducts() {
        try {
            log("读取份类信息");
            String category = (String) inputFromClient.readObject();
            log("类别是 " + category);

            ArrayList<Product> recordingList = myProductDataAccessor.getProducts(category);

            outputToClient.writeObject(recordingList);
            outputToClient.flush();
            log("发出 " + recordingList.size() + "个产品信息到客户端.");
        } catch (IOException exc) {
            log("发生异常:  " + exc);
            exc.printStackTrace();
        } catch (ClassNotFoundException exc) {
            log("发生异常:  " + exc);
            exc.printStackTrace();
        }
    }

    /**
     * 返回所有商品
     */
    protected void opGetAllProducts() {
        try {
            log("读取份类信息");
            String category = (String) inputFromClient.readObject(); // 明确知道字符串是“药品”
            log("类别是 " + category);

            ArrayList<Product> recordingList = myProductDataAccessor.getAllProducts(category);

            outputToClient.writeObject(recordingList);
            outputToClient.flush();
            log("发出 " + recordingList.size() + "个产品信息到客户端.");
        } catch (IOException exc) {
            log("发生异常:  " + exc);
            exc.printStackTrace();
        } catch (ClassNotFoundException exc) {
            log("发生异常:  " + exc);
            exc.printStackTrace();
        }
    }

    /**
     * 处理用户注册
     * 从客户端接收user对象，写入user文件
     */
    public void opAddUser() {
        try {
            User user = (User) this.inputFromClient.readObject();
            this.myProductDataAccessor.save(user);// 注册就是将用户信息加入数据库的表user.db
        } catch (IOException e) {
            log("发生异常:  " + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log("发生异常:  " + e);
            e.printStackTrace();
        }
    }

    /**
     * 重新读取product
     */
    public void opUpdata() {
        try {
            myProductDataAccessor.load();
        } catch (Exception e) {
            log("发生异常:  " + e);
            e.printStackTrace();
        }
    }

    /**
     * 处理线程运行时标志
     * @param flag
     */
    public void setDone(boolean flag) {
        done = flag;
    }

    /**
     * 打印信息到控制台
     * @param msg
     */
    protected void log(Object msg) {
        System.out.println("处理器: " + msg);
    }

}