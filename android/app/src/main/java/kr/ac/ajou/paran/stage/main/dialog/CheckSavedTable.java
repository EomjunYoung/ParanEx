package kr.ac.ajou.paran.stage.main.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import kr.ac.ajou.paran.stage.main.function.TimeTable;
import kr.ac.ajou.paran.util.Recognizer;

/**
 * Created by dream on 2017-11-16.
 */

public class CheckSavedTable extends TwoWays {
    private Button buttonCancel, buttonOK;
    private int number;

    public CheckSavedTable(final Context context) {
        super(context);
        super.setContent("기존에 저장된 시간표가 있습니다.\n사용하시겠습니까?");

        buttonOK = super.getButtonOK();
        buttonCancel = super.getButtonNO();

        buttonOK.setText("사용 하기");
        buttonCancel.setText("새로 인식");
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Recognizer.class).putExtra("number",number));
                dismiss();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TimeTable.class).putExtra("number",number));
                dismiss();
            }
        });
    }

    public void setNumber(int number){
        this.number = number;
    }
}
