package kr.ac.ajou.paran.stage.main.function.subject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import kr.ac.ajou.paran.util.DB;
import kr.ac.ajou.paran.util.adapter.SubjectAdapter;
import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.HTTP;

/**
 * Created by ejy77 on 2017-11-15.
 */

public class SubjectManage extends AppCompatActivity {

    private static String cookie = null;
    private static String subjectList = null;
    private static final List<String> list = new ArrayList<>();
    private ArrayList<ArrayList<String>> list2 = new ArrayList<ArrayList<String>>();

    DB db;
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
        db = new DB(getApplicationContext(), "mydb2.db", null, 1);


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
                db.insertSubject(count, retake, mandate, name);
                count++;
            }


        }

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        String sql = "select * from subjectinfo2";
        final Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
//        Cursor cursor = db.getReadableDatabase().rawQuery(sql, null);
       // Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);

       // String str = getIntent().getStringExtra("eom");
       // Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
       // cookie = getIntent().getStringExtra("cookie");
        // List<String> al = new ArrayList<String>();
        //subjectList = HTTP.printSubject(cookie);
        //  al = Arrays.asList(subjectList);


      //  ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
       // lv.setAdapter(arrayAdapter3);


        final SubjectAdapter subjectAdapter = new SubjectAdapter(this, cursor, 0);
        lv.setAdapter(subjectAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder ad = new AlertDialog.Builder(SubjectManage.this);
                ad.setTitle("Title");
                ad.setMessage("정말 삭제를 원하십니까?");

                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i2) {


                try {
                    db.deleteSubject(i + 1);
                    subjectAdapter.notifyDataSetChanged();
                    SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
                    String sql = "select * from subjectinfo2";
                    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
                    SubjectAdapter subjectAdapter = new SubjectAdapter(getApplicationContext(), cursor, 0);
                    subjectAdapter.notifyDataSetChanged();
                    lv.setAdapter(subjectAdapter);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });

                ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.dismiss();

                    }
                });

                ad.create();
                ad.show();

            }
        });


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

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
