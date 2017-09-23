package com.adurosmart.global;

import com.core.entity.AppDevice;
import com.core.entity.AppGroup;
import com.core.entity.AppScene;
import com.core.entity.AppTask2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by best on 2016/7/14.
 */
public class Constants {

    public static String Sensor_Mac = "";
    public static boolean isTask_Mac = false;

    public static String device_mac = "";
    public static String device_short = "";
    public static String device_mainpoint = "";

    public static short sceneId = 0;
    public static short scene_group_id = 0;

    public static int level = 0;
    public static int hue = 0;
    public static int sat = 0;
    public static int isSwitch = -1;
    public static int temp = 0;


    public static List<AppDevice> appDeviceList = new ArrayList<>();
    public static List<AppGroup> appGroupList = new ArrayList<>();
    public static List<AppScene> appSceneList = new ArrayList<>();
    public static List<AppTask2> appTaskList = new ArrayList<>();

    public static List<AppDevice> groupDeviceList = new ArrayList<>();

}
