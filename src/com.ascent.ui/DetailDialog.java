package com.ascent.ui;

import com.ascent.bean.Product;
import com.ascent.bean.User;
import com.ascent.utils.ShoppingCart;
import com.system.Utils.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DetailDialog extends JDialog {

    private JFrame jf;
    private final int WIDTH = 500;
    private final int HEIGHT = 430;

    private Product myProduct;
    private JButton shoppingButton;
    private User user;

    public DetailDialog(JFrame jf, Product product,User user){
        super(jf, "查看药品详情", true);
        this.jf=jf;
        this.myProduct=product;
        this.user=user;
        setBounds((ScreenUtils.getSreenWidth() - WIDTH) / 2, (ScreenUtils.getSreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        setResizable(false);
        buildGui();
        setVisible(true);

    }

    private void buildGui() {

        Container container = this.getContentPane();

        container.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 0, 2, 10);
        JLabel artistLabel = new JLabel("产品名:  " + myProduct.getProductname());
        artistLabel.setForeground(Color.black);
        infoPanel.add(artistLabel, c);

        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(2, 0, 10, 10);
        JLabel titleLabel = new JLabel("CAS号:  " + myProduct.getCas());
        titleLabel.setForeground(Color.black);
        infoPanel.add(titleLabel, c);

        JLabel categoryLabel = new JLabel("公式:  " + myProduct.getFormula());
        c.insets = new Insets(2, 0, 2, 0);
        categoryLabel.setForeground(Color.black);
        infoPanel.add(categoryLabel, c);

        JLabel durationLabel = new JLabel("数量:  " + myProduct.getRealstock());
        durationLabel.setForeground(Color.black);
        infoPanel.add(durationLabel, c);

        JLabel priceLabel = new JLabel("类别： " + myProduct.getCategory());
        c.insets = new Insets(10, 0, 2, 0);
        priceLabel.setForeground(Color.black);
        infoPanel.add(priceLabel, c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 5;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 5, 20, 0);
        String imageName = myProduct.getStructure();
        ImageIcon recordingIcon = null;
        JLabel recordingLabel = null;

        // 读取图片
        try {
            if (imageName.trim().length() == 0) {
                recordingLabel = new JLabel("  图片不存在  ");
            } else {
                recordingIcon = new ImageIcon(ImageIO.read(new File("AscentSys\\images\\"+imageName)));
                recordingLabel = new JLabel(recordingIcon);
            }
        } catch (Exception exc) {
            recordingLabel = new JLabel("  图片不存在  ");
        }

        recordingLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        recordingLabel.setToolTipText(myProduct.getProductname());

        infoPanel.add(recordingLabel, c);

        container.add(BorderLayout.NORTH, infoPanel);

        JPanel bottomPanel = new JPanel();
        JButton okButton = new JButton("OK");
        bottomPanel.add(okButton);
        shoppingButton = new JButton("添加到购物车");
        bottomPanel.add(shoppingButton);
        container.add(BorderLayout.SOUTH, bottomPanel);

        okButton.addActionListener(new OkButtonActionListener());
        shoppingButton.addActionListener(new PurchaseButtonActionListener());

        this.pack();

    }

    /**
     * ok按钮 退出详情窗口
     */
    class OkButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            setVisible(false);
        }
    }

    /**
     * 添加到购物车按钮的监听器
     */
    class PurchaseButtonActionListener implements ActionListener {
        ShoppingCart shoppingCar = new ShoppingCart();
        public void actionPerformed(ActionEvent event) {
            myProduct.setNumber(1);
            int flag=shoppingCar.addProduct(myProduct,user);
            if(flag==1){
                shoppingButton.setEnabled(true);
                setVisible(false);
                JOptionPane.showMessageDialog(jf,"添加成功");
            }else{
                JOptionPane.showMessageDialog(jf,"该药品已存在！");
            }

        }
    }
}
