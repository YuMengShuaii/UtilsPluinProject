package com.enation.javashop.utils.base.other;

/**
 * Created by LDD on 17/7/31.
 */

public class ScrollBackConfig {
    private static boolean ScrollFlag = false;
    private static String  HomeActivity = "no";
    public static void openScrollBack(){
        ScrollFlag = true;
    }

    public static boolean isOpenScrollBack(){
        return ScrollFlag;
    }

    public static void HomeDisableScrollBack(String homeActivity){
        HomeActivity = homeActivity;
    }

    public static String getHomeName(){
        return HomeActivity;
    }
}
