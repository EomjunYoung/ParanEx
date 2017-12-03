package kr.ac.ajou.paran.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.R.attr.x;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dream on 2017-08-09.
 */

public class DB extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private StringBuilder sb = new StringBuilder();

    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, 1);
        db = getReadableDatabase();
    }

    public void initStringBuilder() {
        sb.delete(0, sb.length());
        sb.trimToSize();
    }

    @Override
    public synchronized void close() {
        db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initStringBuilder();

        try {
            sb.append("CREATE TABLE IF NOT EXISTS subjectinfo2( ");
            sb.append("retake varchar(20), ");
            sb.append("mandate varchar(20), ");
            sb.append("name varchar(20) PRIMARY KEY) ");

            db.execSQL(sb.toString());
            Log.d("eom", "db 생성완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int checkSubject(){
        int number = 0;
        Cursor cursor = db.rawQuery("select count(*) as number from subjectinfo2", null);
        if (cursor.moveToFirst()) {
            final int indexNumber = cursor.getColumnIndex("number");
            number = cursor.getInt(indexNumber);
        }
        cursor.close();

        return number;
    }

    public boolean checkSubject(String subject){
        boolean check =  false;
        Cursor cursor = db.rawQuery("select count(*) as number from subjectinfo2 where name='"+subject.trim()+"'", null);
        if (cursor.moveToFirst()) {
            final int indexNumber = cursor.getColumnIndex("number");
            if(cursor.getInt(indexNumber) != 0)
                check = true;
        }
        cursor.close();

        return check;
    }

    public String getSubjectList()
    {
        StringBuffer subjectList = new StringBuffer();
        Cursor cursor = db.rawQuery("select * from subjectinfo2", null);
        if (cursor.moveToFirst()) {
            final int indexName = cursor.getColumnIndex("name");
            final int indexRetake = cursor.getColumnIndex("retake");
            final int indexMandate = cursor.getColumnIndex("mandate");
            do {
                subjectList.append(String.format("\t%s\t\t%s\t\t%s\n",cursor.getString(indexRetake),cursor.getString(indexMandate),cursor.getString(indexName)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if(subjectList.length()>0)
            return subjectList.toString().substring(0,subjectList.length()-1);
        else
            return subjectList.toString();
    }

    public ArrayList<String> getSubject()
    {
        ArrayList<String> subjects = new ArrayList<String>();
        Cursor cursor = db.rawQuery("select * from subjectinfo2", null);
        if (cursor.moveToFirst()) {
            final int indexName = cursor.getColumnIndex("name");
            final int indexRetake = cursor.getColumnIndex("retake");
            final int indexMandate = cursor.getColumnIndex("mandate");
            do {
                subjects.add(String.format("\t%s\t\t%s\t\t%s\n",cursor.getString(indexRetake),cursor.getString(indexMandate),cursor.getString(indexName)).trim());
            } while (cursor.moveToNext());
        }
        cursor.close();

        return subjects;
    }

    public void insertSubject(String retake, String mandate, String name)
    {
        initStringBuilder();

        try{
            sb.append("INSERT INTO subjectinfo2(retake,mandate,name) VALUES( ");
            sb.append("'"+ retake +"' , '"+ mandate +"', '"+ name.trim() +"'");
            sb.append(")");
            db.execSQL(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteSubject(String name)
    {
        initStringBuilder();
        try{
            sb.append("DELETE FROM subjectinfo2 ");
            sb.append("WHERE name='"+name.split("\t\t")[2].trim()+"'");

            db.execSQL(sb.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
