package kr.ac.ajou.paran.sss.main.function;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.FunctionType;


/**
 * Created by user on 2017-08-11.
 */

public class Lecture extends FunctionType implements Callback {


    private Spinner sptest;
    private Button testbtn;
    private Callback mCallback;
    private String semester = "U0002003";
    private String type = "U0209001";
    private String major = "DS030020201";



    public Lecture(){
        super("강의시간표",R.layout.activity_lecture);


    }

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);



        mCallback = this;
        init();



    }

    public void init()
    {
        sptest = (Spinner)findViewById(R.id.sptest);
        testbtn = (Button)findViewById(R.id.testbtn);

        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkAsync networkAsync = new NetworkAsync(semester, type, major, mCallback);
                networkAsync.execute();
            }
        });

    }





    @Override
    public void getReturn(Object o)
    {


        String string = o.toString();
        StringTokenizer s = new StringTokenizer(string);
        ArrayList arrayList = new ArrayList();

        while(s.hasMoreTokens())
        {

            arrayList.add(s.nextToken());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        sptest.setAdapter(arrayAdapter);


    }

}




