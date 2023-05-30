package com.ascent.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.ascent.bean.Product;
import com.ascent.bean.User;
import com.system.Utils.FileUtils;
import com.system.Utils.LoginUtil;

/**
 * 购物车
 * @author ascent
 * @version 1.0
 */
public class ShoppingCart {
    /**
     * 用户信息
     */
    private User user;

    /**
     * 存放购买商品信息
     */
    private static ArrayList<Product> shoppingList;

    /**
     * 购物车信息文件名
     */
    protected static final String SHOPPINGCART_FILE_NAME = "shoppingCart";

    /**
     * 获取某用户的购物车信息
     * @return shoppingList
     */
    public ArrayList<Product> getShoppingList(User user) {
        this.user=user;
//        shoppingList= LoginUtil.getProductsByUser(this.user);
//        System.out.println("获取初始购物车的user="+this.user);
        StringTokenizer st = null;// 用于分隔字符串
        Product productObject = null;
        String line = "";

        String username,productname, cas, structure, formula, price, realstock, category,number;
        shoppingList=new ArrayList<>();
        try {
            log("读取文件: " + SHOPPINGCART_FILE_NAME + "...");// 文件名

            BufferedReader inputFromFile1 = new BufferedReader(new InputStreamReader(new FileInputStream(SHOPPINGCART_FILE_NAME),"utf-8"));

            while ((line = inputFromFile1.readLine()) != null) {

                st = new StringTokenizer(line, ",");// 以"，"分割产品信息

                username=st.nextToken().trim();
                productname = st.nextToken().trim();
                cas = st.nextToken().trim();
                structure = st.nextToken().trim();
                formula = st.nextToken().trim();
                price = st.nextToken().trim();
                realstock = st.nextToken().trim();
                category = st.nextToken().trim();
                number=st.nextToken().trim();

                if(username.equals(user.getUsername())){
                    productObject = new Product(productname, cas, structure,formula, price, realstock, category,Integer.valueOf(number));
                    shoppingList.add(productObject);
                }
            }
            inputFromFile1.close();
            log("文件读取结束!");

        } catch (Exception exc) {
            log("没有找到文件: " + SHOPPINGCART_FILE_NAME +".");
            log(exc);
        }
        return this.shoppingList;
    }

    /**
     * 添加商品到购物车
     * @param myProduct
     */
    public int addProduct(Product myProduct,User user) {
        this.user=user;
        Product product;
        shoppingList=this.getShoppingList(user);
        boolean bo = false;
        for (int i = 0; i < shoppingList.size(); i++) {
            product = shoppingList.get(i);
            if (myProduct.getProductname().trim().equals(product.getProductname().trim())) {
                bo = true;
                break;
            }
        }
        if (!bo) {
            shoppingList.add(myProduct);
            // 同步更新信息到数据库
//            System.out.println("同步更新信息到数据库的user"+this.user);
//            LoginUtil.addProductToShoppingCart(myProduct,this.user);
            // IO流添加到文件
            FileUtils.addProductToShoppingCart(myProduct,user);

            return 1;
        }
        return 0;
    }


    /**
     * 清空购物车所购买商品
     */
    public void clearProduct() {
        shoppingList.clear();
        FileUtils.clearCart(user);
    }

    /**
     * 日志打印
     * @param msg
     */
    protected void log(Object msg) {
        System.out.println("ShoppingCart类: " + msg);
    }

}
