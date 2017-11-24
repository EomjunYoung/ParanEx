package kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DialogType;

/**
 * Created by dream on 2017-11-24.
 */

public class ReInput extends DialogType {

    public ReInput(Context context) {
        super(context, R.layout.dialog_reinput);

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

            }
        });
    }
}

