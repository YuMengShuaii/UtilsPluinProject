package com.enation.javashop.utils.base.tool;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.enation.javashop.utils.base.tool.CommonTool.TAG;

/**
 * Created by LDD on 2017/12/11.
 */

public class SystemTool {
    /**
     * 安装某个应用
     *
     * @param context
     * @param apkFile
     * @return
     */
    public static boolean installApp(Context context, File apkFile) {
        try {
            context.startActivity(getInstallAppIntent(apkFile));
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * 获取安装应用的Intent
     *
     * @param apkFile
     * @return
     */
    public static Intent getInstallAppIntent(File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            return null;
        }
        chmod("777", apkFile.getAbsolutePath());
        Uri uri = Uri.fromFile(apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取权限
     *
     * @param permission
     *            权限
     * @param path
     *            文件路径
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            Log.e(TAG, "chmod", e);
        }
    }

    /**
     * 查某个包名的App是否已经安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasAppInstalled(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageManager.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 根据包名启动第三方App
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean launchAppByPackageName(Context context,
                                                 String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }

        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return false;
    }

    /**
     * 获取Assets资源
     * @param context
     * @param name
     * @return
     * @throws IOException
     */
    public static String getAssetsFie(Context context, String name)
            throws IOException {

        InputStream is = context.getAssets().open(name);
        int size = is.available();

        // Read the entire asset into a local byte buffer.
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String content = new String(buffer, "UTF-8");

        return content;

    }

    /**
     * 是否安装了sdcard卡
     *
     * @return true表示有，false表示没有
     */
    public static boolean haveSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统内部可用空间大小
     *
     * @return available size
     */
    public static long getSystemAvailableSize() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }

    /**
     * 获取sd卡可用空间大小
     *
     * @return available size
     */
    public static long getSDCardAvailableSize() {
        long available = 0;
        if (haveSDCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            long blocSize = statfs.getBlockSize();
            long availaBlock = statfs.getAvailableBlocks();

            available = availaBlock * blocSize;
        } else {
            available = -1;
        }
        return available;
    }

    /**
     * 获取application层级的metadata
     *
     * @param context
     * @param key
     * @return
     */
    public static String getApplicationMetaData(Context context, String key) {
        try {
            Object metaObj = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA).metaData
                    .get(key);
            if (metaObj instanceof String) {
                return metaObj.toString();
            } else if (metaObj instanceof Integer) {
                return ((Integer) metaObj).intValue() + "";
            } else if (metaObj instanceof Boolean) {
                return ((Boolean) metaObj).booleanValue() + "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        return null;
    }

    /**
     * 获取电池电量,0~1
     *
     * @param context
     * @return
     */
    @SuppressWarnings("unused")
    public static float getBattery(Context context) {
        Intent batteryInfoIntent = context.getApplicationContext()
                .registerReceiver(null,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryInfoIntent.getIntExtra("status", 0);
        int health = batteryInfoIntent.getIntExtra("health", 1);
        boolean present = batteryInfoIntent.getBooleanExtra("present", false);
        int level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 0);
        int plugged = batteryInfoIntent.getIntExtra("plugged", 0);
        int voltage = batteryInfoIntent.getIntExtra("voltage", 0);
        int temperature = batteryInfoIntent.getIntExtra("temperature", 0); // 温度的单位是10�?
        String technology = batteryInfoIntent.getStringExtra("technology");
        return ((float) level) / scale;
    }

    /**
     * 获取手机名称
     *
     * @return
     */
    public static String getMobileName() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    /**
     * 获取手机型号
     * @return 手机型号
     */
    public static String getPhoneType() {

        String phoneType = Build.MODEL;

        Log.d(TAG, "phoneType is : " + phoneType);

        return phoneType;
    }


    /**
     * 获取手机号，几乎获取不到
     *
     * @param context
     * @return
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return TextUtils.isEmpty(mTelephonyMgr.getLine1Number()) ? "" : mTelephonyMgr.getLine1Number();
        } else {
            return "";
        }
    }

    /**
     * 获取imei
     *
     * @param context
     * @return
     */
    public static String getPhoneImei(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        String phoneImei = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            phoneImei = mTelephonyMgr.getDeviceId();
        }
        Log.d(TAG, "IMEI is : " + phoneImei);
        return TextUtils.isEmpty(phoneImei) ? "" : phoneImei;
    }

    /**
     * 获取imsi
     *
     * @param context
     * @return
     */
    public static String getPhoneImsi(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        String phoneImsi ="";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            phoneImsi = mTelephonyMgr.getSubscriberId();
        }
        Log.d(TAG, "IMSI is : " + phoneImsi);
        return TextUtils.isEmpty(phoneImsi) ? "" : phoneImsi;
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getLocalMacAddress() {
        String Mac = null;
        try {
            String path = "sys/class/net/wlan0/address";
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                path = "sys/class/net/eth0/address";
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis.read(buffer_name);
                if (byteCount_name > 0) {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                return "";
            } else if (Mac.endsWith("\n")) {
                Mac = Mac.substring(0, Mac.length() - 1);
            }
        } catch (Exception io) {
            Log.w(TAG, "Exception", io);
        }

        return TextUtils.isEmpty(Mac) ? "" : Mac;
    }


}
