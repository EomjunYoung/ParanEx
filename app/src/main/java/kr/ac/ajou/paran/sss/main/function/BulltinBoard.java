package kr.ac.ajou.paran.sss.main.function;

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
 * Created by user on 2017-08-11.
 */

public class BulltinBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_bulletinboard);
    }
}
