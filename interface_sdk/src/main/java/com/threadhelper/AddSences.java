package com.threadhelper;

/**
 * Created by best on 2016/7/14.
 */
public class AddSences implements Runnable {
    private String ipaddress;
    private int port = -1;
    private int sencesid = -1;
    private String sencesNmae;
    private String sencesIconPath;
    public AddSences(String ipaddress,int port,int sencesid,String sencesName,String sencesIconPath){
        this.ipaddress = ipaddress;
        this.port = port;
        this.sencesid = sencesid;
        this.sencesNmae = sencesName;
        this.sencesIconPath = sencesIconPath;
    }
    @Override
    public void run() {

    }
}
