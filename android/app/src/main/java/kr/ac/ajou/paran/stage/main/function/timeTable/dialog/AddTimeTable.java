package kr.ac.ajou.paran.stage.main.function.timeTable.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.function.timeTable.TimeTable;
import kr.ac.ajou.paran.util.DialogType;
import kr.ac.ajou.paran.util.dialog.AddSubject;

/**
 * Created by dream on 2017-12-01.
 */

public class AddTimeTable extends DialogType {
    private Context context;
    private EditText editText;
    private String recentSubject;
    public AddTimeTable(final Context context, String recentSubject) {
        super(context, R.layout.dialog_add_timetable);
        this.context = context;
        this.recentSubject = recentSubject;

        setEditText(context);
        setButton();
    }

    private void setButton() {
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        Button buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TimeTable)context).setRecentSubject(editText.getText().toString());
                dismiss();
            }
        });
    }

    private void setEditText(final Context context) {
        editText = (EditText)findViewById(R.id.editSubject);
        editText.setText(recentSubject);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddSubject(context,editText).showDialog();
            }
        });
    }
}
