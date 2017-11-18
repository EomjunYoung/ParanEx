package kr.ac.ajou.paran.util.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;

/**
 * Created by dream on 2017-11-18.
 */

public class TableAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private  int height;
    private  ArrayList<String> subjects;
    private  LayoutInflater layoutInflater;

    public TableAdapter(Context context, int layout, ArrayList<String> subjects, int height) {
        this.context = context;
        this.layout = layout;
        this.subjects = subjects;
        this.height = height;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setHeight((int)(height/24));
        holder.textView.setText(getItem(position).toString());
        if(position % 6==0){
            holder.textView.setBackgroundColor(context.getResources().getColor(R.color.login_input));
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
            holder.textView.setGravity(Gravity.TOP|Gravity.RIGHT);
        }else{
            holder.textView.setBackgroundResource(R.drawable.grid_item);
            holder.textView.setTextColor(context.getResources().getColor(R.color.login_input));
        }

        return convertView;
    }
    public class ViewHolder {
        TextView textView;
    }
}
