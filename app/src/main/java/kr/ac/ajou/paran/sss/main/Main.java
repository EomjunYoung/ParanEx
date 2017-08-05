package kr.ac.ajou.paran.sss.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.User;


/**
 * Created by user on 2017-07-08.
 */

public class Main extends AppCompatActivity {

    private User user;

    private String cookie;
    private int width;
    private int height;

    private ImageView imagePicture;
    private ImageView imageLogo;
    private TextView textUser;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_main);

        cookie = getIntent().getStringExtra("Cookie");

        imagePicture = (ImageView)findViewById(R.id.imagePicture);
        imageLogo = (ImageView)findViewById(R.id.imageLogo);
        textUser = (TextView)findViewById(R.id.textUser);
        User user = HTTP.printUser(cookie);
        HTTP.logOut(cookie);
        width = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        height = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        if(user.getNumber()!=0)
            imagePicture.setImageBitmap(HTTP.printPicture(user.getNumber(), (int) (height * 0.3)));
        textUser.setText(user.getName()+"\n"+user.getNumber()+"\n"+user.getGrade()+"학년"+"\n"+user.getMajor());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main_main_logo);
        imageLogo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int)(width*0.45),(int)(height*0.05), true));
        imageLogo.setY(height*0.225f);

    }
}
