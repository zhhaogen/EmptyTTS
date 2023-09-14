package cn.zhg.tts.testapp.adapter;

import android.content.Context;
import android.speech.tts.Voice;
import android.view.*;
import android.widget.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import cn.zhg.tts.testapp.R;

public class VoiceAdapter extends BaseAdapter {
    private final Context context;
    private final List<Voice> datas;
    private final LayoutInflater inflater;

    public VoiceAdapter(Context context, List<Voice> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return datas.size();
    }

    public Voice getItem(int position) {
        return datas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        VoiceViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_voice, parent, false);
            holder = new VoiceViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (VoiceViewHolder) convertView.getTag();
        }
        holder.update(datas.get(position));
        return convertView;
    }
    private   String getFieldName(int v,String prefix){
        Field[] fields = Voice.class.getFields();
        for(Field field:fields){
            int mod=field.getModifiers();
            if(!Modifier.isStatic(mod)||!Modifier.isPublic(mod)){
                continue;
            }
            String name=field.getName();
            if(!name.startsWith(prefix)){
                continue;
            }
            try {
                int value= (int) field.get(null);
                if(value==v){
                    return name.substring(prefix.length());
                }
            } catch (Exception e) {

            }
        }
        return v+"";
    }
    private class VoiceViewHolder {
        TextView nameText;
        TextView featuresText;
        TextView desText;

        public VoiceViewHolder(View itemView) {
            nameText = itemView.findViewById(R.id.nameText);
            featuresText = itemView.findViewById(R.id.featuresText);
            desText = itemView.findViewById(R.id.desText);
        }

        public void update(Voice item) {
            nameText.setText(item.getLocale().getDisplayName() + "-" + item.getName());
            Set<String> features = item.getFeatures();
            if (features == null || features.isEmpty()) {
                featuresText.setText("无");
            } else {
                featuresText.setText(String.join(",", features));
            }
            desText.setText("离线 :" + !item.isNetworkConnectionRequired() + ",延迟 :" + getFieldName(item.getLatency(),"LATENCY_") + ",质量 :" + getFieldName(item.getQuality(),"QUALITY_"));
        }
    }
}
