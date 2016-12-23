package com.core.utils;

/**
 * Created by best on 2016/12/22.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.core.commanddata.appdata.GatewayCmdData;
import com.core.connectivity.UdpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import static com.core.global.Constants.FTP_GLOBAL.FTP_CONNECT_FAIL;
import static com.core.global.Constants.FTP_GLOBAL.FTP_CONNECT_SUCCESSS;
import static com.core.global.Constants.FTP_GLOBAL.FTP_DELETEFILE_FAIL;
import static com.core.global.Constants.FTP_GLOBAL.FTP_DELETEFILE_SUCCESS;
import static com.core.global.Constants.FTP_GLOBAL.FTP_DISCONNECT_SUCCESS;
import static com.core.global.Constants.FTP_GLOBAL.FTP_DOWN_FAIL;
import static com.core.global.Constants.FTP_GLOBAL.FTP_DOWN_LOADING;
import static com.core.global.Constants.FTP_GLOBAL.FTP_DOWN_SUCCESS;
import static com.core.global.Constants.FTP_GLOBAL.FTP_FILE_NOTEXISTS;
import static com.core.global.Constants.FTP_GLOBAL.FTP_UPLOAD_FAIL;
import static com.core.global.Constants.FTP_GLOBAL.FTP_UPLOAD_SUCCESS;

public class FTPUtils {
    public Context context;
    /**
     * 服务器名.
     */
    private String hostName;

    /**
     * 端口号
     */
    private int serverPort;

    /**
     * 用户名.
     */
    private String userName;

    /**
     * 密码.
     */
    private String password;

    /**
     * FTP连接.
     */
    private FTPClient ftpClient;
    public List<String> arFiles;

    public FTPUtils(Context context) {
        this.context = context;
        this.hostName = "updategw.adurosmart.com";
        this.serverPort = 21;
        this.userName = "gateway";
        this.password = "iajs@!9O*kbc";
        this.ftpClient = new FTPClient();
        this.arFiles = new ArrayList<>();
    }

    // -------------------------------------------------------文件上传方法------------------------------------------------

    /**
     * 上传单个文件.
     *
     * @param singleFile 本地文件
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    public void uploadSingleFile(File singleFile, String remotePath, UploadProgressListener listener) throws IOException {

        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;
        flag = uploadingSingle(singleFile, listener);
        if (flag) {
            listener.onUploadProgress(FTP_UPLOAD_SUCCESS, 0, singleFile);
        } else {
            listener.onUploadProgress(FTP_UPLOAD_FAIL, 0, singleFile);
        }
        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }

    /**
     * 上传多个文件.
     *
     * @param fileList   本地文件
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    public void uploadMultiFile(LinkedList<File> fileList, String remotePath, UploadProgressListener listener) throws IOException {

        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;

        for (File singleFile : fileList) {
            flag = uploadingSingle(singleFile, listener);
            if (flag) {
                listener.onUploadProgress(FTP_UPLOAD_SUCCESS, 0, singleFile);
            } else {
                listener.onUploadProgress(FTP_UPLOAD_FAIL, 0, singleFile);
            }
        }
        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }

    /**
     * 上传单个文件.
     *
     * @param localFile 本地文件
     * @return true上传成功, false上传失败
     * @throws IOException
     */
    private boolean uploadingSingle(File localFile, UploadProgressListener listener) throws IOException {
        boolean flag = true;
        // 不带进度的方式
        // // 创建输入流
        // InputStream inputStream = new FileInputStream(localFile);
        // // 上传单个文件
        // flag = ftpClient.storeFile(localFile.getName(), inputStream);
        // // 关闭文件流
        // inputStream.close();

        // 带有进度的方式
        BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(localFile));
        ProgressInputStream progressInput = new ProgressInputStream(buffIn, listener, localFile);
        flag = ftpClient.storeFile(localFile.getName(), progressInput);
        buffIn.close();

        return flag;
    }

    /**
     * 上传文件之前初始化相关参数
     *
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    private void uploadBeforeOperate(String remotePath, UploadProgressListener listener) throws IOException {
        // 打开FTP服务
        try {
            this.openConnect();
            listener.onUploadProgress(FTP_CONNECT_SUCCESSS, 0, null);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onUploadProgress(FTP_CONNECT_FAIL, 0, null);
            return;
        }

        // 设置模式
        ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
        // FTP下创建文件夹
        ftpClient.makeDirectory(remotePath);
        // 改变FTP目录
        ftpClient.changeWorkingDirectory(remotePath);
        // 上传单个文件
    }

    /**
     * 上传完成之后关闭连接
     *
     * @param listener
     * @throws IOException
     */
    private void uploadAfterOperate(UploadProgressListener listener) throws IOException {
        this.closeConnect();
        listener.onUploadProgress(FTP_DISCONNECT_SUCCESS, 0, null);
    }

    // -------------------------------------------------------文件下载方法------------------------------------------------

    /**
     * 下载单个文件，可实现断点下载.
     *
     * @param serverPath Ftp目录及文件路径
     * @param localPath  本地目录
     * @param fileName   下载之后的文件名称
     * @param listener   监听器
     * @throws IOException
     */
    public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener)
            throws Exception {

        // 打开FTP服务
        try {
            System.out.println("连接中。。。。");
            this.openConnect();
            System.out.println("完成连接。。。。");
            listener.onDownLoadProgress(FTP_CONNECT_SUCCESSS, 0, null);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onDownLoadProgress(FTP_CONNECT_FAIL, 0, null);
            return;
        }

        // 先判断服务器文件是否存在
        FTPFile[] files = ftpClient.listFiles(serverPath);
        if (files.length == 0) {
            listener.onDownLoadProgress(FTP_FILE_NOTEXISTS, 0, null);
            System.out.println("文件不存在。。。。");
            return;
        }

        //创建本地文件夹
        File mkFile = new File(localPath);
        if (!mkFile.exists()) {
            mkFile.mkdirs();
        }

        localPath = localPath + fileName;
        // 接着判断下载的文件是否能断点下载
        long serverSize = files[0].getSize(); // 获取远程文件的长度
        File localFile = new File(localPath);
        long localSize = 0;
        if (localFile.exists()) {
            localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
            if (localSize >= serverSize) {
                File file = new File(localPath);
                file.delete();
            }
        }

        // 进度
        long step = serverSize / 100;
        long process = 0;
        long currentSize = 0;
        // 开始准备下载文件
        OutputStream out = new FileOutputStream(localFile, true);
        ftpClient.setRestartOffset(localSize);
        InputStream input = ftpClient.retrieveFileStream(serverPath);
        byte[] b = new byte[1024];
        int length = 0;
        while ((length = input.read(b)) != -1) {
            out.write(b, 0, length);
            currentSize = currentSize + length;
            if (currentSize / step != process) {
                process = currentSize / step;
                if (process % 5 == 0) {  //每隔%5的进度返回一次
                    listener.onDownLoadProgress(FTP_DOWN_LOADING, process, null);
                }
            }
        }
        out.flush();
        out.close();
        input.close();

        // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
        if (ftpClient.completePendingCommand()) {
            listener.onDownLoadProgress(FTP_DOWN_SUCCESS, 0, new File(localPath));
        } else {
            listener.onDownLoadProgress(FTP_DOWN_FAIL, 0, null);
        }

        // 下载完成之后关闭连接
        this.closeConnect();
        listener.onDownLoadProgress(FTP_DISCONNECT_SUCCESS, 0, null);
        System.out.println("下载完成。。。。");
        return;
    }

    // -------------------------------------------------------文件删除方法------------------------------------------------

    /**
     * 删除Ftp下的文件.
     *
     * @param serverPath Ftp目录及文件路径
     * @param listener   监听器
     * @throws IOException
     */
    public void deleteSingleFile(String serverPath, DeleteFileProgressListener listener) throws Exception {

        // 打开FTP服务
        try {
            this.openConnect();
            listener.onDeleteProgress(FTP_CONNECT_SUCCESSS);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onDeleteProgress(FTP_CONNECT_FAIL);
            return;
        }

        // 先判断服务器文件是否存在
        FTPFile[] files = ftpClient.listFiles(serverPath);
        if (files.length == 0) {
            listener.onDeleteProgress(FTP_FILE_NOTEXISTS);
            return;
        }

        //进行删除操作
        boolean flag = true;
        flag = ftpClient.deleteFile(serverPath);
        if (flag) {
            listener.onDeleteProgress(FTP_DELETEFILE_SUCCESS);
        } else {
            listener.onDeleteProgress(FTP_DELETEFILE_FAIL);
        }

        // 删除完成之后关闭连接
        this.closeConnect();
        listener.onDeleteProgress(FTP_DISCONNECT_SUCCESS);

        return;
    }

    // -------------------------------------------------------打开关闭连接------------------------------------------------

    /**
     * 打开FTP服务.
     *
     * @throws IOException
     */
    public void openConnect() throws IOException {
        // 中文转码
        ftpClient.setControlEncoding("UTF-8");
        int reply; // 服务器响应值
        // 连接至服务器
        ftpClient.connect(hostName, serverPort);
        System.out.println("openConnect");
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        }
        // 登录到服务器
        ftpClient.login(userName, password);
        System.out.println("openConnect");
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        } else {
            DirectoryList("/");
            // 获取登录信息
            FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            ftpClient.configure(config);
            // 使用被动模式设为默认
            ftpClient.enterLocalPassiveMode();
            // 二进制文件支持
            ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        }
    }

    //----------------------------------------查找目录及其文件---------------------------------------

    /**
     * 递归遍历出目录下面所有文件
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @throws IOException
     */
    public void DirectoryList(String pathName) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            String directory = pathName;
            //更换目录到当前目录
            this.ftpClient.changeWorkingDirectory(directory);
            FTPFile[] files = this.ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (directory.equalsIgnoreCase("/GW_FIRMWARE/")) {
                        arFiles.add(file.getName());
                        if (file.getName().contains("GATEWAY-Z1")) {
                            System.out.println("arFiles = " + file.getName());
                            int ver_ = SearchUtils.searchString(file.getName(), "VER-");
                            String version = file.getName().substring(ver_, ver_ + 4);

                            int crc_ = SearchUtils.searchString(file.getName(), "CRC-");
                            String crc32 = file.getName().substring(crc_, crc_ + 8);

                            System.out.println("version = " + version + "," + crc32);
                            byte[] bytes_version = version.getBytes("utf-8");
                            System.out.println("version byte = " + Arrays.toString(bytes_version));

                            long serverSize = file.getSize(); // 获取远程文件的长度
                            int longToint = new Long(serverSize).intValue();
                            System.out.println("文件大小 = " + serverSize);
                            System.out.println("serverSize = " + longToint);

                            byte[] bytes_data = GatewayCmdData.StartUpdateVerCmd(9, longToint);
                            new Thread(new UdpClient(context, bytes_data)).start();
                            System.out.println("启动更新命令 = " + Arrays.toString(bytes_data));

                            try {
                                //单文件下载
                                String path = Environment.getExternalStorageDirectory().getPath() + "/screenshot/gateway_update_file";
                                downloadSingleFile("/GW_FIRMWARE/" + file.getName(), path, file.getName(), null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (file.isDirectory()) {
                        DirectoryList(directory + file.getName() + "/");
                    }
                }
            }
        }
    }

        /**
         * 递归遍历目录下面指定的文件名
         *
         * @param pathName 需要遍历的目录，必须以"/"开始和结束
         * @param ext      文件的扩展名
         * @throws IOException
         */

    public void DirectoryList(String pathName, String ext) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            String directory = pathName;
            //更换目录到当前目录
            this.ftpClient.changeWorkingDirectory(directory);
            FTPFile[] files = this.ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (file.getName().endsWith(ext)) {
                        arFiles.add(directory + file.getName());
                    }
                } else if (file.isDirectory()) {
                    DirectoryList(directory + file.getName() + "/", ext);
                }
            }
        }
    }


    /**
     * 关闭FTP服务.
     *
     * @throws IOException
     */
    public void closeConnect() throws IOException {
        if (ftpClient != null) {
            // 退出FTP
            ftpClient.logout();
            // 断开连接
            ftpClient.disconnect();
        }
    }

    // ---------------------------------------------------上传、下载、删除监听---------------------------------------------

    /*
     * 上传进度监听
     */
    public interface UploadProgressListener {
        public void onUploadProgress(String currentStep, long uploadSize, File file);
    }

    /*
     * 下载进度监听
     */
    public interface DownLoadProgressListener {
        public void onDownLoadProgress(String currentStep, long downProcess, File file);
    }

    /*
     * 文件删除监听
     */
    public interface DeleteFileProgressListener {
        public void onDeleteProgress(String currentStep);
    }

}
