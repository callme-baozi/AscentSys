package com.ascent.ui;

import com.ascent.bean.Product;
import com.ascent.bean.User;
import com.ascent.utils.ProductDataClient;
import com.ascent.utils.ShoppingCart;
import com.system.Utils.EventDoneListener;
import com.system.Utils.FileUtils;
import com.system.Utils.LoginUtil;
import com.system.Utils.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AddProductDialog extends JDialog {

    private final int WIDTH = 500;
    private final int HEIGHT = 430;
    private JFrame jf;
    private EventDoneListener listener;

    // 组件声明
    JLabel productNameLabel;
    JLabel CASLabel;
    JLabel structureLabel;
    JLabel formulaLabel;
    JLabel priceLabel;
    JLabel realStockLabel;
    JLabel categoryLabel;

    JTextField productField;
    JTextField CASField;
    JTextField structureField;
    JTextField formulaField;
    JTextField priceField;
    JTextField realstockField;
    JComboBox<String> categorySelection;
    JButton addBtn;

    ProductDataClient productDataClient;
    ArrayList<Product> shoppingList;

    User user;


    public AddProductDialog(JFrame jf, User user,EventDoneListener listener) {
        super(jf, "添加药品", true);
        this.listener = listener;
        this.user=user;
        this.jf = jf;

        try {
            productDataClient=new ProductDataClient();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBounds((ScreenUtils.getSreenWidth() - WIDTH) / 2, (ScreenUtils.getSreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        setResizable(false);

        Box vBox = Box.createVerticalBox();

        Box productNameBox = Box.createHorizontalBox();
        productNameLabel = new JLabel("产品名:");
        productField = new JTextField(20);
        productNameBox.add(productNameLabel);
        productNameBox.add(Box.createHorizontalStrut(20));
        productNameBox.add(productField);

        Box CASBox = Box.createHorizontalBox();
        CASLabel = new JLabel("CAS号:");
        CASField = new JTextField(20);
        CASBox.add(CASLabel);
        CASBox.add(Box.createHorizontalStrut(20));
        CASBox.add(CASField);

        Box structureBox = Box.createHorizontalBox();
        structureLabel = new JLabel("结构图:");
        structureField = new JTextField(20);
        structureBox.add(structureLabel);
        structureBox.add(Box.createHorizontalStrut(20));
        structureBox.add(structureField);

        Box formaluBox = Box.createHorizontalBox();
        formulaLabel = new JLabel("公    式:");
        formulaField = new JTextField(20);
        formaluBox.add(formulaLabel);
        formaluBox.add(Box.createHorizontalStrut(20));
        formaluBox.add(formulaField);

        Box priceBox = Box.createHorizontalBox();
        priceLabel = new JLabel("价    格:");
        priceField = new JTextField(20);
        priceBox.add(priceLabel);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);

        Box realstockBox = Box.createHorizontalBox();
        realStockLabel = new JLabel("数    量:");
        realstockField = new JTextField(20);
        realstockBox.add(realStockLabel);
        realstockBox.add(Box.createHorizontalStrut(20));
        realstockBox.add(realstockField);

        Box categoryBox = Box.createHorizontalBox();
        categoryLabel = new JLabel("类    别:");
//        categoryField = new JTextField(20);
        Vector<String> categories = new Vector<>();
        List<String> names = List.of("保健药", "西药", "维生素", "生化药");
        categories.addAll(names);
        categorySelection = new JComboBox<>(categories);
        categoryBox.add(categoryLabel);
        categoryBox.add(Box.createHorizontalStrut(20));
        categoryBox.add(categorySelection);

        addBtn = new JButton("添加");
        addBtn.addActionListener(addListener);
        JPanel addPanel = new JPanel();
        addPanel.add(addBtn);

        vBox.add(Box.createVerticalStrut(50));
        vBox.add(productNameBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(CASBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(structureBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(formaluBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(priceBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(realstockBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(categoryBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(addPanel);

        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(50));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(50));

        add(hBox);
        setVisible(true);

    }

    ActionListener addListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String productname = productField.getText().trim();
            String cas = CASField.getText().trim();
            String structure = structureField.getText().trim();
            String formula = formulaField.getText().trim();
            String price = priceField.getText().trim();
            String realstock = realstockField.getText().trim();
            String category = categorySelection.getSelectedItem().toString();
            if ("".equals(productname) ||
                    "".equals(cas) ||
                    "".equals(structure) ||
                    "".equals(formula) ||
                    "".equals(price) ||
                    "".equals(realstock) ||
                    "".equals(category)
            ) {
                JOptionPane.showMessageDialog(jf, "信息不能为空！");
            } else {
                int flag=1;
                try {
                    shoppingList = productDataClient.getAllProducts();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                for (Product product1 : shoppingList) {
                    if(product1.getProductname().equals(productname)){
                        flag=0;
                    }
                }
                if(flag==0){
                    JOptionPane.showMessageDialog(jf, "添加失败！该商品已存在!");
                }else{
                    Product product = new Product(productname, cas, structure, formula, price, realstock, category);
                    FileUtils.addProductToProduct(product);
                    JOptionPane.showMessageDialog(jf, "添加成功！");
                    setVisible(false);
                    dispose();
                    listener.Done(category);
                }

            }
        }
    };

}
