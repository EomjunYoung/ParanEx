package kr.ac.ajou.paran.sss.main;

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

    private ImageView imagePicture;
    private TextView textUser;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_main);

        cookie = getIntent().getStringExtra("Cookie");

        imagePicture = (ImageView)findViewById(R.id.imagePicture);
        textUser = (TextView)findViewById(R.id.textUser);
        User user = HTTP.printUser(cookie);
        HTTP.logOut(cookie);
        if(user.getNumber()!=0)
            imagePicture.setImageBitmap(HTTP.printPicture(user.getNumber(), (int) (((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 0.3)));
        textUser.setText(user.getName()+"\n"+user.getNumber()+"\n"+user.getGrade()+"학년"+"\n"+user.getMajor());
    }
}
