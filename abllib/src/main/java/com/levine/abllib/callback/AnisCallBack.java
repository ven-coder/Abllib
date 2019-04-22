package com.levine.abllib.callback;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * 2019/4/21
 * 13:04
 * Levine
 * wechat 1483232332
 */
public interface AnisCallBack {
    void succ(List<AccessibilityNodeInfo> infos);

    default void fail() {
    }
}
