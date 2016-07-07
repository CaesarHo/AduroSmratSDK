package com.adurosmart.mqtt;

/**
 * Created by best on 2016/7/1.
 */
public class MqttSendData {
    public static final String mTopic = "200004401331";

    private static MqttSendData mSendData = null;
    private MqttSendData(){}

    public synchronized static MqttSendData getInstance(){
        if(null==mSendData){
            synchronized(MqttSendData.class){
                if(null==mSendData){
                    mSendData = new MqttSendData();
                }
            }
        }
        return mSendData;
    }


}
