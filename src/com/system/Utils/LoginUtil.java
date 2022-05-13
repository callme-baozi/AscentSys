package com.system.Utils;

import com.ascent.bean.Product;
import com.ascent.bean.User;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class LoginUtil {


    private LoginUtil() {
    }

    private static String driverClass;
    private static String url;
    private static String username;
    private static String password;
    private static Connection con;

    private static ArrayList<Product> allProducts;

    static {
        try {
            InputStream is = LoginUtil.class.getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            prop.load(is);

            driverClass = prop.getProperty("driverClass");
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            Class.forName(driverClass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接方法
     * @return
     */
    public static Connection getConnection() {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    //关闭资源
    public static void close(Connection con, Statement stat, ResultSet re) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (re != null) {
            try {
                re.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void close(Connection con, Statement stat) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 获取全部产品信息
     * @return
     */
    public static ArrayList<Product> getAllProducts() {
        Connection con = null;
        Statement stat = null;
        ResultSet re = null;
        allProducts = new ArrayList<Product>();
        try {
            con = LoginUtil.getConnection();
            String sql = "SELECT * FROM product";
            stat = con.createStatement();
            re = stat.executeQuery(sql);
            while (re.next()) {
                String productname = re.getString("productname");
                String cas = re.getString("cas");
                String structure = re.getString("structure");
                String formula = re.getString("formula");
                String price = re.getString("price");
                String realstock = re.getString("realstock");
                String category = re.getString("category");
                Product p = new Product(productname, cas, structure, formula, price, realstock, category);
                allProducts.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LoginUtil.close(con, stat, re);
        }
        return allProducts;
    }

    /**
     * 获取用户信息
      * @return
     */
    public static HashMap<String, User> getUserData() {
        HashMap<String, User> userTable = new HashMap<>();
        Connection con1 = null;
        Statement stat1 = null;
        ResultSet re1 = null;

        try {
            con1 = LoginUtil.getConnection();
            String sql = "SELECT * from user";
            stat1 = con1.createStatement();
            re1 = stat1.executeQuery(sql);
            while (re1.next()) {
                String username = re1.getString("userName");
                String password = re1.getString("password");
                int authority = Integer.valueOf(re1.getString("authority"));
//                System.out.println("user"+username+"的权限"+authority);
//                System.out.println(username);
//                System.out.println(password);
                User user = new User(username, password,authority);
                if (!userTable.containsKey(username)) {
                    userTable.put(username, user);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con1, stat1, re1);
        }
        return userTable;
    }

    /**
     * 添加产品的jdbc
     * @param product
     * @return  返回1表示添加成功
     */
    public static int addProduct(Product product){
        Connection con = null;
        Statement stat = null;
        int count=0;

        try {
            con=LoginUtil.getConnection();
            String sql="INSERT INTO product VALUE('"+
                    product.getProductname()+"','"+
                    product.getCas()+"','"+
                    product.getStructure()+"','"+
                    product.getFormula()+"','"+
                    product.getPrice()+"','"+
                    product.getRealstock()+"g','"+
                    product.getCategory()+"')";
            stat=con.createStatement();
            count=stat.executeUpdate(sql);
            if (count>=1)return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con,stat);
        }
        return 0;
    }

    /**
     * 获取某一分类的产品的jdbc
     * @param name
     * @return ArrayList<Product>
     */
    public static ArrayList<Product> getProductsByCategory(String name){
        Connection con = null;
        Statement stat = null;
        ResultSet re = null;
        ArrayList<Product>products = new ArrayList<Product>();
        try {
            con = LoginUtil.getConnection();
            String sql = "SELECT * FROM product WHERE category='"+name+"'";
            stat = con.createStatement();
            re = stat.executeQuery(sql);
            while (re.next()) {
                String productname = re.getString("productname");
                String cas = re.getString("cas");
                String structure = re.getString("structure");
                String formula = re.getString("formula");
                String price = re.getString("price");
                String realstock = re.getString("realstock");
                String category = re.getString("category");
                Product p = new Product(productname, cas, structure, formula, price, realstock, category);
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LoginUtil.close(con, stat, re);
        }
        return products;
    }

    /**
     * 获取某一用户的购物车的jdbc
     * TODO 这里以用户名为唯一标签获取购物车信息，或许可以用uid=user.id的标签，添加用户之后接口回调，给user设置id
     * @param user
     * @return ArrayList<Product>
     */
    public static ArrayList<Product>getProductsByUser(User user){
        Connection con = null;
        Statement stat = null;
        ResultSet re = null;
        ArrayList<Product>products = new ArrayList<>();
        try {
            con = LoginUtil.getConnection();
            String sql = "SELECT * FROM shoppingcart WHERE uid='"+user.getUsername()+"'";
            stat = con.createStatement();
            re = stat.executeQuery(sql);
            while (re.next()) {
                String productname = re.getString("productname");
                String cas = re.getString("casId");
                String structure = re.getString("structure");
                String formula = re.getString("formula");
                String price = re.getString("price");
                String realstock = re.getString("realstock");
                String category = re.getString("category");
                Product p = new Product(productname, cas, structure, formula, price, realstock, category);
                int number=re.getInt("number");
                p.setNumber(number);
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LoginUtil.close(con, stat, re);
        }
        return products;
    }



    /**
     *  执行修改某条product的sql指令
     */
    public static int updateProduct(Product product){
        Connection con = null;
        Statement stat = null;
        int count=0;
        try {
            con=LoginUtil.getConnection();
            String sql="update product set cas='"+
                    product.getCas()+
                    "',structure='"+product.getStructure()+
                    "',formula='"+product.getFormula()+
                    "',price='"+product.getPrice()+
                    "',realstock='"+product.getRealstock()+
                    "',category='"+product.getCategory()+
                    "' where productname='"+product.getProductname()+"'";
            stat=con.createStatement();
            count=stat.executeUpdate(sql);
            if (count>=1)return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con,stat);
        }
        return 0;
    }

    /**
     * 在 主界面上删除
     * 执行 删除某条product的sql语句
     */
    public static int deleteProduct(Product product){
        Connection con = null;
        Statement stat = null;
        int count=0;
        try {
            con=LoginUtil.getConnection();
            String sql="delete from product where productname='"+product.getProductname()+"'";
            stat=con.createStatement();
            count=stat.executeUpdate(sql);
            if (count>=1)return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con,stat);
        }
        return 0;
    }

    /**
     * 在购物车界面
     * 删除某条产品
     * @param product
     * @return
     */
    public static int deleteProductFromCart(Product product,User user){
        Connection con = null;
        Statement stat = null;
        int count=0;
        try {
            con=LoginUtil.getConnection();
            String sql="delete from shoppingcart where productname='"+product.getProductname()+"'and uid='"+user.getUsername()+"'";
            stat=con.createStatement();
            count=stat.executeUpdate(sql);
            if (count>=1)return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con,stat);
        }
        return 0;
    }

    /**
     * 添加产品到购物车
     * 参数：product，user
     */
    public static int addProductToShoppingCart(Product product,User user){
        Connection con = null;
//        Statement stat = null;
        PreparedStatement stat=null;
        int count=0;
        try {
            con=LoginUtil.getConnection();
            // INSERT INTO shoppingCart VALUES(NULL,"产品5","177900-48-8","ss.jpg","C12H22N2O1","1100","280g","西药",10,"abc");
//            String sql="insert into shoppingcart values(null,\"'"+
//                    product.getProductname()+"'\",\"'"+
//                    product.getCas() +"'\",\"'"+
//                    product.getStructure()+"'\",\"'"+
//                    product.getFormula()+"'\",\"'"+
//                    product.getPrice()+"'\",\"'"+
//                    product.getRealstock()+"'\",\"'"+
//                    product.getCategory()+"'\",'"+product.getNumber()+"',\"''"+user.getUsername()+"\")";
           // 使用PreparedStatement预编译，防止sql注入，还方便书写
            String sql="insert into shoppingcart values (null,?,?,?,?,?,?,?,?,?)";
            stat=con.prepareStatement(sql);
            stat.setString(1,product.getProductname());
            stat.setString(2,product.getCas());
            stat.setString(3,product.getStructure());
            stat.setString(4,product.getFormula());
            stat.setString(5,product.getPrice());
            stat.setString(6,product.getRealstock());
            stat.setString(7,product.getCategory());
            stat.setInt(8,product.getNumber());
            stat.setString(9,user.getUsername());
            count=stat.executeUpdate();
            if (count>=1)return 1;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con,stat);
        }
        return 0;
    }

    /**
     * 清空购物车
     */
    public static int clearCart(User user){
        Connection con = null;
        Statement stat = null;
        int count=0;
        try {
            con=LoginUtil.getConnection();
            String sql="delete from shoppingcart where uid='"+user.getUsername()+"'";
            stat=con.createStatement();
            count=stat.executeUpdate(sql);
            if (count>=1)return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con,stat);
        }
        return 0;
    }
    /**
     * 修改购物车药品的数量
     *  TODO 可以增加事务回滚，有一个修改不成功就回滚
     */
    public static int updateShoppingCart(ArrayList<Product> productArrayList,User user){
        Connection con = null;
        Statement stat = null;
        int count=0;
        int len=productArrayList.size();
        try {
            con=LoginUtil.getConnection();
            stat=con.createStatement();
            // 开启事务
            con.setAutoCommit(false);
            for (int i=0;i<len;i++){
                Product product = productArrayList.get(i);
                String sql="UPDATE shoppingcart SET number='"+product.getNumber()+"' WHERE uid='"+user.getUsername()+"' AND productname='"+product.getProductname()+"'";
                count= stat.executeUpdate(sql);
                if(count<=0){int temp=1/0;} // 如果影响行数小于1，人为创造异常，让事务回滚
            }
            // 提交事务
            con.commit();
        } catch (Exception throwables) {
            try {
                con.rollback(); // 出现异常，事务回滚
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            LoginUtil.close(con,stat);
        }
        return 1;
    }
}
