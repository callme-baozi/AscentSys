package com.ascent.utils;


import com.ascent.bean.Product;
import com.ascent.bean.User;

import java.util.*;

/**
 * 这个抽象类定义了如何读取一个数据文件。
 * 它提供的方法可以用来获得产品的分类和具体的产品信息
 * @author ascent
 * @version 1.0
 */
public abstract class DataAccessor {

    /**
     * 存放产品信息的HashMap/Hashtable .
     */
    protected TreeMap<String, ArrayList<Product>> dataTable;

    /**
     * 存放用户信息的HashMap/Hashtable .
     */
    protected HashMap<String, User> userTable;

    /**
     * 最近增加的产品
     */
    protected ArrayList<Product> recentProductList;

    /**
     * 默认构造方法
     */
    public DataAccessor() {
        dataTable = new TreeMap<>();
        userTable = new HashMap<String,User>();
        recentProductList = new ArrayList<Product>();
    }
    /**
     * 拷贝构造，我自己加的，防止浅拷贝
     */
    public DataAccessor(DataAccessor dataAccessor){
        this.dataTable=new TreeMap<String, ArrayList<Product>>(dataAccessor.dataTable);
        this.userTable=new HashMap<String, User>(dataAccessor.userTable);
        this.recentProductList=new ArrayList<Product>(dataAccessor.recentProductList);
    }

    /**
     * 获得产品分类名称集合
     * @return categorySet 产品分类名称集合
     */
    public ArrayList<String> getCategories() {
        Set<String> categorySet = dataTable.keySet();
        log("获得分类...");
        ArrayList<String> categories = new ArrayList<String>(categorySet);
        // 排序
        Collections.sort(categories);
        log("完成获得分类!\n");
        return categories;
    }

    /**
     * 获得某类产品的集合
     * @param category 分类名称
     * @return productList 商品集合
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Product> getProducts(String category) {
        log("获得产品集合信息， 它们属于: " + category);
        ArrayList<Product> productList = dataTable.get(category);// 通过key获取值，value是一个ArrayList对象
        log("该类产品数量：" + productList.size());
        // 排序
        Collections.sort(productList);
        log("完成获得产品集合信息!\n");
        return productList;
    }

    /**
     * 获取用户
     * @return userTable Key:用户名，Value:用户对象
     */
    public HashMap<String,User> getUsers() {
        return this.userTable;
    }

    /**
     * 增加新的产品
     * @param theProduct 被添加到购物车的商品
     */
    public void addProduct(Product theProduct) {
        String category = theProduct.getCategory();
        log("添加新的产品:  " + theProduct);
        ArrayList<Product> productList = dataTable.get(category);
        productList.add(theProduct); // 在原来的产品集合中加入新产品
        recentProductList.add(theProduct); // 在新加产品集合中加入新产品，原本该集合为空的
        log("完成添加新的产品!\n");
    }

    /**
     * 从文件中读取数据
     */
    public abstract void load(); // 给子类重写用的

    /**
     * 向文件中保存数据
     */
    public abstract void save(User user);

    /**
     * 日志方法
     */
    protected void log(Object msg) {
        System.out.println("数据存取类　Data Accessor:  " + msg);
    }
}
