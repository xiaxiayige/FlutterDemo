package com.example.flutter_app.meetingboard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by wangyong on 7/4/17.
 */
public class ApplicationResources extends Application {

    public static String mPenName = "SmartPen";
    public static String mFirmWare = "B736_OID1-V10000";
    public static String mMCUFirmWare = "MCUF_R01";
    public static String mCustomerID = "0000";
    public static String mBTMac = "00:00:00:00:00:2F";
    public static int mBattery = 100;
    public static boolean mCharging = false;
    public static int mUsedMem = 0;
    public static boolean mBeep = true;
    public static boolean mPowerOnMode = true;
    public static int mPowerOffTime = 20;
    public static long mTimer = 1262275200; // 2010-01-01 00:00:00
    public static int mPenSens = 0;
    public static int mTwentyPressure = 0;
    public static int mThreeHundredPressure = 0;
    public static long mElementCode = 0;

    public static String tmp_mPenName;
    public static boolean tmp_mBeep = true;
    public static boolean tmp_mPowerOnMode = true;
    public static boolean tmp_mEnableLED = false;
    public static int tmp_mPowerOffTime;
    public static int tmp_mPenSens;
    public static long tmp_mTimer;

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            Log.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;
        PackageManager pm = ctx.getApplicationContext().getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
        /*debuggable variable will remain false*/
        }
        return debuggable;
    }

    @Override
    protected void attachBaseContext(Context base) {
        SharedPreferences preferences = base.getSharedPreferences("language", Context.MODE_PRIVATE);
        String selectedLanguage = preferences.getString("language", "");
        super.attachBaseContext(LanguageUtil.attachBaseContext(base, selectedLanguage));
    }
}
