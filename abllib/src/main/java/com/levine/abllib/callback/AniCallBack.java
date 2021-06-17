package com.levine.abllib.callback;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * 2019/4/21
 * 13:05
 * Levine
 * wechat 1483232332
 */
public interface AniCallBack {
    void succ(AccessibilityNodeInfo info);

    default void fail() {
    }
}
