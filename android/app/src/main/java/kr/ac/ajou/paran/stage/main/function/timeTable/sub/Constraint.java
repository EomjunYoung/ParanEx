package kr.ac.ajou.paran.stage.main.function.timeTable.sub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog.ReInput;
import kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog.ScoreInput;
import kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog.SubjectInput;
import kr.ac.ajou.paran.util.adapter.ConstraintAdapter;

/**
 * Created by dream on 2017-11-23.
 */

public class Constraint extends AppCompatActivity {

    private Context context;
    private String parser;

    private ConstraintAdapter adapter;

    private int gridViewHeight;
    private final int NUMBER_OF_ITEMS = 6*2*12;

    private ArrayList<String> subjects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint);

        initSetting();
        makeSubjects();
        setGridView();
        setDialog();
    }

    public void initSetting(){
        context = this;
        TextView textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText("제약조건");

        parser = super.getIntent().getStringExtra("parser");
    }

    public void makeSubjects(){
        subjects = new ArrayList<String>();
        for(int i=0;i<NUMBER_OF_ITEMS;i++){
            if(i%12 == 0)
                subjects.add(String.format("%02d:00 ", (i / 12 + 9)));
            else
                subjects.add("");
        }

        //parser = "시프:1s10.5f11.0/객프:3s9.0f10.5/";
        if(parser.equals("") == false){
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
                adapter = new ConstraintAdapter(context,R.layout.grid_subject,subjects,gridViewHeight);
                gridView.setAdapter(adapter);
                gridView.getLayoutParams().height = (gridViewHeight/24)*24;

                TextView textWeek[] = new TextView[5];
                textWeek[0] = (TextView)findViewById(R.id.textMon);
                textWeek[1] = (TextView)findViewById(R.id.textTue);
                textWeek[2] = (TextView)findViewById(R.id.textWed);
                textWeek[3] = (TextView)findViewById(R.id.textThu);
                textWeek[4] = (TextView)findViewById(R.id.textFri);

                for(int i=0;i<5;i++) {
                    final int finalI = i;
                    textWeek[i].setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            adapter.setColumn(finalI);
                            adapter.notifyDataSetInvalidated();
                            return false;
                        }
                    });
                }

            }
        });
    }

    private void setDialog() {
        final TextView textScore = (TextView) findViewById(R.id.textScore);
        textScore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new ScoreInput(context, textScore).showDialog();
                return false;
            }
        });

        Button buttonRe = (Button) findViewById(R.id.buttonRe);
        buttonRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReInput(context).showDialog();
            }
        });

        Button buttonInclude = (Button) findViewById(R.id.buttonInclude);
        buttonInclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubjectInput(context).showDialog();
            }
        });

        Button buttonExclude = (Button) findViewById(R.id.buttonExclude);
        buttonExclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubjectInput(context).showDialog();
            }
        });
    }
}
