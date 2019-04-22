package luoyi.android.com.abldemo;

import android.os.Message;
import android.view.accessibility.AccessibilityNodeInfo;

import com.levine.abllib.AblStepHandler;
import com.levine.abllib.AblSteps;
import com.levine.abllib.utils.AblViewUtil;
import com.levine.abllib.AblStepBase;
import com.levine.abllib.callback.AniCallBack;

/**
 * 2019/4/21
 * 17:01
 * Levine
 * wechat 1483232332
 */
public class TestAblStep1 extends AblStepBase {
    @Override
    public void onStep(int step, Message msg) {
        switch (step) {
            case AblSteps.STEP_1:
                AblViewUtil.findById(
                        "luoyi.android.com.abldemo:id/btn_3",
                        0,
                        new AniCallBack() {
                            @Override
                            public void succ(AccessibilityNodeInfo info) {
                                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                AblStepHandler.sendMsg(AblSteps.STEP_2);
                            }
                        }
                );
                break;
            case AblSteps.STEP_2:
                AblViewUtil.back();
                AblStepHandler.sendMsg(AblSteps.STEP_1);
                break;
        }
    }
}
