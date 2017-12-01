package kr.ac.ajou.paran.stage.main.function.timeTable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.function.timeTable.dialog.AddTimeTable;
import kr.ac.ajou.paran.stage.main.function.timeTable.dialog.DeleteTimeTable;
import kr.ac.ajou.paran.stage.main.function.timeTable.sub.Constraint;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.Raw;
import kr.ac.ajou.paran.util.adapter.TableAdapter;


/**
 * Created by user on 2017-08-11.
 */

public class TimeTable extends AppCompatActivity{

    private Context context;

    private int gridViewHeight;

    private String parser;
    private int studentNumber;
    private final int NUMBER_OF_ITEMS = 6*2*12;

    private String recentSubject;

    private ArrayList<String> subjects;
    private TableAdapter tableAdapter;
    private String ip;
    private String port;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_timetable);

        initSetting();
        getTimeTable();
        makeSubjects();
        setGridView();
        setDialog();
    }

    public void initSetting(){
        context = this;
        TextView textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText("인식편집");
        Button buttonPass = (Button)findViewById(R.id.buttonOK);
        buttonPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parser = tableAdapter.getParser();
                HTTP.updateTable(ip + ":" + port, parser,studentNumber);
                startActivity(new Intent(context, Constraint.class).putExtra("parser",parser).putExtra("number",studentNumber));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public void getTimeTable(){
        studentNumber = super.getIntent().getIntExtra("number", 0);
        if(super.getIntent().getBooleanExtra("new",false) == false) {
            if (studentNumber != 0) {
                ip = Raw.readIP(TimeTable.this);
                port = Raw.readPort(TimeTable.this);
                parser = HTTP.getTable(ip + ":" + port, studentNumber);
            }
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

    private void setDialog() {
        TextView textAdd = (TextView) findViewById(R.id.textAdd);
        recentSubject = "";
        textAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int cursor = tableAdapter.getCursor();
                if(cursor>0)
                    if(subjects.get(cursor).equals("") == true)
                        new AddTimeTable(context,recentSubject).showDialog();
                    else
                        Toast.makeText(context,"이미 과목이 존재합니다",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context,"과목을 추가할 구간을 선택해주세요",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        TextView textModify = (TextView) findViewById(R.id.textModify);
        textModify.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int cursor = tableAdapter.getCursor();
                if(cursor>0)
                    if(subjects.get(cursor).equals("") == false)
                        new AddTimeTable(context,subjects.get(cursor)).showDialog();
                    else
                        Toast.makeText(context,"과목을 선택해주세요",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context,"과목을 편집할 구간을 선택해주세요",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        TextView textDelete = (TextView) findViewById(R.id.textDelete);
        textDelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int cursor = tableAdapter.getCursor();
                if(cursor>0){
                    if(subjects.get(cursor).equals("") == false)
                        new DeleteTimeTable(context).showDialog();
                    else
                        Toast.makeText(context,"과목을 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context,"과목을 삭제할 구간을 선택해주세요",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public ArrayList<String> getSubjects(){
        return subjects;
    }

    public TableAdapter getTableAdapter(){
        return tableAdapter;
    }

    public void setRecentSubject(String recentSubject) {
        this.recentSubject = recentSubject;
        subjects.set(tableAdapter.getCursor(),recentSubject);
        tableAdapter.notifyDataSetChanged();
    }
}