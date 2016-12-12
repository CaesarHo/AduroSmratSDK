package com.core.entity;

import java.io.Serializable;

/**
 * Created by best on 2016/12/1.
 */

public class AppGateway implements Serializable {
    private String gateway_no;//网关编号:比如     9EF1DC584FCBCFBC
    private String gateway_mac;//网关网络MAC地址:比如: 02-00-00-06-00-1a
    private String ip_address;//网关IP地址:192.168.0.55
    private int gateway_version = -1;//网关软件版本号 比如:10004
    private int node_main_version = -1;//网关协调器的软件版本(主版本号)比如:1005
    private int node_installed_version = -1;//网关协调器的软件版本(安装版本号)比如:1364
    private int bootrodr = -1;

    public void setGateway_no(String gateway_no) {
        this.gateway_no = gateway_no;
    }

    public void setGateway_mac(String gateway_mac) {
        this.gateway_mac = gateway_mac;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public void setGateway_version(int gateway_version) {
        this.gateway_version = gateway_version;
    }

    public void setNode_main_version(int node_main_version) {
        this.node_main_version = node_main_version;
    }

    public void setNode_installed_version(int node_installed_version) {
        this.node_installed_version = node_installed_version;
    }

    public int getBootrodr() {
        return bootrodr;
    }

    public void setBootrodr(int bootrodr) {
        this.bootrodr = bootrodr;
    }

    public String getGateway_no() {
        return gateway_no;
    }

    public String getGateway_mac() {
        return gateway_mac;
    }

    public String getIp_address() {
        return ip_address;
    }

    public int getGateway_version() {
        return gateway_version;
    }

    public int getNode_main_version() {
        return node_main_version;
    }

    public int getNode_installed_version() {
        return node_installed_version;
    }
}
