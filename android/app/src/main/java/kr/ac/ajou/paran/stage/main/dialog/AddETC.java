package kr.ac.ajou.paran.stage.main.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.Main;
import kr.ac.ajou.paran.util.DB;
import kr.ac.ajou.paran.util.DialogType;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.Raw;
import kr.ac.ajou.paran.util.adapter.MajorAdapter;
import kr.ac.ajou.paran.util.object.User;

/**
 * Created by dream on 2017-12-05.
 */

public class AddETC extends DialogType {

    private int abeek;
    private ArrayList<String> majors;
    private MajorAdapter majorAdapter;
    private int studentNumber;

    public AddETC(final Context context, int number) {
        super(context, R.layout.dialog_etcinput);

        studentNumber = number;
        setButton(context);
        setRadioButton();
        setListView(context);
    }

    private void setListView(Context context) {
        ListView listMajor = findViewById(R.id.listMajor);
        majors = HTTP.printBefore(abeek,studentNumber);
        majorAdapter = new MajorAdapter(context, R.layout.list_lecture, majors);
        listMajor.setAdapter(majorAdapter);
    }

    private void setRadioButton() {
        final RadioButton radioNormal, radioAbeek;
        radioNormal = findViewById(R.id.radioNormal);
        radioAbeek = findViewById(R.id.radioAbeek);
        radioNormal.setChecked(true);
        radioNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(abeek == 1){
                    radioNormal.setChecked(true);
                    radioAbeek.setChecked(false);
                    abeek = 0;

                    majors = HTTP.printBefore(abeek,studentNumber/100000);
                }
            }
        });
        radioAbeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(abeek == 0){
                    radioAbeek.setChecked(true);
                    radioNormal.setChecked(false);
                    abeek = 1;

                    majors = HTTP.printBefore(abeek,studentNumber/100000);
                }
            }
        });
    }

    private void setButton(final Context context) {
        Button buttonOK, buttonCancel;
        buttonOK = (Button)findViewById(R.id.buttonOK);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DB db = new DB(context, "mydb2.db", null, 1);
            boolean check = db.checkETC(studentNumber);
            db.close();
            if(check)
                dismiss();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(majorAdapter.getCursor()>-1) {
                    DB db = new DB(context, "mydb2.db", null, 1);
                    if (db.checkETC(studentNumber))
                        db.updateETC(studentNumber, abeek, majors.get(majorAdapter.getCursor()).split("/")[0]);
                    else
                        db.insertETC(studentNumber, abeek, majors.get(majorAdapter.getCursor()).split("/")[0]);
                    db.close();
                    ((Main)context).checkETCFromDB();
                    ((Main)context).sendSubjectToServer();
                    dismiss();
                }
            }
        });
    }
}
