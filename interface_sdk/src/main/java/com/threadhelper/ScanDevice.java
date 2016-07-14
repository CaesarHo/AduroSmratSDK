package com.threadhelper;

/**
 * Created by best on 2016/7/14.
 */
public class ScanDevice implements Runnable {
    private String ipaddress;
    private int port;
    public ScanDevice(String ipaddress,int port){
        this.ipaddress = ipaddress;
        this.port = port;
    }
    @Override
    public void run() {

    }
}
