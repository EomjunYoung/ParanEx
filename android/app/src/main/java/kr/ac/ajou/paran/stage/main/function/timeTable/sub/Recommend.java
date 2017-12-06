package kr.ac.ajou.paran.stage.main.function.timeTable.sub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.adapter.TableAdapter;

/**
 * Created by dream on 2017-12-06.
 */

public class Recommend extends AppCompatActivity {
    private Context context;

    private int studentNumber;
    private ArrayList<String> subjects;
    private TableAdapter tableAdapter;

    private int gridViewHeight;

    private String parser;
    private final int NUMBER_OF_ITEMS = 6*2*12;

    private String ip;
    private String port;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        initSetting();
        makeSubjects();
        setGridView();
    }

    private void initSetting() {
        context = this;
        this.studentNumber = super.getIntent().getIntExtra("number",0);
    }

    public void makeSubjects(){
        subjects = new ArrayList<String>();
        for(int i=0;i<NUMBER_OF_ITEMS;i++){
            if(i%12 == 0)
                subjects.add(String.format("%02d:00 ", (i / 12 + 9)));
            else
                subjects.add("");
        }

        //parser = "시프:1s10.5f11.0/객프:3s9.0f10.5";
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
                for(int i=start;i<finish;subjects.set(6*i+week,name),i++);
            }
        }
    }

    public void setGridView(){
        final GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.post(new Runnable() {
            @Override
            public void run() {
                gridViewHeight = gridView.getHeight();
                tableAdapter = new TableAdapter(context,R.layout.grid_subject,subjects,gridViewHeight);
                gridView.setAdapter(tableAdapter);
                gridView.getLayoutParams().height = (gridViewHeight/24)*24;
            }
        });
    }
}
