package com.core.entity;

import java.io.Serializable;

/**
 * Created by oaosj on 2016/9/18 0018.
 */
public class AppTask implements Serializable {
    private static final long serialVersionUID = 1L;
    private int task_no;
    private String task_name;
    private int isEnabled;
    private int task_type;
    private int task_cycle;
    private int task_hour;
    private int task_minute;
    private int task_scensor;
    private String sensor_mac;

    private String device_mac;
    private int cmd_size;



    private String is_device_switch_type;
    private String is_device_level_type;
    private String is_device_hue_sat_type;
    private String is_device_temp_type;
    private String is_scene_type;

    private int device_State; // 设备状态（灯:开、关)0 开 1 关
    private int device_level = -1;  //设备亮度:
    private int device_temp = -1;   //色温
    private int device_colorSat = -1;
    private int device_colorHue = -1;

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

    public int getTask_action() {
        return task_scensor;
    }

    public void setTask_action(int task_scensor) {
        this.task_scensor = task_scensor;
    }

    public String getSensor_mac() {
        return sensor_mac;
    }

    public void setSensor_mac(String sensor) {
        this.sensor_mac = sensor;
    }

    public String getDevice_mac() {
        return device_mac;
    }

    public void setDevice_mac(String device_mac) {
        this.device_mac = device_mac;
    }

    public int getCmd_size() {
        return cmd_size;
    }

    public void setCmd_size(int cmd_size) {
        this.cmd_size = cmd_size;
    }




    public String getIs_device_switch() {
        return is_device_switch_type;
    }

    public void setIs_device_switch(String type){
        this.is_device_switch_type = type;
    }

    public String getIs_device_level(){
        return is_device_level_type;
    }

    public void setIs_device_level(String type){
        this.is_device_level_type = type;
    }

    public String getIs_device_hue(){
        return is_device_hue_sat_type;
    }

    public void setIs_device_hue(String type){
        this.is_device_hue_sat_type = type;
    }

    public String getIs_device_temp(){
        return is_device_temp_type;
    }

    public void setIs_device_temp(String type){
        this.is_device_temp_type = type;
    }

    public String getIs_scene_type(){
        return is_scene_type;
    }

    public void setIs_scene_type(String type){
        this.is_scene_type = type;
    }




    public int getDevice_State(){
        return device_State;
    }

    public void setDevice_State(int state){
        this.device_State = state;
    }

    public int getDevice_level(){
        return device_level;
    }

    public void setDevice_level(int level){
        this.device_level = level;
    }

    public int getDevice_temp(){
        return device_temp;
    }

    public void setDevice_temp(int temp){
        this.device_temp = temp;
    }

    public int getDevice_colorSat(){
        return device_colorSat;
    }

    public void setDevice_colorSat(int sat){
        this.device_colorSat = sat;
    }

    public int getDevice_colorHue(){
        return device_colorHue;
    }

    public void setDevice_colorHue(int hue){
        this.device_colorHue = hue;
    }

    public int getTask_scene_id(){
        return task_scene_id;
    }

    public void setTask_scene_id(int scene_id){
        this.task_scene_id = scene_id;
    }

    public int getTask_group_id(){
        return task_group_id;
    }

    public void setTask_group_id(int group_id){
        this.task_group_id = group_id;
    }
}
