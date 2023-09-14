package cn.zhg.tts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;

import java.util.*;

import cn.zhg.tts.util.SomeUtil;

/**
 * 系统调用回调activity,用于获取样本文本,无需界面
 */
public class GetSampleActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//        Logger.d("传入intent :"+intent);
        if (intent == null || !Engine.ACTION_GET_SAMPLE_TEXT.equals(intent.getAction())) {
            //错误调用
            finish();
            return;
        }
        Bundle extras = intent.getExtras();
        String sampleText;
        if(extras==null){
            sampleText=getString(R.string.sample_text);
        }else{
            Locale locale= SomeUtil.getLocale(extras.getString("language"),extras.getString("country"),extras.getString("variant"));
            Configuration config = new Configuration();
            config.setLocale(locale);
            Context localeContext = createConfigurationContext(config);
            Resources localeResources = localeContext.getResources();
            sampleText=localeResources.getString(R.string.sample_text);
        }
        Intent data=new Intent();
        data.putExtra(Engine.EXTRA_SAMPLE_TEXT,sampleText);
        setResult(TextToSpeech.LANG_AVAILABLE,data);
        finish();
    }
}
