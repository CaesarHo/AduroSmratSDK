package com.core.commanddata.gwdata;

import com.core.entity.AppGroup;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by best on 2016/10/10.
 */

public class ParseGroupData {

    public static void ParseGetGroupsInfo(byte[] bt) throws Exception {
        String groups_info = FtFormatTransfer.byteToASCIIString(bt);
        System.out.println("GetAllGroupsMac = " + groups_info);
        Short group_id = 0;
        String group_name = "";

        ArrayList<String> device_list = new ArrayList<>();
        int strToint = groups_info.indexOf(":");
        String isGroup = "";
        if (strToint >= 0) {
            isGroup = groups_info.substring(strToint - 4, strToint);
        }
        device_list.clear();

        if (groups_info.contains("GW") && !groups_info.contains("K64") && isGroup.contains("upId")) {
            String[] group_data = groups_info.split(",");

            for (int i = 2; i < group_data.length; i++) {
                if (group_data.length < 2) {
                    continue;
                }
                if (group_data[i].length() >= 6){
                    String mac = group_data[i].substring(6, group_data[i].length());
                    if (!mac.equals("")) {
                        device_list.add(mac);
                    }
                }

                String[] group_id_arr = group_data[0].split(":");
                String[] group_name_arr = group_data[1].split(":");

                if (group_id_arr.length > 1 && group_name_arr.length > 1) {
                    if (group_id_arr.length >= 3) {
                        group_id = Short.valueOf(group_id_arr[2]);
                    } else {
                        group_id = Short.valueOf(group_id_arr[1]);
                    }
                    group_name = group_name_arr[1];
                }
            }
            for (int i = 0; i < device_list.size(); i++) {
                System.out.println("GetAllGroupsMac = " + device_list.get(i));
            }
            AppGroup appGroup = new AppGroup();
            appGroup.setGroup_id(group_id);
            appGroup.setGroup_name(group_name);
            appGroup.setMac_data(device_list);

            DataSources.getInstance().GetAllGroups(appGroup);
        }
    }

    //return data
    public static void ParseAddGroupBack(byte[] data, int len) {
        byte[] id_bt = new byte[2];
        byte[] name_bt = new byte[len];

        System.arraycopy(data, 32, id_bt, 0, 2);
        short group_id = FtFormatTransfer.hBytesToShort(id_bt);

        System.arraycopy(data, 36, name_bt, 0, len);
        String group_name = FtFormatTransfer.byteToASCIIString(name_bt);

        //判断是否是所添加的组
        if (group_name.equals(Constants.GROUP_GLOBAL.ADD_GROUP_NAME)) {
            if (group_id == 0 && group_name == null) {
                return;
            }
            DataSources.getInstance().AddGroupResult(group_id, Constants.GROUP_GLOBAL.ADD_GROUP_NAME);
        }
    }

    //解析delete group 返回的数据
    public static class ParseDeleteGroupResult {
        public short mGroupID;

        public ParseDeleteGroupResult() {
            mGroupID = 0;
        }

        public void parseBytes(byte[] data) {
            System.out.println("解析byte数组 = " + Arrays.toString(data));
            byte[] mId_Bt = new byte[2];

            System.arraycopy(data, 32, mId_Bt, 0, 2);
            mGroupID = FtFormatTransfer.hBytesToShort(mId_Bt);
        }
    }
}
