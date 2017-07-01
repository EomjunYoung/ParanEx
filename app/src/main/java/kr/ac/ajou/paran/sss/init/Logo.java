package kr.ac.ajou.paran.sss.init;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import kr.ac.ajou.paran.R;

public class Logo extends AppCompatActivity {

    private InitThread initThread;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        initThread = new InitThread();
        initThread.start();
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void change(){
        startActivity(new Intent(this, Developer.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    class InitThread extends Thread{

        private Message msg;

        final Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                ((ImageView)findViewById(R.id.imageLogo2)).setVisibility(View.VISIBLE);
            }
        };

        public void run() {
            try {
                sleep(500);     //wait two second
                animationDrawable =(AnimationDrawable)((ImageView)findViewById(R.id.imageLogo)).getBackground();
                animationDrawable.start();
                sleep(1500);
                msg = handler.obtainMessage();
                handler.sendMessage(msg);
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            change();
        }
    }
}
