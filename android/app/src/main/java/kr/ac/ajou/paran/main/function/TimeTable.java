package kr.ac.ajou.paran.main.function;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.Raw;


/**
 * Created by user on 2017-08-11.
 */

public class TimeTable extends AppCompatActivity {

    private Context context;

    private int gridViewHeight;

    private String parser;
    private int studentNumber;
    private final int NUMBER_OF_ITEMS = 6*2*12;

    private ArrayList<String> subjects;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_timetable);

        initSetting();
        getTimeTable();
        makeSubjects();
        setGridView();
    }

    class GridAdapter extends BaseAdapter {
        Context context;
        int layout;
        ArrayList<String> subjects;
        LayoutInflater layoutInflater;

        public GridAdapter(Context context, int layout, ArrayList<String> subjects) {
            this.context = context;
            this.layout = layout;
            this.subjects = subjects;
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

            holder.textView.setHeight((int)(gridViewHeight/24));
            holder.textView.setText(getItem(position).toString());
            if(position % 6==0){
                holder.textView.setBackgroundColor(getResources().getColor(R.color.login_input));
                holder.textView.setTextColor(getResources().getColor(R.color.white));
                holder.textView.setGravity(Gravity.TOP|Gravity.RIGHT);
            }else{
                holder.textView.setBackgroundResource(R.drawable.grid_item);
                holder.textView.setTextColor(getResources().getColor(R.color.login_input));
            }

            return convertView;
        }
    }
    public class ViewHolder {
        TextView textView;
    }

    public void initSetting(){
        context = this;
        TextView textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText("인식편집");
        Button buttonBack = (Button)findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getTimeTable(){
        studentNumber = super.getIntent().getIntExtra("number", 0);
        if (studentNumber != 0) {
            String ip = Raw.readIP(TimeTable.this);
            String port = Raw.readPort(TimeTable.this);
            parser = HTTP.getTable(ip + ":" + port,studentNumber);
        }
    }

    public void makeSubjects(){
        subjects = new ArrayList<String>();
        for(int i=0;i<NUMBER_OF_ITEMS;i++){
            if(i%12 == 0)
                subjects.add(String.format("%02d:00 ", (i / 12 + 9)));
            else
                subjects.add("");
        }

     //   parser = "시프:1s10.5f11.0/객프:3s9.0f10.5/";
        if(parser != null){
            String name;
            int week;
            int start;
            int finish;
            String resultOfRegular[];

            for (String subject : parser.split("/")){
                resultOfRegular = subject.split(":|s|f");
                name = resultOfRegular[0];
                week = Integer.parseInt(resultOfRegular[1])+1;
                start = (int)((Float.parseFloat(resultOfRegular[2])-9)*2);
                finish = (int)((Float.parseFloat(resultOfRegular[3])-9)*2);
                for(int i=start;i<=finish;subjects.set(6*i+week,name),i++);
            }
        }
    }

    public void setGridView(){
        final GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.post(new Runnable() {
            @Override
            public void run() {
                gridViewHeight = gridView.getHeight();
                gridView.setAdapter(new GridAdapter(context,R.layout.grid_subject,subjects));
                gridView.getLayoutParams().height = (int)(gridViewHeight/24)*24;
            }
        });
    }
}