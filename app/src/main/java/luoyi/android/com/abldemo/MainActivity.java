package luoyi.android.com.abldemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.levine.abllib.AblConfig;
import com.levine.abllib.AblService;
import com.levine.abllib.AblStepHandler;
import com.levine.abllib.AblSteps;
import com.levine.abllib.utils.AblUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AblConfig.Builder()
                .setLogTag("123456")
                .setStepMsgDelayMillis(2000)
                .setFindViewCountDownInterval(200)
                .setFindViewMillisInFuture(10000)
                .build().init();
        AblStepHandler.getInstance().initStepClass(new TestAblStep1());
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AblUtil.openDrawOverlaysAnth(MainActivity.this);
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AblUtil.addSuspensionWindowView(MainActivity.this, initWindowView());
            }
        });
        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AblUtil.openAccessibilitySettings();
            }
        });
        findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AblUtil.isAccessibilityServiceOpen(MainActivity.this)) {
                    AblStepHandler.getInstance().setStop(false);
                    AblStepHandler.sendMsg(AblSteps.STEP_1);
                } else {
                    ToastUtils.showShort("请先开启辅助服务");
                }

            }
        });
        findViewById(R.id.btn_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AblStepHandler.getInstance().setStop(true);
            }
        });
        findViewById(R.id.btn_6).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                AblService.getInstance().disableSelf();
            }
        });
    }

    private View initWindowView() {
        View view = View.inflate(this, R.layout.view_test, null);
        view.findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AblUtil.isAccessibilityServiceOpen(MainActivity.this)) {
                    AblStepHandler.getInstance().setStop(false);
                    AblStepHandler.sendMsg(AblSteps.STEP_1);
                } else {
                    ToastUtils.showShort("请先开启辅助服务");
                }
            }
        });
        view.findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AblStepHandler.getInstance().setStop(true);
            }
        });
        view.findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                AblService.getInstance().disableSelf();
            }
        });
        view.findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                windowManager.removeView(view);
            }
        });
        return view;
    }
}
