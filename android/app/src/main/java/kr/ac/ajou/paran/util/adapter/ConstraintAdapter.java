package kr.ac.ajou.paran.util.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;

/**
 * Created by dream on 2017-11-18.
 */

public class ConstraintAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private  int height;
    private  ArrayList<Integer> rows, columns;
    private  Integer row, column;
    private  ArrayList<String> subjects;
    private  LayoutInflater layoutInflater;

    public ConstraintAdapter(Context context, int layout, ArrayList<String> subjects, int height) {
        this.context = context;
        this.layout = layout;
        this.subjects = subjects;
        this.height = height;
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        row = new Integer(0);
        column = new Integer(0);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setHeight(height/24);
        holder.textView.setTextSize(8);

        if(position % 6==0){
            holder.textView.setText(getItem(position).toString());
            holder.textView.setBackgroundColor(context.getResources().getColor(R.color.login_input));
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
            holder.textView.setGravity(Gravity.TOP|Gravity.RIGHT);
            holder.textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        row = (position/6);
                        if(rows.contains(row))
                            rows.remove(row);
                        else
                            rows.add(row);
                        notifyDataSetChanged();
                        return false;
                    }
                });
        }else{
            if(getItem(position).toString().equals("") == false)
                holder.textView.setText("●");
            holder.textView.setGravity(Gravity.CENTER_HORIZONTAL);
            holder.textView.setBackgroundResource(R.drawable.grid_item);
            holder.textView.setTextColor(context.getResources().getColor(R.color.login_input));

            if(rows.size()>0){
                row = position/6;
                if(rows.contains(row))
                    holder.textView.setBackgroundColor(context.getResources().getColor(R.color.login_input_back));
            }

            if(columns.size()>0){
                column = position%6-1;
                if(columns.contains(column))
                    holder.textView.setBackgroundColor(context.getResources().getColor(R.color.login_input_back));
            }
        }

        return convertView;
    }

    public class ViewHolder {
        TextView textView;
    }

    public void setColumn(int i){
        column = i;
        if(columns.contains(column))
            columns.remove(column);
        else
            columns.add(column);
    }

    public ArrayList<Integer> getRows(){
        return rows;
    }

    public ArrayList<Integer> getColumns(){
        return columns;
    }
}
