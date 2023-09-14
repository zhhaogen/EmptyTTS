package cn.zhg.tts.testapp.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.*;
import android.widget.*;

import java.util.*;

import cn.zhg.tts.testapp.R;

public class EngineInfoAdapter extends BaseAdapter {
    private final Context context;
    private final List<TextToSpeech.EngineInfo> datas;
    private final LayoutInflater inflater;

    public EngineInfoAdapter(Context context, List<TextToSpeech.EngineInfo> datas){
        this.context=context;
        this.datas=datas;
         inflater = LayoutInflater.from(context);
    }
    public int getCount() {
        return datas.size();
    }

    public TextToSpeech.EngineInfo getItem(int position) {
        return datas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        EngineInfoViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_engin_info,parent,false);
            holder=new EngineInfoViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (EngineInfoViewHolder) convertView.getTag();
        }
        holder.update(datas.get(position));
        return convertView;
    }
    private class EngineInfoViewHolder{
        ImageView iconImage;
        TextView labelText;
        TextView  nameText;
        public EngineInfoViewHolder(View itemView) {
            iconImage=itemView.findViewById(R.id.iconImage);
            labelText=itemView.findViewById(R.id.labelText);
            nameText=itemView.findViewById(R.id.nameText);
        }
        public void update(TextToSpeech.EngineInfo item) {
            iconImage.setImageResource(item.icon);
            labelText.setText(item.label);
            nameText.setText(item.name);
        }
    }
}
