package com.adurosmart.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.adurosmart.util.ScreenUtils;


/**
 * Created by best on 2016/11/9.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void initView();
    /**
     * 修改了statusbar的颜色。
     * 可是在子类重写修改颜色，我考虑了写成工具类，但是还是觉得没必要，多此一举。
     * @param color
     */
    protected void initStatusBar(int color) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,ScreenUtils.getStatusHeight(this));
        params.gravity = Gravity.TOP;

        View view = new View(this);
        view.setBackgroundColor(color);
        view.setLayoutParams(params);

        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        decorView.addView(view);
    }
}
