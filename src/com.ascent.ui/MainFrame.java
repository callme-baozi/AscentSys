package com.ascent.ui;

import com.ascent.bean.User;
import com.system.Utils.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class MainFrame {
    // 创建窗口，设置显示内容
    JFrame jf = new JFrame("AscentSys欢迎您！");
    // 定义好宽和高
    final int WIDTH = 1000;
    final int HEIGHT = 600;
    // 菜单条
    JMenuBar menuBar;
    // 分隔条
    JSplitPane jsp;
    // 目录树
    JTree tree; // 根目录
    DefaultMutableTreeNode root;
    DefaultMutableTreeNode healthCareDrug; // 保健药
    DefaultMutableTreeNode biochemicalDrug; // 生化药
    DefaultMutableTreeNode vitamin; // 维生素
    DefaultMutableTreeNode westDrug; // 西药
    // 展示产品，默认选中生化药
    ProductTable productTable;

    public void init(User user) throws Exception{
        // 展示产品，默认选中生化药
        productTable=new ProductTable(jf,user,"生化药");
        // 窗口的小图标
        jf.setIconImage(ImageIO.read(new File("AscentSys\\images\\logo.png")));
        // 设置窗口出现在屏幕正中央
        jf.setBounds((ScreenUtils.getSreenWidth() - WIDTH) / 2, (ScreenUtils.getSreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        // 组装菜单条
        menuBar = new JMenuBar();
        // 文件菜单栏
        JMenu file = new JMenu("文件");

        JMenu open = new JMenu("打开");
        JMenuItem hddItem = new JMenuItem("本机硬盘...");
        JMenuItem netItem = new JMenuItem("网络");
        JMenuItem interItem = new JMenuItem("互联网...");
        open.add(hddItem);
        open.add(netItem);
        open.add(interItem);

        JMenuItem save = new JMenuItem("保存");
        save.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK)); // 设置快捷键
        JMenuItem exit = new JMenuItem("退出");
        exit.addActionListener(ExitListener);
        exit.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));

        file.add(open);
        file.add(save);
        file.add(exit);
        // 选项菜单栏
        JMenu option = new JMenu("选项");

        JMenuItem metal = new JMenuItem("Metal 外观");
        JMenuItem nimbus = new JMenuItem("Nimbus 外观");
        JMenuItem motif = new JMenuItem("CDE/Motif 外观");
        JMenuItem windows = new JMenuItem("Windows 外观");
        JMenuItem classic = new JMenuItem("Windows Classic 外观");
        // 外观监听器
        metal.addActionListener(OutLookListener);
        nimbus.addActionListener(OutLookListener);
        motif.addActionListener(OutLookListener);
        windows.addActionListener(OutLookListener);
        classic.addActionListener(OutLookListener);
        option.add(metal);
        option.add(nimbus);
        option.add(motif);
        option.add(windows);
        option.add(metal);
        option.add(classic);
        // 帮助菜单栏
        JMenu help = new JMenu("帮助");
        JMenuItem about = new JMenuItem("关于");
        // “关于”的监听器
        about.addActionListener(e -> JOptionPane.showMessageDialog(jf, "超值享受"));
        about.setAccelerator(KeyStroke.getKeyStroke('A', KeyEvent.CTRL_MASK));
        help.add(about);
        // 保存 的监听器
        save.addActionListener(e-> JOptionPane.showMessageDialog(jf,"保存成功！"));
        // 组装菜单条
        menuBar.add(file);
        menuBar.add(option);
        menuBar.add(help);
        // 把菜单条添加到窗口
        jf.setJMenuBar(menuBar);
        // 设置一个分隔条，左边药品分类，右边展示药品
        jsp = new JSplitPane();
        // 设置分隔条的宽
        jsp.setDividerSize(5);
        // 设置可连续布局
        jsp.setContinuousLayout(true);
        // 把药品分类的tree放入分隔条左边
        jsp.setLeftComponent(productTree());
        // 把具体的产品表格放在分隔条右边
        jsp.setRightComponent(productTable);
        // 添加分隔条到窗口
        jf.add(jsp);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 点击关闭自动退出
    }

//    public static void main(String[] args) {
//        try {
//            new MainFrame().init(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // 改变外观监听器
    ActionListener OutLookListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            try {
                switch (command) {
                    case "Metal 外观" -> UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    case "Nimbus 外观" -> UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    case "CDE/Motif 外观" -> UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    case "Windows 外观" -> UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    case "Windows Classic 外观" -> UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                }
                // 更新frame窗口内顶级容器和所有组件的UI
                SwingUtilities.updateComponentTreeUI(jf.getContentPane());
                // 更新菜单条UI
                SwingUtilities.updateComponentTreeUI(menuBar);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };

    // “退出”监听器
    ActionListener ExitListener = e -> {
        jf.setVisible(false);
        jf.dispose();
    };

    // 设置目录树
    public JScrollPane productTree() {
        // 添加各目录名字
        root=new DefaultMutableTreeNode("药品");
        healthCareDrug=new DefaultMutableTreeNode("保健药");
        biochemicalDrug=new DefaultMutableTreeNode("生化药");
        vitamin=new DefaultMutableTreeNode("维生素");
        westDrug=new DefaultMutableTreeNode("西药");
        // 各分类从属于根目录下
        root.add(healthCareDrug);
        root.add(biochemicalDrug);
        root.add(vitamin);
        root.add(westDrug);
        // 创建目录树
        tree=new JTree(root);
        // 自定义目录外观
        tree.setCellRenderer(new MyRenderer());
        // 目录点击监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                // 获取点击的目录条目的名字
                Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();
                String category = lastPathComponent.toString();
//                System.out.println("你点击了:"+category);
                productTable.update(category); // 更新表格
            }
        });
        // 将目录树包装一个滚动条组件，返回滚动条
        JScrollPane js=new JScrollPane(tree);
        return js;
    }
    // 自定义结点绘制器
    private class MyRenderer extends DefaultTreeCellRenderer{
        private ImageIcon rootIcon=null;
        private ImageIcon healthCreaIcon=null;
        private ImageIcon biochemicaIcon=null;
        private ImageIcon vitaminIcon=null;
        private ImageIcon westIcon=null;

        public MyRenderer(){
            // 设置图标路径
            rootIcon=new ImageIcon("AscentSys\\images\\manager.png");
            healthCreaIcon=new ImageIcon("AscentSys\\images\\red.png");
            biochemicaIcon=new ImageIcon("AscentSys\\images\\green.png");
            vitaminIcon=new ImageIcon("AscentSys\\images\\red.png");
            westIcon=new ImageIcon("AscentSys\\images\\green.png");
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            ImageIcon image=null;
            // 将图标添加到各目录
            switch(row){
                case 0->image=rootIcon;
                case 1->image=healthCreaIcon;
                case 2->image=biochemicaIcon;
                case 3->image=vitaminIcon;
                case 4->image=westIcon;

            }
            this.setIcon(image);
            return this;
        }
    }
}
