package com.adurosmart.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by best on 2016/6/30.
 */
public class SharedPreferencesManager {
    public static final String SP_FILE_GWELL = "gwell";

    private static SharedPreferencesManager manager = null;

    private SharedPreferencesManager() {

    }

    public synchronized static SharedPreferencesManager getInstance() {
        if (null == manager) {
            synchronized (SharedPreferencesManager.class) {
                if (null == manager) {
                    manager = new SharedPreferencesManager();
                }
            }
        }
        return manager;
    }

    public String getData(Context context, String fileName, String key) {
        SharedPreferences sf = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        return sf.getString(key, "");
    }

    public void putData(Context context, String fileName, String key,
                        String value) {
        SharedPreferences sf = context.getSharedPreferences(fileName,
                context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
