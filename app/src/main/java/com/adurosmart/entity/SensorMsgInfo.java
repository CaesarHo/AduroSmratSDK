package com.adurosmart.entity;

/**
 * Created by best on 2016/10/8.
 */

public class SensorMsgInfo {

    public String device_mac;
    public String time;
    public int state = -1;
    public SensorMsgInfo(){
        this.device_mac = "";
        this.time = "";
        this.state = -1;
    }

}
