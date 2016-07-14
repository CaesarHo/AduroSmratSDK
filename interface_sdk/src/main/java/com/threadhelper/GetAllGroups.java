package com.threadhelper;

import android.util.Log;

import com.interfacecallback.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/13.
 */
public class GetAllGroups implements Runnable {
    private DatagramSocket m_CMDSocket = null;
    private String ipaddress;
    private int port = -1;

    public GetAllGroups(String ipaddress, int port){
        this.ipaddress = ipaddress;
        this.port = port;
    }
    @Override
    public void run() {
        try {
            m_CMDSocket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(ipaddress);

            String mDeleteRoom = "DeleteRoom";
//            NewCmdData.add_cmd_remove cmdInfo = new NewCmdData.add_cmd_remove();
//            cmdInfo.id = id;
//            cmdInfo.Cmd = cmd;
//            cmdInfo.Length = 20;
//            cmdInfo.app_id = app_id;
            DatagramPacket packet_send = new DatagramPacket(mDeleteRoom.getBytes(),mDeleteRoom.getBytes().length,serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
//            NewCmdData.add_cmd_remove_Rep repdata = new NewCmdData.add_cmd_remove_Rep();
//            repdata.parseBytes(packet.getData());

            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().GetAllGroups((short)123456,"组名称","http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E6%88%BF%E9%97%B4&step_word=&pn=14&spn=0&di=69509290400&pi=&rn=1&tn=baiduimagedetail&is=&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1776678388%2C3366643901&os=3988324733%2C312848502&simid=4219021306%2C683951249&adpicid=0&ln=1000&fr=&fmq=1468388670898_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fhousephotolib%2F1404%2F26%2Fc0%2F33586990_1398496143956_690x460.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fri5p5_z%26e3Brvi57fj_z%26e3Bv54_z%26e3BvgAzdH3F2657rAzdH3Fdcanlm_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0");
            m_CMDSocket.close();
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}
