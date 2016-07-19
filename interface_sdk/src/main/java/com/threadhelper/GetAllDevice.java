package com.threadhelper;

/**
 * Created by best on 2016/7/14.
 */
public class GetAllDevice implements Runnable {
    private String ipaddress;
    private int port;
    public GetAllDevice(String ipaddress, int port){
        this.ipaddress = ipaddress;
        this.port = port;
    }
    @Override
    public void run() {

    }
}
