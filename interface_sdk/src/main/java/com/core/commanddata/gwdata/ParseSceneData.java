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
        Short Scene_Group_Id = 0;
        Short Scene_Id = 0;
        String Scene_Name = "";
        boolean isRun = true;
        ArrayList<String> mac_list = new ArrayList<String>();
        int strToint = sceneinfo.indexOf(":");
        String isScene = "";
        if (strToint >= 0) {
            isScene = sceneinfo.substring(strToint - 3, strToint);
            Log.i("isScene = ", isScene);
        }
        mac_list.clear();

        if (sceneinfo.contains("GW") && !sceneinfo.contains("K64") && isScene.contains("eId")) {
            String[] group_data = sceneinfo.split(",");
            //get scenes
            for (int i = 1; i < group_data.length; i++) {
                if (group_data.length < 2) {
                    continue;
                }
                if (group_data[i].contains("MAC:")) {
                    mac_list.add(group_data[i].substring(4, group_data[i].length()));
                }
                String[] Id_Source = group_data[0].split(":");
                String[] Group_Id_Source = group_data[1].split(":");
                String[] Name_Source = group_data[2].split(":");

                if (Id_Source.length > 1 && Name_Source.length > 1 && Group_Id_Source.length > 1) {
                    if (Id_Source.length >= 3) {
                        Scene_Id = Short.valueOf(Id_Source[2]);
                        System.out.println("Scene_Id = " + Arrays.toString(Id_Source));
                    } else {
                        Scene_Id = Short.valueOf(Id_Source[1]);
                        System.out.println("Scene_Id = " + Arrays.toString(Id_Source));
                    }
                    Scene_Name = Utils.toStringHex(Name_Source[1]);
                    Scene_Group_Id = Short.valueOf(Group_Id_Source[1]);
                }
                AppScene appScene = new AppScene();
                appScene.setSencesId(Scene_Id);
                appScene.setSencesName(Scene_Name);
                appScene.setGroups_id(Scene_Group_Id);
                appScene.setDevices_mac(mac_list);
                DataSources.getInstance().getAllScenes(appScene);
            }
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
