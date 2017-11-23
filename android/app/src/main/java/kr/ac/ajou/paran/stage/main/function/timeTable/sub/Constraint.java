package kr.ac.ajou.paran.stage.main.function.timeTable.sub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import kr.ac.ajou.paran.R;

/**
 * Created by dream on 2017-11-23.
 */

public class Constraint extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint);

        initSetting();
    }

    public void initSetting(){
        context = this;
        TextView textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText("제약조건");
    }
}
