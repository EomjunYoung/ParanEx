package kr.ac.ajou.paran.sss.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.sss.main.Main;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void change(){
    //    startActivity(new Intent(this, Developer.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                Toast.makeText(getApplicationContext(), "Success in login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Main.class));
        }
    }
}
