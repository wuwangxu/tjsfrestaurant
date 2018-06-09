package com.wangxu.flags;

import com.wangxu.util.FoodMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Menus {
    //通过hashmap将每个菜单大项与对应的菜单项对应起来
    public static HashMap<String, ArrayList<FoodMenu>> menus = new HashMap<String, ArrayList<FoodMenu>>();
    //通过arraylist将每个大项的名称添加进去
    public static ArrayList<String> menuTitles = new ArrayList<String>();

    public static void clearMenu() {
        menus.clear();
        menuTitles.clear();
    }

    //数据库版本号
    public static int DatabaseVersion;
    //订单
    public static HashSet<FoodMenu> order = new HashSet<FoodMenu>();
    public static String CustomerNumber;
    public static double total;
}
