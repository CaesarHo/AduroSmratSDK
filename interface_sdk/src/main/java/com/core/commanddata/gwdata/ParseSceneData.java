package com.core.commanddata.gwdata;

import com.core.entity.AppScene;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.utils.TransformUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by best on 2016/10/10.
 */

public class ParseSceneData {

    public static void ParseGetScenesInfo(byte[] bt) throws Exception {
        String sceneinfo = new String(bt);
        System.out.println("场景设备列表 = " + sceneinfo);
        Short scene_group_id = 0;
        Short scene_id = 0;
        String scene_name = "";
        List<String> device_list = new ArrayList<>();
        device_list.clear();
        String[] scene_data = sceneinfo.split(",");
        for (int i = 3; i < scene_data.length; i++) {
            if (scene_data.length < 2) {
                continue;
            }

            if (scene_data[i].length() >= 6) {
                String mac = scene_data[i].substring(6, scene_data[i].length());
                if (!mac.equals("")) {
                    device_list.add(mac);
                }
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
                scene_name = scene_name_arr[1];
                scene_group_id = Short.valueOf(group_id_arr[1]);
            }
        }
        if (scene_id <= 0) {
            return;
        }
        AppScene appScene = new AppScene();
        appScene.setSencesId(scene_id);
        appScene.setSencesName(scene_name);
        appScene.setGroups_id(scene_group_id);
        appScene.setDevices_mac(device_list);
        DataSources.getInstance().getAllScenes(appScene);
    }

    public static class ParseAddSceneInfo {
        public String scene_name;
        public short scene_id;
        public short group_id;
        public ParseAddSceneInfo(){
            this.scene_name = "";
            this.scene_id = -1;
            this.group_id = -1;
        }
        public void parseBytes(byte[] data, int len){
            byte[] scene_id_bt = new byte[2];
            byte[] scene_name_bt = new byte[len];

            byte sceneId = data[32];
            int id_i = sceneId & 0xff;
            scene_id = (short) id_i;

            System.arraycopy(data, 35, scene_name_bt, 0, len);
            scene_name = new String(scene_name_bt);

            System.arraycopy(data, 35 + len, scene_id_bt, 0, 2);
            group_id = TransformUtils.hBytesToShort(scene_id_bt);

            if (scene_name.equals(Constants.SCENE_GLOBAL.ADD_SCENE_NAME)) {
                if (scene_id <= 0) {
                    return;
                }
                DataSources.getInstance().AddScene(scene_id, scene_name, Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID);
            }
        }
    }

    /**
     * 解析修改场景名称
     */
    public static class ParseModifySceneInfo {
        public short scene_id;
        public String scene_name;

        public ParseModifySceneInfo() {
            scene_id = 0;
            scene_name = "";
        }

        public void parseBytes(byte[] data, int len) {
            byte bt_scene = data[32];
            int i = bt_scene & 0xff;
            scene_id = (short) i;

            byte[] bt_name = new byte[len];
            System.arraycopy(data, 35, bt_name, 0, len);
            scene_name = TransformUtils.bytesToString(bt_name);
        }
    }
}
