//package com.example.weeseed_test.user.worker;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.example.weeseed_test.util.UtilClass;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.SocketException;
//import java.net.URL;
//
////회원가입 worker
//public class SignInWorker extends Worker {
//
//    private SignInResultListener listener;
//
//    public SignInWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        try{
//            Log.i("SignInWorker", "2: dowork 진입");
//
//            //step1) 전송 데이터 세팅
//            String id = getInputData().getString("id");
//            String pw = getInputData().getString("pw");
//            String email = getInputData().getString("email");
//            String name = getInputData().getString("name");
//            String org = getInputData().getString("org");
//            int isPatho = getInputData().getInt("isPatho", 1);
//
//            Log.i("SignInWorker", "3: 전송데이터 세팅 완");
//
//            //step2) 서버로 전송ㄱ
//            signInToServer(id, pw, email, name, org, isPatho);
//
//            Log.i("SignInWorker", "4: 메소드 호출 성공");
//
//            return Result.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.failure();
//        }
//    }
//
//    private void signInToServer(String id, String pw, String email, String name, String org, int isPatho) {
//        try {
//
//            Log.i("signInToServer", "1: ");
//
//            //default: 1 nok.
//            URL url = new URL("http://192.168.0.5:8080/nokSignIn");
//
//            //case: 2 patho
//            if (isPatho == 2)
//                url = new URL("http://192.168.0.5:8080/pathSignIn");
//
//            Log.i("signInToServer", "2: ");
//
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setDoOutput(true);
//
//            Log.i("signInToServer", "3: ");
//
//
//            //default: 1 nok.
//            String jsonInputString = "{\"nokId\": \"" + id + "\", " +
//                    "\"password\": \"" + pw + "\", " +
//                    "\"email\": \"" + email + "\", " +
//                    "\"name\": \"" + name + "\"}";
//
//            //case: 2 path
//            if (isPatho == 2)
//                jsonInputString = "{\"pathologistId\": \"" + id + "\", " +
//                        "\"password\": \"" + pw + "\", " +
//                        "\"email\": \"" + email + "\", " +
//                        "\"name\": \"" + name + "\", " +
//                        "\"organizationName\": \"" + org + "\"}";
//
//            Log.i("signInToServer", "4: ");
//
//
//            //......서버컴퓨터가 켜져있어야함.....
//            try (OutputStream os = conn.getOutputStream()) {
//                Log.i("signInToServer", "4.1: ");
//                byte[] input = jsonInputString.getBytes("utf-8");
//                Log.i("signInToServer", "4.2: ");
//                os.write(input, 0, input.length);
//                Log.i("signInToServer", "4.3: ");
//            }
//            catch (IOException e) {
//                Log.i("signInToServer", "4.4: ");
//            }
//            catch (Exception e) {
//                Log.i("signInToServer", "4.6: ");
//            }
//
//            Log.i("signInToServer", "5: ");
//
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
//            StringBuilder response = new StringBuilder();
//            String responseLine;
//
//            Log.i("signInToServer", "6: ");
//
//
//            while ((responseLine = in.readLine()) != null) {
//                response.append(responseLine.trim());
//            }
//            in.close();
//
//            Log.i("signInToServer", "7: ");
//
//
//            String responseMessage = response.toString();
//            if (responseMessage.equals("yes")) {
//                // 회원가입 성공 처리
//                listener.onSignInSuccess();
//            }
//
//            Log.i("signInToServer", "8: ");
//
//
//            int code = conn.getResponseCode();
//            if (code == 200) {
//                //실패 case: response 표시 (ID/PW 문제)
//                listener.onSignInFailure(responseMessage);
//            } else {
//                //전송실패
//                listener.onSignInFailure("전송에 실패했습니다.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            listener.onSignInError("에러: 회원가입"+ e.getMessage());
//        }
//    }
//}
