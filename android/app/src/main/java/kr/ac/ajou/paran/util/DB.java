package kr.ac.ajou.paran.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dream on 2017-08-09.
 */

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydb.db";

    private SQLiteDatabase db;

    private StringBuilder sb;

    public DB(Context context) {
        super(context, DB_NAME, null, 1);
        db = getReadableDatabase();
        sb = new StringBuilder();
    }

    public void initStringBuilder(){
        sb.delete(0,sb.length());
        sb.trimToSize();
    }

    public void createUserTable(User user){
        initStringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS userInfo(");
        sb.append("id INTEGER PRIMARY KEY, ");
        sb.append("name TEXT, ");
        sb.append("grade TEXT, ");
        sb.append("major TEXT");
        sb.append("before TEXT");
        sb.append("abeek INTEGER");
        sb.append("fresh INTEGER)");

        try {
            db.execSQL(sb.toString());
            String sql = "insert into userInfo(name, id, grade, major, before, abeek, fresh) values('"+user.getName()+"', '"+user.getNumber()+"', '"+user.getGrade()+"', '"+user.getMajor()+"', '"+user.getBefore()+"', '"+(user.isAbeek()?1:0)+"', '"+(user.isNewORtrans()?1:0)+"')";
            db.execSQL(sql);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String createSubject(String cookie, int number) {
        Cursor cursor = db.rawQuery("select count(*) from sqlite_master where name='synchronization'", null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 0) {
            cursor.close();
            initStringBuilder();
            sb.append("CREATE TABLE subject( ");
            sb.append("id INTEGER, ");
            sb.append("re INTEGER, ");
            sb.append("name VARCHAR(100), ");
            sb.append("type VARCHAR(10))");
            try {
                db.execSQL(sb.toString());
                String subjectList = HTTP.printSubject(cookie);
                String re, name, type;
                StringTokenizer s = new StringTokenizer(subjectList);
                while(s.hasMoreTokens()) {
                    re = s.nextToken("\t");
                    type = s.nextToken("\t");
                    name = s.nextToken("\t");
                    db.execSQL("insert into subject(id,re,name,type) values(" + number + ","+(re.equals("X")?0:1)+",'"+name+"','"+type+"')");
                }
                return subjectList;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return null;
    }

    public void deleteTable(){
        db.execSQL("drop table userInfo;");
        db.execSQL("drop table subject;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
