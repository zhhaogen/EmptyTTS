package cn.zhg.tts.testapp.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import java.util.*;

import cn.zhg.tts.testapp.R;

public class LocaleAdapter extends BaseAdapter {
    private final Context context;
    private final List<Locale> datas;
    private final LayoutInflater inflater;
    public LocaleAdapter(Context context, List<Locale> datas){
        this.context=context;
        this.datas=datas;
        inflater = LayoutInflater.from(context);
    }
    public int getCount() {
        return datas.size();
    }

    public Locale getItem(int position) {
        return datas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LocaleViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_locale,parent,false);
            holder=new LocaleViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (LocaleViewHolder) convertView.getTag();
        }
        holder.update(datas.get(position));
        return convertView;
    }
    private class LocaleViewHolder{
        TextView languageText;
        TextView  nameText;
        public LocaleViewHolder(View itemView) {
            languageText=itemView.findViewById(R.id.languageText);
            nameText=itemView.findViewById(R.id.nameText);
        }
        public void update(Locale item) {
            nameText.setText(item.getDisplayName());
            languageText.setText(item.getLanguage());
        }
    }
}
