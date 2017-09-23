package com.adurosmart.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adurosmart.activitys.ColourMixingActivity;
import com.adurosmart.activitys.SensorsActivity;
import com.adurosmart.sdk.R;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.SerialHandler;
import com.adurosmart.global.Constants;
import com.adurosmart.widget.CompatSeekBar;

import java.util.List;

import static com.adurosmart.activitys.GroupDetailActivity.group_id;
import static com.adurosmart.activitys.SceneDetailActivity.isSceneDetail;
import static com.adurosmart.global.Constants.device_mac;
import static com.adurosmart.global.Constants.device_mainpoint;
import static com.adurosmart.global.Constants.device_short;
import static com.adurosmart.global.Constants.isSwitch;
import static com.adurosmart.global.Constants.isTask_Mac;
import static com.adurosmart.global.Constants.level;
import static com.adurosmart.global.Constants.sceneId;

/**
 * Created by best on 2016/7/25.
 */
public class DeviceInfoAdapter extends BaseAdapter {
    List<AppDevice> datas;
    Context mContext;
    boolean isSwtch = false;

    public DeviceInfoAdapter(Context context, List<AppDevice> datas,boolean isSwtch) {
        this.datas = datas;
        this.mContext = context;
        this.isSwtch = isSwtch;
    }

    class ViewHolder {
        private TextView vv_text;
        private TextView ca_text;
        private TextView pw_text;
        private TextView hz_text;
        private TextView pf_text;

        private TextView dev_name;
        private TextView dev_mac;
        private TextView dev_short_addr;
        private TextView device_main_point;
        private ImageView image_defence_state;

        private CompatSeekBar seekBar;
        private  Button button;

        private LinearLayout layout_ll;
        private RelativeLayout rl_seekbar;

        public LinearLayout getLayout_ll(){
            return layout_ll;
        }

        public void setLayout_ll(LinearLayout layout_ll){
            this.layout_ll = layout_ll;
        }

        public TextView getDev_short_addr() {
            return dev_short_addr;
        }

        public void setDev_short_addr(TextView dev_short_addr) {
            this.dev_short_addr = dev_short_addr;
        }

        public TextView getDevice_name() {
            return dev_name;
        }

        public void setDevice_name(TextView dev_name) {
            this.dev_name = dev_name;
        }

        public TextView getDev_mac(){
            return dev_mac;
        }

        public void setDev_mac(TextView dev_mac){
            this.dev_mac = dev_mac;
        }

        public void setDevice_main_point(TextView device_main_point){
            this.device_main_point = device_main_point;
        }
        public TextView getDevice_main_point(){
            return device_main_point;
        }

        public ImageView getImage_defence_state() {
            return image_defence_state;
        }

        public void setImage_defence_state(ImageView image_defence_state) {
            this.image_defence_state = image_defence_state;
        }

        // Access to adjust light brightness control
        public CompatSeekBar getSeekBar() {
            return seekBar;
        }

        public void setSeekBar_brightness(CompatSeekBar seekBar) {
            this.seekBar = seekBar;
        }

        public Button getDeleteBtn(){
            return button;
        }
        public void setDeleteBtn(Button btn){
            this.button = btn;
        }

        public RelativeLayout getRelativeLayout(){
            return rl_seekbar;
        }

        public void setRelativeLayout(RelativeLayout rl_seekbar){
            this.rl_seekbar = rl_seekbar;
        }

        public TextView getVv_text(){
            return vv_text;
        }
        public TextView getCa_text(){
            return ca_text;
        }
        public TextView getPw_text(){
            return pw_text;
        }
        public TextView getHz_text(){
            return hz_text;
        }
        public TextView getPf_text(){
            return pf_text;
        }

        public void setVv_text(TextView text){
            this.vv_text = text;
        }
        public void setCa_text(TextView text){
            this.ca_text = text;
        }
        public void setPw_text(TextView text){
            this.pf_text = text;
        }
        public void setHz_text(TextView text){
            this.hz_text = text;
        }
        public void setPf_text(TextView text){
            this.pw_text= text;
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
    public View getView(final int position, View convertView,final ViewGroup parent) {

        View view = convertView;

        final ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_device_item, null);
            holder = new ViewHolder();

            //device_name
            TextView dev_name = (TextView) view.findViewById(R.id.devname);
            holder.setDevice_name(dev_name);

            TextView dev_mac = (TextView) view.findViewById(R.id.dev_mac);
            holder.setDev_mac(dev_mac);
            //device_onlineState
            TextView dev_short_addr = (TextView) view.findViewById(R.id.online_state);
            holder.setDev_short_addr(dev_short_addr);

            TextView device_main_point = (TextView)view.findViewById(R.id.device_main_point);
            holder.setDevice_main_point(device_main_point);

            ImageView image_defence_state = (ImageView) view.findViewById(R.id.image_defence_state);
            holder.setImage_defence_state(image_defence_state);

            CompatSeekBar seekbar = (CompatSeekBar)view.findViewById(R.id.BAR);
            holder.setSeekBar_brightness(seekbar);

            Button button = (Button) view.findViewById(R.id.delete_btn);
            holder.setDeleteBtn(button);

            RelativeLayout rl_seekbar = (RelativeLayout)view.findViewById(R.id.rl_seebar);
            holder.setRelativeLayout(rl_seekbar);




            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_ll);
            holder.setLayout_ll(linearLayout);

            TextView vv_text = (TextView) view.findViewById(R.id.vv_text);
            holder.setVv_text(vv_text);

            TextView ca_text = (TextView) view.findViewById(R.id.ca_text);
            holder.setCa_text(ca_text);

            TextView pw_text = (TextView) view.findViewById(R.id.pw_text);
            holder.setPw_text(pw_text);

            TextView hz_text = (TextView) view.findViewById(R.id.hz_text);
            holder.setHz_text(hz_text);

            TextView pf_text = (TextView) view.findViewById(R.id.pf_text);
            holder.setPf_text(pf_text);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppDevice localDevice = datas.get(position);

        holder.getDevice_name().setText("Name:" + localDevice.getDeviceName());
        holder.getDev_mac().setText("Mac:" + localDevice.getDeviceMac());
        holder.getDev_short_addr().setText("短地址：" + localDevice.getShortaddr() + "，ZONE_TYPE:" + localDevice.getZonetype());
        holder.getDevice_main_point().setText("目标端点：" + localDevice.getEndpoint() + "，设备ID:" + localDevice.getDeviceid());

        holder.getVv_text().setText("" + localDevice.getVoltage());
        holder.getCa_text().setText("" + localDevice.getCurrent());
        holder.getPf_text().setText("" + localDevice.getPower());
        holder.getHz_text().setText("" + localDevice.getFrequency());
        holder.getPw_text().setText("" + localDevice.getPower_factor());

        if (localDevice.getDeviceid().endsWith("0051")){
            holder.getLayout_ll().setVisibility(View.VISIBLE);
        }else{
            holder.getLayout_ll().setVisibility(View.GONE);
        }

        if (localDevice.getDeviceid().equals("0402")){
            holder.getImage_defence_state().setVisibility(View.GONE);
            holder.getSeekBar().setVisibility(View.GONE);
        }else{
            holder.getImage_defence_state().setVisibility(View.VISIBLE);
            holder.getSeekBar().setVisibility(View.VISIBLE);
        }

        if (localDevice.getDeviceState() == 1){
            holder.getRelativeLayout().setVisibility(View.VISIBLE);
            holder.getImage_defence_state().setBackgroundResource(R.mipmap.btn_on);
        }else if (localDevice.getDeviceState() == 0){
            holder.getRelativeLayout().setVisibility(View.GONE);
            holder.getImage_defence_state().setBackgroundResource(R.mipmap.btn_off);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SerialHandler.getInstance().IdentifyDevice(localDevice,4);
                if (!Constants.isTask_Mac){
                    if (localDevice.getDeviceid().equals("0402")){
                        Intent intent1 = new Intent(parent.getContext() , SensorsActivity.class);
                        intent1.putExtra("AppDeviceInfo",localDevice);
                        parent.getContext().startActivity(intent1);
                    }else if(localDevice.getDeviceid().equals("0200")
                            |localDevice.getDeviceid().equals("0220")
                            |localDevice.getDeviceid().equals("0210")
                            |localDevice.getDeviceid().equals("0110")){
                        Intent intent = new Intent(parent.getContext(),ColourMixingActivity.class);
                        intent.putExtra("AppDeviceInfo", localDevice);
                        intent.putExtra("device_mac",localDevice.getDeviceMac());
                        intent.putExtra("device_shortaddr",localDevice.getShortaddr());
                        intent.putExtra("main_endpoint",localDevice.getEndpoint());
                        intent.putExtra("device_id",localDevice.getDeviceid());
                        parent.getContext().startActivity(intent);
                    }
                }else{
                    System.out.println("isTask_Mac = " + isTask_Mac);
                    if (localDevice.getDeviceid().equals("0402")){
                        device_mac = localDevice.getDeviceMac();
                        device_mainpoint = localDevice.getEndpoint();
                        device_short = localDevice.getShortaddr();
                    }else{
                        device_mac = localDevice.getDeviceMac();
                        device_mainpoint = localDevice.getEndpoint();
                        device_short = localDevice.getShortaddr();
                    }
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(parent.getContext());
                dialog.setMessage("delete device ?");
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_view, null);
                final EditText searchC = (EditText) layout.findViewById(R.id.searchC);
                searchC.setText(localDevice.getDeviceName());
                dialog.setView(layout);
                dialog.setPositiveButton("modify", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SerialHandler.getInstance().UpdateDeviceName(localDevice,searchC.getText().toString());
                    }
                });

                dialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (isSceneDetail){
                            SerialHandler.getInstance().DeleteDeviceFromSceneFile(sceneId,1,localDevice.getDeviceMac());
                            try{
                                Thread.sleep(1000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            SerialHandler.getInstance().deleteDeviceFromScene(localDevice,sceneId);
                            return;
                        }
                        if (isSwtch){
                            SerialHandler.getInstance().DeleteDeviceFromGroupFile(group_id,1,localDevice.getDeviceMac());
                            try{
                                Thread.sleep(1000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            SerialHandler.getInstance().deleteDeviceFromGroup(localDevice,group_id);
                        }else{
                            SerialHandler.getInstance().DeleteDevice(localDevice.getDeviceMac());
                            datas.remove(position);
                            notifyDataSetInvalidated();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        holder.getDeleteBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("delete device ?");
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_view, null);
                final EditText searchC = (EditText) layout.findViewById(R.id.searchC);
                dialog.setView(layout);
                dialog.setPositiveButton("modify", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String device_name = searchC.getText().toString();
                        SerialHandler.getInstance().UpdateDeviceName(localDevice,device_name);
                    }
                });

                dialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SerialHandler.getInstance().DeleteDevice(localDevice.getDeviceMac());
                        datas.remove(position);
                        notifyDataSetInvalidated();
                    }
                });
                dialog.show();
            }
        });

        holder.getImage_defence_state().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isSwtch){
                    isSwitch = 1;
                    holder.getRelativeLayout().setVisibility(View.VISIBLE);
                    holder.getImage_defence_state().setBackgroundResource(R.mipmap.btn_on);
                    SerialHandler.getInstance().setDeviceSwitchState(localDevice,1);
                    isSwtch = false;
                    localDevice.setDeviceState(1);
                }else {
                    isSwitch = 0;
                    holder.getRelativeLayout().setVisibility(View.GONE);
                    holder.getImage_defence_state().setBackgroundResource(R.mipmap.btn_off);
                    SerialHandler.getInstance().setDeviceSwitchState(localDevice,0);
                    isSwtch = true;
                    localDevice.setDeviceState(0);
                }
            }
        });

        //brightness adjusting
        holder.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onStopTrackingTouch(SeekBar seekBar){

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){

            }
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                level = 255*progress/100;
                SerialHandler.getInstance().setDeviceLevel(localDevice,255*progress/100);
                localDevice.setBright(level);
//                holder.getSeekBar().setProgress(localDevice.getBright());
            }
        });

        return view;
    }
}
