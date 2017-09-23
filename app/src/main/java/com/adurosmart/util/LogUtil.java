package com.adurosmart.util;

/**
 * Created by best on 2016/10/17.
 */

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 日志工具类。
 * 包括日志输出格式，日志路径，日志保存，日志清理
 *
 * @author Bruce
 * @version V1.0 2012-12-12 友盟E用车版本号及日期
 */
public class LogUtil {
    private static ArrayList<String> listLogCache = new ArrayList<String>();
    private static String Tag = "LogUtil";

    public static void debug(String tag, String msg) {
        Log.d(tag, msg);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .format(new Date());
        listLogCache.add("[" + time + "] " + " [d/" + tag + "] " + msg + "\n");
    }

    public static String getLogDirPath() {
        String logPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/"
                + ConstsDefine.Settings.LogPath.sdLogDir;
        return logPath;
    }

    public static void clearHisLog() {
        String currDate = DateUtil.getDate();
        String filename = null;
        File f = new File(LogUtil.getLogDirPath());
        if (f != null && f.isDirectory()) {
            File[] subFile = f.listFiles();
            int fileCount = f.listFiles().length;
            for (int i = 0; i < fileCount; i++) {
                if (subFile[i].isFile()) {
                    filename = subFile[i].getName();
                    if (!filename.startsWith(currDate)) {
                        LogUtil.debug("LogUti", "filename:" + filename);
                        subFile[i].delete();
                    }
                }
            }
            if (f != null && f.isFile()) {
                long time = f.lastModified();
            }
        }
    }

    // //verifyLogFile未被调用
    // public static void verifyLogFile(String filename)
    // {
    // File file = new File(filename);
    // long sizeMB = file.length() / (1024 * 1024);
    // if (sizeMB > 50)
    // {
    // file.delete();
    // }
    // }

    public static String getLogName() {
        String logDirPath = getLogDirPath();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        String filePath = logDirPath + "/" + DateUtil.getDate() + "_" + hour
                + "h.log";
        return filePath;
    }

    public static String getLogPath() {
        String logDirPath = getLogDirPath();
        File dir = new File(logDirPath);
        if (!dir.exists()) {
            boolean bMkdirResult = dir.mkdirs();

        }
        String filePath = getLogName();
        return filePath;
    }

    public static void saveLog(String Tag, String mobile, String message) {
        String logDirPath = getLogDirPath();
        File dir = new File(logDirPath);
        if (!dir.exists()) {
            boolean bMkdirResult = dir.mkdirs();
        }
        String filePath = getLogName();
        // LogUtil.debug(Tag,"[getLogDirPath] filePath: "+filePath);
        writeFileSdcard(filePath, message);
    }

    public static void writeFromCache(String mobile) {
        Object[] messages = listLogCache.toArray();
        LogUtil.debug(Tag, "[writeFromCache]  message.length():"
                + messages.length);
        if (messages.length == 0) {
            return;
        }
        listLogCache.clear();
        String fileName = getLogPath();
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName, true);
            String line = null;
            for (Object message : messages) {
                line = message.toString();
                // LogUtil.debug(Tag,"[writeFromCache] line:"+line);
                fw.write(line, 0, line.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void writeFileSdcard(String fileName, String message) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(fileName, true);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static boolean deleteSDFile(String fileName) {
        String logDirPath = getLogDirPath();
        File file = new File(logDirPath);
        if (file != null && file.exists() && file.isDirectory()) {
            return file.delete();
        }
        return false;
    }
}
