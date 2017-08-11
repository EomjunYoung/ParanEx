package kr.ac.ajou.paran.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import kr.ac.ajou.paran.R;

/**
 * Created by dream on 2017-08-12.
 */

public class FunctionType extends AppCompatActivity {

    private int layout;

    private Button buttonBack;

    public FunctionType(int layout){
        this.layout = layout;
    }
    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(layout);

        buttonBack = (Button)findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
