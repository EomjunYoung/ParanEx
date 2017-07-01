package kr.ac.ajou.paran.sss.init;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kr.ac.ajou.paran.MainActivity;
import kr.ac.ajou.paran.R;

public class Logo extends AppCompatActivity {

    private InitThread initThread;

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

        public void run(){
            try {
                Thread.sleep(1000); //wait one second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            change();
        }
    }
}
