package kr.ac.ajou.paran.main.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DialogType;

/**
 * Created by dream on 2017-11-16.
 */

public class TwoWays extends DialogType {

    private TextView textTitle, textContent;
    private Button buttonCancel, buttonOK;

    public TwoWays(final Context context) {
        super(context, R.layout.dialog_twoways);

        textTitle = (TextView)findViewById(R.id.textTitle);
        textContent = (TextView)findViewById(R.id.textContent);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);
        buttonOK = (Button)findViewById(R.id.buttonOK);

        textTitle.setText("선택 상자");
    }

    public void setContent(String content){
        textContent.setText(content);
    }

    public Button getButtonOK(){
        return buttonOK;
    }

    public Button getButtonNO(){
        return buttonCancel;
    }
}
