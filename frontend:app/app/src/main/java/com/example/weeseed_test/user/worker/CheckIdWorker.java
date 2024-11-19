package com.example.weeseed_test.user.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckIdWorker extends Worker {
    private CheckIdResultListener listener;

    public CheckIdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        try{
            //step1) 전송 데이터 세팅
            String id = getInputData().getString("id");
            int isPatho = getInputData().getInt("isPatho", 1);

            //step2) 서버로 전송
            checkIdToServer(id, isPatho);

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }


    private void checkIdToServer(String id, int isPatho) {
        try {

            //default: 1 nok.
            URL url = new URL("http://192.168.0.5:8080/checkId");


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String jsonInputString = "{\"id\": \"" + id + "\"}";

            Log.i("checkIdToServer", "4: ");


            //......서버컴퓨터가 켜져있어야함.....
            try (OutputStream os = conn.getOutputStream()) {
                Log.i("checkIdToServer", "4.1: ");
                byte[] input = jsonInputString.getBytes("utf-8");
                Log.i("checkIdToServer", "4.2: ");
                os.write(input, 0, input.length);
                Log.i("checkIdToServer", "4.3: ");
            }
            catch (IOException e) {
                Log.i("checkIdToServer", "4.4: ");
            }
            catch (Exception e) {
                Log.i("checkIdToServer", "4.6: ");
            }

            Log.i("checkIdToServer", "5: ");


            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;

            Log.i("checkIdToServer", "6: ");


            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
            in.close();

            Log.i("checkIdToServer", "7: ");
            
            //서버로부터 response처리
            String responseMessage = response.toString();
            if (responseMessage.equals("yes")) {
                //ID 사용 가능;
                listener.oncheckIdSuccess();
            }

            Log.i("checkIdToServer", "8: ");


            int code = conn.getResponseCode();
            if (code == 200) {
                //ID 사용 가능;
                listener.oncheckIdFailure(responseMessage);
            } else {
                //ID 사용 불가
                listener.oncheckIdFailure(responseMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("checkIdToServer", "Error occurred: " + e.getMessage());
            //에러: ID check
            listener.oncheckIdError("에러: ID 체크"+ e.getMessage());
        }
    }


}
