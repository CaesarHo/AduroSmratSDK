package com.adurosmart.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by best on 2016/11/9.
 */

public class CompatViewpager extends ViewPager {

    private Boolean isSeekMode = false;
    private Boolean isViewpagerMode = false;

    public CompatViewpager(Context context) {
        super(context);
        EventBus.getDefault().register(this);
    }


    public CompatViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e("TGA","viewpager: dispatchTouchEvent" + "调度：" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e("TGA","viewpager: onInterceptTouchEvent" + "拦截：" + ev.getAction());
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                int count = getChildCount();
//                for(int i=0;i<count;i++){
//                    View view = getChildAt(i);
//                    if(view instanceof CompatSeekbar){
//                        Log.e("TGA","viewpager: onInterceptTouchEvent" + "拦截：" + ev.getAction()+"捕获Seekbar，继续分发");
//                        return false;
//                    }
//                }
//                break;
//        }

        if (isSeekMode) {
//            Log.e("TGA","viewpager: onInterceptTouchEvent" + "拦截：" + ev.getAction() + "false");
            return false;

        } else {
            return super.onInterceptTouchEvent(ev);
        }

//        return false;
//        return super.onInterceptTouchEvent(ev);
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGrounThread(Boolean isSeekBar) {
        isSeekMode = isSeekBar;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

//        Log.e("TGA","viewpager: onTouchEvent" + "触控：" + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
//                Log.e("TGAA","Viewpager: true");
                isViewpagerMode = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                Log.e("TGAA","Viewpager: false");
                isViewpagerMode = false;
                break;
        }
        return super.onTouchEvent(ev);
    }
}
