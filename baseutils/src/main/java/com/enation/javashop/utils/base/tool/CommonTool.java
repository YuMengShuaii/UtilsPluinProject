package com.enation.javashop.utils.base.tool;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enation.javashop.utils.base.R;
import com.enation.javashop.utils.base.widget.LoadingDialog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LDD on 17/2/27.
 */

public class CommonTool {
    public static final String TAG = "JavaShop_Base";
    private static final String REGULAR_NUMBER = "(-?[0-9]+)(,[0-9]+)*(\\.[0-9]+)?";
    private static String regEx = "[\u4e00-\u9fa5]";
    private static Pattern pat = Pattern.compile(regEx);
    /**
     * 创建对话框
     * @param message  标题
     * @param noHint   取消
     * @param yesHint  确定
     * @param context  上下文
     * @param listener 监听接口
     */
    public static void createVerifyDialog(String message, String noHint, String yesHint, Context context, final DialogInterface listener) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        View localView = LayoutInflater.from(context).inflate(R.layout.newdialog_lay, null);
        dialog.setContentView(localView);
        TextView yesTv = (TextView) localView.findViewById(R.id.yes);
        TextView noTv = (TextView) localView.findViewById(R.id.on);
        yesTv.setText(yesHint);
        noTv.setText(noHint);
        ((TextView) localView.findViewById(R.id.message)).setText(message);
        yesTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.dismiss();
                listener.yes();
            }
        });
        noTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.dismiss();
                listener.no();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        if (!(dialog.isShowing()))
            dialog.show();
    }

    public static LoadingDialog createLoadingDialog(Context context, int layoutid, int imageviewid) {
        View localView = LayoutInflater.from(context).inflate(layoutid, null);
        ImageView view = (ImageView) localView.findViewById(imageviewid);
        LoadingDialog localLoadingDialog = LoadingDialog.getInstance(context, R.style.loading_dialog, view);
        localLoadingDialog.setCancelable(true);
        localLoadingDialog.setContentView(localView);
        return localLoadingDialog;
    }

    public static LoadingDialog createLoadingDialog(Context paramContext) {
        View localView = LayoutInflater.from(paramContext).inflate(R.layout.dialog_loading, null);
        ImageView view = (ImageView) localView.findViewById(R.id.img);
        LoadingDialog localLoadingDialog = LoadingDialog.getInstance(paramContext, R.style.loading_dialog, view);
        localLoadingDialog.setCancelable(true);
        localLoadingDialog.setContentView(localView);
        return localLoadingDialog;
    }

    public static LoadingDialog createLoadingPage(Context context) {
        View localView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        RelativeLayout layout = (RelativeLayout) localView.findViewById(R.id.background);
        layout.setBackgroundColor(Color.parseColor("#ffffff"));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = (int) ScreenTool.getScreenHeight(context);
        layoutParams.width = (int) ScreenTool.getScreenWidth(context);
        localView.setLayoutParams(layoutParams);
        ImageView view = (ImageView) localView.findViewById(R.id.img);
        LoadingDialog localLoadingDialog = LoadingDialog.getInstance(context, R.style.loading_dialog, view);
        localLoadingDialog.setCancelable(false);
        localLoadingDialog.setContentView(localView);
        return localLoadingDialog;
    }


    /**
     * 监听接口
     */
    public interface DialogInterface {
        void no();

        void yes();
    }

    /**
     * Bitmap缩放，注意源Bitmap在缩放后将会被回收
     *
     * @param origin
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getScaleBitmap(Bitmap origin, int width, int height) {
        float originWidth = origin.getWidth();
        float originHeight = origin.getHeight();

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap scale = Bitmap.createBitmap(origin, 0, 0, (int) originWidth,
                (int) originHeight, matrix, true);
        origin.recycle();
        return scale;
    }




    /**
     * 获取重复字段多的个数
     *
     * @param s
     * @return
     */
    public static int getRepeatTimes(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }

        int mCount = 0;
        char[] mChars = s.toCharArray();
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < mChars.length; i++) {
            char key = mChars[i];
            Integer value = map.get(key);
            int count = value == null ? 0 : value.intValue();
            map.put(key, ++count);
            if (mCount < count) {
                mCount = count;
            }
        }

        return mCount;
    }

    /**
     * 判断是否为手机号码
     *
     * @param num
     * @return
     */
    public static boolean isPhoneNum(String num) {
        // 确保每一位都是数�?
        return !TextUtils.isEmpty(num) && num.matches("1[0-9]{10}")
                && !isRepeatedStr(num) && !isContinuousNum(num);
    }

    /**
     * 判断是否400服务代码
     *
     * @param num
     * @return
     */
    public static boolean is400or800(String num) {
        return !TextUtils.isEmpty(num)
                && (num.startsWith("400") || num.startsWith("800"))
                && num.length() == 10;
    }

    /**
     * 判断是否区域号码
     *
     * @param num
     * @return
     */
    public static boolean isAdCode(String num) {
        return !TextUtils.isEmpty(num) && num.matches("[0]{1}[0-9]{2,3}")
                && !isRepeatedStr(num);
    }

    /**
     * 判断是否座机号码
     *
     * @param num
     * @return
     */
    public static boolean isPhoneHome(String num) {
        return !TextUtils.isEmpty(num) && num.matches("[0-9]{7,8}")
                && !isRepeatedStr(num);
    }

    /**
     * 判断是否为重复字符串
     *
     * @param str
     *         需要检查的字符
     */
    public static boolean isRepeatedStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int len = str.length();
        if (len <= 1) {
            return false;
        } else {
            char firstChar = str.charAt(0);// 第一个字�?
            for (int i = 1; i < len; i++) {
                char nextChar = str.charAt(i);// 第i个字�?
                if (firstChar != nextChar) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 判断字符串是否为连续数字 45678901
     */
    public static boolean isContinuousNum(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        if (!isNumbericString(str))
            return true;
        int len = str.length();
        for (int i = 0; i < len - 1; i++) {
            char curChar = str.charAt(i);
            char verifyChar = (char) (curChar + 1);
            if (curChar == '9')
                verifyChar = '0';
            char nextChar = str.charAt(i + 1);
            if (nextChar != verifyChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为连续字母 xyZaBcd
     */
    public static boolean isContinuousWord(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        if (!isAlphaBetaString(str))
            return true;
        int len = str.length();
        String local = str.toLowerCase();
        for (int i = 0; i < len - 1; i++) {
            char curChar = local.charAt(i);
            char verifyChar = (char) (curChar + 1);
            if (curChar == 'z')
                verifyChar = 'a';
            char nextChar = local.charAt(i + 1);
            if (nextChar != verifyChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     *            ，需要检查的字符
     */
    public static boolean isNumbericString(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern p = Pattern.compile("^[0-9]+$");// 从开头到结尾必须全部为数�?
        Matcher m = p.matcher(str);

        return m.find();
    }

    /**
     * 判断是否为纯字母
     *
     * @param str
     * @return
     */
    public static boolean isAlphaBetaString(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern p = Pattern.compile("^[a-zA-Z]+$");// 从开头到结尾必须全部为字母或者数�?
        Matcher m = p.matcher(str);

        return m.find();
    }

    /**
     * 判断是否为纯字母或数
     *
     * @param str
     * @return
     */
    public static boolean isAlphaBetaNumbericString(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");// 从开头到结尾必须全部为字母或者数�?
        Matcher m = p.matcher(str);

        return m.find();
    }

    /**
     * 判断是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        return pat.matcher(str).find();
    }

    public static boolean patternMatcher(String pattern, String input) {
        if (TextUtils.isEmpty(pattern) || TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern pat = Pattern.compile(pattern);
        Matcher matcher = pat.matcher(input);

        return matcher.find();
    }

    /**
     * 将输入流转为字节数组
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] read2Byte(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();

        return outSteam.toByteArray();
    }

    /**
     * 判断是否符合月和年的过期时间规则
     *
     * @param date
     * @return
     */
    public static boolean isMMYY(String date) {
        try {
            if (!TextUtils.isEmpty(date) && date.length() == 4) {
                int mm = Integer.parseInt(date.substring(0, 2));
                return mm > 0 && mm < 13;
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }

        return false;
    }

    /**
     * 20120506 共八位，前四�?-年，中间两位-月，�?后两�?-�?
     *
     * @param date
     * @return
     */
    public static boolean isRealDate(String date, int yearlen) {
        // if(yearlen != 2 && yearlen != 4)
        // return false;
        int len = 4 + yearlen;
        if (date == null || date.length() != len)
            return false;

        if (!date.matches("[0-9]+"))
            return false;

        int year = Integer.parseInt(date.substring(0, yearlen));
        int month = Integer.parseInt(date.substring(yearlen, yearlen + 2));
        int day = Integer.parseInt(date.substring(yearlen + 2, yearlen + 4));

        if (year <= 0)
            return false;
        if (month <= 0 || month > 12)
            return false;
        if (day <= 0 || day > 31)
            return false;

        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return day > 30 ? false : true;
            case 2:
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                    return day > 29 ? false : true;
                return day > 28 ? false : true;
            default:
                return true;
        }
    }

    /**
     * 判断字符串是否为连续字符 abcdef 45678
     */
    public static boolean isContinuousStr(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char curChar = str.charAt(i);
            char nextChar = (char) (curChar + 1);
            if (i + 1 < len) {
                nextChar = str.charAt(i + 1);
            }
            if (nextChar != (curChar + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 为字符串中的数字染颜色
     *
     * @param str
     *            ，待处理的字符串
     * @param color
     *            ，需要染的颜色
     * @return
     */
    public static SpannableString setDigitalColor(String str, int color) {
        if (str == null)
            return null;
        SpannableString span = new SpannableString(str);

        Pattern p = Pattern.compile(REGULAR_NUMBER);
        Matcher m = p.matcher(str);
        while (m.find()) {
            int start = m.start();
            int end = start + m.group().length();
            span.setSpan(new ForegroundColorSpan(color), start, end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return span;
    }

    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    public static String getFixedNumber(String str, int length) {
        if (str == null || length <= 0 || str.length() < length) {
            return null;
        }
        Log.d(TAG, "getFixedNumber, str is : " + str);
        Pattern p = Pattern.compile("\\d{" + length + "}");
        Matcher m = p.matcher(str);
        String result = null;
        if (m.find()) {
            int start = m.start();
            int end = start + m.group().length();
            result = str.substring(start, end);
        }

        return result;
    }

    public static int getLengthWithoutSpace(CharSequence s) {
        int len = s.length();

        int rlen = 0;
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) != ' ')
                rlen++;
        }

        return rlen;
    }

    public static String getDisDsrc(float dis) {
        if (dis <= 0) {
            return "";
        }
        String disStr = null;
        if (dis > 1000) {
            disStr = (float) Math.round(dis / 1000 * 10) / 10 + "km";
        } else {
            disStr = dis + "m";
        }
        return disStr;
    }
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比�?2007/02/29会被接受，并转换�?2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或�?�NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
    /**
     * 将时间戳转换为字符串
     * @param time
     * @param pattern
     * @return
     */
    public static String DataMilltoString(Long time,String pattern){
        if(time>0){
            return toString(new Date(time * 1000), pattern);
        }
        return "";
    }

    /**
     * 把日期转换成字符串型
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Date date, String pattern){
        if(date == null){
            return "";
        }
        if(pattern == null){
            pattern = "yyyy-MM-dd";
        }
        String dateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            dateString = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateString;
    }

    /**
     * 字符串为数字
     */
    public static boolean isNumber(String number) {
        if (TextUtils.isEmpty(number))
            return false;
        else {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(number);
            if (m.matches())
                return true;
            else
                return false;
        }
    }

    /**
     * 带小数的数字
     */
    public static boolean isDecimal(String number) {
        if (TextUtils.isEmpty(number))
            return false;
        else {
            Pattern p = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");
            Matcher m = p.matcher(number);
            if (m.matches())
                return true;
            else
                return false;
        }
    }

    /**
     * 字符串为字母
     */
    public static boolean isLetter(String letter) {
        if (TextUtils.isEmpty(letter))
            return false;
        else
            return letter.matches("^[a-zA-Z]*");
    }

    /**
     * 字符串是否含有汉字汉字
     */
    public static boolean hasChinese(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        else {
            String regEx = "[\u4e00-\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            if (m.find())
                return true;
            else
                return false;
        }
    }


    /**
     * 判断数字是奇数还是偶数
     */
    public static int isEvenNumbers(String even) {
        if (!TextUtils.isEmpty(even) && isNumber(even)) {
            int i = Integer.parseInt(even);
            if (i % 2 == 0) {
                //偶数
                return 2;
            } else {
                //奇数
                return 1;
            }
        } else {
            //不是奇数也不是偶数
            return 0;
        }
    }

    /**
     * 判断字符串是否字母开头
     */
    public static boolean isLetterBegin(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        else {
            char c = s.charAt(0);
            int i = (int) c;
            if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断字符串是否以指定内容开头
     */
    public static boolean startWithMytext(String mytext, String begin) {
        if (TextUtils.isEmpty(mytext) && TextUtils.isEmpty(begin))
            return false;
        else {
            if (mytext.startsWith(begin))
                return true;
            else
                return false;
        }
    }

    /**
     * 判断字符串是否以指定内容结尾
     */
    public static boolean endWithMytext(String mytext, String end) {
        if (TextUtils.isEmpty(mytext) && TextUtils.isEmpty(end))
            return false;
        else {
            if (mytext.endsWith(end))
                return true;
            else
                return false;
        }
    }

    /**
     * 判断字符串中是否含有指定内容
     */
    public static boolean hasMytext(String string, String mytext) {
        if (TextUtils.isEmpty(string) && TextUtils.isEmpty(mytext))
            return false;
        else {
            if (string.contains(mytext))
                return true;
            else
                return false;
        }
    }


    /**
     * 验证是否是手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、180、189、（1349卫通）
	    新增：17开头的电话号码，如170、177
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 验证是否是邮箱格式格式
     */
    public static boolean isEmailAdd(String email) {
        String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (TextUtils.isEmpty(email))
            return false;
        else
            return email.matches(emailRegex);
    }


    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    public static String IDCardValidate(String IDStr) throws ParseException, ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }
    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 数组转换ArrayList
     * @param data 数组源数据
     * @param <T>  泛型
     * @return     ArrayList
     */
    public static <T> ArrayList<T> TranformList(T[] data){
        if (data==null||data.length==0){
            return null;
        }
        ArrayList<T> list = new ArrayList<>();
        for (T t : data) {
            list.add(t);
        }
        return list;
    }


}
