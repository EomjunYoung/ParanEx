package kr.ac.ajou.paran.sss.main.function;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.FunctionType;


/**
 * Created by user on 2017-08-11.
 */

public class Lecture extends FunctionType {

    EditText editText;
    Spinner spinner;
    Spinner major, type, semester, year;
    ArrayList<String> arrayList = new ArrayList<>();

    public Lecture(){
        super("강의시간표",R.layout.activity_lecture);


    }

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute();
        editText = (EditText)findViewById(R.id.name);
        major = (Spinner)findViewById(R.id.major);
        type = (Spinner)findViewById(R.id.type);
        semester = (Spinner)findViewById(R.id.semester);
        year = (Spinner)findViewById(R.id.year);

    }



    private class NetworkTask extends AsyncTask<Void, Void, LinkedHashMap<String, String>> {

        @Override
        protected LinkedHashMap<String, String> doInBackground(Void... voids) {
            LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();

            String target = "http://haksa.ajou.ac.kr/uni/uni/cour/lssn/findCourLecturePlanDocumentReg.action";

            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) new URL(target).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.6,en;q=0.4");
            con.setRequestProperty("Content-Type", "text/xml/SosFlexMobile;charset=utf-8");
            con.setDoOutput(true);
            try {
                con.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            OutputStreamWriter wr = null;
            try {
                wr = new OutputStreamWriter(con.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String parameter = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
            parameter += "<root>\n";
            parameter += "<params>\n";
            parameter += "<param id=\"strYy\" type=\"STRING\">" + "2017" + "</param>\n";
            parameter += "<param id=\"strShtmCd\" type=\"STRING\">" + "U0002003" + "</param>\n";
            parameter += "<param id=\"strSubmattFg\" type=\"STRING\">" + "U0209002" + "</param>\n";
            parameter += "</params>\n";
            parameter += "</root>";

            try {
                wr.write(parameter);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wr.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }


            String temp = null;
            int i = 0;

            try {
                while ((temp = br.readLine()) != null) {

                    i++;



                    if (temp.contains("<maLecturerEmplNm>")) {

                        String name = temp.split(">")[1].split("</")[0];
                        result.put("eom"+ i, name);

                    }

                    if ( i==1000)
                    break;



                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }


        @Override
        protected void onPostExecute(LinkedHashMap<String, String> aVoid) {
            super.onPostExecute(aVoid);



            Iterator<String> itr = aVoid.values().iterator();


            for (; itr.hasNext();)
            {
                arrayList.add(itr.next());
            }



            ArrayAdapter majorAdapter = new ArrayAdapter(
                    getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);

            major.setAdapter(majorAdapter);



        }

    }

}




