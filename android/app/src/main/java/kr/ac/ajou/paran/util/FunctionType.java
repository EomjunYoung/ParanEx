package kr.ac.ajou.paran.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.ac.ajou.paran.R;

/**
 * Created by dream on 2017-08-12.
 */

public class FunctionType extends AppCompatActivity {

    private int layout;
    private String title;

    private TextView textTitle;

    public FunctionType(String title, int layout){
        this.title = title;
        this.layout = layout;
    }
    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(layout);

        textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText(title);
    }
}
