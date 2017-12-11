package com.enation.javashop.utils.base.tool;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LDD on 2017/12/11.
 */

public class TimeTool {
    /**
     * 计算某一时间与现在时间间隔的文字提示
     */
    public static String countTimeIntervalText(long time) {
        long dTime = System.currentTimeMillis() - time;
        // 15分钟
        if (dTime < 15 * 60 * 1000) {
            return "刚刚";
        } else if (dTime < 60 * 60 * 1000) {
            // 小时
            return "1小时前";
        } else if (dTime < 24 * 60 * 60 * 1000) {
            return (int) (dTime / (60 * 60 * 1000)) + "小时前";
        } else {
            return DateFormat.format("MM-dd kk:mm", System.currentTimeMillis())
                    .toString();
        }
    }

    /**
     * 获取当前小时分钟
     *
     * @return
     */
    public static String getTime24Hours() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

}
