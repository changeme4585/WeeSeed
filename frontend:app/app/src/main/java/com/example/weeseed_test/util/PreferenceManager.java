package com.example.weeseed_test.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.weeseed_test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*여기저기서 쓰이는 메소드들*/
public class PreferenceManager {

    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;

    //sharedpref setter

    private static SharedPreferences getpreferences(Context context){
        return context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String key, String value){
        SharedPreferences prefs = getpreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setBoolean(Context context, String key, Boolean value){
        SharedPreferences prefs = getpreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setInt(Context context, String key, int value){
        SharedPreferences prefs = getpreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setStringList(Context context, String key, List<String> value){
        String joined = String.join("::", value);

        SharedPreferences prefs = getpreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, joined);
        editor.apply();
    }


    ///getter
    public static Boolean getBoolean(Context context, String key){
        SharedPreferences prefs = getpreferences(context);
        Boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);
        return value;
    }
    public static String getString(Context context, String key){
        SharedPreferences prefs = getpreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }

    public static List<String> getStringList(Context context, String key){
        SharedPreferences prefs = getpreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);

        if (value == null || value.isEmpty()) {
            return new ArrayList<>();  // 비어 있는 경우 빈 리스트 반환
        }

        List<String> result = Arrays.asList(value.split("::"));

        return result;
    }

    //세션체크
//    public static void checkSession(Context context, String sessionId, String baseUrl) {
//        //baseUrl엔 viewmodel.getServerADDR() 줄 것
//        try {
//            Log.e("checkSession","cs1: ");
//            URL url = new URL(baseUrl+"/checkSession"); // 서버 URL
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            if (sessionId != null) {
//                conn.setRequestProperty("Cookie", sessionId);
//            }
//            Log.e("checkSession","cs2: ");
//            int code = conn.getResponseCode();
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//            Log.e("checkSession","cs3: ");
//
//            if (code == 200) {
//                Log.e("checkSession","cs4: ");
//            } else {
//                Log.e("checkSession","cs5: ");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("checkSessionERR","세션 에러: "+e.getMessage());
////            Toast.makeText(context, "세션 에러: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

}
