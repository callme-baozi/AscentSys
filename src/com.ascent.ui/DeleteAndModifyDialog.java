package com.ascent.ui;

import com.ascent.bean.Product;
import com.system.Utils.EventDoneListener;
import com.system.Utils.FileUtils;
import com.system.Utils.LoginUtil;
import com.system.Utils.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class DeleteAndModifyDialog extends JDialog {

    private final int WIDTH = 500;
    private final int HEIGHT = 430;

    private JFrame jf;
    private Product myProduct;
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

    JButton deleteBtn;
    JButton modifyBtn;
    JButton savaBtn;

    public DeleteAndModifyDialog(JFrame jf, Product product, EventDoneListener listener) {
        super(jf, "管理药品信息", true);

        this.jf = jf;
        this.myProduct = product;
        this.listener = listener;

        setBounds((ScreenUtils.getSreenWidth() - WIDTH) / 2, (ScreenUtils.getSreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        setResizable(false);

        Box vBox = Box.createVerticalBox();

        Box productNameBox = Box.createHorizontalBox();
        productNameLabel = new JLabel("产品名:");
        productField = new JTextField(20);
        productField.setText(product.getProductname());
        productField.setEnabled(false);
        productNameBox.add(productNameLabel);
        productNameBox.add(Box.createHorizontalStrut(20));
        productNameBox.add(productField);

        Box CASBox = Box.createHorizontalBox();
        CASLabel = new JLabel("CAS号:");
        CASField = new JTextField(20);
        CASField.setText(product.getCas());
        CASBox.add(CASLabel);
        CASBox.add(Box.createHorizontalStrut(20));
        CASBox.add(CASField);

        Box structureBox = Box.createHorizontalBox();
        structureLabel = new JLabel("结构图:");
        structureField = new JTextField(20);
        structureField.setText(product.getStructure());
        structureBox.add(structureLabel);
        structureBox.add(Box.createHorizontalStrut(20));
        structureBox.add(structureField);

        Box formaluBox = Box.createHorizontalBox();
        formulaLabel = new JLabel("公    式:");
        formulaField = new JTextField(20);
        formulaField.setText(product.getFormula());
        formaluBox.add(formulaLabel);
        formaluBox.add(Box.createHorizontalStrut(20));
        formaluBox.add(formulaField);

        Box priceBox = Box.createHorizontalBox();
        priceLabel = new JLabel("价    格:");
        priceField = new JTextField(20);
        priceField.setText(product.getPrice());
        priceBox.add(priceLabel);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);

        Box realstockBox = Box.createHorizontalBox();
        realStockLabel = new JLabel("数    量:");
        realstockField = new JTextField(20);
        realstockField.setText(product.getRealstock());
        realstockBox.add(realStockLabel);
        realstockBox.add(Box.createHorizontalStrut(20));
        realstockBox.add(realstockField);

        Box categoryBox = Box.createHorizontalBox();
        categoryLabel = new JLabel("类    别:");
        Vector<String> categories = new Vector<>();
        List<String> names = List.of("保健药", "西药", "维生素", "生化药");
        categories.addAll(names);
        categorySelection = new JComboBox<>(categories);
        categorySelection.setSelectedItem(product.getCategory());

        categoryBox.add(categoryLabel);
        categoryBox.add(Box.createHorizontalStrut(20));
        categoryBox.add(categorySelection);

        // 文本框不可编辑
        setEnable(false);

        Panel managePanel = new Panel();
        deleteBtn = new JButton("删除");
        managePanel.add(deleteBtn);
        modifyBtn = new JButton("修改");
        managePanel.add(Box.createHorizontalStrut(20));
        managePanel.add(modifyBtn);
        savaBtn = new JButton("保存");
        managePanel.add(Box.createHorizontalStrut(20));
        managePanel.add(savaBtn);
        // 监听器
        modifyBtn.addActionListener(e -> setEnable(true));
        savaBtn.addActionListener(saveListener);
        deleteBtn.addActionListener(deleteListener);

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
        vBox.add(managePanel);

        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(50));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(50));

        add(hBox);
        setVisible(true);

    }

    /**
     * 设置文本框是否可编辑
     * @param bo
     */
    public void setEnable(boolean bo) {
        CASField.setEnabled(bo);
        structureField.setEnabled(bo);
        formulaField.setEnabled(bo);
        priceField.setEnabled(bo);
        realstockField.setEnabled(bo);
        categorySelection.setEnabled(bo);  // 选择框不可编辑
    }

    /**
     * 保存键的监听器
     */
    ActionListener saveListener = new ActionListener() {
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
                Product product = new Product(productname, cas, structure, formula, price, realstock, category);
//                int status = LoginUtil.updateProduct(product);
                int status = FileUtils.updateProduct(product);
                if (status == 1) {
                    JOptionPane.showMessageDialog(jf, "修改成功！");
                    setEnable(false);
                    listener.Done(category);
                } else {
                    JOptionPane.showMessageDialog(jf, "修改失败！");
                }
            }
        }
    };

    ActionListener deleteListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = "确定删除【" + myProduct.getProductname() + "】?";
            int flag = JOptionPane.showConfirmDialog(jf, message, "确认删除", JOptionPane.YES_NO_OPTION);
            if (flag == 0) {
//                System.out.println("你选择了是");
                int status = FileUtils.deleteProduct(myProduct);
                if (status == 1) {
                    JOptionPane.showMessageDialog(jf, "删除成功");
                    listener.Done(myProduct.getCategory());
                    setVisible(false);
                    dispose();
                }
            }
        }
    };
}