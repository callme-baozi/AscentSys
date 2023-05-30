package com.ascent.ui;

import com.ascent.bean.Product;
import com.ascent.bean.User;
import com.ascent.utils.ProductDataClient;
import com.ascent.utils.ShoppingCart;
import com.system.Utils.EventDoneListener;
import com.system.Utils.LoginUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

// 该类继承Box类，上面放按钮，下面展示表格
public class ProductTable extends Box {
    private ProductDataClient myDataClient;
    private ArrayList<Product> productArrayList;
    private User user;
    // 定义宽和高
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private JFrame jf;
    private JPanel btnPanel;
    protected DefaultTableModel tableModel;
    private Vector<String> titles;
    protected Vector<Vector> productData;
    private JTable table;

    JButton details;
    JButton checkCart;
    JButton buy;
    JButton exit;
    JButton addBtn;
    JButton deleteAndModify;
    JButton chatBtn;

    public ProductTable(JFrame jf, User user, String category) {
        super(BoxLayout.Y_AXIS);
        this.jf = jf;
        this.user=user;
        btnPanel = new JPanel();
        btnPanel.setMaximumSize(new Dimension(WIDTH, 80));
//        updataBtn=new JButton("刷新");
        addBtn = new JButton("添加");
        deleteAndModify = new JButton("删除与修改");
        checkCart = new JButton("查看购物车");
        details = new JButton("详情");
        buy = new JButton("购买");
        chatBtn=new JButton("联系客服");
        exit = new JButton("退出");

        btnPanel.add(addBtn);
        btnPanel.add(deleteAndModify);
        btnPanel.add(checkCart);
        btnPanel.add(details);
        btnPanel.add(buy);
        btnPanel.add(chatBtn);
        btnPanel.add(exit);
//        btnPanel.add(updataBtn);

        this.add(btnPanel, BorderLayout.NORTH);

        details.setEnabled(false);
        // 添加监听器
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddProductDialog(jf,user, new EventDoneListener() {
                    @Override
                    public void Done(String object) {
                        myDataClient.updata(); // 刷新文件
                        update(object); // 刷新表格
                    }
                });
            }
        });
        checkCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShoppingCartDialog(jf,user);
            }
        });
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if(selectedRow>=0){
                    Product product = productArrayList.get(selectedRow);
                    new DetailDialog(jf, product,user);
                }else{
                    JOptionPane.showMessageDialog(jf,"您未选中药品！");
                }

            }
        });
        deleteAndModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if(selectedRow>=0){
                    System.out.println("选中的条目是:" + selectedRow);
                    Product product = productArrayList.get(selectedRow);
                    System.out.println(product);
                    new DeleteAndModifyDialog(jf, product, new EventDoneListener() {
                        @Override
                        public void Done(String object) {
                            myDataClient.updata();
                            update(object);
                        }
                    });
                }else{
                    JOptionPane.showMessageDialog(jf,"您未选中药品！");
                }
            }
        });
        buy.addActionListener(buyListener);
        chatBtn.addActionListener(chatListener);
        exit.addActionListener(e -> jf.dispose());
        // 刷新没必要了
//        updataBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                myDataClient.updata();
//
//            }
//        });

        // 根据用户权限不同，部分功能不能展示
        if (user.getAuthority() == 1) {
            addBtn.setEnabled(true);
            deleteAndModify.setEnabled(true);
        } else {
            addBtn.setEnabled(false);
            deleteAndModify.setEnabled(false);
        }

        // 设置table
        try {
            myDataClient = new ProductDataClient();
//            productArrayList = LoginUtil.getAllProducts();
            productArrayList=myDataClient.getProducts("保健药");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] ts = {"产品名", "CAS号", "结构图", "公式", "价格", "数量", "类别"};
        titles = new Vector<>();
        for (String t : ts) {
            titles.add(t);
        }
        productData = new Vector<>();
        for (Product product : productArrayList) {
            Vector<String> detail = new Vector<>();
            detail.add(product.getProductname());
            detail.add(product.getCas());
            detail.add(product.getStructure());
            detail.add(product.getFormula());
            detail.add(product.getPrice());
            detail.add(product.getRealstock());
            detail.add(product.getCategory());
            productData.add(detail);
        }
        tableModel = new DefaultTableModel(productData, titles);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };
        // 选中某一行，然后“详情”按钮才会亮
        table.addMouseListener(TableSelectionListener);
        // 只能选中一行
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(new JScrollPane(table));
    }

    public void update(String category) {
        productData.clear();
        try {
            productArrayList = new ArrayList<>();
            if (category == "药品") {
                productArrayList = myDataClient.getAllProducts();
            } else {
                productArrayList = myDataClient.getProducts(category);
            }
            // 按产品名大小顺序排放
//            Collections.sort(productArrayList, (o1, o2)->{
//                int n1=Integer.valueOf(o1.getProductname().substring(2));
//                int n2=Integer.valueOf((o2.getProductname()).substring(2));
//                return n1>n2?1:-1;
//            });
//            System.out.println(productArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Product product : productArrayList) {
            Vector<String> detail = new Vector<>();
            detail.add(product.getProductname());
            detail.add(product.getCas());
            detail.add(product.getStructure());
            detail.add(product.getFormula());
            detail.add(product.getPrice());
            detail.add(product.getRealstock());
            detail.add(product.getCategory());
            productData.add(detail);
        }
        tableModel = new DefaultTableModel(productData, titles);
        table.setModel(tableModel);
    }

    // table选中的监听器
    MouseListener TableSelectionListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                details.setEnabled(true);
            }
        }
    };

    /**
     * “购买”按钮的监听器
     * 购买就是添加到购物车
     */
    ActionListener buyListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            ShoppingCart shoppingCart = new ShoppingCart();
            int selectedRow = table.getSelectedRow();
            if(selectedRow>=0){
                Product product = productArrayList.get(selectedRow);
                product.setNumber(1);
                int flag=shoppingCart.addProduct(product,user);
                if(flag==1){
                    JOptionPane.showMessageDialog(jf,"已添加到购物车");
                }else{
                    JOptionPane.showMessageDialog(jf,"该药品已存在！");
                }
            }else{
                JOptionPane.showMessageDialog(jf,"您未选中药品！");
            }


        }
    };

    /**
     * “联系客服” 按钮的监听器
     */
    ActionListener chatListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 这里直接new一个WeChatServer不太行，页面是空白的，好像当前线程被WeChatServer对象抢占了
            // 所以这里开辟一个线程做聊天
            WeChatServer we = new WeChatServer();
            Thread thread = new Thread(new MyThread(we));
            thread.start();
        }
    };

    class MyThread implements Runnable {
        WeChatServer weChatServer;
        public MyThread(WeChatServer weChatServer){
            this.weChatServer=weChatServer;
        }
        @Override
        public void run() {
            weChatServer.init();
        }
    }
}
