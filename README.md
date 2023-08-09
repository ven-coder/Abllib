# Abllib   （此开源库已不再维护，已重新开发更加易用稳定的库[Assists](https://github.com/ven-coder/assists)）

[Demo:https://github.com/Levine1992/Abllib](https://github.com/Levine1992/Abllib)

**AccessibilityService无障碍服务库，一行代码启用，快速开发复杂的自动操作业务**
 
 **AccessibilityService可以帮助我们做一些自动操作手机的动作，像微信自动抢红包、各种应用市场的自动安装功能就是利用的AccessibilityService服务，
利用这个服务我们可以做更多有意思的事，但是直接继承这个服务，要实现一些复杂点的业务逻辑就很麻烦，所以写了这个库。** 

### 引用
project build.gradle中添加

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
app build.gradle中添加

```
dependencies {
    ...
    implementation 'com.github.Levine1992:Abllib:V1.0'
}
```

### 初始化一些配置

```
AblConfig.Builder()
                .setLogTag("123456")//logtag，不设置默认是abllib
                .setStepMsgDelayMillis(2000)//步骤延迟时间
                .setFindViewMillisInFuture(10000)//寻找界面超时时间
                .setFindViewCountDownInterval(200)//寻找界面间隔时间
                .build().init();
```
### 使用说明
新建具体的自动操作的步骤器继承AblStepBase，如果自动操作业务多可以分开多建几个，区分开便于维护，但是步骤的id最好不要重复

```
public class TestAblStep1 extends AblStepBase {
    @Override
    public void onStep(int step, Message msg) {
        switch (step) {
            case AblSteps.STEP_1://步骤1
                AblViewUtil.findById(
                        "luoyi.android.com.abldemo:id/btn_3",//传入需要获取抓取的界面的id
                        0,//由于抓到的界面有可能有多个所以传入要获取第几个，一般0就可以
                        new AniCallBack() {//回调
                            @Override
                            public void succ(AccessibilityNodeInfo info) {
                                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);//抓到后执行点击界面操作
                                AblStepHandler.sendMsg(AblSteps.STEP_2);//执行步骤2
                            }

                            @Override
                            public void fail() {//处理抓取失败逻辑

                            }
                        }
                );
                break;
            case AblSteps.STEP_2://步骤2
                AblViewUtil.back();//返回
                AblStepHandler.sendMsg(AblSteps.STEP_1);
                break;
        }
    }
```

```
public class TestAblStep2 extends AblStepBase {
    @Override
    public void onStep(int step, Message msg) {
        switch (step) {
            case AblSteps.STEP_3://步骤3
                
                break;
            case AblSteps.STEP_4://步骤4
                
                break;
        }
    }
```


 初始化这些步骤器

```
AblStepHandler.getInstance().initStepClass(new TestAblStep1(),new TestAblStep2());
```

执行

```
AblStepHandler.getInstance().setStop(false);//开启步骤器
AblStepHandler.sendMsg(AblSteps.STEP_1);//执行步骤，如果设置步骤间隔时间为2000，则会延迟两秒执行
```

 ***

**AccessibilityService技术交流群**
<div align="left"> <img src=https://github.com/Ven-CN/Abllib/blob/master/resource/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230403143012.jpg width=30% height=30% /> </div>

如果群二维码失效可直接加我**Wechat：1483232332**拉进群
<div align="left"> <img src=https://github.com/LiQiwen-CN/Abllib/blob/master/resource/20230303100049.jpg width=25% height=25% /> </div>
