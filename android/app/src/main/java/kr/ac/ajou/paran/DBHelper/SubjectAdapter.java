package kr.ac.ajou.paran.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import kr.ac.ajou.paran.R;

/**
 * Created by ejy77 on 2017-11-16.
 */

public class SubjectAdapter extends CursorAdapter

{

    public SubjectAdapter(Context context, Cursor c, int flag)
    {
        super(context,c,flag);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.activity_adapter, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView tvretake = (TextView)view.findViewById(R.id.retake);
        TextView tvmandate = (TextView)view.findViewById(R.id.mandate);
        TextView tvname = (TextView)view.findViewById(R.id.name);
        Log.d("eom", "TEXTVIEW 완료");

        String retake = cursor.getString(cursor.getColumnIndexOrThrow("retake"));
        String mandate = cursor.getString(cursor.getColumnIndexOrThrow("mandate"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        Log.d("eom", "TEST 완료");


        tvretake.setText(retake);
        tvmandate.setText(mandate);
        tvname.setText(name);
        Log.d("eom", "setTEXT 완료");


    }
}
