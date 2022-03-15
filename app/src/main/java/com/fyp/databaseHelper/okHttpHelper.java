package com.fyp.databaseHelper;

import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okHttpHelper {

    public void UploadFile(String path){
        OkHttpClient httpClient = new OkHttpClient();

        MediaType contentType = MediaType.parse("text/plain");
        File file = new File(path);
        RequestBody body = RequestBody.create(file, contentType);

        Request getRequest = new Request.Builder()
                .url("http://118.31.20.251:8080/newServlet/upload")
                .post(body)
                .build();

        Call call = httpClient.newCall(getRequest);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("http", "http failure" + e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e("http", "okHttpPost enqueue: \n onResponse:"+ response.toString() +"\n body:" +response.body().string());
            }
        });

    }

    public void downloadFile(){

        //create httpclient
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://118.31.20.251:8080/newServlet/download")
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("http", "http failure" + e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //TODO
                
            }
        });
    }
}
