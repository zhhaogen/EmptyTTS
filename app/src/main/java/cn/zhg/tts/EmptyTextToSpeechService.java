package cn.zhg.tts;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.media.*;
import android.os.Build;
import android.speech.tts.*;
import android.text.TextUtils;

import java.util.*;

import cn.zhg.tts.inter.Constant;
import cn.zhg.tts.util.SomeUtil;

public class EmptyTextToSpeechService extends TextToSpeechService implements Constant {
    private SharedPreferences sh;
    public void onCreate() {
        super.onCreate();
        //不一定会优先调用?
//        Logger.d();
        sh=getSharedPreferences(getPackageName(),MODE_PRIVATE);
        SomeUtil.getAvailableLocales();
    }
    /**
     * @param lang    ISO-3 language code.
     * @param country ISO-3 country code. May be empty or null.
     * @param variant Language variant. May be empty or null.
     * @return
     */
    protected int onIsLanguageAvailable(String lang, String country, String variant) {
//        Logger.d("lang="+lang+",country="+country+",variant="+variant);
        if (TextUtils.isEmpty(lang) && TextUtils.isEmpty(country) && TextUtils.isEmpty(variant)) {
            return TextToSpeech.LANG_NOT_SUPPORTED;
        }
        Locale locale = SomeUtil.getAvailableLocale(lang, country, variant);
        if (locale == null) {
            return TextToSpeech.LANG_NOT_SUPPORTED;
        }
        //检查设置是否支持
        if (!isSettingSupportLocale(locale)) {
            return TextToSpeech.LANG_NOT_SUPPORTED;
        }
        if (!TextUtils.isEmpty(variant)) {
            return TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;
        }
        if (!TextUtils.isEmpty(country)) {
            return TextToSpeech.LANG_COUNTRY_AVAILABLE;
        }
        return TextToSpeech.LANG_AVAILABLE;
    }

    protected int onLoadLanguage(String lang, String country, String variant) {
//        Logger.d("lang="+lang+",country="+country+",variant="+variant);
        return onIsLanguageAvailable(lang, country, variant);
    }

    /**
     * @deprecated 仅适用于API <= 17 不会调用
     */
    protected String[] onGetLanguage() {
        Locale[] locales = SomeUtil.getAvailableLocales();
        String[] localeNames = new String[locales.length];
        for (int i = 0; i < localeNames.length; i++) {
            localeNames[i] = SomeUtil.getLanguageISO3Tag(locales[i]);
        }
        return localeNames;
    }

    protected void onStop() {

    }

    protected void onSynthesizeText(SynthesisRequest request, SynthesisCallback callback) {
//
//        if(request==null){
//            callback.done();
//            return;
//        }
//        CharSequence text=request.getCharSequenceText();
//        if(TextUtils.isEmpty(text)){
//            callback.done();
//            return;
//        }
////        callback.start(16000, AudioFormat.ENCODING_PCM_16BIT,1);
////        callback.audioAvailable(new byte[1],0,1);
        callback.done();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public List<Voice> onGetVoices() {
        boolean voiceNameOption = sh.getBoolean(KEY_voiceNameOption, false);
        if (!voiceNameOption) {
            return super.onGetVoices();
        }
        List<Voice> vs = new ArrayList<>();
        int availableLanguagesOption=sh.getInt(KEY_availableLanguagesOption,0);
        Locale[] locales = SomeUtil.getAvailableLocales();
        if (availableLanguagesOption == availableLanguagesOption_LIST) {
            //自定义列表
            Set<String>  availableLanguages = sh.getStringSet(KEY_availableLanguages, new HashSet<>());
            for (Locale locale : locales) {
                if(availableLanguages.contains(SomeUtil.getLanguageISO3Tag(locale))){
                    vs.add(new Voice(getVoiceName(locale), locale, Voice.LATENCY_HIGH, Voice.QUALITY_HIGH, false, new HashSet<>()));
                    continue;
                }
            }
        }else  if (availableLanguagesOption == availableLanguagesOption_NONE) {
            //do noting
        }else{
            for (Locale locale : locales) {
                vs.add(new Voice(getVoiceName(locale), locale, Voice.LATENCY_HIGH, Voice.QUALITY_HIGH, false, new HashSet<>()));
            }
        }
        return vs;
    }
    public String onGetDefaultVoiceNameFor(String lang, String country, String variant) {
        boolean voiceNameOption = sh.getBoolean(KEY_voiceNameOption, false);
        if (!voiceNameOption) {
            return super.onGetDefaultVoiceNameFor(lang, country, variant);
        }
        Locale locale = SomeUtil.getAvailableLocale(lang, country, variant);
        if (locale == null) {
            return null;
        }
        return getVoiceName(locale);
    }

    public int onIsValidVoiceName(String voiceName) {
        return TextToSpeech.SUCCESS;
    }

    public int onLoadVoice(String voiceName) {
        return TextToSpeech.SUCCESS;
    }

    @SuppressLint("StringFormatMatches")
    private String getVoiceName(Locale locale) {
        return getString(R.string.voice_name_format, locale.getDisplayName(), locale.getLanguage(), locale.getCountry(), locale.getVariant());
    }

    /**
     * 检查设置选项是否支持该语言
     *
     * @param locale
     * @return
     */
    private boolean isSettingSupportLocale(Locale locale) {
        if(sh==null){
            //服务尚未创建
            return true;
        }
        //支持语言选项
        int availableLanguagesOption=sh.getInt(KEY_availableLanguagesOption,0);
        if (availableLanguagesOption == availableLanguagesOption_LIST) {
            //自定义列表
            Set<String>  availableLanguages = sh.getStringSet(KEY_availableLanguages, new HashSet<>());
            return availableLanguages.contains(SomeUtil.getLanguageISO3Tag(locale));
        }
        if (availableLanguagesOption == availableLanguagesOption_NONE) {
            return false;
        }
        return true;
    }
}
