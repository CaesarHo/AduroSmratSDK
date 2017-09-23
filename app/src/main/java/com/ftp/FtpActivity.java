package com.ftp;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.adurosmart.sdk.R;
import com.core.db.GatewayInfo;

import java.io.File;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by best on 2016/12/21.
 */

public class FtpActivity extends AppCompatActivity {
    private Context context ;
    private static Logger logger = Logger.getLogger("FtpActivity");
    public static final String TAG = "FtpActivity";
    public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
    public static final String FTP_CONNECT_FAIL = "ftp连接失败";
    public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
    public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";

    public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";
    public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";
    public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";

    public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
    public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
    public static final String FTP_DOWN_FAIL = "ftp文件下载失败";

    public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";
    public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败";
    @BindView(R.id.btn_dow)
    Button btnDow;
    @BindView(R.id.btn_upd)
    Button btnUpd;
    public static String SERVER = "data.adurosmart.com";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        context = this;
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_dow, R.id.btn_upd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dow:
//                ftpDownload();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //单文件下载
                            final String path = Environment.getExternalStorageDirectory().getPath() + "/gateway_update_file/";
                            final String file_name = GatewayInfo.getInstance().getGatewayUpdateFileName(context);
                            System.out.println("path = " + path);
                            System.out.println("file_name = " + file_name);
                            new FTP(context).downloadSingleFile("/GW_FIRMWARE/" + file_name, path, file_name,
                                    new FTP.DownLoadProgressListener() {
                                        @Override
                                        public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                                            Log.d("====", "-----xiazai---" + downProcess + "%");
                                        }
                                    }, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            System.out.println("finally = " + "UpdateHelper");
                        }
                    }
                }).start();

                break;
            case R.id.btn_upd:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new FTP(context).openConnect(true);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    // 下载例子
    private void ftpDownload() {
        new Thread() {
            public void run() {
                try {
                    System.out.println("正在连接ftp服务器....");
                    btnDow.setText("正在连接......");
                    FTPManager ftpManager = new FTPManager();
                    if (ftpManager.connect()) {
                        btnDow.setText("成功");
                        String path = Environment.getExternalStorageDirectory().getPath() + "/screenshot/TestUdpCmd";
                        if (ftpManager.downloadFile(path, "updategw.adurosmart.com")) {
                            ftpManager.closeFTP();
                            btnDow.setText("下载完成关闭中......");
                        }
                    }
                } catch (Exception e) {
                    // System.out.println(e.getMessage());
                }
            }
        }.start();
    }


//    public void createFlorder() {
//        String path = Environment.getExternalStorageDirectory().getPath() + "/screenshot/TestUdpCmd";
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//    }
}
