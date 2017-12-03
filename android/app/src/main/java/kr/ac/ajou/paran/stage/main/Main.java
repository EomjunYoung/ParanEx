package kr.ac.ajou.paran.stage.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.StringTokenizer;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.dialog.AlertTrans;
import kr.ac.ajou.paran.stage.main.dialog.CheckSavedTable;
import kr.ac.ajou.paran.stage.main.dialog.InitSubject;
import kr.ac.ajou.paran.stage.main.function.bulltinBoard.BulltinBoard;
import kr.ac.ajou.paran.stage.main.function.lecture.Lecture;
import kr.ac.ajou.paran.stage.main.function.subject.SubjectManage;
import kr.ac.ajou.paran.util.DB;
import kr.ac.ajou.paran.util.HTTP;
import kr.ac.ajou.paran.util.Raw;
import kr.ac.ajou.paran.util.object.User;


/**
 * Created by user on 2017-07-08.
 */

public class Main extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private String ip, port;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_main);

        setNetwork();
        setButton();
        fetchUserInformation();
        printView();
        sendSubjectToServer();
    }

    private void setNetwork() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setButton() {
        Button buttonTimeTable, buttonLecture, buttonBulletinBoard, buttonSubject;

        buttonBulletinBoard = (Button) findViewById(R.id.buttonBulletinBoard);
        buttonSubject = (Button) findViewById(R.id.buttonSubject);
        buttonLecture = (Button) findViewById(R.id.buttonLecture);
        buttonTimeTable = (Button) findViewById(R.id.buttonTimeTable);
        buttonBulletinBoard.setOnClickListener(this);
        buttonSubject.setOnClickListener(this);
        buttonLecture.setOnClickListener(this);
        buttonTimeTable.setOnClickListener(this);
    }

    private void fetchUserInformation() {
        String cookie = getIntent().getStringExtra("Cookie");

         /*유저 정보 받아옴*/
        user = HTTP.printUser(cookie);
        /*유저 정보 받아옴*/

        /*수강 정보 받아옴*/
        user.setSubjectList(HTTP.printSubject(cookie));
        checkSubjectFromDB(user);
        new InitSubject(this, user.getSubjectList()).showDialog();
        /*수강 정보 받아옴*/

        if (user.isNewORtrans() == false)
            new AlertTrans(this).showDialog();

        /*공학인증 여부 조사*/
        user.setAbeek(HTTP.checkAbeek(cookie, user.getNumber()));
        /*공학인증 여부 조사*/

        /*초기 전공 조사*/
        user.setBefore(HTTP.inspectMajor(cookie, user.getNumber()));
        /*초기 전공 조사*/
    }

    private void checkSubjectFromDB(User user) {
        DB db = new DB(getApplicationContext(), "mydb2.db", null, 1);

        int number_subject;
        number_subject = db.checkSubject();
        if (number_subject == 0)
            addSubjectToDB(db);
        else
            compareSubjectWithDB(db, number_subject);
        user.setSubjectList(db.getSubjectList());

        db.close();
    }

    private void compareSubjectWithDB(DB db, int number_subject) {
        if (user.getSubjectList() != null) {
            int count = number_subject + 1;
            StringTokenizer st = new StringTokenizer(user.getSubjectList(), "\t");

            while (st.hasMoreTokens()) {
                String retake = st.nextToken();
                String mandate = st.nextToken();
                String name = st.nextToken();

                if (db.checkSubject(name) == false) {
                    db.insertSubject(retake, mandate, name);
                    count++;
                }
            }
        }
    }

    private void addSubjectToDB(DB db) {
        if (user.getSubjectList() != null) {
            StringTokenizer st = new StringTokenizer(user.getSubjectList(), "\t");

            while (st.hasMoreTokens()) {
                String retake = st.nextToken();
                String mandate = st.nextToken();
                String name = st.nextToken();

                db.insertSubject(retake, mandate, name);
            }
        }
    }

    private void printView() {
        int width, height;
        /*이미지 출력을 위해 핸드폰 스크린 크기 계산*/
        width = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        height = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        /*이미지 출력을 위해 핸드폰 스크린 크기 계산*/

        printUserInformation(width, height);
        printLogo(width, height);
    }

    private void printUserInformation(int width, int height) {
         /*이미지 출력-조건 학번을 제대로 받아올 경우*/
        ImageView imagePicture = (ImageView) findViewById(R.id.imagePicture);
        if (user.getNumber() != 0)
            imagePicture.setImageBitmap(HTTP.printPicture(user.getNumber(), (int) (height * 0.3)));
        /*이미지 출력-조건 학번을 제대로 받아올 경우*/

        /*뷰에 정보 셋팅*/
        TextView textUser = (TextView) findViewById(R.id.textUser);
        textUser.setText(user.getName() + "\n" + user.getNumber() + "\n" + user.getGrade() + "학년" + "\n" + user.getMajor());
        /*뷰에 정보 셋팅*/
    }

    private void printLogo(int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_main_logo);
        ImageView imageLogo = (ImageView) findViewById(R.id.imageLogo);
        imageLogo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int) (width * 0.45), (int) (height * 0.075), true));
        imageLogo.setY(height * 0.2f);
    }

    private void sendSubjectToServer() {
        ip = Raw.readIP(Main.this);
        port = Raw.readPort(Main.this);
        if (!ip.equals("") && !port.equals("")) {
            HTTP.postSubject(ip + ":" + port, user.getSubjectList(), user.getNumber());
            HTTP.postUser(ip + ":" + port, user.serializedUser());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonTimeTable:
                /*시간표 있는지 확인*/
                if (!ip.equals("") && !port.equals("")) {
                    CheckSavedTable checkSavedTable;
                    if (HTTP.checkTable(ip + ":" + port, user.getNumber()))
                        checkSavedTable = new CheckSavedTable(this, true);
                    else
                        checkSavedTable = new CheckSavedTable(this, false);
                    checkSavedTable.setNumber(user.getNumber());
                    checkSavedTable.showDialog();
                }
                /*시간표 있는지 확인*/

                break;
            case R.id.buttonLecture:
                startActivity(new Intent(this, Lecture.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.buttonSubject:
                startActivity(new Intent(this, SubjectManage.class).putExtra("number", user.getNumber()));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.buttonBulletinBoard:
                startActivity(new Intent(this, BulltinBoard.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
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
