package kr.ac.ajou.paran.play;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.sss.init.Logo;
import kr.ac.ajou.paran.sss.main.Main;

/**
 * Created by dream on 2017-06-30.
 */

public class Play extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startActivity(new Intent(this, Main.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}