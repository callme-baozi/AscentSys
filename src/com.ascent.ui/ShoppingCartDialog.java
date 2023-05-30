package com.ascent.ui;

import com.ascent.bean.Product;
import com.ascent.bean.User;
import com.ascent.utils.ShoppingCart;
import com.system.Utils.FileUtils;
import com.system.Utils.LoginUtil;
import com.system.Utils.ScreenUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class ShoppingCartDialog extends JDialog {
    // 修改 按钮，出现连个按钮名分别是删除和确认
    // 购买  按钮 ，支持多选，增加提示框“支持多选”
    // 清空

    private JFrame jf;
    private User user;

    private final int WIDTH=600;
    private final int HEIGHT=400;

    private ShoppingCart shoppingCart;

    private JTable table;
    private Vector<String> titles;
    protected DefaultTableModel tableModel;
    protected Vector<Vector> productData;
    private ArrayList<Product> productArrayList;

    private JPanel btnPanel;
    private JButton buyBtn;
    private JButton clearAllBtn;
    private JButton updateBtn;
    private JButton deleteBtn;


    public ShoppingCartDialog(JFrame jf, User user){
        super(jf,"购物车信息",true);
        this.jf=jf;
        this.user=user;

        setBounds((ScreenUtils.getSreenWidth()-WIDTH)/2,(ScreenUtils.getSreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);

        btnPanel=new JPanel();
        buyBtn=new JButton("提交表单");
        clearAllBtn=new JButton("清空");
        updateBtn=new JButton("确认修改");
        deleteBtn=new JButton("移出");

        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(buyBtn);
        btnPanel.add(clearAllBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        // 监听器
        deleteBtn.addActionListener(deleteListener); // 移出
        clearAllBtn.addActionListener(clearListener); // 清空
        updateBtn.addActionListener(updateListener);  // 确认修改
        buyBtn.addActionListener(buyListener);   // 提交表单


        String[] ts = {"产品名", "CAS号", "结构图", "公式", "价格", "重量", "类别", "数量"};
        titles = new Vector<>();
        for (String t : ts) {
            titles.add(t);
        }
        productData = new Vector<>();

        shoppingCart=new ShoppingCart();
        productArrayList=shoppingCart.getShoppingList(user);
//        System.out.println("productArrayList信息有几条："+productArrayList.size());
        for (Product product : productArrayList) {
            Vector<String> detail = new Vector<>();
            detail.add(product.getProductname());
            detail.add(product.getCas());
            detail.add(product.getStructure());
            detail.add(product.getFormula());
            detail.add(product.getPrice());
            detail.add(product.getRealstock());
            detail.add(product.getCategory());
            detail.add(String.valueOf(product.getNumber()));
            productData.add(detail);
        }
        System.out.println("shoppingCartDialog类接收到:"+productData.size()+"条信息");

        tableModel = new DefaultTableModel(productData, titles);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column==7){
                    return true;
                }else{
                    return false; // 不可编辑
                }
            }
        };
        // 可以选中多行
//        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // 还是选中单行吧，选中多行不好处理
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(btnPanel,BorderLayout.NORTH);
        add(new JScrollPane(table));
        setVisible(true);


    }

    /**
     * 刷新表格
     */
    public void update(){
        productData.clear();
        productArrayList=shoppingCart.getShoppingList(user);
        for (Product product : productArrayList) {
            Vector<String> detail = new Vector<>();
            detail.add(product.getProductname());
            detail.add(product.getCas());
            detail.add(product.getStructure());
            detail.add(product.getFormula());
            detail.add(product.getPrice());
            detail.add(product.getRealstock());
            detail.add(product.getCategory());
            detail.add(String.valueOf(product.getNumber()));
            productData.add(detail);
        }
        tableModel = new DefaultTableModel(productData, titles);
        table.setModel(tableModel);
    }

    /**
     * 移除按钮监听器
     */
    ActionListener deleteListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow>0){
                Product product = productArrayList.get(selectedRow);
                int flag = JOptionPane.showConfirmDialog(jf, "你确定要将【" + product.getProductname() + "】移出购物车吗？", "移出购物车", JOptionPane.YES_NO_OPTION);
                if (flag==0) { // 选择了“是”
//                    int status = LoginUtil.deleteProductFromCart(product, user);
                    int status = FileUtils.deleteProductFromCart(product, user);
                    if (status==1){
                        update();
                        JOptionPane.showMessageDialog(jf,"已移出！");
                    }else{
                        JOptionPane.showMessageDialog(jf,"移出失败！");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(jf,"您未选择药品！");
            }

        }
    };
    /**
     * 清空按钮的监听器
     */
    ActionListener clearListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int flag = JOptionPane.showConfirmDialog(jf, "确定要清空购物车吗？", "清空购物车", JOptionPane.YES_NO_OPTION);
            if (flag==0){ // 选择了 “是”
                shoppingCart.clearProduct();
                update();
            }
        }
    };
    /**
     * 确认修改 按钮的监听器
     */
    ActionListener updateListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 遍历表格，获取每条产品信息，打包成产品ArraysList，传入Loginutils执行，结束刷新表单update
            int rowCount = table.getRowCount();
            for (int i=0;i<rowCount;i++){
                Product product = productArrayList.get(i);
                Integer valueAt = Integer.parseInt((String) table.getValueAt(i, 7));
                product.setNumber(valueAt);
            }
            int flag = FileUtils.updateShoppingCart(productArrayList, user);
            if(flag==1){
                update();
                JOptionPane.showMessageDialog(jf,"修改成功！");
            }

        }
    };
    /**
     * 提交表单
     */
    ActionListener buyListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(jf,"成功提交！");
        }
    };
}
