package com.ascent.utils;

import java.io.*;
import java.net.*;
import java.util.*;
import com.ascent.bean.Product;

/**
 * 这个类连接数据服务器来获得数据
 * @author ascent
 * @version 1.0
 */
public class ProductDataClient implements ProtocolPort {

    /**
     * socket引用
     */
    protected Socket hostSocket;

    /**
     * 输出流的引用
     */
    protected ObjectOutputStream outputToServer;

    /**
     * 输入流的引用
     */
    protected ObjectInputStream inputFromServer;

    /**
     * 默认构造方法
     */
    public ProductDataClient() throws IOException {
        this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
        // hhj:调用本类下其他构造器，也就是下面这个有参构造
    }

    /**
     * 接受主机名和端口号的构造方法
     */
    public ProductDataClient(String hostName, int port) throws IOException {

        log("连接数据服务器..." + hostName + ":" + port);

        hostSocket = new Socket(hostName, port);
        outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
        inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

        log("连接成功.");
    }

    /**
     * 返回类别集合
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getCategories() throws IOException {

        ArrayList<String> categoryList = null;

        try {
            log("发送请求: OP_GET_PRODUCT_CATEGORIES");
            outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCT_CATEGORIES);
            outputToServer.flush();

            log("接收数据...");
            categoryList = (ArrayList<String>) inputFromServer.readObject();
            log("收到 " + categoryList.size() + " 类别.");
        } catch (ClassNotFoundException exc) {
            log("=====>>>  异常: " + exc);
            throw new IOException("找不到相关类");
        }

        return categoryList;
    }

    /**
     * 返回产品集合
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Product> getProducts(String category) throws IOException {

        ArrayList<Product> productList = null;

        try {
            log("发送请求: OP_GET_PRODUCTS  类别 = " + category);
            outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCTS);
            outputToServer.writeObject(category);
            outputToServer.flush();

            log("接收数据...");
            productList = (ArrayList<Product>)inputFromServer.readObject();
            log("收到 " + productList.size() + " 产品.");
        } catch (ClassNotFoundException exc) {
            log("=====>>>  异常: " + exc);
            throw new IOException("找不到相关类");
        }
        return productList;
    }

    /**
     * 获取全部产品集合
     * @param
     * @return
     * @throws IOException
     */
    public ArrayList<Product> getAllProducts() throws IOException {
        ArrayList<Product> productList = null;
        try {
            log("发送请求: OP_GET_PRODUCTS  类别 = " + "药品");
            outputToServer.writeInt(ProtocolPort.OP_GET_ALL_PRODUCTS);
            outputToServer.writeObject("药品");
            outputToServer.flush();

            log("接收数据...");
            productList = (ArrayList<Product>)inputFromServer.readObject();
            log("收到 " + productList.size() + " 产品.");
        } catch (ClassNotFoundException exc) {
            log("=====>>>  异常: " + exc);
            throw new IOException("找不到相关类");
        }
        return productList;
    }

    /**
     * 重新读取文件
     * @return
     * @throws IOException
     */
    public void updata(){
        try {
            log("发送请求: UPDATA  重新读取");
            outputToServer.writeInt(ProtocolPort.UPDATA);
            outputToServer.flush();

//            log("接收数据...");
//            inputFromServer.readObject();
            log("读取结束.");
        } catch (Exception exc) {
            log("=====>>>  异常: " + exc);
        }
    }



    /**
     * 日志方法.
     */
    protected void log(Object msg) {
        System.out.println("ProductDataClient类: " + msg);
    }
}