package kr.ac.ajou.paran.util;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import kr.ac.ajou.paran.R;

/**
 * Created by dream on 2017-10-17.
 */

public class Raw {

    public static String readIP(Activity activity){
        String ip="";
        try{
            InputStream in = activity.getResources().openRawResource(R.raw.ip);

            if(in != null){
                InputStreamReader stream = new InputStreamReader(in, "utf-8");
                BufferedReader buffer = new BufferedReader(stream);
                ip=buffer.readLine();
                in.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ip;
    }

    public static String readPort(Activity activity){
        String port="";
        try{
            InputStream in = activity.getResources().openRawResource(R.raw.port);

            if(in != null){
                InputStreamReader stream = new InputStreamReader(in, "utf-8");
                BufferedReader buffer = new BufferedReader(stream);
                port=buffer.readLine();
                in.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return port;
    }
}
