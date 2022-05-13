package com.ascent.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.io.*;
import com.ascent.bean.Product;
import com.ascent.bean.User;
import com.system.Utils.LoginUtil;

import javax.swing.*;

/**
 * 产品数据读取的实现类
 * @author ascent
 * @version 1.0
 */
public class ProductDataAccessor extends DataAccessor {

    // ////////////////////////////////////////////////////
    //
    // 产品文件格式如下
    // 产品名称,化学文摘登记号,结构图,公式,价格,数量,类别
    // ----------------------------------------------------
    //
    // ////////////////////////////////////////////////////

    // ////////////////////////////////////////////////////
    //
    // 用户文件格式如下
    // 用户帐号,用户密码,用户权限
    // ----------------------------------------------------
    //
    // ////////////////////////////////////////////////////
    /**
     * 商品信息数据文件名
     */
    protected static final String PRODUCT_FILE_NAME = "AscentSys\\product";

    /**
     * 用户信息数据文件名
     */
    protected static final String USER_FILE_NAME = "AscentSys\\user";

    /**
     * 数据记录的分割符  record_separator
     */
    protected static final String RECORD_SEPARATOR = "----------";

    /**
     * 默认构造方法
     */
    public ProductDataAccessor() {
        load();
    }

    /**
     * 读取数据的方法
     * 将数据表的信息放入dataTable、userTable中
     */
    @Override
    public void load() {

        dataTable = new TreeMap<>();
        userTable = new HashMap<String,User>();

        ArrayList<Product> productArrayList = null;
        StringTokenizer st = null;// 用于分隔字符串

        Product productObject = null;
        User userObject = null;
        String line = "";

        String productname, cas, structure, formula, price, realstock, category;
        String userName, password, authority;
        // 将连接数据库放在这里
        Connection con = null;
        Statement stat = null;
        ResultSet re = null;

        try {
            log("读取文件: " + PRODUCT_FILE_NAME + "...");// 文件名

            BufferedReader inputFromFile1 = new BufferedReader(new FileReader(PRODUCT_FILE_NAME));

            while ((line = inputFromFile1.readLine()) != null) {

                st = new StringTokenizer(line, ",");// 以"，"分割产品信息

                productname = st.nextToken().trim(); //返回从当前位置到下一个分隔符的字符串。
                cas = st.nextToken().trim();
                structure = st.nextToken().trim();
                formula = st.nextToken().trim();
                price = st.nextToken().trim();
                realstock = st.nextToken().trim();
                category = st.nextToken().trim();
                // 用读取的信息创建一个Product，赋值给productObject
                productObject = getProductObject(productname, cas, structure,formula, price, realstock, category);

                if (dataTable.containsKey(category)) {
                    productArrayList = dataTable.get(category);// 如果类名存在，就将产品加入对应分类的产品集合
                } else {
                    productArrayList = new ArrayList<Product>();
                    dataTable.put(category, productArrayList);
                    // 如果类名不存在，就在数据表加入新类名和对应的产品集合，该集合目前为空
                }
                productArrayList.add(productObject); // 将产品加入到对应的产品集合
            }

            // TODO 这里总的药品获取还是用的数据库
//            ArrayList<Product> all=LoginUtil.getAllProducts();
////            System.out.println(all);
//            dataTable.put("药品",all);

            inputFromFile1.close();
            log("文件读取结束!");

            line = "";
            log("读取文件: " + USER_FILE_NAME + "...");
            BufferedReader inputFromFile2 = new BufferedReader(new FileReader(USER_FILE_NAME));
            while ((line = inputFromFile2.readLine()) != null) {

                st = new StringTokenizer(line, ",");

                userName = st.nextToken().trim();
                password = st.nextToken().trim();
                authority = st.nextToken().trim();
                // 通过已有数据创建一个user对象
                userObject = new User(userName, password, Integer.parseInt(authority));

                if (!userTable.containsKey(userName)) {
                    userTable.put(userName, userObject);
                }
            }
            inputFromFile2.close();
            log("文件读取结束!");
            log("准备就绪!\n");
        } catch (Exception exc) {
            log("没有找到文件: " + PRODUCT_FILE_NAME + " 或 "+USER_FILE_NAME+".");
            log(exc);
        }
    }

    /**
     * 返回带有这些参数的商品对象
     * @param productName 药品名称
     * @param cas 化学文摘登记号
     * @param structure 结构图名称
     * @param formula 公式
     * @param price 价格
     * @param realstock 数量
     * @param category 类别
     * @return new Product(productName, cas, structure, formula, price, realstock, category);
     */
    private Product getProductObject(String productName, String cas,
                                     String structure, String formula, String price, String realstock, String category) {
        return new Product(productName, cas, structure, formula, price, realstock, category);
    }

    /**
     * 保存数据
     * 将user对象加入数据表user.db中
     * TODO 这里有问题，用户名是惟一的，不能重复，添加失败的话这里没有设置返回值
     */
    @Override
    public void save(User user) {
        log("读取文件: " + USER_FILE_NAME + "...");
        try {
            String userinfo = user.getUsername() + "," + user.getPassword() + "," + user.getAuthority();
            RandomAccessFile fos = new RandomAccessFile(USER_FILE_NAME, "rws");
            System.out.println(USER_FILE_NAME);
            fos.seek(fos.length()); // 从这里写入
            fos.write(("\n" + userinfo).getBytes()); // 按字节写入
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 日志方法.
     */
    @Override
    protected void log(Object msg) {
        System.out.println("ProductDataAccessor类: " + msg);
    }

    @Override
    public HashMap<String,User> getUsers() {
        this.load();  // 读取数据的方法
        return this.userTable;
    }
}