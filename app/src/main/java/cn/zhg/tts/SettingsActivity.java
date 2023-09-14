package cn.zhg.tts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.*;

import cn.zhg.tts.inter.Constant;
import cn.zhg.tts.util.SomeUtil;

public class SettingsActivity extends Activity implements Constant, View.OnClickListener {
    /**
     * 支持语言选项按钮
     */
    private View availableLanguagesOptionLayout;
    /**
     * 支持语言选项
     */
    private TextView availableLanguagesOptionText;
    /**
     * 自定义支持语言按钮
     */
    private View availableLanguagesLayout;
    /**
     * 自定义支持语言
     */
    private TextView availableLanguagesText;
    /**
     * 自定义语音名称选项
     */
    private CompoundButton voiceNameOptionCheck;
    /**
     * 自定义语音名称格式
     */
    private TextView voiceNameFormatText;
    private SharedPreferences sh;
    private String[] availableLanguagesOptions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        //初始化视图
        availableLanguagesOptionLayout = findViewById(R.id.availableLanguagesOptionLayout);
        availableLanguagesOptionText = findViewById(R.id.availableLanguagesOptionText);
        availableLanguagesLayout = findViewById(R.id.availableLanguagesLayout);
        availableLanguagesText = findViewById(R.id.availableLanguagesText);
        voiceNameOptionCheck = findViewById(R.id.voiceNameOptionCheck);
        voiceNameFormatText = findViewById(R.id.voiceNameFormatText);
        //读取配置
        sh = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        availableLanguagesOptions = getResources().getStringArray(R.array.available_languages_option);
        //设置视图
        initSettings();
        //监听器
        availableLanguagesOptionLayout.setOnClickListener(this);
        availableLanguagesLayout.setOnClickListener(this);
        voiceNameOptionCheck.setOnClickListener(this);
    }

    public final void onClick(View v) {
        if (v == availableLanguagesOptionLayout) {
            availableLanguagesOptionClick();
            return;
        }
        if (v == availableLanguagesLayout) {
            availableLanguagesLayoutClick();
            return;
        }
        if (v == voiceNameOptionCheck) {
            sh.edit().putBoolean(KEY_voiceNameOption,voiceNameOptionCheck.isChecked()).apply();
            return;
        }
    }

    /**
     *
     */
    private void availableLanguagesLayoutClick() {
        Set<String> availableLanguages = sh.getStringSet(KEY_availableLanguages, new HashSet<>());
        Locale[] locales = SomeUtil.getAvailableLocales();
        //显示名称
        String[] languageNames = new String[locales.length];
        boolean[] checkedItems = new boolean[locales.length];
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            languageNames[i] = locale.getDisplayName();
            checkedItems[i] = availableLanguages.contains(SomeUtil.getLanguageISO3Tag(locale));
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.lab_available_languages_option)
                .setMultiChoiceItems(languageNames, checkedItems, (dia, position, b) -> {
                    checkedItems[position] = b;
//                    Logger.d(languageNames[position]+" :"+b);
                })
                .setPositiveButton(android.R.string.ok, (dia, w) -> {
                   Set<String> list=new HashSet<>();
                    for (int i = 0; i < locales.length; i++) {
                        if (checkedItems[i]) {
                            list.add(SomeUtil.getLanguageISO3Tag(locales[i]));
                        }
                    }
                    sh.edit().putStringSet(KEY_availableLanguages,list).apply();
                    availableLanguagesText.setText(getString(R.string.available_languages_size, list.size()));
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        ;
    }

    /**
     *
     */
    private void availableLanguagesOptionClick() {
        int[] positions = new int[]{sh.getInt(KEY_availableLanguagesOption, 0)};
        new AlertDialog.Builder(this)
                .setTitle(R.string.lab_available_languages_option)
                .setSingleChoiceItems(availableLanguagesOptions, positions[0], (dia, position) -> {
                    positions[0] = position;
                })
                .setPositiveButton(android.R.string.ok, (dia, w) -> {
                    sh.edit().putInt(KEY_availableLanguagesOption, positions[0]).apply();
                    availableLanguagesOptionText.setText(availableLanguagesOptions[positions[0]]);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        ;
    }

    /**
     * 设置视图
     */
    @SuppressLint("StringFormatMatches")
    private void initSettings() {
        availableLanguagesOptionText.setText(availableLanguagesOptions[sh.getInt(KEY_availableLanguagesOption, 0)]);
        Set<String> availableLanguages = sh.getStringSet(KEY_availableLanguages, null);
        if (availableLanguages == null) {
            availableLanguagesText.setText(R.string.lab_available_languages);
        } else {
            availableLanguagesText.setText(getString(R.string.available_languages_size, availableLanguages.size()));
        }
        voiceNameOptionCheck.setChecked(sh.getBoolean(KEY_voiceNameOption, false));
        Locale currentLocale = getResources().getConfiguration().locale;
        if(currentLocale==null){
            currentLocale=Locale.getDefault();
        }
        voiceNameFormatText.setText(getString(R.string.voice_name_format,currentLocale.getDisplayName(),currentLocale.getLanguage(),currentLocale.getCountry(),currentLocale.getVariant()));
    }
}
