package kr.ac.ajou.paran.stage.main.function.timeTable.sub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.Raw;
import kr.ac.ajou.paran.util.adapter.ConstraintAdapter;

/**
 * Created by dream on 2017-11-23.
 */

public class Constraint extends AppCompatActivity {

    private Context context;
    private String parser;
    private int studentNumber;

    private ConstraintAdapter adapter;

    private int gridViewHeight;
    private final int NUMBER_OF_ITEMS = 6*2*12;

    private ArrayList<String> subjects;

    private TextView textRe, textInclude, textExclude, textScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint);

        initSetting();
        makeSubjects();
        setGridView();
        setDialog();
        setRecommend();
    }

    private void setRecommend() {
        Button buttonRecommend = (Button)findViewById(R.id.buttonRecommend);
        buttonRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Integer> rows = adapter.getRows();
                ArrayList<Integer> columns = adapter.getColumns();

                String str;

                int score;
                str = textScore.getText().toString();
                str = str.substring(0,str.indexOf("학"));
                score = Integer.parseInt(str);

                ArrayList<String> res = new ArrayList<>();
                ArrayList<String> includes = new ArrayList<>();
                ArrayList<String> excludes = new ArrayList<>();

                str = textRe.getText().toString();
                if(str.equals("") == false){
                    for(String re : str.split("\n"))
                        res.add(re);
                }

                str = textInclude.getText().toString();
                if(str.equals("") == false){
                    for(String include : str.split("\n"))
                        includes.add(include);
                }

                str = textExclude.getText().toString();
                if(str.equals("") == false){
                    for(String exclude : str.split("\n"))
                        excludes.add(exclude);
                }

                //studentNumber = 201222702;
                if (studentNumber != 0) {
                    String ip = Raw.readIP(Constraint.this);
                    String port = Raw.readPort(Constraint.this);
                    HTTP.postConstraint(ip+":"+port,studentNumber,columns,rows,score,res,includes,excludes);
                    startActivity(new Intent(context, Recommend.class).putExtra("number",studentNumber));
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    public void initSetting(){
        context = this;
        TextView textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText("제약조건");

        parser = super.getIntent().getStringExtra("parser");
        studentNumber = super.getIntent().getIntExtra("number",0);

        setNetwork();
    }

    private void setNetwork() {
           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
           StrictMode.setThreadPolicy(policy);
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
        textScore = (TextView) findViewById(R.id.textScore);
        textScore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new ScoreInput(context, textScore).showDialog();
                return false;
            }
        });

        textRe = (TextView)findViewById(R.id.textRe);
        Button buttonRe = (Button) findViewById(R.id.buttonRe);
        buttonRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReInput(context,textRe).showDialog();
            }
        });

        textInclude = (TextView)findViewById(R.id.textInclude);
        Button buttonInclude = (Button) findViewById(R.id.buttonInclude);
        buttonInclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubjectInput(context,textInclude).showDialog();
            }
        });

        textExclude = (TextView)findViewById(R.id.textExclude);
        Button buttonExclude = (Button) findViewById(R.id.buttonExclude);
        buttonExclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubjectInput(context,textExclude).showDialog();
            }
        });
    }
}
