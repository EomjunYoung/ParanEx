package kr.ac.ajou.paran.sss.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.DialogType;
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

    final String string = "mydb.db";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase(string, MODE_PRIVATE, null);
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS dbtable2(");
        sb.append("_id INTEGER PRIMARY KEY autoincrement, ");
        sb.append("name TEXT, ");
        sb.append("studentId TEXT, ");
        sb.append("grade TEXT, ");
        sb.append("major TEXT)");


        /* 쿠키를 가져옴 */
        cookie = getIntent().getStringExtra("Cookie");
        /* 쿠키를 가져옴 */

        imagePicture = (ImageView)findViewById(R.id.imagePicture);
        imageLogo = (ImageView)findViewById(R.id.imageLogo);
        textUser = (TextView)findViewById(R.id.textUser);

        /*유저 정보 받아옴*/
        User user = HTTP.printUser(cookie);
        /*유저 정보 받아옴*/

        /*DB에 기록 저장되있으면 불러올필요 없음 if문 처리할 것*/
        /*수강 정보 받아옴*/
        new InitSubject(this,cookie).showDialog();
        /*수강 정보 받아옴*/
        /*DB에 기록 저장되있으면 불러올필요 없음 if문 처리할 것*/

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

        /*로고 출력*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main_main_logo);
        imageLogo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int)(width*0.45),(int)(height*0.075), true));
        imageLogo.setY(height*0.2f);
        /*로고 출력*/


        try
        {
            db.execSQL(sb.toString());
            String sql = "insert into dbtable2(name, studentId, grade, major) values('"+user.getName()+"', '"+user.getNumber()+"', '"+user.getGrade()+"', '"+user.getMajor()+"')";
            db.execSQL(sql);

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    public String printData() //db저장된거 확인
    {
        String str = "select *from dbtable2 order by _id desc";
        Cursor cursor = db.rawQuery(str, null);

        String str3 = "";
        while(cursor.moveToNext())
        {


            str3 += "id:" + cursor.getInt(0) + "이름:" + cursor.getString(1) + "학번:"
                    + cursor.getString(2) + "학년:" + cursor.getString(3) + "전공:" + cursor.getString(4) +"\n";

        }

        return str3;
    }
}
