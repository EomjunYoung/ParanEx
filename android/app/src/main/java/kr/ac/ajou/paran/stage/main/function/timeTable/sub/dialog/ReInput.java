package kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DB;
import kr.ac.ajou.paran.util.DialogType;
import kr.ac.ajou.paran.util.adapter.LectureAdapter;
import kr.ac.ajou.paran.util.adapter.ReAdapter;

/**
 * Created by dream on 2017-11-24.
 */

public class ReInput extends DialogType {

    private ArrayList<String> res;
    private ReAdapter reAdapter;
    private TextView textView;
    public ReInput(Context context, TextView textView) {
        super(context, R.layout.dialog_reinput);

        this.textView = textView;
        setButton();
        setList(context);

    }

    private void setList(Context context) {
        ListView listRe = findViewById(R.id.listRe);
        DB db = new DB(context, "mydb2.db", null, 1);
        res = db.getRe();
        db.close();
        reAdapter = new ReAdapter(context,R.layout.list_lecture,res);
        String str = textView.getText().toString().trim();
        if(str.equals("") == false) {
            for (String subject : str.split("\n"))
                reAdapter.addSelect(subject);
        }
        listRe.setAdapter(reAdapter);
    }

    private void setButton() {
        Button buttonCancel = findViewById(R.id.buttonCancel);
        Button buttonOK = findViewById(R.id.buttonOK);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reAdapter.checkSelect()){
                    String str="";
                    for(String re : reAdapter.getSelect())
                        str+=re+"\n";
                    textView.setText(str);

                }else
                    textView.setText("");
                dismiss();
            }
        });
    }
}

