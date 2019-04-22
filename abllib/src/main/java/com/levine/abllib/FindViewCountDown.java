package com.levine.abllib;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityNodeInfo;

import com.levine.abllib.utils.AblViewUtil;

/**
 * 寻找界面倒计时
 * 2019/4/21
 * 15:21
 * Levine
 * wechat 1483232332
 */
public class FindViewCountDown {

    private static DownTimer mDownTimer;
    public static  long      millisInFuture    = 10000;//默认倒计时总时长
    public static  long      countDownInterval = 500;//间隔时间

    private static class DownTimer extends CountDownTimer {

        private String[] mMsgViewIdOrText;
        private int      mStep;
        private CallBack mCallback;

        public DownTimer(
                long millisInFuture,
                long countDownInterval,
                int step, CallBack callback,
                String... viewIdOrText
        ) {
            super(millisInFuture, countDownInterval);
            mMsgViewIdOrText = viewIdOrText.clone();
            mStep = step;
            mCallback = callback;
        }

        @Override
        public void onTick(long l) {
            if (AblStepHandler.getInstance().isStop()) {
                cancel();
                return;
            }
            for (String string : mMsgViewIdOrText) {
                AccessibilityNodeInfo fromId = AblViewUtil.findById(string, 0);
                AccessibilityNodeInfo fromText = AblViewUtil.findByText(string, 0);
                if (fromId != null || fromText != null) {
                    cancel();
                    if (mCallback != null) {
                        mCallback.succ();
                    } else {
                        AblStepHandler.sendMsg(mStep);
                    }
                    return;
                }
            }
        }

        @Override
        public void onFinish() {
            if (mCallback != null) mCallback.fail();
        }
    }

    public interface CallBack {
        void succ();

        default void fail() {
        }
    }

    /**
     * 根据viewid或文字发送执行消息
     *
     * @param millisInFuture    view查找超时时间
     * @param countDownInterval 查找间隔时间
     * @param step              步骤
     * @param viewIdOrText      viewid或者文字内容
     */
    public static void start(
            long millisInFuture,
            long countDownInterval,
            int step,
            String... viewIdOrText
    ) {
        start(millisInFuture, countDownInterval, step, null, viewIdOrText);
    }

    /**
     * 根据viewid或文字发送执行消息
     *
     * @param millisInFuture    view查找超时时间
     * @param countDownInterval 查找间隔时间
     * @param callBack          回调，注：如果回调不为空将不发送消息，要发送消息就自己在成功的回调里发送
     * @param viewIdOrText      viewid或者文字内容
     */
    public static void start(
            long millisInFuture,
            long countDownInterval,
            @NonNull CallBack callBack,
            String... viewIdOrText
    ) {
        start(millisInFuture, countDownInterval, 0, callBack, viewIdOrText);
    }

    /**
     * 根据viewid或文字发送执行消息
     *
     * @param callBack     回调，注：如果回调不为空将不发送消息，要发送消息就自己在成功的回调里发送
     * @param viewIdOrText viewid或者文字内容
     */
    public static void start(
            @NonNull CallBack callBack,
            String... viewIdOrText
    ) {
        start(millisInFuture, countDownInterval, 0, callBack, viewIdOrText);
    }

    /**
     * 根据viewid或文字发送执行消息
     *
     * @param step         步骤
     * @param viewIdOrText viewid或者文字内容
     */
    public static void start(
            int step,
            String... viewIdOrText
    ) {
        start(millisInFuture, countDownInterval, step, null, viewIdOrText);
    }

    /**
     * 根据viewid或文字发送执行消息
     *
     * @param millisInFuture    view查找超时时间
     * @param countDownInterval 查找间隔时间
     * @param step              步骤
     * @param callBack          回调，注：如果回调不为空将不发送消息，要发送消息就自己在成功的回调里发送
     * @param viewIdOrText      viewid或者文字内容
     */
    private static void start(
            long millisInFuture,
            long countDownInterval,
            int step,
            CallBack callBack,
            String... viewIdOrText
    ) {
        if (mDownTimer != null) mDownTimer.cancel();
        mDownTimer = new DownTimer(millisInFuture, countDownInterval, step, callBack, viewIdOrText);
        mDownTimer.start();
    }

    public static DownTimer getDownTimer() {
        return mDownTimer;
    }
}
