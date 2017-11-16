package kr.ac.ajou.paran.subject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.main.dialog.InitSubject;
import kr.ac.ajou.paran.main.function.Subject;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.User;

/**
 * Created by ejy77 on 2017-11-15.
 */

public class SubjectManage extends AppCompatActivity {



    private String cookie;
    private String subjectList;


    Button btnadd, btndel;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        btnadd = (Button) findViewById(R.id.btnadd);
        btndel = (Button) findViewById(R.id.btndel);
        lv = (ListView) findViewById(R.id.lv);
        cookie = getIntent().getStringExtra("cookie");
        // List<String> al = new ArrayList<String>();
        subjectList = HTTP.printSubject(cookie);
        //  al = Arrays.asList(subjectList);

        List<String> list = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(subjectList, "\n");

        while(st.hasMoreTokens())
        {
            list.add(st.nextToken());
        }


        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);

        lv.setAdapter(arrayAdapter3);





        // String str = getIntent().getStringExtra("eom");
        // subjectList += str;
        //  new InitSubject(this, subjectList).showDialog();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SubjectManage.this, Subject.class);
                startActivity(intent);
            }
        });

    }

}
