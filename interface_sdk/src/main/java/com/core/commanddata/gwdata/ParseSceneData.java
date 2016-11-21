package com.core.commanddata.gwdata;

import android.util.Log;

import com.core.entity.AppScene;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by best on 2016/10/10.
 */

public class ParseSceneData {

    public static void ParseAddSceneBackInfo(byte[] data, int len) {
        byte[] scene_id_bt = new byte[2];
        byte[] scene_name_bt = new byte[len];

        byte sceneId = data[32];
        int id_i = sceneId & 0xff;
        short scene_id = (short)id_i;

        System.arraycopy(data, 35, scene_name_bt, 0, len);
        String scene_name = FtFormatTransfer.bytesToUTF8String(scene_name_bt);

        System.arraycopy(data, 35 + len, scene_id_bt, 0, 2);
        short mGroupId = FtFormatTransfer.hBytesToShort(scene_id_bt);

        System.out.println("mGroupId_Bt = " + Arrays.toString(scene_id_bt));
        if (scene_name.equals(Constants.SCENE_GLOBAL.ADD_SCENE_NAME)) {
            if (scene_id == 0) {
                return;
            }
            DataSources.getInstance().AddScene(scene_id,scene_name, Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID);
        }
    }

    public static void ParseGetScenesInfo(byte[] bt) throws Exception{
        String sceneinfo = FtFormatTransfer.bytesToUTF8String(bt);
        Short scene_group_id = 0;
        Short scene_id = 0;
        String scene_name = "";
        ArrayList<String> device_list = new ArrayList<>();
        int strToint = sceneinfo.indexOf(":");
        String isScene = "";
        if (strToint >= 0) {
            isScene = sceneinfo.substring(strToint - 3, strToint);
        }
        device_list.clear();

        if (sceneinfo.contains("GW") && !sceneinfo.contains("K64") && isScene.contains("eId")) {
            String[] scene_data = sceneinfo.split(",");
            for (int i = 3; i < scene_data.length; i++) {
                if (scene_data.length < 2) {
                    continue;
                }
                String mac = scene_data[i].substring(4,scene_data[i].length());
                if (!mac.equals("")){
                    device_list.add(mac);
                }
                String[] scene_id_arr = scene_data[0].split(":");
                String[] group_id_arr = scene_data[1].split(":");
                String[] scene_name_arr = scene_data[2].split(":");

                if (scene_id_arr.length > 1 && scene_name_arr.length > 1 && group_id_arr.length > 1) {
                    if (scene_id_arr.length >= 3) {
                        scene_id = Short.valueOf(scene_id_arr[2]);
                    } else {
                        scene_id = Short.valueOf(scene_id_arr[1]);
                    }
                    scene_name = Utils.toStringHex(scene_name_arr[1]);
                    scene_group_id = Short.valueOf(group_id_arr[1]);
                }
            }
            for(int j = 0;j < device_list.size();j++){
                System.out.println("场景设备列表 = " + device_list.get(j));
            }
            AppScene appScene = new AppScene();
            appScene.setSencesId(scene_id);
            appScene.setSencesName(scene_name);
            appScene.setGroups_id(scene_group_id);
            appScene.setDevices_mac(device_list);
            DataSources.getInstance().getAllScenes(appScene);
        }
    }

    /**
     * 解析修改场景名称
     */
    public static class ParseModifySceneInfo {
        public short mSceneID;
        public String mSceneName;

        public ParseModifySceneInfo() {
            mSceneID = 0;
            mSceneName = "";
        }

        public void parseBytes(byte[] data, int len) {
            byte[] mName_Bt = new byte[len];

            byte mSceneId = data[32];
            int i = mSceneId & 0xff;
            mSceneID = (short) i;

            System.arraycopy(data, 35, mName_Bt, 0, len);
            mSceneName = FtFormatTransfer.bytesToString(mName_Bt);
        }
    }

}
