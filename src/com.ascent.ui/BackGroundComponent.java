package com.ascent.ui;
/**
 * 界面背景类
 * @author baozi
 */

import javax.swing.*;
import java.awt.*;

public class BackGroundComponent extends JPanel {
    private Image image;

    public BackGroundComponent(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,this.getWidth(),this.getHeight(),null);
    }
}
