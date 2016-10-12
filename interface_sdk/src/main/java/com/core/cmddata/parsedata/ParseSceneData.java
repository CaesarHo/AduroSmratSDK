package com.core.cmddata.parsedata;

import android.util.Log;

import com.core.entity.AppScene;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.utils.CRC8;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by best on 2016/10/10.
 */

public class ParseSceneData {

    public static void ParseAddSceneBackInfo(byte[] data, int len) {
        byte[] mGroupId_Bt = new byte[2];
        byte[] mName_Bt = new byte[len];

        byte mSceneId = data[32];
        int i = mSceneId & 0xff;
        short mSceneID = (short) i;

        System.arraycopy(data, 35, mName_Bt, 0, len);
        String mSceneName = FtFormatTransfer.bytesToString(mName_Bt);

        System.arraycopy(data, 35 + len, mGroupId_Bt, 0, 2);
        short mGroupId = FtFormatTransfer.hBytesToShort(mGroupId_Bt);

        System.out.println("mGroupId_Bt = " + Arrays.toString(mGroupId_Bt));
        if (mSceneName.equals(Constants.SCENE_GLOBAL.ADD_SCENE_NAME)) {
            if (mSceneID == 0) {
                return;
            }
            DataSources.getInstance().AddScene(mSceneID,mSceneName, Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID);
        }
    }

    public static void ParseGetScenesInfo(String sceneinfo) {
        Short Scene_Group_Id = 0;
        Short Scene_Id = 0;
        String Scene_Name = "";
        boolean isRun = true;
        ArrayList<String> SceneFormGroupDevlist = new ArrayList<String>();
        int strToint = sceneinfo.indexOf(":");
        String isScene = "";
        if (strToint >= 0) {
            isScene = sceneinfo.substring(strToint - 3, strToint);
            Log.i("isScene = ", isScene);
        }
        SceneFormGroupDevlist.clear();

        if (sceneinfo.contains("GW") && !sceneinfo.contains("K64") && isScene.contains("eId")) {

            String[] group_data = sceneinfo.split(",");

            //get scenes
            for (int i = 1; i < group_data.length; i++) {
                if (group_data.length < 2) {
                    continue;
                }

                if (group_data[i].contains("MAC:")) {
                    SceneFormGroupDevlist.add(group_data[i].substring(4, group_data[i].length()));
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
                    Scene_Name = Utils.toStringHex2(Name_Source[1]);
                    Scene_Group_Id = Short.valueOf(Group_Id_Source[1]);

                }

                Log.i("Scene_Group_Id = ", "" + Scene_Group_Id);
                Log.i("Scene_Id = ", "" + Scene_Id);
                Log.i("Scene_Name = ", Scene_Name);
            }
            AppScene appScene = new AppScene();
            appScene.setSencesId(Scene_Id);
            appScene.setSencesName(Scene_Name);
            appScene.setGroups_id(Scene_Group_Id);
            appScene.setDevices_mac(SceneFormGroupDevlist);

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
