package cn.zhg.tts.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.*;

import java.util.*;

import cn.zhg.tts.testapp.adapter.*;


public class MainActivity extends Activity implements TextToSpeech.OnInitListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private TextToSpeech tts;
    private Spinner enginesSelect;
    private Spinner languagesSelect;
    private Spinner voicesSelect;
    private TextView speakText;
    private View speakBtn;
    private View stopBtn;
    private TextView resultText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        enginesSelect = findViewById(R.id.enginesSelect);
        languagesSelect = findViewById(R.id.languagesSelect);
        voicesSelect = findViewById(R.id.voicesSelect);
        speakText = findViewById(R.id.speakText);
        speakBtn = findViewById(R.id.speakBtn);
        stopBtn = findViewById(R.id.stopBtn);
        resultText = findViewById(R.id.resultText);
        resultText.setText("");
        setContainerEnabled(false);
        //初始化列表
        initEngines();
        //监听器
        enginesSelect.setOnItemSelectedListener(this);
        speakBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
    }

    protected void onDestroy() {
        if (tts != null) {
            tts.shutdown();
            tts=null;
        }
        super.onDestroy();
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent==enginesSelect){
            TextToSpeech.EngineInfo engineInfo= (TextToSpeech.EngineInfo) parent.getItemAtPosition(position);
            if(tts!=null){
                tts.shutdown();
            }
            resultText.append("\n选择语言引擎:" + engineInfo.name);
            tts=new TextToSpeech(MainActivity.this,MainActivity.this,engineInfo.name);
            return;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void onClick(View v) {
        if(v==speakBtn){
            if(tts==null){
                return;
            }
            String text = speakText.getText().toString();
            if (text.isEmpty()) {
                return;
            }
            speek(text);
            return;
        }
        if(v==stopBtn){
            if(tts==null){
                return;
            }
            tts.stop();
            return;
        }
    }
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            resultText.append("\n初始化引擎失败,status=" + status);
            return;
        }
        setContainerEnabled(true);
        resultText.append("\n初始化引擎成功");
        Set<Locale> languages = tts.getAvailableLanguages();
        resultText.append("\n语言数量 :" + languages.size());
        languagesSelect.setAdapter(new LocaleAdapter(this, new ArrayList<>(languages)));

        Set<Voice> voices = tts.getVoices();
        resultText.append("\n语音数量 :" + voices.size());
        voicesSelect.setAdapter(new VoiceAdapter(this, new ArrayList<>(voices)));

    }

    private void initEngines() {
        final TextToSpeech[] ttses = new TextToSpeech[1];
        ttses[0] = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                TextToSpeech tts = ttses[0];
                if (status != TextToSpeech.SUCCESS) {
                    resultText.append("获取引擎列表失败" );
                    tts.shutdown();
                    return;
                }
                List<TextToSpeech.EngineInfo> engines = tts.getEngines();
                tts.shutdown();
                if (engines == null || engines.isEmpty()) {
                    resultText.append("\n无可用的语音引擎");
                    return;
                }
                resultText.append("\n语音引擎数量 :" + engines.size());
                enginesSelect.setAdapter(new EngineInfoAdapter(MainActivity.this, engines));
            }
        });
    }

    private void speek(String text) {
        tts.setVoice((Voice) voicesSelect.getSelectedItem());
        int ret = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, System.nanoTime() + "");
        if (ret == TextToSpeech.ERROR) {
            resultText.append("\n朗读["+text+"]失败");
            return;
        }
        if (ret == TextToSpeech.SUCCESS) {
            resultText.append("\n朗读["+text+"]成功");
        }
    }

    /**
     * 禁用或启用容器组件
     * @param b
     */
    private void setContainerEnabled(boolean b) {
         enginesSelect.setEnabled(b);
         languagesSelect.setEnabled(b);
         voicesSelect.setEnabled(b);
         speakText.setEnabled(b);
         speakBtn.setEnabled(b);
         stopBtn.setEnabled(b);
    }



}
