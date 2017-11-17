package kr.ac.ajou.paran.main.function;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.FunctionType;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.Raw;


/**
 * Created by user on 2017-08-11.
 */

public class TimeTable extends AppCompatActivity {

    private Button buttonBack;
    private TextView textTitle;

    private String parser;
    private int number;
    private String ip, port;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_timetable);

        textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText("인식편집");
        buttonBack = (Button)findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        number = super.getIntent().getIntExtra("number", 0);
        if (number != 0) {
            ip = Raw.readIP(TimeTable.this);
            port = Raw.readPort(TimeTable.this);
            parser = HTTP.getTable(ip + ":" + port,number);
        }

        if(parser != null){
            for (String subject : parser.split("/")){
            }
        }
    }
}