package com.jpardogo.android.listbuddies.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.jpardogo.android.listbuddies.R;
import com.jpardogo.android.listbuddies.Utils.sharepreference.MySharePreference;
import com.jpardogo.android.listbuddies.models.ImageBean;
import com.jpardogo.android.listbuddies.models.ImagesResponse;
import com.jpardogo.android.listbuddies.models.RetrofitAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends Activity {

    public final static String LEFT_IMAGES = "leftImages";
    public final static String RIGHT_IMAGES = "rightImages";
    private boolean isImageDone,isTimeDone;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getImages();
        waitTime();
    }

    private void waitTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);//2s
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    isTimeDone = true;
                    jumpActivity();
                }
            }
        }).start();
    }

    private void getImages() {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        RetrofitAPI retrofitApi = retrofit.create(RetrofitAPI.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<ImagesResponse> call= retrofitApi.images();

        // Fetch and print a list of the contributors to the library.
        call.enqueue(new Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                if(response.body()!=null){
                    Log.e("urls","left:"+json2String(LEFT_IMAGES,response.body().getLeftImages()));
                    Log.e("urls","right:"+json2String(RIGHT_IMAGES,response.body().getLeftImages()));
                    MySharePreference.newInstance(getApplicationContext()).setImages(LEFT_IMAGES,response.body().getLeftImages());
                    MySharePreference.newInstance(getApplicationContext()).setImages(RIGHT_IMAGES,response.body().getRightImages());
                }
                isImageDone = true;
                jumpActivity();
            }

            @Override
            public void onFailure(Call<ImagesResponse> call, Throwable t) {
                t.printStackTrace();
                showDialog();
            }
        });
    }


    private void jumpActivity(){
        if(isTimeDone && isImageDone){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    private void showDialog(){
        dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                .setMessage("网络错误,退出 or 继续")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isImageDone=true;
                        jumpActivity();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(dialog!= null && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }

    private String json2String(String tag,List<ImageBean> images){
        int len = images.size();
        JSONObject jsonObject = new JSONObject();
        JSONArray imagesJson = new JSONArray();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("url", images.get(i).getUrl());
                json.put("comment", images.get(i).getComment());
                imagesJson.put(i,json);
            }
            jsonObject.put(tag, imagesJson);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
