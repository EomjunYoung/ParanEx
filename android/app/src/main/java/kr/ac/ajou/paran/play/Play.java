package kr.ac.ajou.paran.play;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.login.Login;
import kr.ac.ajou.paran.stage.main.function.timeTable.sub.Constraint;
import kr.ac.ajou.paran.util.Recognizer;

/**
 * Created by dream on 2017-06-30.
 */

public class Play extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, Constraint.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}