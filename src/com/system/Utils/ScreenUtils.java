package com.system.Utils;

import java.awt.*;

public class ScreenUtils {

    // 获取当前屏幕的宽和高
    public static int getSreenWidth(){
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }
    public static int getSreenHeight(){
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }
}
