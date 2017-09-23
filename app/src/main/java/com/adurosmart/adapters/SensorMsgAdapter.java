package com.adurosmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.adurosmart.entity.SensorMsgInfo;
import com.adurosmart.sdk.R;

import java.util.List;

/**
 * Created by best on 2016/9/1.
 */
public class SensorMsgAdapter extends BaseAdapter{
    private Context context;
    private List<SensorMsgInfo> datas;

    public SensorMsgAdapter(Context context, List<SensorMsgInfo> datas) {
        this.datas = datas;
        this.context = context;
    }

    class ViewHolder {
        private TextView deviceId;
        private TextView msgState;
        private TextView msgtime;
        private ImageView group_state;

        public void setDeviceId(TextView textView){
            this.deviceId = textView;
        }
        public void setMsgState(TextView textView){
            this.msgState = textView;
        }

        public void setMsgTime(TextView textView){
            this.msgtime = textView;
        }

        public TextView getDeviceId(){
            return deviceId;
        }

        public TextView getMsgState(){
            return msgState;
        }

        public TextView getMsgtime(){
            return msgtime;
        }

        public ImageView getMsg_State_Img(){
            return group_state;
        }

        public void setMsg_State_Img(ImageView imageView){
            this.group_state = imageView;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View view = convertView;

        final ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.list_sensor_item, null);
            holder = new ViewHolder();

            //ID
            TextView device_id_text = (TextView)view.findViewById(R.id.device_id);
            holder.setDeviceId(device_id_text);

            //时间
            TextView msg_time_text = (TextView)view.findViewById(R.id.msg_time);
            holder.setMsgTime(msg_time_text);

            //状态
            TextView msg_state_text = (TextView)view.findViewById(R.id.msg_state);
            holder.setMsgState(msg_state_text);

            ImageView msg_state = (ImageView) view.findViewById(R.id.msg_state_img);
            holder.setMsg_State_Img(msg_state);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final SensorMsgInfo device_msg = datas.get(position);

        holder.getDeviceId().setText("" + device_msg.device_mac);
        holder.getMsgtime().setText("" + device_msg.time);
        if (device_msg.state == 0){
            holder.getMsgState().setText(R.string.close);
        }else if (device_msg.state == 1){
            holder.getMsgState().setText(R.string.open);
        }

        if (device_msg.state == 1){
            holder.getMsg_State_Img().setBackgroundResource(R.mipmap.btn_on);
        }else if (device_msg.state == 0){
            holder.getMsg_State_Img().setBackgroundResource(R.mipmap.btn_off);
        }

        return view;
    }
}
