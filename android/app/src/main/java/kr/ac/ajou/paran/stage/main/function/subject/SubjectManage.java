package kr.ac.ajou.paran.stage.main.function.subject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.ajou.paran.util.DB;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.Raw;
import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.adapter.SubjectAdapter;

/**
 * Created by ejy77 on 2017-11-15.
 */

public class SubjectManage extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    private int studentNumber;

    private ListView lv;
    private int index_item;
    private SubjectAdapter subjectAdapter;

    private ArrayList<String> subjects;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        studentNumber = super.getIntent().getIntExtra("number", 0);
        setNetwork();
        setList();
        setButton();

    }

    private void setNetwork() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setList() {
        lv = (ListView) findViewById(R.id.lv);

        DB db = new DB(getApplicationContext(), "mydb2.db", null, 1);
        subjects = db.getSubject();
        db.close();

        subjectAdapter = new SubjectAdapter(getApplicationContext(),R.layout.list_subject,subjects);
        lv.setAdapter(subjectAdapter);
        lv.setOnItemClickListener(this);
    }

    private void updateSubjectToServer(String subjectList) {
        String ip, port;
        ip = Raw.readIP(SubjectManage.this);
        port = Raw.readPort(SubjectManage.this);
        HTTP.postSubject(ip + ":" + port, subjectList, studentNumber);
    }

    private void setButton() {
        Button btnadd, btndel;
        btnadd = (Button) findViewById(R.id.btnadd);
        btndel = (Button) findViewById(R.id.btndel);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SubjectManage.this, Subject.class).putExtra("number", studentNumber);
                startActivity(intent);
                finish();
            }
        });

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ad = new AlertDialog.Builder(SubjectManage.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view2 = inflater.inflate(R.layout.dialog_delete, null);
                ad.setView(view2);
                final Button buttonRegister = (Button) view2.findViewById(R.id.buttonRegister);
                final AlertDialog dialog = ad.create();

                buttonRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder ad = new AlertDialog.Builder(SubjectManage.this);
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_deletecheck, null);
        ad.setView(view2);
        Button buttonRegister = view2.findViewById(R.id.buttonRegister);
        Button buttonCancel = view2.findViewById(R.id.buttonCancel);
        dialog = ad.create();

        index_item = i;
        buttonRegister.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonRegister:

                DB db = new DB(getApplicationContext(), "mydb2.db", null, 1);
                db.deleteSubject(subjects.get(index_item));
                String subjectList = db.getSubjectList();
                updateSubjectToServer(subjectList);
                db.close();
                subjects.remove(index_item);
                subjectAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "정상적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.buttonCancel:
                dialog.dismiss();
                break;
        }
    }
}
