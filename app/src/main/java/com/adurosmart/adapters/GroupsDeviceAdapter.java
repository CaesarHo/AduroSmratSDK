package com.adurosmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.adurosmart.global.Constants;
import com.adurosmart.sdk.R;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.SerialHandler;

import java.util.List;

/**
 * Created by best on 2016/8/11.
 */
public class GroupsDeviceAdapter extends BaseAdapter {
    List<AppDevice> datas;
    Context mContext;

    public GroupsDeviceAdapter(Context context, List<AppDevice> datas) {
        this.datas = datas;
        this.mContext = context;
    }

    class ViewHolder {
        private TextView name;
        private TextView online_state;

        private CheckBox image_defence_state;

        public TextView getOnline_state() {
            return online_state;
        }

        public void setOnline_state(TextView online_state) {
            this.online_state = online_state;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public CheckBox getImage_defence_state() {
            return image_defence_state;
        }

        public void setImage_defence_state(CheckBox image_defence_state) {
            this.image_defence_state = image_defence_state;
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        final ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.group_device_item, null);
            holder = new ViewHolder();

            //device_name
            TextView name = (TextView) view.findViewById(R.id.devname);
            holder.setName(name);

            //device_onlineState
            TextView onlineState = (TextView) view.findViewById(R.id.online_state);
            holder.setOnline_state(onlineState);

            CheckBox image_defence_state = (CheckBox) view.findViewById(R.id.checkBox1);
            holder.setImage_defence_state(image_defence_state);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppDevice localDevice = datas.get(position);

        holder.getName().setText(localDevice.getDeviceMac());
        holder.getOnline_state().setText(localDevice.getShortaddr());

        if (!localDevice.getSelected()) {
            holder.getImage_defence_state().setChecked(false);
        } else {
            holder.getImage_defence_state().setChecked(true);
        }
        holder.getImage_defence_state().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SerialHandler.getInstance().IdentifyDevice(localDevice, 4);
                if (localDevice.getSelected()) {
//                    for (int i = 0; i < Constants.groupDeviceList.size(); i++) {
//                        if (Constants.groupDeviceList.get(i).getDeviceMac().equalsIgnoreCase(localDevice.getDeviceMac())) {
//                            Constants.groupDeviceList.remove(i);
//                            System.out.println("groupDeviceList = " + Constants.groupDeviceList.size());
//                        }
//                    }
                    localDevice.setSelected(false);
                } else {
//                    if (Constants.groupDeviceList.size() == 0) {
//                        Constants.groupDeviceList.add(localDevice);
//                        System.out.println("groupDeviceList = " + Constants.groupDeviceList.size());
//                    } else {
//                        synchronized (Constants.groupDeviceList){
//                            for (int i = 0; i < Constants.groupDeviceList.size(); i++) {
//                                if (!Constants.groupDeviceList.get(i).getDeviceMac().equalsIgnoreCase(localDevice.getDeviceMac())) {
//                                    Constants.groupDeviceList.add(localDevice);
//                                    System.out.println("groupDeviceList = " + Constants.groupDeviceList.size());
//                                }
//                            }
//                        }
//                    }
                    localDevice.setSelected(true);
                }
            }
        });

        return view;
    }
}
