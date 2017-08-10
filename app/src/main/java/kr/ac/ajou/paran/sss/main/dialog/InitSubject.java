package kr.ac.ajou.paran.sss.main.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DialogType;
import kr.ac.ajou.paran.util.HTTP;

/**
 * Created by dream on 2017-08-06.
 */

public class InitSubject extends DialogType {

    private TextView textSubject;
    private Button buttonCancel;

    public InitSubject(Context context, String subjectList) {
        super(context, R.layout.dialog_init_subject);

        textSubject = (TextView)findViewById(R.id.textSubject);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);

        textSubject.setText(subjectList);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
