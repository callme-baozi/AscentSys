package com.ascent.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.ascent.bean.Product;
import com.ascent.bean.User;
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
    private static ArrayList<Product> shoppingList = new ArrayList<>();

    /**
     * 获取所有购买商品信息
     * @return shoppingList
     */
    public ArrayList<Product> getShoppingList(User user) {
        this.user=user;
        shoppingList= LoginUtil.getProductsByUser(this.user);
//        System.out.println("获取初始购物车的user="+this.user);
        return this.shoppingList;
    }

    /**
     * 添加商品到购物车
     * @param myProduct
     */
    public int addProduct(Product myProduct,User user) {
        this.user=user;
        Product product;
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
            LoginUtil.addProductToShoppingCart(myProduct,this.user);
            return 1;
        }
        return 0;
    }

    /**
     * 清空购物车所购买商品
     */
    public void clearProduct() {
        shoppingList.clear();
        LoginUtil.clearCart(user);
    }

}
