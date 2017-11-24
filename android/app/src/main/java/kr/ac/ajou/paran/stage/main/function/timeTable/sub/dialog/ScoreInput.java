package kr.ac.ajou.paran.stage.main.function.timeTable.sub.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DialogType;

/**
 * Created by dream on 2017-11-24.
 */

public class ScoreInput extends DialogType {

    public ScoreInput(final Context context, final TextView textScore) {
        super(context, R.layout.dialog_scoreinput);

        final EditText editScore = (EditText)findViewById(R.id.editScore);
        editScore.setText(textScore.getText().toString().replace("학점",""));
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
                int score = Integer.parseInt(editScore.getText().toString());
                if (score < 0 || score > 24)
                    Toast.makeText(context, "입력을 확인 해주세요", Toast.LENGTH_SHORT).show();
                else {
                    textScore.setText(score + "학점");
                    dismiss();
                }
            }
        });
    }
}

