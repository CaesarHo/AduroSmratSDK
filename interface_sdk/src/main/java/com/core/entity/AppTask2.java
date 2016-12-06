package com.core.entity;

import java.io.Serializable;

/**
 * Created by best on 2016/12/6.
 */

public class AppTask2 implements Serializable{
    private int task_no;
    private String task_name;
    private int isEnabled;
    private int task_type;
    private int task_cycle;
    private int task_hour;
    private int task_minute;
    private int task_status;
    private String sensor_mac;
    private String sensor_short;
    private int task_scene_id = -1;
    private int task_group_id = -1;

    public int getTask_no() {
        return task_no;
    }

    public void setTask_no(int task_no) {
        this.task_no = task_no;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public int getTask_cycle() {
        return task_cycle;
    }

    public void setTask_cycle(int task_cycle) {
        this.task_cycle = task_cycle;
    }

    public int getTask_hour() {
        return task_hour;
    }

    public void setTask_hour(int task_hour) {
        this.task_hour = task_hour;
    }

    public int getTask_minute() {
        return task_minute;
    }

    public void setTask_minute(int task_minute) {
        this.task_minute = task_minute;
    }

    public int getTask_status() {
        return task_status;
    }

    public void setTask_status(int task_status) {
        this.task_status = task_status;
    }

    public String getSensor_short() {
        return sensor_short;
    }

    public void setSensor_short(String sensor_short) {
        this.sensor_short = sensor_short;
    }

    public String getSensor_mac() {
        return sensor_mac;
    }

    public void setSensor_mac(String sensor_mac) {
        this.sensor_mac = sensor_mac;
    }

    public int getTask_scene_id() {
        return task_scene_id;
    }

    public void setTask_scene_id(int task_scene_id) {
        this.task_scene_id = task_scene_id;
    }

    public int getTask_group_id() {
        return task_group_id;
    }

    public void setTask_group_id(int task_group_id) {
        this.task_group_id = task_group_id;
    }
}
