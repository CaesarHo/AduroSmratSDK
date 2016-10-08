package com.core.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by oaosj on 2016/8/5 0005.
 */
public class AppScene implements Serializable {
    private static final long serialVersionUID = 2L;
    private short sencesId;
    private String sencesName;
    private short groups_id;
    private ArrayList<String> devices_mac;

    public short getSencesId() {
        return sencesId;
    }

    public void setSencesId(short sencesId) {
        this.sencesId = sencesId;
    }

    public String getSencesName() {
        return sencesName;
    }

    public void setSencesName(String sencesName) {
        this.sencesName = sencesName;
    }

    public short getGroups_id() {
        return groups_id;
    }

    public void setGroups_id(short groups_id) {
        this.groups_id = groups_id;
    }

    public ArrayList<String> getDevices_mac() {
        return devices_mac;
    }

    public void setDevices_mac(ArrayList<String> devices_mac) {
        this.devices_mac = devices_mac;
    }
}
