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
 * Created by dream on 2017-12-06.
 */

public class ReAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> subjects;
    private LayoutInflater layoutInflater;
    private ArrayList<String> selects;

    public ReAdapter(Context context, int layout, ArrayList<String> subjects) {
        this.context = context;
        this.layout = layout;
        this.subjects = subjects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selects=new ArrayList<>();
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

        holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.textView.setTextColor(context.getResources().getColor(R.color.login_input));

        if(selects.size()>0){
            if(selects.contains(getItem(position).toString())){
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.login_button));
                holder.textView.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        holder.textView.setText(getItem(position).toString());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selects.contains(getItem(position).toString()))
                    selects.remove(getItem(position).toString());
                else
                    selects.add(getItem(position).toString());
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public boolean checkSelect(){
        if(selects.size()>0)
            return true;
        else
            return false;
    }

    public void addSelect(String select) {
        selects.add(select);
    }

    public ArrayList<String> getSelect() {
        return selects;
    }

    public class ViewHolder {
        TextView textView;
        ConstraintLayout layout;
    }
}
