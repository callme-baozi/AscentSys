package com.system.Utils;

import com.ascent.bean.Product;
import com.ascent.bean.User;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileUtils {
    /**
     * 商品信息数据文件名
     */
    protected static final String PRODUCT_FILE_NAME = "AscentSys\\product";

    /**
     * 用户信息数据文件名
     */
    protected static final String USER_FILE_NAME = "AscentSys\\user";
    /**
     * 购物车信息文件名
     */
    protected static final String SHOPPINGCART_FILE_NAME = "AscentSys\\shoppingCart";


    /**
     * 添加商品到shoppingCart的IO操作
     * @param product
     * @param user
     * @return
     */
    public static int addProductToShoppingCart(Product product, User user) {
        String productinfo = user.getUsername()+","+product.getProductname()+ ","+product.getCas()+ ","+product.getStructure()+ ","+product.getFormula()+ ","+product.getPrice()+ ","+product.getRealstock()+ ","+product.getCategory()+","+product.getNumber();
        try {
//            RandomAccessFile fos = new RandomAccessFile(SHOPPINGCART_FILE_NAME,"rws");
//            fos.seek(fos.length());
//            fos.write(("\n" + user.getUsername()+","+productinfo).getBytes());
//            fos.close();
            // 以上方法没有指定编码，会使文件不可访问
            FileOutputStream fos = new FileOutputStream(SHOPPINGCART_FILE_NAME, true); // true是追加模式
            fos.write(("\n"+productinfo).getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 添加商品到product的IO操作
     * @param product
     * @return
     */
    public static int addProductToProduct(Product product) {
        String productinfo = product.getProductname()+ ","+product.getCas()+ ","+product.getStructure()+ ","+product.getFormula()+ ","+product.getPrice()+ ","+product.getRealstock()+ ","+product.getCategory();
        try {
            FileOutputStream fos = new FileOutputStream(PRODUCT_FILE_NAME, true); // true是追加模式
            fos.write(("\n"+productinfo).getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 根据传入的productArrayList遍历shoppingCar文件，按顺序修改user用户的商品数量
     * @param productArrayList
     * @param user
     * @return
     */
    public static int updateShoppingCart(ArrayList<Product> productArrayList, User user) {
        ArrayList<String >shoppingList=new ArrayList<>();
        try {
            // 读取数据到shoppingList，除user的数据以外
            InputStream is = new FileInputStream(SHOPPINGCART_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(is, "utf-8"); // 指定读取编码
            BufferedReader br = new BufferedReader(isr);
            String len;
            while ((len = br.readLine()) != null) {
                if(!len.startsWith(user.getUsername())){
                    shoppingList.add(len);
                }
            }
            br.close();
            isr.close();
            is.close();

            String info;
            for (Product product : productArrayList) {
                info = user.getUsername()+","+product.getProductname()+ ","+product.getCas()+ ","+product.getStructure()+ ","+product.getFormula()+ ","+product.getPrice()+ ","+product.getRealstock()+ ","+product.getCategory()+","+product.getNumber();
                shoppingList.add(info);
            }

            // 重新写入
            FileOutputStream fos=new FileOutputStream(new File(SHOPPINGCART_FILE_NAME));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8"); // 指定写入编码
            BufferedWriter  bw=new BufferedWriter(osw);

            int i=0;
            for(i=0;i<shoppingList.size()-1;i++){
                bw.write(shoppingList.get(i)+"\n");
            }
            bw.write(shoppingList.get(i));
            bw.flush();

            bw.close();
            osw.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;

    }

    /**
     * 根据user删除某条数据
     * @param product
     * @param user
     * @return
     */
    public static int deleteProductFromCart(Product product, User user) {
        ArrayList<String >shoppingList=new ArrayList<>();
        String productinfo = user.getUsername()+","+product.getProductname()+ ","+product.getCas()+ ","+product.getStructure()+ ","+product.getFormula()+ ","+product.getPrice()+ ","+product.getRealstock()+ ","+product.getCategory()+","+product.getNumber();
        try {
            // 读取数据到shoppingList，除productinfo的数据以外
            InputStream is = new FileInputStream(SHOPPINGCART_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(is, "utf-8"); // 指定读取编码
            BufferedReader br = new BufferedReader(isr);
            String len;
            while ((len = br.readLine()) != null) {
                if(!len.equals(productinfo)){
                    shoppingList.add(len);
                }
            }
            br.close();
            isr.close();
            is.close();

            // 重新写入
            FileOutputStream fos=new FileOutputStream(new File(SHOPPINGCART_FILE_NAME));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8"); // 指定写入编码
            BufferedWriter  bw=new BufferedWriter(osw);

            // 因为添加时会加入了换行符\n,所以每次重新写入时，最后一条不要加换行符，不然删除后在添加就会有空一行
            int i=0;
            for(i=0;i<shoppingList.size()-1;i++){
                bw.write(shoppingList.get(i)+"\n");
            }
            bw.write(shoppingList.get(i));
            bw.flush();

            bw.close();
            osw.close();
            fos.close();


        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 清空user购物车
     * @param user
     */
    public static void clearCart(User user) {
        ArrayList<String >shoppingList=new ArrayList<>();
        try {
            // 读取数据到shoppingList，除user的数据以外
            InputStream is = new FileInputStream(SHOPPINGCART_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(is, "utf-8"); // 指定读取编码
            BufferedReader br = new BufferedReader(isr);
            String len;
            while ((len = br.readLine()) != null) {
                if(!len.startsWith(user.getUsername())){
                    shoppingList.add(len);
                }
            }
            br.close();
            isr.close();
            is.close();

            // 重新写入
            FileOutputStream fos=new FileOutputStream(new File(SHOPPINGCART_FILE_NAME));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8"); // 指定写入编码
            BufferedWriter  bw=new BufferedWriter(osw);
            int i=0;
            for(i=0;i<shoppingList.size()-1;i++){
                bw.write(shoppingList.get(i)+"\n");
            }
            bw.write(shoppingList.get(i));
            bw.flush();

            bw.close();
            osw.close();
            fos.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除product某条数据
     * @param myProduct
     * @return
     */
    public static int deleteProduct(Product myProduct) {
        ArrayList<String >shoppingList=new ArrayList<>();
        try {
            // 读取数据到shoppingList，除user的数据以外
            InputStream is = new FileInputStream(PRODUCT_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(is, "utf-8"); // 指定读取编码
            BufferedReader br = new BufferedReader(isr);
            String len;
            while ((len = br.readLine()) != null) {
                if(!len.startsWith(myProduct.getProductname())){
                    shoppingList.add(len);
                }
            }
            br.close();
            isr.close();
            is.close();

            // 重新写入
            FileOutputStream fos=new FileOutputStream(new File(PRODUCT_FILE_NAME));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8"); // 指定写入编码
            BufferedWriter  bw=new BufferedWriter(osw);
            int i=0;
            for(i=0;i<shoppingList.size()-1;i++){
                bw.write(shoppingList.get(i)+"\n");
            }
            bw.write(shoppingList.get(i));
            bw.flush();

            bw.close();
            osw.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 修改某条商品信息
     * 这个除了产品名，其他信息都可能有改动，感觉用方案一好一点。
     * @param product
     * @return
     */
    public static int updateProduct(Product product) {
        ArrayList<String >shoppingList=new ArrayList<>();
        try {
            // 读取数据到shoppingList，除user的数据以外
            InputStream is = new FileInputStream(PRODUCT_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(is, "utf-8"); // 指定读取编码
            BufferedReader br = new BufferedReader(isr);
            String len;
            while ((len = br.readLine()) != null) {
                if(!len.startsWith(product.getProductname())){
                    shoppingList.add(len);
                }else{
                    String productinfo =product.getProductname()+ ","+product.getCas()+ ","+product.getStructure()+ ","+product.getFormula()+ ","+product.getPrice()+ ","+product.getRealstock()+ ","+product.getCategory();
                    shoppingList.add(productinfo);
                }
            }
            br.close();
            isr.close();
            is.close();

            // 重新写入
            FileOutputStream fos=new FileOutputStream(new File(PRODUCT_FILE_NAME));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8"); // 指定写入编码
            BufferedWriter  bw=new BufferedWriter(osw);
            int i=0;
            for(i=0;i<shoppingList.size()-1;i++){
                bw.write(shoppingList.get(i)+"\n");
            }
            bw.write(shoppingList.get(i));
            bw.flush();

            bw.close();
            osw.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}
