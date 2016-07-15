package com.adurosmart.activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adurosmart.adapters.TestUPDAdapters;
import com.adurosmart.global.FList;
import com.adurosmart.global.ListenCallback;
import com.adurosmart.global.MainService;
import com.adurosmart.utils.AESUtils;
import com.adurosmart.utils.UDPHelper2;
import com.interfacecallback.SerialHandler;
import com.interfacecallback.UDPHelper;

import org.eclipse.paho.android.service.sample.MyApp;
import org.eclipse.paho.android.service.sample.R;

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by best on 2016/6/24.
 */
public class UpdActivity extends Activity {
    Context context;

    public static class DevInfo {
        public String devId;
        public String devIp;

        public DevInfo() {
            this.devId= "";
            this.devIp = "";
        }
    }
    EditText packet_edit;
    TextView ip_tv,receive_data,local_ip,encrypt_tv,decode_tv;
    ListView list;
    private int port_int = 8888;
    private DatagramSocket m_CMDSocket = null;
    private Button send,getIpAddress;
    String ip;
    String devIP;
    public static final int RETRIES = 3;
    List<DevInfo> mDevInfoList = new ArrayList<DevInfo>();
    TestUPDAdapters testUPDAdapters;

    boolean isReceive = false;
    private boolean isRegFilter = false;
    WifiManager.MulticastLock lock;

    Thread tReceived;
    UDPHelper2 udpHelper2;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        context = this;
//        DataSources.getInstance().setSettingInterface(new ListenCallback());
        SerialHandler.getInstance().Init(context, "200004401331", new ListenCallback());
        new FList();
        //获取wifi服务
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        ip = intToIp(ipAddress);
//        MainThread.setOpenThread(true);
//        FList flist = FList.getInstance();
//        flist.searchLocalDevice();
        //监听
//        mHelper = new UDPHelper(context,8888);
//        listen();
//        mHelper.StartListen();
        initSocket();
        regFilter();
        initview();

        //监听2
        udpHelper2 = new UDPHelper2(context,wifiManager);
        tReceived = new Thread(udpHelper2);
        tReceived.start();
//        SerialHandler.getInstance().Init(context,new ListenCallback());
    }

    public void initview() {
        packet_edit = (EditText) findViewById(R.id.packet_edit);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new Thread(new Send()).start();
                UDPHelper udpHelper = new UDPHelper(context,wifiManager);
                Thread thread = new Thread(udpHelper);
                thread.start();

                Intent service = new Intent(MyApp.MAIN_SERVICE_START);
                context.startService(service);
                context.bindService(service,conn,Context.BIND_AUTO_CREATE);

//                String ipaddress = GatewayInfo.getInstance().getInetAddress(context);
//                Toast t3 = Toast.makeText(context, "当前数据=" + ipaddress, Toast.LENGTH_SHORT);
//                t3.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
//                t3.setMargin(0f, 0.5f);
//                t3.show();
            }
        });

        getIpAddress = (Button)findViewById(R.id.getIpAddress);
        getIpAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new GetIpAddress()).start();
            }
        });
        ip_tv =(TextView) findViewById(R.id.ip_tv);
        receive_data = (TextView)findViewById(R.id.receive_data);
        local_ip = (TextView)findViewById(R.id.local_ip);
        local_ip.setText(MyApp.mAndroidId);
        list = (ListView)findViewById(R.id.list);
        testUPDAdapters = new TestUPDAdapters(context,mDevInfoList);
        list.setAdapter(testUPDAdapters);

        encrypt_tv = (TextView)findViewById(R.id.encrypt_tv);
        decode_tv = (TextView)findViewById(R.id.decode_tv);

        String masterPassword = "1234567812345678";
        String originalText = "0123456789";
        byte[] text = new byte[]{'0','1','2','3','4','5','6','7','8','9'};
        byte[] password = new byte[]{'a'};
        try {
            byte[] encrypting = AESUtils.encrypt(masterPassword,originalText);

            String encryptingCode = new String(encrypting);
            Log.i("加密结果为:",encryptingCode);
            encrypt_tv.setText(encryptingCode);

            byte[] decrypting = AESUtils.decrypt(encryptingCode.getBytes(),masterPassword);
            String decryptingCode = new String(decrypting);
            Log.i("解密结果为:",decryptingCode);
            decode_tv.setText(decryptingCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainService.MyBinder binder = (MainService.MyBinder)service;
            MainService bindService = binder.getService();
            bindService.MyMethod();
        }
    };

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_LISTEN_UDP_DATA");
        filter.addAction("adddevicecallback");
        filter.addAction("GatewatInfoCallback");
        context.registerReceiver(broadcastReceiver, filter);
        isRegFilter = true;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACTION_LISTEN_UDP_DATA")){
                String ipstr = intent.getStringExtra("ipaddrss");
                String str =  intent.getStringExtra("data");
                int port_int = intent.getIntExtra("port",-1);

                DevInfo item = new DevInfo();
                item.devId = ipstr;
                item.devIp = str;
                mDevInfoList.add(item);
                testUPDAdapters.notifyDataSetChanged();
                ip_tv.setText(ipstr);
                receive_data.setText(String.valueOf(port_int));

                Toast t3=Toast.makeText(context, "当前数据=" + str, Toast.LENGTH_SHORT);
                t3.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
                t3.setMargin(0f, 0.5f);
                t3.show();
            }else if (intent.getAction().equals("adddevicecallback")){
                int i = intent.getIntExtra("i",-1);
                Toast ti = Toast.makeText(context,""+ i,Toast.LENGTH_LONG);
                ti.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,0);
                ti.setMargin(0f,0.5f);
                ti.show();
            }else if(intent.getAction().equals("GatewatInfoCallback")){
                String gatewayName = intent.getStringExtra("gatewayName");
                String gatewayNo = intent.getStringExtra("gatewayNo");
                String gatewaySoftwareVersion = intent.getStringExtra("gatewaySoftwareVersion");
                String gatewayHardwareVersion = intent.getStringExtra("gatewayHardwareVersion");
                String gatewayIPv4Address = intent.getStringExtra("gatewayIPv4Address");
                String gatewayDatetime = intent.getStringExtra("gatewayDatetime");

                Toast ti = Toast.makeText(context,gatewayName + gatewayNo + gatewaySoftwareVersion + gatewayHardwareVersion + gatewayIPv4Address + gatewayDatetime,Toast.LENGTH_LONG);
                ti.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,0);
                ti.setMargin(0f,0.5f);
                ti.show();
            }
        }
    };

    public void initSocket(){
        try {
            m_CMDSocket = new DatagramSocket();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String intToIp(int i) {
        return (i & 0xFF ) + "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) + "." + ( i >> 24 & 0xFF) ;
    }

    public class Send implements Runnable {
        //encrypt_tv            packet_edit
        @Override
        public void run() {
            String sendmessage = encrypt_tv.getText().toString().trim();
            String mRemoteIp = ip_tv.getText().toString().trim();
            Log.d("UDP Demo", "UDP发送数据:" + sendmessage);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                //使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址
                InetAddress serverAddress = InetAddress.getByName(mRemoteIp);
                //Inet4Address serverAddress = (Inet4Address) Inet4Address.getByName("192.168.1.32");
                byte data[] = sendmessage.getBytes();//把字符串str字符串转换为字节数组
                //创建一个DatagramPacket对象，用于发送数据。
                //参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号
                DatagramPacket packet = new DatagramPacket(data, data.length ,new InetSocketAddress(serverAddress,port_int));
                m_CMDSocket.send(packet);//把数据发送到服务端。
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetIpAddress implements Runnable{
        byte [] recvData ;
        @Override
        public void run() {
            try {
                byte[] bytes = packet_edit.getText().toString().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("255.255.255.255"),8888);
                m_CMDSocket.send(sendPacket);
                for(int k=0; k<50; k++) {//-----500ms 接收一次……。
                    try {
                        // 创建UDP接收包
                        recvData = new byte[1024];// 一次接一个结构体
                        DatagramPacket recvPacket = new DatagramPacket(recvData,recvData.length);
                        // 接收UDP包
                        m_CMDSocket.receive(recvPacket);
                        // 解析接收到的UDP包1，并赋值给FD_SVR_PACKET类型对象RecvCleint

                        String devIP =  recvPacket.getAddress().getHostAddress().trim();
                        System.out.printf("intranetSerarchDev recv ip:%s size:%d\n",devIP, recvPacket.getLength());
                        ip_tv.setText(devIP);

                        String str = new String(recvData);
                        Log.e("UDPActivityGETIP", "UDPActivityGETIP=" + str);
                        DevInfo item = new DevInfo();
                        item.devId = str;
                        item.devIp = devIP;
                        mDevInfoList.add(item);
                        testUPDAdapters.notifyDataSetChanged();
                    }catch (InterruptedIOException e){
                        continue;  //非阻塞循环
                    }
                }//k=60
            } catch (Exception e) {
                Log.e("deviceinfo IOException", "Client: Error!");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            this.finish();
            if(isRegFilter){
                isRegFilter = false;
                this.unregisterReceiver(broadcastReceiver);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tReceived.interrupt();
        if(isRegFilter){
            isRegFilter = false;
            m_CMDSocket.close();
            this.unregisterReceiver(broadcastReceiver);
        }
    }

//    private Handler handler = new Handler();
//
//    private Runnable task = new Runnable() {
//        public void run() {
//            handler.postDelayed(this,3*1000);//设置延迟时间，此处是5秒
//            //需要执行的代码
//            while (true){
//                byte[] buf = new byte[1024];
//                receive(9999,buf.length);
//            }
//        }
//    };
//    public  String receive(int udpPort,int byteLengt){
//        try {
//            //创建数据报套接字，并绑定到固定端口
//            DatagramSocket ds=new DatagramSocket(udpPort);
//            System.out.println("监听"+udpPort+"端口启动...");
//            byte[] buf = new byte[byteLengt];//接受UDP端口数据
//            //创建接收数据的UDP包    new DatagramPacket(byte[],length);
//            DatagramPacket dp = new DatagramPacket(buf,byteLengt);
//            //接受数据
//            ds.receive(dp);
//            String strReceive = new String(buf);
//            DevInfo item = new DevInfo();
//            item.devId = "Receive";
//            item.devIp = strReceive;
//            mDevInfoList.add(item);
//            testUPDAdapters.notifyDataSetChanged();
//
//            return new String(buf,0,dp.getLength());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
