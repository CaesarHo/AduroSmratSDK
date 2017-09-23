package com.okhttp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.adurosmart.sdk.R;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkhttpActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    private Context context;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i("wocao = ","onSaveInstanceState");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.i("wocao = ","onNewIntent");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i("wocao = ","onCreate");
        context = OkhttpActivity.this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                new Thread(new getDevice()).start();
//                Intent intent =  new Intent(context, TabActivity.class);
//                startActivity(intent);
            }
        });
    }

    public String getURI(String url)throws  IOException{
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    public class getDevice implements Runnable {
        String uri1 = "https://data.adurosmart.com/adurosmart/index.php/Sensor/smartctrl/readone";
        String uri2 = "https://data.adurosmart.com/adurosmart/index.php/User/login/loginMail";
        @Override
        public void run() {
            try {
                String response = getURI(uri2);
                System.out.println("wocao = " + response);

                String json = bowlingJson("Jesse", "Jake");
                String responseTopost = post("http://www.roundsapp.com/post", json);
                System.out.println("wocao = " + responseTopost);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("wocao = ","");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("wocao = ","onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("wocao = ","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("wocao = ","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("wocao = ","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("wocao = ","onStart");
    }
}
