package kr.ac.ajou.paran.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.google.api.client.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.ajou.paran.util.object.User;

/**
 * Created by dream on 2017-08-05.
 */

public class HTTP {

    private final static String MOBILE = "https://mb.ajou.ac.kr";
    private final static String LIBAPP = "http://libapp.ajou.ac.kr";
    private final static String HAKSA = "http://haksa.ajou.ac.kr";

    private final static String GET_PROFILE = "/mobile/M03/M03_010_010.es";
    private final static String GET_TOTAL = "/mobile/M02/M02_020.es";
    private final static String REQUEST_PICTURE = "/QrCodeService/GetPhotoImg.svc/GetUserPhotoAJOU?Loc=AJOU&Idno=";
    private final static String GET_PICTURE = "/KCPPhoto//PHO_";
    private final static String GET_LECTURE = "/uni/uni/cour/lssn/findCourLecturePlanDocumentReg.action";

    private static HttpURLConnection makeConnection(URL url) {
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
            con.setRequestProperty("Accept-Upgrade-Insecure-Requests", "1");
            return con;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private static String loginPara(String id, String pwd) {
        String parameter;
        try {
            parameter = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            parameter += "&" + URLEncoder.encode("passwd", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8");
            parameter += "&" + URLEncoder.encode("rememberMe", "UTF-8") + "=" + URLEncoder.encode("N", "UTF-8");
            parameter += "&" + URLEncoder.encode("platformType", "UTF-8") + "=" + URLEncoder.encode("A", "UTF-8");
            parameter += "&" + URLEncoder.encode("deviceToken", "UTF-8") + "=";
            return parameter;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String printSubject(String cookie) {
        String list = "";
        try {
            String line = null;
            BufferedReader rd = getSimple(new URL(MOBILE + GET_TOTAL), cookie);
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("listview") > -1) {
                    do {
                        while ((line = rd.readLine()).indexOf("href") < 0)
                            ;
                        if (line.indexOf("M02_020_010.es") < 0)
                            break;
                        line = line.substring(line.indexOf("\"") + 1, line.indexOf("\">"));
                        list += inspectSubject(line, cookie);
                    } while (true);
                    break;
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!list.isEmpty())
            list= list.substring(0,list.length()-1);
        return list;
    }

    private static String inspectSubject(String url, String cookie) {
        String list = "";
        try {
            String line = null;
            String subject;
            String type;
            String rate;
            BufferedReader rd = getSimple(new URL(MOBILE + url), cookie);
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("학기") > -1) {
                    do {
                        while ((line = rd.readLine()).indexOf("<h3>") < 0)
                            ;
                        subject=line.substring(line.indexOf("<h3>") + 4, line.indexOf("</h3>"))+"\n";
                        while ((line = rd.readLine()).indexOf("구분") < 0)
                            ;
                        line = rd.readLine();
                        type = line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>"));
                        while ((line = rd.readLine()).indexOf("등급") < 0)
                            ;
                        line = rd.readLine();
                        rate = line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>"));
                        if (rate.equals("C+") || rate.equals("C0") || rate.equals("D+") || rate.equals("D0")
                                || rate.equals("F"))
                            list+="\tO\t\t"+type+"\t\t"+subject;
                        else
                            list+="\tX\t\t"+type+"\t\t"+subject;
                        while ((line = rd.readLine()).indexOf("<div") < 0)
                            ;
                        if (line.indexOf("collapsible") < 0)
                            break;
                    } while (true);
                    break;
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.lang.NullPointerException e) {
        }
        return list;
    }

    private static String postLogin(URL url, String id, String pwd) {
        try {
            HttpURLConnection con = makeConnection(url);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(loginPara(id, pwd));
            wr.flush();

            String cookie = con.getHeaderFields().get("Set-Cookie").toString();
            if(cookie.indexOf("JSESS")<0)
                return null;
            cookie = cookie.substring(cookie.indexOf("JSESS"));
            cookie = cookie.substring(0, cookie.indexOf(";"));
            con.disconnect();
            return cookie;
        } catch (IOException | IndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static String loginAjou(String id, String pwd) {
        try {
            String cookie = postLogin(new URL(MOBILE + "/mobile/login.json"), id, pwd);

            return cookie;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap printPicture(int number, int size) {
        try {
            getSimple(new URL(LIBAPP+REQUEST_PICTURE+number),"");
            Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(makeConnection(new URL(LIBAPP+GET_PICTURE+number+".jpg")).getInputStream()));
            return Bitmap.createScaledBitmap(bitmap, size,size, true);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedReader getSimple(URL url, String cookie) {
        try {
            HttpURLConnection con = makeConnection(url);
            con.setDoOutput(false);

            if(cookie.equals("") == false)
               con.setRequestProperty("Cookie", cookie);
            con.setRequestMethod("GET");

            return new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedReader getXML(URL url, String cookie) {
        try {
            HttpURLConnection con = makeConnection(url);
            con.setDoOutput(false);

            if(cookie.equals("") == false) {
                con.setRequestProperty("Cookie", cookie);
                con.setRequestProperty("Content-Type", "text/xml/SosFlexMobile;charset=utf-8");
            }
            con.setRequestMethod("GET");

            return new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static User printUser(String cookie) {
        User user = new User();
        try {
            String line = null;
            Pattern pattern;
            Matcher matcher;
            BufferedReader rd = getSimple(new URL(MOBILE + GET_PROFILE), cookie);
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("성명") > -1) {
                    line = rd.readLine();
                    user.setName(line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")));
                } else if (line.indexOf("학번") > -1) {
                    line = rd.readLine();
                    pattern = Pattern.compile("[0-9]+");
                    matcher = pattern.matcher(line);
                    if (matcher.find())
                        user.setNumber(Integer.parseInt(matcher.group(0)));
                } else if (line.indexOf("가진급학년") > -1) {
                    line = rd.readLine();
                    line = line.substring(line.indexOf("/") + 1);
                    pattern = Pattern.compile("[1-9]");
                    matcher = pattern.matcher(line);
                    if (matcher.find())
                        user.setGrade(matcher.group(0));
                } else if (line.indexOf("입학구분") > -1) {
                    if (rd.readLine().indexOf("편입학") > -1)
                        user.setNewORtrans(false);
                    else
                        user.setNewORtrans(true);
                } else if (line.indexOf("대학") > -1) {
                    line = rd.readLine();
                   user.setCampus(line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")));
                } else if (line.indexOf("학부") > -1) {
                    line = rd.readLine();
                    user.setDepartment(line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")));
                } else if (line.indexOf("전공") > -1) {
                    line = rd.readLine();
                    user.setMajor(line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")));
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<String> printLecture(int mode, String code) {
        ArrayList<String> subjects = new ArrayList<String>();

        try {
            String line = null;
            HttpURLConnection con = makeConnection(new URL(HAKSA+GET_LECTURE));
            con.setRequestProperty("Content-Type", "text/xml/SosFlexMobile;charset=utf-8");
            con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.6,en;q=0.4");
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            String param = "";
            switch(mode){
                case 0:
                    break;
                case 1:
                    param+="<param id=\"strYy\" type=\"STRING\">2017</param>\n";
                    param+="<param id=\"strShtmCd\" type=\"STRING\">U0002003</param>\n";
                    param+="<param id=\"strSubmattFg\" type=\"STRING\">U0209001</param>\n";
                    param+="<param id=\"strMjCd\" type=\"STRING\">"+code+"</param>\n";
                    break;
                case 2:
                    param+="<param id=\"strYy\" type=\"STRING\">2017</param>\n";
                    param+="<param id=\"strShtmCd\" type=\"STRING\">U0002003</param>\n";
                param+="<param id=\"strSubmattFg\" type=\"STRING\">U0209002</param>\n";
                break;
                case 3:
                    param+="<param id=\"strYy\" type=\"STRING\">2017</param>\n";
                    param+="<param id=\"strShtmCd\" type=\"STRING\">U0002003</param>\n";
                    param+="<param id=\"strSubmattFg\" type=\"STRING\">U0209003</param>\n";
                    param+="<param id=\"strSustcd\" type=\"STRING\">"+code+"</param>\n";
                    break;
                case 4:
                    param+="<param id=\"strYy\" type=\"STRING\">2017</param>\n";
                    param+="<param id=\"strShtmCd\" type=\"STRING\">U0002003</param>\n";
                    param+="<param id=\"strSubmattFg\" type=\"STRING\">U0209005</param>\n";
                    param+="<param id=\"strSubmattFldFg\" type=\"STRING\">"+code+"</param>\n";
                    break;
            }
            wr.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<root>\n<params>\n"+param+"</params>\n</root>");
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("<sbjtKorNm>") > -1) {
                    line = line.substring(line.indexOf("<sbjtKorNm>")+11,line.indexOf("</sbjtKorNm>"));
                    line = line.replaceAll("&#32;", " ");
                    if(subjects.contains(line) == false)
                        subjects.add(line);
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return subjects;
    }

    public static ArrayList<String> printBefore(int abeek, int year){
        ArrayList<String> majors = new ArrayList<>();

        String line = "";
        try {
            HttpURLConnection con = makeConnection(new URL("http://haksa.ajou.ac.kr/com/com/cmmn/code/findDeptList3.action"));
            con.setRequestProperty("Content-Type", "text/xml/SosFlexMobile;charset=utf-8");
            con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.6,en;q=0.4");
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            String params;
            params="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
            params+="<root>\n";
            params+="<params>\n";
            params+="<param id=\"strDataSet\" type=\"STRING\">DS_MJ_CD_SH</param>\n";
            params+="<param id=\"strUserNo\" type=\"STRING\">000000000</param>\n";
            params+="<param id=\"strDeptUseFg\" type=\"STRING\">C0040002</param>\n";
            params+="<param id=\"strMngtYn1\" type=\"STRING\">1</param>\n";
            params+="<param id=\"strMngtYn2\" type=\"STRING\">0</param>\n";
            params+="<param id=\"strModeFg\" type=\"STRING\">S</param>\n";
            params+="<param id=\"strYy\" type=\"STRING\">"+year+"</param>\n";
            params+="<param id=\"strPosiGrpCd\" type=\"STRING\">31</param>\n";
            params+="<param id=\"strUpDeptCd\" type=\"STRING\"></param>\n";
            params+="<param id=\"strUseFg\" type=\"STRING\">1</param>\n";
            params+="<param id=\"strCampCd\" type=\"STRING\">S</param>\n";
            params+="<param id=\"strUserDeptCd\" type=\"STRING\">0000000000</param>\n";
            params+="<param id=\"strFg\" type=\"STRING\">"+abeek+"</param>\n";
            params+="<param id=\"AUDIT9_ID\" type=\"STRING\"></param>\n";
            params+="</params>\n";
            params+="</root>\n";
            wr.write(params);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null){
                if(line.indexOf("<useFg/>") > -1)
                    break;
            }
            String str="";
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("<deptCd>") > -1) {
                    str = line.substring(line.indexOf("<deptCd>")+8,line.indexOf("</deptCd>"));
                }else if(line.indexOf("<deptKorNm>") > -1){
                    line = line.substring(line.indexOf("<deptKorNm>")+11,line.indexOf("</deptKorNm>"));
                    line = line.replaceAll("&#32;", " ");
                    majors.add(line+"/"+str);
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return majors;
    }

    public static void postSubject(String server, String data, int number) {
        try {
            HttpURLConnection con = makeConnection(new URL("http://"+server+"/postSubject"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write("data="+data+"&number="+number);
            wr.flush();
            new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void updateTable(String server, String data, int number) {
        try {
            HttpURLConnection con = makeConnection(new URL("http://"+server+"/updateTable"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write("data="+data+"&number="+number);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void postUser(String server, String data) {
        try {
            HttpURLConnection con = makeConnection(new URL("http://"+server+"/postUser"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write("data="+data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean checkTable(String server, int number) {
        try {
            HttpURLConnection con = makeConnection(new URL("http://"+server+"/checkTable"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write("number="+number);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            if(Integer.parseInt(rd.readLine())==0)
                return false;
            else
                return true;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static String postTable(String server, int number) {
        String base64 = "";
        String result = "";
        try {
            HttpURLConnection con = makeConnection(new URL("http://" + server + "/postTable"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            File file = new File("/storage/emulated/0/test.jpg");
            if (file.isFile()) {
                byte[] bt = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    fileInputStream.read(bt);
                    base64 = new String(Base64.encodeBase64(bitmapToByte(rotate(byteToBitmap(bt)))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fileInputStream.close();
            }
            wr.write("data=" + base64 + "&number=" + number);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private static byte[] bitmapToByte(Bitmap rotate) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        rotate.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        return stream.toByteArray();
    }

    /* reference : http://thegreedyman.tistory.com/13 */
    private static Bitmap rotate(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();

        return resizedBitmap;
    }

    private static Bitmap byteToBitmap(byte[] bt) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
        return bitmap;
    }
    /* reference : http://thegreedyman.tistory.com/13 */

    public static String getTable(String server, int number) {
        try {
            HttpURLConnection con = makeConnection(new URL("http://"+server+"/getTable"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write("number="+number);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            return rd.readLine();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void postConstraint(String server, int number, ArrayList<Integer> weeks, ArrayList<Integer> times, int score, ArrayList<String> res, ArrayList<String> includes, ArrayList<String> excludes) {
        try {
            HttpURLConnection con = makeConnection(new URL("http://"+server+"/postConstraint"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            String data="";
            data+="number="+number;
            data+="&week=";
            if(weeks != null){
                String str="";
                for(Integer week : weeks)
                    str+=week.toString()+"/";
                if(str.length()>0)
                    data+=str.substring(0,str.length()-1);
            }
            data+="&time=";
            if(times != null){
                String str="";
                for(Integer time : times)
                    str+=time.toString()+"/";
                if(str.length()>0)
                    data+=str.substring(0,str.length()-1);
            }
            data+="&score="+score;
            data+="&re=";
            if(res != null){
                String str="";
                for(String re : res)
                    str+=re+"/";
                if(str.length()>0)
                    data+=str.substring(0,str.length()-1);
            }
            data+="&include=";
            if(includes != null){
                String str="";
                for(String include : includes)
                    str+=include+"/";
                if(str.length()>0)
                    data+=str.substring(0,str.length()-1);
            }
            data+="&exclude=";
            if(res != null){
                String str="";
                for(String exclude : excludes)
                    str+=exclude+"/";
                if(str.length()>0)
                    data+=str.substring(0,str.length()-1);
            }
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
