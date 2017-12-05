package kr.ac.ajou.paran.util.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;

/**
 * Created by dream on 2017-12-05.
 */

public class MajorAdapter extends BaseAdapter {
    private  Context context;
    private  int layout;
    private  ArrayList<String> subjects;
    private  LayoutInflater layoutInflater;
    private  int cursor;

    public MajorAdapter(Context context, int layout, ArrayList<String> subjects) {
        this.context = context;
        this.layout = layout;
        this.subjects = subjects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor = -1;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.layout = (ConstraintLayout)convertView.findViewById(R.id.layoutSubject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(cursor == position){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.login_button));
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.textView.setTextColor(context.getResources().getColor(R.color.login_input));
        }

        holder.textView.setText(getItem(position).toString().split("/")[0]);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cursor != position)
                    cursor = position;
                else
                    cursor = -1;
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public int getCursor() {
        return cursor;
    }

    public class ViewHolder {
        TextView textView;
        ConstraintLayout layout;
    }
}
