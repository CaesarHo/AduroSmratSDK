package com.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oaosj on 2016/8/10 0010.
 */
public class AppGroup implements Serializable{
    private static final long serialVersionUID = 1L;
    private Short group_id;
    private String group_name;
    private String icon_path;
    private ArrayList<String> mac_data;
    private Boolean selected = false;
    public Short getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Short group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public ArrayList<String> getMac_data() {
        return mac_data;
    }

    public void setMac_data(ArrayList<String> mac_data) {
        this.mac_data = mac_data;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
