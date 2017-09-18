package kr.ac.ajou.paran.sss.init;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    private AnimationDrawable animationDrawable, animationDrawable2;

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
                animationDrawable2 =(AnimationDrawable)((ImageView)findViewById(R.id.imageLogo2)).getBackground();
                animationDrawable2.start();
            }
        };

        public void run() {
            try {
                sleep(500);     //wait two second
                animationDrawable =(AnimationDrawable)((ImageView)findViewById(R.id.imageLogo)).getBackground();
                animationDrawable.start();
                sleep(1200);
                for(int i=0;i<animationDrawable.getNumberOfFrames()-1;i++){
                    Drawable frame = animationDrawable.getFrame(i);
                    if(frame instanceof BitmapDrawable)
                        ((BitmapDrawable)frame).getBitmap().recycle();

                    frame.setCallback(null);
                }
                msg = handler.obtainMessage();
                handler.sendMessage(msg);
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            change();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationDrawable.stop();
        animationDrawable2.stop();
        Drawable frame = animationDrawable.getFrame(animationDrawable.getNumberOfFrames()-1);
        if(frame instanceof BitmapDrawable)
            ((BitmapDrawable)frame).getBitmap().recycle();

        frame.setCallback(null);
        animationDrawable.setCallback(null);
        for(int i=0;i<animationDrawable2.getNumberOfFrames();i++){
            frame = animationDrawable2.getFrame(i);
            if(frame instanceof BitmapDrawable)
                ((BitmapDrawable)frame).getBitmap().recycle();

            frame.setCallback(null);
        }
        animationDrawable2.setCallback(null);
        animationDrawable = null;
        animationDrawable2 = null;
        System.gc();
    }
}
