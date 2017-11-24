package kr.ac.ajou.paran.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.StringTokenizer;

import static android.R.attr.x;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dream on 2017-08-09.
 */

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydb2.db";

    private StringBuilder sb = new StringBuilder();

    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, 1);

    }

    public void initStringBuilder() {
        sb.delete(0, sb.length());
        sb.trimToSize();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initStringBuilder();


        try {
            sb.append("CREATE TABLE IF NOT EXISTS subjectinfo2( ");
            sb.append("_id integer PRIMARY KEY, ");
            sb.append("retake varchar(20), ");
            sb.append("mandate varchar(20), ");
            sb.append("name varchar(20)) ");

            db.execSQL(sb.toString());
            Log.d("eom", "db 생성완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSubject(int _id, String retake, String mandate, String name)
    {
        initStringBuilder();

        try{
            SQLiteDatabase db = getWritableDatabase();
            sb.append("INSERT INTO subjectinfo2 VALUES( ");
            sb.append(""+ _id +", '"+ retake +"' , '"+ mandate +"', '"+ name +"'");
            sb.append(")");
            db.execSQL(sb.toString());
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteSubject(int _id)
    {
        initStringBuilder();
        try{
            SQLiteDatabase db = getWritableDatabase();
            sb.append("DELETE FROM subjectinfo2 ");
            sb.append("WHERE _id="+_id+"");

            db.execSQL(sb.toString());
            db.close();
        } catch(Exception e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
