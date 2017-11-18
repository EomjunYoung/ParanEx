package kr.ac.ajou.paran.stage.init;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.login.Login;

public class Developer extends AppCompatActivity {

    private InitThread initThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        initThread = new InitThread();
        initThread.start();
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void change(){
        startActivity(new Intent(this, Login.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    class InitThread extends Thread{

        public void run(){
            try {
                Thread.sleep(1500); //wait 1.5 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            change();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Drawable drawable = ((ImageView)findViewById(R.id.imageDeveloper)).getDrawable();
        if(drawable instanceof BitmapDrawable)
            ((BitmapDrawable) drawable).getBitmap().recycle();
        System.gc();
    }
}
