package kr.ac.ajou.paran.sss.main.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DialogType;

/**
 * Created by dream on 2017-08-06.
 */

public class AlertTrans extends DialogType {

    private TextView textTitle, textContent;
    private Button buttonCancel;

    public AlertTrans(Context context) {
        super(context, R.layout.dialog_alert);

        textTitle = (TextView)findViewById(R.id.textTitle);
        textContent = (TextView)findViewById(R.id.textContent);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);

        textTitle.setText("과목 관련 안내");
        textContent.setText("편입생의 경우\n\n전적대에서 이수 받은 과목을 따로 등록해야합니다\n\n메인>과목관리");

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
