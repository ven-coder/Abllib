package com.levine.abllib.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.levine.abllib.AblConfig;
import com.levine.abllib.AblService;

import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 2019/4/21
 * 18:21
 * Levine
 * wechat 1483232332
 */
public class AblUtil {

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
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;//窗口的宽和高
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
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
