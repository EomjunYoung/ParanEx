package kr.ac.ajou.paran.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dream on 2017-08-09.
 */

public class DB {

    private final String dbName = "mydb.db";
    private final String dbName2 = "mydb2.db";

    private SQLiteDatabase db;

    public void createUserTable(Context context, User user){
        db = context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS userInfo(");
        sb.append("_id INTEGER PRIMARY KEY autoincrement, ");
        sb.append("name TEXT, ");
        sb.append("studentId TEXT, ");
        sb.append("grade TEXT, ");
        sb.append("major TEXT)");

        try {
            db.execSQL(sb.toString());
            String sql = "insert into userInfo(name, studentId, grade, major) values('"+user.getName()+"', '"+user.getNumber()+"', '"+user.getGrade()+"', '"+user.getMajor()+"')";
            db.execSQL(sql);
        }catch(Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    public void createSubjectList(Context context, String cookie)
    {
        db = context.openOrCreateDatabase(dbName2, MODE_PRIVATE, null);
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS subjectList( ");
        sb.append("subject varchar(1000)) ");

        try
        {
            db.execSQL(sb.toString());
            String str = "insert into subjectList(subject) values('"+HTTP.printSubject(cookie)+"')";
            db.execSQL(str);
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        db.close();
    }
}
