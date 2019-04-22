package com.levine.abllib.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.levine.abllib.AblService;
import com.levine.abllib.callback.AniCallBack;
import com.levine.abllib.callback.AnisCallBack;
import com.levine.abllib.callback.GestureCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 界面操作工具类
 * wechat 1483232332
 */
public class AblViewUtil {

    /**
     * 根据id获取view
     *
     * @param parent
     * @param id       界面id
     * @param position
     * @return
     */
    public static AccessibilityNodeInfo findById(
            AccessibilityNodeInfo parent,
            String id,
            int position,
            @Nullable AniCallBack callBack
    ) {
        if (parent != null) {
            List<AccessibilityNodeInfo> accessibilityNodeInfos =
                    parent.findAccessibilityNodeInfosByViewId(id);
            if (accessibilityNodeInfos != null &&
                    !accessibilityNodeInfos.isEmpty() &&
                    position < accessibilityNodeInfos.size()) {
                if (callBack != null) {
                    callBack.succ(accessibilityNodeInfos.get(position));
                }
                return accessibilityNodeInfos.get(position);
            }
        }
        if (callBack != null) {
            callBack.fail();
        }
        return null;
    }

    public static AccessibilityNodeInfo findById(
            AccessibilityNodeInfo parent,
            String id,
            int position
    ) {
        return findById(parent, id, position, null);
    }

    public static AccessibilityNodeInfo findById(String id, int position) {
        return findById(AblService.getInstance().getRootInActiveWindow(), id, position, null);
    }

    public static AccessibilityNodeInfo findById(String id, int position, AniCallBack callBack) {
        return findById(AblService.getInstance().getRootInActiveWindow(), id, position, callBack);
    }

    public static List<AccessibilityNodeInfo> findById(
            AccessibilityNodeInfo parent,
            String id,
            @Nullable AnisCallBack anisCallBack
    ) {
        if (parent != null) {
            List<AccessibilityNodeInfo> accessibilityNodeInfos =
                    parent.findAccessibilityNodeInfosByViewId(id);
            if (accessibilityNodeInfos != null &&
                    !accessibilityNodeInfos.isEmpty()) {
                if (anisCallBack != null) anisCallBack.succ(accessibilityNodeInfos);
                return accessibilityNodeInfos;
            }
        }
        if (anisCallBack != null) anisCallBack.fail();
        return new ArrayList<>();
    }

    public static List<AccessibilityNodeInfo> findById(
            AccessibilityNodeInfo parent,
            String id
    ) {
        return findById(parent, id, null);
    }

    public static List<AccessibilityNodeInfo> findById(String id) {
        return findById(AblService.getInstance().getRootInActiveWindow(), id, null);
    }

    public static List<AccessibilityNodeInfo> findById(String id, AnisCallBack anisCallBack) {
        return findById(AblService.getInstance().getRootInActiveWindow(), id, anisCallBack);
    }

    /**
     * 根据文本获取view
     *
     * @param parent
     * @param text
     * @param position
     * @return
     */
    public static AccessibilityNodeInfo findByText(AccessibilityNodeInfo parent, String text, int position) {
        if (parent != null ) {
            List<AccessibilityNodeInfo> accessibilityNodeInfos =
                    parent.findAccessibilityNodeInfosByText(text);
            if (accessibilityNodeInfos != null &&
                    !accessibilityNodeInfos.isEmpty() &&
                    position < accessibilityNodeInfos.size()) {
                return accessibilityNodeInfos.get(position);
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findByText(String text, int position) {
        return findByText(AblService.getInstance().getRootInActiveWindow(), text, position);
    }

    public static List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo parent, String text) {
        if (parent != null ) {
            List<AccessibilityNodeInfo> accessibilityNodeInfos =
                    parent.findAccessibilityNodeInfosByText(text);
            if (accessibilityNodeInfos != null &&
                    !accessibilityNodeInfos.isEmpty()) {
                return accessibilityNodeInfos;
            }
        }
        return new ArrayList<>();
    }

    public static List<AccessibilityNodeInfo> findByText(String text) {
        return findByText(AblService.getInstance().getRootInActiveWindow(), text);
    }

    /**
     * 手势模拟
     *
     * @param start_position 开始位置，长度为2的数组，下标0为x轴，下标1为y轴
     * @param end_position
     * @param startTime      开始间隔时间
     * @param duration       持续时间
     * @param callback       回调
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static void dispatch_gesture(
            float[] start_position,
            float[] end_position,
            long startTime,
            long duration,
            @Nullable GestureCallBack callback
    ) {
        Path path = new Path();
        path.moveTo(start_position[0], start_position[1]);
        path.lineTo(end_position[0], end_position[1]);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription.StrokeDescription strokeDescription =
                new GestureDescription.StrokeDescription(path, startTime, duration);
        GestureDescription gestureDescription = builder.addStroke(strokeDescription).build();
        AblService.getInstance().dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                if (callback != null) callback.succ(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                if (callback != null) callback.fail(gestureDescription);
            }
        }, null);
    }

    /**
     * x轴居中竖直滑动屏幕
     *
     * @param yRatio          y轴份数
     * @param startSlideRatio y轴开始的份数
     * @param stopSlideRatio  y轴结束的份数
     */
    public static void scrollVertical(
            int yRatio,
            int startSlideRatio,
            int stopSlideRatio,
            long startTime,
            long duration,
            @Nullable GestureCallBack callback
    ) {
        int screenHeight = ScreenUtils.getScreenHeight();
        int screenWidth = ScreenUtils.getScreenWidth();
        int start = (screenHeight / yRatio) * startSlideRatio;
        int stop = (screenHeight / yRatio) * stopSlideRatio;
        dispatch_gesture(
                new float[]{screenWidth >> 1, start},
                new float[]{screenWidth >> 1, stop},
                startTime,
                duration,
                callback
        );

    }

    /**
     * 竖直滑动，y轴分为20份
     *
     * @param startSlideRatio 开始份数
     * @param stopSlideRatio  结束份数
     */
    public static void scrollVertical(
            int startSlideRatio,
            int stopSlideRatio
    ) {
        int screenHeight = ScreenUtils.getScreenHeight();
        int screenWidth = ScreenUtils.getScreenWidth();
        int start = (screenHeight / 20) * startSlideRatio;
        int stop = (screenHeight / 20) * stopSlideRatio;
        dispatch_gesture(
                new float[]{screenWidth >> 1, start},
                new float[]{screenWidth >> 1, stop},
                50,
                500,
                null
        );

    }

    /**
     * x轴居中竖直滑动屏幕
     *
     * @param startY y轴开始位置
     * @param stopY  y轴结束位置
     */
    public static void scrollVertical(
            float startY,
            float stopY,
            long startTime,
            long duration,
            @Nullable GestureCallBack callback
    ) {
        int screenWidth = ScreenUtils.getScreenWidth();
        dispatch_gesture(
                new float[]{screenWidth >> 1, startY},
                new float[]{screenWidth >> 1, stopY},
                startTime,
                duration,
                callback
        );

    }


    /**
     * y轴居中横向滑动
     *
     * @param xRatio          x轴份数
     * @param startSlideRatio x轴开始滑动份数
     * @param stopSlideRatio  x轴结束滑动份数
     * @param startTime       开始延迟时间
     * @param duration        滑动持续时间
     * @param callback        回调
     */
    public void scrollHorizontal(
            int xRatio,
            int startSlideRatio,
            int stopSlideRatio,
            long startTime,
            long duration,
            @Nullable GestureCallBack callback
    ) {
        int screenHeight = ScreenUtils.getScreenHeight();
        int screenWidth = ScreenUtils.getScreenWidth();
        int start = (screenWidth / xRatio) * startSlideRatio;
        int stop = (screenWidth / xRatio) * stopSlideRatio;
        dispatch_gesture(
                new float[]{start, screenHeight >> 1},
                new float[]{stop, screenHeight >> 1},
                startTime,
                duration,
                callback
        );
    }

    /**
     * y轴居中横向滑动
     *
     * @param startX    x轴开始位置
     * @param stopX     x轴结束位置
     * @param startTime 开始延迟时间
     * @param duration  滑动持续时间
     * @param callback  回调
     */
    public void scrollHorizontal(
            int startX,
            int stopX,
            long startTime,
            long duration,
            @Nullable GestureCallBack callback
    ) {
        int screenHeight = ScreenUtils.getScreenHeight();
        dispatch_gesture(
                new float[]{startX, screenHeight >> 1},
                new float[]{stopX, screenHeight >> 1},
                startTime,
                duration,
                callback
        );
    }

    /**
     * 点击屏幕
     *
     * @param ratio  屏幕长宽份数
     * @param xRatio 屏幕宽ratio份的比例
     * @param yRatio
     */
    public static void clickScreen(
            int ratio,
            int xRatio,
            int yRatio,
            @Nullable GestureCallBack callback
    ) {
        int screenHeight = ScreenUtils.getScreenHeight();
        int screenWidth = ScreenUtils.getScreenWidth();
        int y = (screenHeight / ratio) * yRatio;
        int x = (screenWidth / ratio) * xRatio;
        dispatch_gesture(
                new float[]{x, y},
                new float[]{x, y},
                100,
                50,
                callback
        );
    }

    /**
     * 点击屏幕
     */
    public static void clickScreen(
            int xRatio,
            int yRatio
    ) {
        int screenHeight = ScreenUtils.getScreenHeight();
        int screenWidth = ScreenUtils.getScreenWidth();
        int y = (screenHeight / 20) * yRatio;
        int x = (screenWidth / 20) * xRatio;
        dispatch_gesture(
                new float[]{x, y},
                new float[]{x, y},
                100,
                50,
                null
        );
    }

    /**
     * 点击屏幕
     *
     * @param x         x轴像素
     * @param y
     * @param startTime 开始间隔时间
     * @param duration  持续时间
     */
    public static void clickScreen(
            float x,
            float y,
            long startTime,
            long duration,
            @Nullable GestureCallBack callback
    ) {
        dispatch_gesture(
                new float[]{x, y},
                new float[]{x, y},
                startTime,
                duration,
                callback
        );
    }

    /**
     * 返回
     */
    public static void back() {
        AblService.getInstance().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 黏贴文本至view
     *
     * @param info
     * @param text
     */
    public static void paste(AccessibilityNodeInfo info, String text) {
        ClipData clip = ClipData.newPlainText(System.currentTimeMillis() + "", text);
        ClipboardManager clipboardManager = (ClipboardManager) AblService.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        assert clipboardManager != null;
        clipboardManager.setPrimaryClip(clip);
        ClipData abc = clipboardManager.getPrimaryClip();
        assert abc != null;
        ClipData.Item item = abc.getItemAt(0);
        if (info != null) info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
    }
}
