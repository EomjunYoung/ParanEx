package kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DialogType;
import kr.ac.ajou.paran.util.adapter.LectureAdapter;
import kr.ac.ajou.paran.util.dialog.AddSubject;

/**
 * Created by dream on 2017-11-25.
 */

public class SubjectInput extends DialogType{
    private SubjectInput subjectInput;
    private ListView listSubject;
    private ArrayList<String> subjects;
    private LectureAdapter subjectAdapter;

    private TextView textView;
    public SubjectInput(Context context, TextView textView) {
        super(context, R.layout.dialog_subjectinput);
        subjectInput = this;
        this.textView = textView;

        setButton(context);
        setList(context);
    }

    private void setList(Context context) {
        listSubject = (ListView)findViewById(R.id.listSubject);
        subjects = new ArrayList<String>();
        String str = textView.getText().toString().trim();
        if(str.equals("") == false) {
            for (String subject : str.split("\n"))
                subjects.add(subject);
        }
        subjectAdapter = new LectureAdapter(context, R.layout.list_subject, subjects);
        listSubject.setAdapter(subjectAdapter);
    }

    private void setButton(final Context context) {
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str="";
                if(subjects.size()>0){
                    for(String subject : subjects)
                        str+=subject+"\n";
                    textView.setText(str);
                }else
                    textView.setText("");
                dismiss();
            }
        });

        Button buttonAdd = (Button)findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddSubject(context, subjectInput).showDialog();
            }
        });

        Button buttonDelete = (Button)findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursor = subjectAdapter.getCursor();
                if(cursor>-1) {
                    subjects.remove(cursor);
                    subjectAdapter.notifyDataSetChanged();
                    subjectAdapter.setCursor(-1);
                }
            }
        });
    }

    public void addSubject(String subject){
        subjects.add(subject);
        subjectAdapter.notifyDataSetChanged();
    }
}
