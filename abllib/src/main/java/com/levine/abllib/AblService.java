package com.levine.abllib;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

/**
 * 2019/4/21
 * 11:29
 * Levine
 * wechat 1483232332
 */
public class AblService extends AccessibilityService {

    private static AblService mAblService;

    public static AblService getInstance() {
        if (mAblService == null) {
            throw new NullPointerException("AblService辅助服务未开启");
        }
        return mAblService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAblService = this;
        init();
    }

    private void init() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.v("onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.v(event.getPackageName() + "");
    }

    @Override
    public void onInterrupt() {

    }
}
