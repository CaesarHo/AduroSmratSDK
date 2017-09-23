package com.adurosmart.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import com.adurosmart.sdk.R;

/**
 * Created by best on 2016/11/1.
 */

public class SplashActivity extends BaseActivity{

    private final int DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skip();
            }
        }, DURATION);
    }

    @Override
    public void initView() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            skip();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void skip() {

        if (isFinishing()) {
            return;
        }

        Intent intent = new Intent(this, SectionActivity.class);
        startActivity(intent);
        finish();
    }
}
