package com.king.run.activity.sport.walk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.listener.AutoCheck;
import com.king.run.listener.InitConfig;
import com.king.run.listener.MySyntherizer;
import com.king.run.listener.NonBlockSyntherizer;
import com.king.run.listener.OfflineResource;
import com.king.run.listener.UiMessageListener;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_voice_setting)
public class VoiceSettingActivity extends BaseActivity {
    protected String appId = "10616019";

    protected String appKey = "b3eq76bslYq9T3myv9G2pKXc";

    protected String secretKey = "KuMHQG8ZMXGwZB6kUtyLtj0yQSepQYLH";

    protected String offlineVoice = OfflineResource.VOICE_MALE;
    protected TtsMode ttsMode = TtsMode.MIX;
    protected MySyntherizer synthesizer;
    @ViewInject(R.id.iv_man)
    ImageView iv_man;
    @ViewInject(R.id.iv_woman)
    ImageView iv_woman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
//        if (checkStoragePhonePermission()) {
        initViews();
//            initialTts();
//        }
    }

    private void initViews() {
        title_tv_title.setText(R.string.voice_type);
        int type = PrefName.getVoiceSetting(context);
        if (type == 0) {
            iv_man.setBackgroundResource(R.mipmap.common_icon_check_selected);
            iv_woman.setBackgroundResource(0);
        } else {
            iv_man.setBackgroundResource(0);
            iv_woman.setBackgroundResource(R.mipmap.common_icon_check_selected);
        }
    }

    @Override
    protected void gotPhoneStoragePermissionResult(boolean isGrant) {
        super.gotPhoneStoragePermissionResult(isGrant);
        if (isGrant) {
            initViews();
            initialTts();
        } else
            senToa(R.string.permission_phone_storage);
    }


    @Override
    public void setFinish() {
        setResult(RESULT_OK);
        super.setFinish();
    }

    @Event(value = {R.id.tv_girl, R.id.ly_china_man_voice, R.id.ly_china_woman_voice})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_girl:
//                speak();
                break;
            case R.id.ly_china_man_voice:
                iv_man.setBackgroundResource(R.mipmap.common_icon_check_selected);
                iv_woman.setBackgroundResource(0);
                PreferencesUtils.putInt(context, PrefName.VOICE_SETTING, 0);
                break;
            case R.id.ly_china_woman_voice:
                iv_man.setBackgroundResource(0);
                iv_woman.setBackgroundResource(R.mipmap.common_icon_check_selected);
                PreferencesUtils.putInt(context, PrefName.VOICE_SETTING, 1);
                break;
        }
    }

    private void speak() {
//        mShowText.setText("");
//        String text = mInput.getText().toString();
        // 需要合成的文本text的长度不能超过1024个GBK字节。
//        if (TextUtils.isEmpty(mInput.getText())) {
        String text = "欢迎使用百度语音合成SDK,百度语音为你提供支持。";
//            mInput.setText(text);
//        }
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        int result = synthesizer.speak(text);
//        checkResult(result, "speak");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        Map<String, String> params = getParams();


        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
//                        toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                        Log.w("AutoCheckMessage", message);
                    }
                }
            }

        });
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "5");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }


    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
//            toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

}
