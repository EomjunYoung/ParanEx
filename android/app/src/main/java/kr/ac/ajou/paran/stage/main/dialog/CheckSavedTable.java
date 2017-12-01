package kr.ac.ajou.paran.stage.main.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import kr.ac.ajou.paran.stage.main.function.timeTable.TimeTable;
import kr.ac.ajou.paran.util.Recognizer;
import kr.ac.ajou.paran.util.dialog.TwoWays;

/**
 * Created by dream on 2017-11-16.
 */

public class CheckSavedTable extends TwoWays {
    private Button buttonCancel, buttonOK;
    private int number;

    public CheckSavedTable(final Context context, boolean timeTable_exist) {
        super(context);
        buttonOK = super.getButtonOK();
        buttonCancel = super.getButtonNO();

        if(timeTable_exist) {
            super.setContent("기존에 저장된 시간표가 있습니다.\n사용하시겠습니까?");
            buttonOK.setText("사용 하기");
            buttonCancel.setText("새로 인식");
        }else {
            super.setContent("카메라를 통해 시간표를 인식하시겠습니까?");
            buttonOK.setText("아니오");
            buttonCancel.setText("예");
        }

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonCancel.getText().toString().equals("새로 인식")) {
                    CheckSavedTable.super.setContent("카메라를 통해 시간표를 인식하시겠습니까?");
                    buttonCancel.setText("예");
                    buttonOK.setText("아니오");
                }else{
                    context.startActivity(new Intent(context, Recognizer.class).putExtra("number", number));
                    dismiss();
                }
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonOK.getText().toString().equals("사용 하기")) {
                    context.startActivity(new Intent(context, TimeTable.class).putExtra("number", number));
                    dismiss();
                }else {
                    context.startActivity(new Intent(context, TimeTable.class).putExtra("number", number).putExtra("new",true));
                    dismiss();
                }
            }
        });
    }

    public void setNumber(int number){
        this.number = number;
    }
}
