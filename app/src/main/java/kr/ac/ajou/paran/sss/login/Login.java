package kr.ac.ajou.paran.sss.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.sss.main.Main;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private EditText editID, editPWD;

    private String cookie;

    private final static String MOBILE = "https://mb.ajou.ac.kr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.loginButton);
        editID = (EditText)findViewById(R.id.editID);
        editPWD = (EditText)findViewById(R.id.editPWD);

        loginButton.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public HttpURLConnection makeConnection(URL url) {
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
            con.setRequestProperty("Accept-Upgrade-Insecure-Requests", "1");
            return con;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public String loginMobilePara(String id, String pwd) {
        String parameter;
        try {
            parameter = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            parameter += "&" + URLEncoder.encode("passwd", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8");
            parameter += "&" + URLEncoder.encode("rememberMe", "UTF-8") + "=" + URLEncoder.encode("N", "UTF-8");
            parameter += "&" + URLEncoder.encode("platformType", "UTF-8") + "=" + URLEncoder.encode("A", "UTF-8");
            parameter += "&" + URLEncoder.encode("deviceToken", "UTF-8") + "="
                    + URLEncoder.encode(
                    "APA91bHLPmSyNjpcYoWFGEdHIJiNYIConV_kcx6DdXpuzH7o3OimWFLDZ_J60nK9EQRdmyMFHJbJsoLC2y-tKceiYJTmAl_0L7ZtkkZF8fgKR0VMwMkJy4o",
                    "UTF-8");
            return parameter;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public String postLoginMOBILE(URL url, String id, String pwd) {
        try {
            HttpURLConnection con = makeConnection(url);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(loginMobilePara(id, pwd));
            wr.flush();
            String cookie = con.getHeaderFields().get("Set-Cookie").get(2);
            cookie = cookie.substring(0, cookie.indexOf(";"));
            con.disconnect();
            return cookie;
        } catch (IOException | IndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public String loginMobile(String id, String pwd, String cookie) {
        try {
            cookie = postLoginMOBILE(new URL(MOBILE + "/mobile/login.json"), id, pwd);

            return cookie;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                if ((cookie = loginMobile(editID.getText().toString(), editPWD.getText().toString(), cookie)) != null) {
                    Toast.makeText(getApplicationContext(), "Success in login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Main.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }else
                    Toast.makeText(getApplicationContext(), "Please check your input", Toast.LENGTH_SHORT).show();
        }
    }
}
