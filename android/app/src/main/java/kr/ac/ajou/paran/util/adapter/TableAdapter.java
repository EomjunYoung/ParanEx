package kr.ac.ajou.paran.util.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private final int NUMBER_OF_ITEMS = 6*2*12;
    private  ArrayList<String> subjects;
    private  LayoutInflater layoutInflater;
    private int cursor;

    public TableAdapter(Context context, int layout, ArrayList<String> subjects, int height) {
        this.context = context;
        this.layout = layout;
        this.subjects = subjects;
        this.height = height;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            if(position == cursor) {
                holder.textView.setBackgroundResource(R.color.login_button);
                holder.textView.setTextColor(context.getResources().getColor(R.color.white));
            }else{
                holder.textView.setBackgroundResource(R.drawable.grid_item);
                holder.textView.setTextColor(context.getResources().getColor(R.color.login_input));
            }
            holder.textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(cursor == position)
                            cursor = -1;
                        else
                            cursor = position;
                        notifyDataSetChanged();
                        return false;
                    }
            });
        }

        return convertView;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor){
        this.cursor = cursor;
    }

    public class ViewHolder {
        TextView textView;
    }

    public String getParser(){
        String parser="";
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<NUMBER_OF_ITEMS;i++){
            if(i%6==0)
                continue;
            if(subjects.get(i).equals("") == false)
                stringBuffer.append(subjects.get(i)+":"+((i%6)-1)+String.format("s%.1ff%.1f/",((i/6)/2.0+9),((i/6)/2.0+9.5)));
        }
        parser = stringBuffer.toString();

        if(parser.length()>0)
            parser = parser.substring(0,parser.length()-1);
        return parser;
    }
}
