package kr.ac.ajou.paran.stage.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.stage.main.Main;
import kr.ac.ajou.paran.util.HTTP;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private EditText editID, editPWD;

    private String cookie;

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                if ((cookie = HTTP.loginAjou(editID.getText().toString(), editPWD.getText().toString())) != null) {
                    startActivity(new Intent(this, Main.class).putExtra("Cookie",cookie));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }else
                    Toast.makeText(getApplicationContext(), "Please check your input", Toast.LENGTH_SHORT).show();
        }
    }
}
