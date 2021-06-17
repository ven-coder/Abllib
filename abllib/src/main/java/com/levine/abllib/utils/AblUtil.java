package com.levine.abllib.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.levine.abllib.AblConfig;
import com.levine.abllib.AblService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 2019/4/21
 * 18:21
 * Levine
 * wechat 1483232332
 */
public class AblUtil {

    /***
     * 检查悬浮窗开启权限
     * @param context
     * @return
     */
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }

    /**
     * 添加悬浮界面
     *
     * @param context
     * @param view
     */
    public static void addSuspensionWindowView(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                addSuspensionView(context, view);
            } else {
                ToastUtils.showShort("请先开启允许显示在其他应用上权限");
            }
        } else {
            addSuspensionView(context, view);
        }
    }

    public static void removeSuspensionWindowView(Context context, View view) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(view);
    }

    /**
     * 添加悬浮界面
     *
     * @param context
     * @param view
     */
    private static void addSuspensionView(Context context, View view) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.TRANSLUCENT;// 支持透明
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;// 焦点
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;//窗口的宽和高
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.x = 0;//窗口位置的偏移量
        layoutParams.y = 0;
        layoutParams.gravity = Gravity.CENTER;
        windowManager.addView(view, layoutParams);//添加窗口
    }

    /**
     * 打开悬浮窗授权界面
     *
     * @param context
     */
    public static void openDrawOverlaysAnth(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())
        );
        context.startActivity(intent);
    }

    /**
     * 检查是否是需要监听的APP包名
     *
     * @param thisPackageName APP包名
     * @return
     */
    public static boolean checkPackageName(String thisPackageName) {
        if (AblConfig.sMonitoringPackageNames != null && AblConfig.sMonitoringPackageNames.length > 0) {
            for (String packageName : AblConfig.sMonitoringPackageNames) {
                if (packageName.contentEquals(thisPackageName)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 打开无障碍服务设置
     */
    public static void openAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    /**
     * 辅助服务是否开启
     *
     * @return
     */
    public static boolean isAccessibilityServiceOpen(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            LogUtils.v(info.service.getClassName());
            if (info.service.getClassName().equals(AblService.class.getName())) {
                return true;
            }
        }
        return false;
    }
}
