package kr.ac.ajou.paran.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ejy77 on 2017-11-16.
 */

public class DBHelper extends SQLiteOpenHelper {


    private Context context;
    private StringBuilder sb = new StringBuilder();

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

            sb.append("CREATE TABLE IF NOT EXISTS subjectinfo( ");
            sb.append("_id integer PRIMARY KEY, ");
            sb.append("retake varchar(20), ");
            sb.append("mandate varchar(20), ");
            sb.append("name varchar(20)) ");


            db.execSQL(sb.toString());
            Log.d("eom", "db 생성완료!");

        } catch (Exception e)

        {
            e.printStackTrace();
        }


    }

    public void insertsubject(int _id, String retake, String mandate, String name)
    {

        initStringBuilder();

        try{
            SQLiteDatabase db = getWritableDatabase();
            sb.append("INSERT INTO subjectinfo VALUES( ");
            sb.append("'"+ _id +"', '"+ retake +"' , '"+ mandate +"', '"+ name +"'");
            sb.append(")");
            db.execSQL(sb.toString());
            db.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deletesubject(int _id)
    {

        initStringBuilder();

        try{

            SQLiteDatabase db = getWritableDatabase();
            sb.append("DELETE FROM subjectinfo(");
            sb.append("WHERE = '"+ _id +"'");
            sb.append(")");
            db.execSQL(sb.toString());
            db.close();

        }


        catch(Exception e)
        {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
