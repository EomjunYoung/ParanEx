package kr.ac.ajou.paran.sss.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.sss.main.dialog.AlertTrans;
import kr.ac.ajou.paran.sss.main.dialog.InitSubject;
import kr.ac.ajou.paran.util.DB;
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

    private DB db;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_main);

        /* 쿠키를 가져옴 */
        cookie = getIntent().getStringExtra("Cookie");
        /* 쿠키를 가져옴 */

        imagePicture = (ImageView)findViewById(R.id.imagePicture);
        imageLogo = (ImageView)findViewById(R.id.imageLogo);
        textUser = (TextView)findViewById(R.id.textUser);

        /*DB 생성*/
        db = new DB(this);
        /*DB 생성*/

        /*유저 정보 받아옴*/
        user = HTTP.printUser(cookie);
        /*유저 정보 받아옴*/

        /*처음 로그인시 - 수강 정보 받아옴*/
        String subjectList;
        if((subjectList = db.createSubject(cookie,user.getNumber())) != null) {
            new InitSubject(this, subjectList).showDialog();
            if(user.isNewORtrans() == false)
                new AlertTrans(this).showDialog();
        }
        /*처음 로그인시 - 수강 정보 받아옴*/

        /*원격 접속한 것 로그아웃*/
        HTTP.logOut(cookie);
        /*원격 접속한 것 로그아웃*/

        /*이미지 출력을 위해 핸드폰 스크린 크기 계산*/
        width = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        height = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        /*이미지 출력을 위해 핸드폰 스크린 크기 계산*/

        /*이미지 출력-조건 학번을 제대로 받아올 경우*/
        if(user.getNumber()!=0)
            imagePicture.setImageBitmap(HTTP.printPicture(user.getNumber(), (int) (height * 0.3)));
        /*이미지 출력-조건 학번을 제대로 받아올 경우*/

        /*뷰에 정보 셋팅*/
        textUser.setText(user.getName()+"\n"+user.getNumber()+"\n"+user.getGrade()+"학년"+"\n"+user.getMajor());
        /*뷰에 정보 셋팅*/

        /*DB에 정보 저장*/
        db.createUserTable(user);
        /*DB에 정보 저장*/

        /*로고 출력*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main_main_logo);
        imageLogo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int)(width*0.45),(int)(height*0.075), true));
        imageLogo.setY(height*0.2f);
        /*로고 출력*/

        db.close();
    }

/*
    public String printData() //db저장된거 확인
    {
        String str = "select *from dbtable2 order by _id desc";
        Cursor cursor = db.rawQuery(str, null);

        String str3 = "";
        while(cursor.moveToNext()){
            str3 += "id:" + cursor.getInt(0) + "이름:" + cursor.getString(1) + "학번:"
                    + cursor.getString(2) + "학년:" + cursor.getString(3) + "전공:" + cursor.getString(4) +"\n";
        }

        return str3;
    }*/
}
