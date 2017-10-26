package kr.ac.ajou.paran.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dream on 2017-08-05.
 */

public class HTTP {

    private final static String MOBILE = "https://mb.ajou.ac.kr";
    private final static String LIBAPP = "http://libapp.ajou.ac.kr";
    private final static String HAKSA = "http://haksa.ajou.ac.kr";

    private final static String GET_PROFILE = "/mobile/M03/M03_010_010.es";
    private final static String GET_TOTAL = "/mobile/M02/M02_020.es";
    private final static String GET_GRADUATION = "/mobile/M01/M01_010.es";
    private final static String GET_A_GRADUATION = "/mobile/M01/M01_020.es";
    private final static String REQUEST_PICTURE = "/QrCodeService/GetPhotoImg.svc/GetUserPhotoAJOU?Loc=AJOU&Idno=";
    private final static String GET_PICTURE = "/KCPPhoto//PHO_";
    private final static String GET_ABEEK = "/uni/uni/abee/cmmn/findAccept.action?strStdNo=";

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

    public static boolean checkAbeek(String cookie,int number) {
        try {
            String line = null;
            BufferedReader rd = getSimple(new URL(HAKSA + GET_ABEEK+number), cookie);
            while ((line = rd.readLine()) != null) {
                if(line.indexOf("<accept>")>-1){
                    if(line.indexOf("Y")>-1)
                        return true;
                    else
                        return false;
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
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
        StringBuilder builder = new StringBuilder();
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

    public static String postTable(String server) {
        String base64="";
        String result="";
        try {
            HttpURLConnection con = makeConnection(new URL("http://"+server+"/postTable"));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            File file = new File("/storage/emulated/0/test.jpg");
            if(file.isFile()){
                byte[] bt = new byte[(int)file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                try{
                    fileInputStream.read(bt);
                    base64 = new String(Base64.encodeBase64(bt));
                }catch (Exception e){
                    e.printStackTrace();
                }
                fileInputStream.close();
            }
            wr.write("data="+base64);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                result+=line;
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
}
