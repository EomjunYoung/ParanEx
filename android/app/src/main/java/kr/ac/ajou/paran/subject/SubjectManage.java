package kr.ac.ajou.paran.subject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import kr.ac.ajou.paran.DBHelper.DBHelper;
import kr.ac.ajou.paran.DBHelper.SubjectAdapter;
import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.main.dialog.InitSubject;
import kr.ac.ajou.paran.main.function.Subject;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.User;

import static android.R.id.custom;
import static android.R.id.list;

/**
 * Created by ejy77 on 2017-11-15.
 */

public class SubjectManage extends AppCompatActivity {



    private static String cookie = null;
    private static String subjectList = null;
    private static final List<String> list = new ArrayList<>();
    private ArrayList<ArrayList<String>> list2 = new ArrayList<ArrayList<String>>();




    DBHelper dbHelper;
    SQLiteDatabase db;
    Button btnadd, btndel;
    ListView lv;
    int count = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        btnadd = (Button) findViewById(R.id.btnadd);
        btndel = (Button) findViewById(R.id.btndel);
        lv = (ListView) findViewById(R.id.lv);
        dbHelper = new DBHelper(getApplicationContext(), "subject.db", null, 1);


        if(subjectList == null) {

            cookie = getIntent().getStringExtra("cookie");
            subjectList = HTTP.printSubject(cookie);
            StringTokenizer st = new StringTokenizer(subjectList, "\t");


            while(st.hasMoreTokens())
            {
                 String retake = st.nextToken();
                 String mandate = st.nextToken();
                 String name = st.nextToken();

                 //list.add(st.nextToken())
                dbHelper.insertsubject(count, retake, mandate, name);
                count++;
            }


        }

        String sql = "select * from subjectinfo";
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

       // String str = getIntent().getStringExtra("eom");
       // Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
       // cookie = getIntent().getStringExtra("cookie");
        // List<String> al = new ArrayList<String>();
        //subjectList = HTTP.printSubject(cookie);
        //  al = Arrays.asList(subjectList);





      //  ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
       // lv.setAdapter(arrayAdapter3);


        SubjectAdapter subjectAdapter = new SubjectAdapter(this, cursor, 0);
        lv.setAdapter(subjectAdapter);






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
