package com.core.cmddata.parsedata;

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

    public static void ParseGetGroupsInfo(String groupsinfo) {
        Short Group_Id = 0;
        String Group_Name = "";

        ArrayList<String> device_list = new ArrayList<>();
        int strToint = groupsinfo.indexOf(":");
        String isGroup = "";
        if (strToint >= 0) {
            isGroup = groupsinfo.substring(strToint - 4, strToint);
        }
        device_list.clear();

        if (groupsinfo.contains("GW") && !groupsinfo.contains("K64") && isGroup.contains("upId")) {
            String[] group_data = groupsinfo.split(",");

            for (int i = 2; i < group_data.length; i++) {
                if (group_data.length < 2) {
                    continue;
                }
                device_list.add(group_data[i]);

                String[] Id_Source = group_data[0].split(":");
                String[] Name_Source = group_data[1].split(":");

                if (Id_Source.length > 1 && Name_Source.length > 1) {
                    if (Id_Source.length >= 3) {
                        Group_Id = Short.valueOf(Id_Source[2]);
                    } else {
                        Group_Id = Short.valueOf(Id_Source[1]);
                    }
                    Group_Name = Utils.toStringHex2(Name_Source[1]);
                }
            }
            AppGroup appGroup = new AppGroup();
            appGroup.setGroup_id(Group_Id);
            appGroup.setGroup_name(Group_Name);
            appGroup.setMac_data(device_list);

            DataSources.getInstance().GetAllGroups(appGroup);
        }
    }

    //return data
    public static void ParseAddGroupBack(byte[] data, int len) {
        byte[] mId_Bt = new byte[2];
        byte[] mName_Bt = new byte[len];

        System.arraycopy(data, 32, mId_Bt, 0, 2);
        short mGroupID = FtFormatTransfer.hBytesToShort(mId_Bt);

        System.arraycopy(data, 36, mName_Bt, 0, len);
        String mGroupName = FtFormatTransfer.bytesToString(mName_Bt);
        //判断是否是所添加的组
        if (mGroupName.equals(Constants.GROUP_GLOBAL.ADD_GROUP_NAME)) {
            if (mGroupID == 0 && mGroupName == null) {
                return;
            }
            DataSources.getInstance().AddGroupResult(mGroupID, Constants.GROUP_GLOBAL.ADD_GROUP_NAME);
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
