package cn.zhg.tts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.Engine;

import java.util.*;

import cn.zhg.tts.inter.Constant;
import cn.zhg.tts.util.SomeUtil;

/**
 * 系统调用回调activity,用于获取语言列表,无需界面
 */
public class GetVoicesActivity extends Activity implements Constant
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//        Logger.d("传入intent :"+intent);
        if (intent == null || !Engine.ACTION_CHECK_TTS_DATA.equals(intent.getAction()))
        {
            //错误调用
            finish();
            return;
        }
        //返回数据
        Intent data = new Intent();
        data.putExtra(Engine.EXTRA_AVAILABLE_VOICES, getAvailableVoices());
        data.putExtra(Engine.EXTRA_UNAVAILABLE_VOICES, new ArrayList<>());
        setResult(Engine.CHECK_VOICE_DATA_PASS, data);
        finish();
    }
    /**支持语言选项*/
    private ArrayList<String> getAvailableVoices()
    {
        SharedPreferences sh = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        //支持语言选项
        int availableLanguagesOption = sh.getInt(KEY_availableLanguagesOption, 0);
        ArrayList<String> availableVoices = new ArrayList<>();
        if (availableLanguagesOption == availableLanguagesOption_LIST)
        {
            //自定义列表
            availableVoices.addAll(sh.getStringSet(KEY_availableLanguages, new HashSet<>()));
            return availableVoices;
        }
        if (availableLanguagesOption == availableLanguagesOption_NONE)
        {
            //一个都没有
            return availableVoices;
        }
        //全部可用
        Locale[] locales = SomeUtil.getAvailableLocales();
        for (Locale locale : locales)
        {
            availableVoices.add(SomeUtil.getLanguageISO3Tag(locale));
        }
        return availableVoices;
    }
}
