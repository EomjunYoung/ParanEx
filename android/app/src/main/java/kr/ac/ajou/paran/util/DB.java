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
        try {
            initStringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS subjectinfo2( ");
            sb.append("retake varchar(20), ");
            sb.append("mandate varchar(20), ");
            sb.append("name varchar(20) PRIMARY KEY) ");

            db.execSQL(sb.toString());

            initStringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS etc2( ");
            sb.append("number integer PRIMARY KEY, ");
            sb.append("abeek integer, ");
            sb.append("before varchar(20))");

            db.execSQL(sb.toString());
            Log.d("eom", "db 생성완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public boolean checkETC(int number){
        boolean check =  false;
        Cursor cursor = db.rawQuery("select count(*) as number from etc2 where number="+number, null);
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

    public String getETC(int number)
    {
        String etc = "";
        Cursor cursor = db.rawQuery("select * from etc2 where number="+number, null);
        if (cursor.moveToFirst()) {
            final int indexAbeek = cursor.getColumnIndex("abeek");
            final int indexBefore = cursor.getColumnIndex("before");
            etc = cursor.getInt(indexAbeek)+"/"+cursor.getString(indexBefore);
        }
        cursor.close();

        return etc;
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

    public ArrayList<String> getRe()
    {
        ArrayList<String> res = new ArrayList<String>();
        Cursor cursor = db.rawQuery("select * from subjectinfo2 where retake='O'", null);
        if (cursor.moveToFirst()) {
            final int indexName = cursor.getColumnIndex("name");
            do {
                res.add(cursor.getString(indexName).trim());
            } while (cursor.moveToNext());
        }
        cursor.close();

        return res;
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

    public void insertETC(int number, int abeek, String before) {
        initStringBuilder();

        try{
            sb.append("INSERT INTO etc2(number,abeek,before) VALUES( ");
            sb.append(number +" , "+ abeek +", '"+ before.trim() +"'");
            sb.append(")");
            db.execSQL(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateETC(int number, int abeek, String before) {
        initStringBuilder();

        try{
            db.execSQL("UPDATE etc2 SET abeek="+abeek+", before='"+before.trim()+"' WHERE number="+number);
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

    public SQLiteDatabase getDB(){
        return db;
    }
}
