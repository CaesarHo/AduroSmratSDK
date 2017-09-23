package com.adurosmart.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by best on 2016/11/9.
 */

public class CompatSeekBar extends SeekBar {
    public CompatSeekBar(Context context) {
        super(context);
    }


    public CompatSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TGA","Seekbar: dispatchTouchEvent");
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        Log.e("TGA","Seekbar: onTouchEvent");
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                Log.e("TGAA","Seekbar: true");
                EventBus.getDefault().post(true);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TGAA","Seekbar: false");
                EventBus.getDefault().post(false);
                break;
        }
        return super.onTouchEvent(ev);
    }


}
