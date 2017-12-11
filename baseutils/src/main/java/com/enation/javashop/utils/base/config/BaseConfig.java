package com.enation.javashop.utils.base.config;

/**
 * 滑动退出全局控制类
 */

public class BaseConfig {

    /**
     * 全局滑动退出标记
     */
    private static boolean ScrollFlag = true;

    /**
     * 首页Activity名称 用来设置首页不参与滑动退出
     */
    private static String  HomeActivity = "no";

    /**
     * 开启滑动退出功能
     */
    public static void openScrollBack(){
        ScrollFlag = true;
    }

    /**
     * 是否开启滑动退出功能
     * @return  flag
     */
    public static boolean isOpenScrollBack(){
        return ScrollFlag;
    }

    /**
     * 设置首页而不参与滑动退出
     * @param homeActivity 首页activity包名
     */
    public static void HomeDisableScrollBack(String homeActivity){
        HomeActivity = homeActivity;
    }

    /**
     * 获取首页包名
     * @return
     */
    public static String getHomeName(){
        return HomeActivity == null ? "" : HomeActivity;
    }
}
