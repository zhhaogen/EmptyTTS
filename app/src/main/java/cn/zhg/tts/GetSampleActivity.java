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
public class GetSampleActivity extends Activity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//        Logger.d("传入intent :"+intent);
        if (intent == null || !Engine.ACTION_GET_SAMPLE_TEXT.equals(intent.getAction()))
        {
            //错误调用
            finish();
            return;
        }
        Intent data = new Intent();
        data.putExtra(Engine.EXTRA_SAMPLE_TEXT, getSampleText(intent));
        setResult(TextToSpeech.LANG_AVAILABLE, data);
        finish();
    }

    private String getSampleText(Intent data)
    {
        Bundle extras = data.getExtras();
        if (extras == null)
        {
            return getString(R.string.sample_text);
        }
        Locale locale = SomeUtil.getLocale(extras.getString("language"), extras.getString("country"), extras.getString("variant"));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            Resources res = getResources();
            Configuration conf = res.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = locale;
            res.updateConfiguration(conf, null);
            String sampleText = res.getString(R.string.sample_text);
            conf.locale = savedLocale;
            res.updateConfiguration(conf, null);
            return sampleText;
        }
        Configuration config = new Configuration();
        config.setLocale(locale);
        Context localeContext  = createConfigurationContext(config);
        Resources localeResources = localeContext.getResources();
        return localeResources.getString(R.string.sample_text);
    }
}
