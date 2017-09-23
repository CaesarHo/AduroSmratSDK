package com.adurosmart.util;

/**
 * Created by best on 2016/9/2.
 */
public class DelayThread extends Thread {
    int delayTime;
    OnRunListener onRunListener;

    public DelayThread(int delayTime, OnRunListener onRunListener) {
        this.delayTime = delayTime;
        this.onRunListener = onRunListener;
    }

    public void run() {
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onRunListener.run();
    }

    public interface OnRunListener {
        void run();
    }
}
