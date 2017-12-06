package kr.ac.ajou.paran.stage.main.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import kr.ac.ajou.paran.util.dialog.TwoWays;
import kr.ac.ajou.paran.util.object.User;

/**
 * Created by dream on 2017-12-05.
 */

public class CheckETC extends TwoWays {
    private Button buttonCancel, buttonOK;

    public CheckETC(final Context context, final User user, String etc) {
        super(context);
        buttonOK = super.getButtonOK();
        buttonCancel = super.getButtonNO();

        super.setTitle("안내");
        super.setContent("등록된 입학전공은\n\""+etc.split("/")[1]+"\"이며\n공학인증을 "+(etc.split("/")[0].equals("0")?"하지 않고":"하고")+" 있습니다\n");
        buttonOK.setText("수정");
        buttonCancel.setText("확인");

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dismiss();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddETC(context,user.getNumber()).showDialog();
                dismiss();
            }
        });
    }
}
