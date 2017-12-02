package kr.ac.ajou.paran.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedHashMap;

/**
 * Created by ejy77 on 2017-09-02.
 */

public class NetworkAsync extends AsyncTask {

    private String semester, type, major;
    private Callback mCallback;

    public NetworkAsync(String semester, String type, String major, Callback mCallback) {
        this.semester = semester;
        this.type = type;
        this.major = major;
        this.mCallback = mCallback;
    }

    public NetworkAsync(String semester, String type, Callback mCallback)
    {

        this.semester = semester;
        this.type = type;
        this.mCallback = mCallback;

    }


    @Override
    protected String doInBackground(Object[] objects) {



        try {

            //교양과목
            if(type == "U0209002") {
                return sendPost1(semester, type);
            }
            else if(type == "U0209003") //기초과목
                return sendPost3(semester, type, major);

            else if(type == "U0209001")
                return sendPost(semester, type, major);

            else
                return sendPost2(semester, type, major);

        } catch (Exception e) {
            e.printStackTrace();


        }

        return null;
    }



    private String sendPost(String tmpSemester, String tmpType, String tmpMajor) {
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
        parameter += "<param id=\"strShtmCd\" type=\"STRING\">" + tmpSemester + "</param>\n";
        parameter += "<param id=\"strSubmattFg\" type=\"STRING\">" + tmpType + "</param>\n";
        parameter += "<param id=\"strMjCd\" type=\"STRING\">" + tmpMajor + "</param>\n";
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


        String temp;
        int i = 0;
        StringBuilder response = new StringBuilder();
        StringBuilder response2 = new StringBuilder();

        try {
            while ((temp = br.readLine()) != null) {


                i++;

                if (temp.contains("<sbjtKorNm>")) {

                    String name = temp.split(">")[1].split("</")[0];
                    if (name.contains("&#32;"))
                    {
                        name = name.replace("&#32;", " ");
                    }
                    response.append(name);
                    response.append("E");


                }

                if (temp.contains("<sustLsnFgNm>"))
                {
                    String name2 = temp.split(">")[1].split("</")[0];
                    response.append(name2);
                    response.append("E");
                }



                if (i == 3000)
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

        return String.valueOf(response);

    }



    private String sendPost2(String tmpSemester, String tmpType, String tmpArea) {
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
        parameter += "<param id=\"strShtmCd\" type=\"STRING\">" + tmpSemester + "</param>\n";
        parameter += "<param id=\"strSubmattFg\" type=\"STRING\">" + tmpType + "</param>\n";
        parameter += "<param id=\"strSubmattFldFg\" type=\"STRING\">" + tmpArea + "</param>\n";
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


        String temp;
        int i = 0;
        StringBuilder response = new StringBuilder();
        StringBuilder response2 = new StringBuilder();

        try {
            while ((temp = br.readLine()) != null) {


                i++;

                if (temp.contains("<sbjtKorNm>")) {

                    String name = temp.split(">")[1].split("</")[0];
                    if (name.contains("&#32;"))
                    {
                        name = name.replace("&#32;", " ");
                    }
                    response.append(name);
                    response.append("E");


                }

                if (temp.contains("<sustLsnFgNm>"))
                {
                    String name2 = temp.split(">")[1].split("</")[0];
                    response.append(name2);
                    response.append("E");
                }



                if (i == 3000)
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

        return String.valueOf(response);

    }



    private String sendPost3(String tmpSemester, String tmpType, String tmpMajor) {
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
        parameter += "<param id=\"strShtmCd\" type=\"STRING\">" + tmpSemester + "</param>\n";
        parameter += "<param id=\"strSubmattFg\" type=\"STRING\">" + tmpType + "</param>\n";
        parameter += "<param id=\"strSustcd\" type=\"STRING\">" + tmpMajor + "</param>\n";
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


        String temp;
        int i = 0;
        StringBuilder response = new StringBuilder();

        try {
            while ((temp = br.readLine()) != null) {


                i++;

                if (temp.contains("<sbjtKorNm>")) {

                    String name = temp.split(">")[1].split("</")[0];
                    if (name.contains("&#32;"))
                    {
                        name = name.replace("&#32;", " ");
                    }
                    response.append(name);
                    response.append("E");
                }

                if (temp.contains("<sustLsnFgNm>"))
                {
                    String name2 = temp.split(">")[1].split("</")[0];
                    response.append(name2);
                    response.append("E");
                }

                if (i == 10000)
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

        return String.valueOf(response);

    }



    private String sendPost1(String tmpSemester, String tmpType) {
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
        parameter += "<param id=\"strShtmCd\" type=\"STRING\">" + tmpSemester + "</param>\n";
        parameter += "<param id=\"strSubmattFg\" type=\"STRING\">" + tmpType + "</param>\n";
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


        String temp;
        int i = 0;
        StringBuilder response = new StringBuilder();

        try {
            while ((temp = br.readLine()) != null) {


                i++;

                if (temp.contains("<sbjtKorNm>")) {

                    String name = temp.split(">")[1].split("</")[0];
                    if (name.contains("&#32;"))
                    {
                        name = name.replace("&#32;", " ");
                    }
                    response.append(name);
                    response.append("E");
                }

                if (temp.contains("<sustLsnFgNm>"))
                {
                    String name2 = temp.split(">")[1].split("</")[0];
                    response.append(name2);
                    response.append("E");
                }

                if (i == 10000)
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

        return String.valueOf(response);

    }




    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mCallback.getReturn(o);

    }


}

