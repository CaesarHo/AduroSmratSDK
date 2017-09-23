package com.adurosmart.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by best on 2016/7/8.
 */
public abstract class TraceableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate " , this + ": " + savedInstanceState);
        super.onCreate(savedInstanceState);
//        ActiveActivitiesTracker.activityCreated();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult " , this + ": " + requestCode + ", " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        Log.d("onStart " , this + "onStart()");
        super.onStart();
//        ActiveActivitiesTracker.activityStarted(this.getApplicationContext());
    }

    @Override
    protected void onRestart() {
        Log.d("onRestart " , this + "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d("onResume " , this + "onResume()");
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        Log.d("onPostResume " , this + "onPostResume");
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        Log.d("onPause " , this + "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("onStop " , this + "onStop()");
        super.onStop();
//        ActiveActivitiesTracker.activityStopped(this.getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        Log.d("onDestroy " , this + "onDestroy()");
        Intent intent = new Intent();
        intent.setAction("main_finish");
        getApplicationContext().sendBroadcast(intent);
        super.onDestroy();
//        ActiveActivitiesTracker.activityDestroyed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("onSaveInstanceState " , this + ": " + outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("onRestoreInstanceState " , this + ": " + savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
}