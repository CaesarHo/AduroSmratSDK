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

    private String serial_type1;
    private int action_state1;
    private int action_state6;
    private String serial_type2;
    private int action_state2;
    private int action_state7;
    private String serial_type3;
    private int action_state3;
    private int action_state8;
    private String serial_type4;
    private int action_state4;
    private int action_state9;
    private String serial_type5;
    private int action_state5;
    private int action_state10;


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

























    public String getSerial_type1() {
        return serial_type1;
    }

    public void setSerial_type1(String serial_type1) {
        this.serial_type1 = serial_type1;
    }

    public int getAction_state1() {
        return action_state1;
    }

    public void setAction_state1(int action_state1) {
        this.action_state1 = action_state1;
    }

    public int getAction_state6() {
        return action_state6;
    }

    public void setAction_state6(int action_state6) {
        this.action_state6 = action_state6;
    }

    public String getSerial_type2() {
        return serial_type2;
    }

    public void setSerial_type2(String serial_type2) {
        this.serial_type2 = serial_type2;
    }

    public int getAction_state2() {
        return action_state2;
    }

    public void setAction_state2(int action_state2) {
        this.action_state2 = action_state2;
    }

    public int getAction_state7() {
        return action_state7;
    }

    public void setAction_state7(int action_state7) {
        this.action_state7 = action_state7;
    }

    public String getSerial_type3() {
        return serial_type3;
    }

    public void setSerial_type3(String serial_type3) {
        this.serial_type3 = serial_type3;
    }

    public int getAction_state3() {
        return action_state3;
    }

    public void setAction_state3(int action_state3) {
        this.action_state3 = action_state3;
    }

    public int getAction_state8() {
        return action_state8;
    }

    public void setAction_state8(int action_state8) {
        this.action_state8 = action_state8;
    }

    public String getSerial_type4() {
        return serial_type4;
    }

    public void setSerial_type4(String serial_type4) {
        this.serial_type4 = serial_type4;
    }

    public int getAction_state4() {
        return action_state4;
    }

    public void setAction_state4(int action_state4) {
        this.action_state4 = action_state4;
    }

    public int getAction_state9() {
        return action_state9;
    }

    public void setAction_state9(int action_state9) {
        this.action_state9 = action_state9;
    }

    public String getSerial_type5() {
        return serial_type5;
    }

    public void setSerial_type5(String serial_type5) {
        this.serial_type5 = serial_type5;
    }

    public int getAction_state5() {
        return action_state5;
    }

    public void setAction_state5(int action_state5) {
        this.action_state5 = action_state5;
    }

    public int getAction_state10() {
        return action_state10;
    }

    public void setAction_state10(int action_state10) {
        this.action_state10 = action_state10;
    }
}
