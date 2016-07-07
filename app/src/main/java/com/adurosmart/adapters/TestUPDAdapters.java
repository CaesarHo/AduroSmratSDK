package com.adurosmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adurosmart.activitys.UpdActivity;

import org.eclipse.paho.android.service.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by best on 2016/6/24.
 */
public class TestUPDAdapters extends BaseAdapter{

    List<UpdActivity.DevInfo> datas;
    Context mContext;
    boolean isCreatePassword = false;
    public TestUPDAdapters(Context context,List<UpdActivity.DevInfo> datas){
        this.datas = datas;//new ArrayList<DevInfo>();
        this.mContext = context;
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
        if(null==view){
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_lan,null);
        }

        TextView devid = (TextView) view.findViewById(R.id.devId);
        TextView devip = (TextView) view.findViewById(R.id.devIp);

        final UpdActivity.DevInfo localDevice = datas.get(position);
        devid.setText(localDevice.devId);
        devip.setText(localDevice.devIp);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext,AddContactNextActivity.class);
//                intent.putExtra("ID", strid);
//                intent.putExtra("IP", localDevice.devIp);
//                mContext.startActivity(intent);
                return;
            }
        });
        return view;
    }

    public void updateData(){
        this.datas = new ArrayList<UpdActivity.DevInfo>();
        this.notifyDataSetChanged();
    }
}
