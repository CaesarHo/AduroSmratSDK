package com.adurosmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.adurosmart.sdk.R;
import com.core.entity.AppGroup;
import java.util.List;

/**
 * Created by best on 2016/8/12.
 */
public class ScenesGroupsAdapter extends BaseAdapter{
    List<AppGroup> datas;
    Context mContext;
    public ScenesGroupsAdapter(Context context, List<AppGroup> datas) {
        this.datas = datas;
        this.mContext = context;
    }

    class ViewHolder {
        private TextView scene_group_name;
        private TextView scene_group_id;

        private CheckBox image_defence_state;

        public TextView getId() {
            return scene_group_id;
        }

        public void setId(TextView scene_group_id) {
            this.scene_group_id = scene_group_id;
        }

        public TextView getName() {
            return scene_group_name;
        }

        public void setName(TextView scene_group_name) {
            this.scene_group_name = scene_group_name;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.scene_group_item, null);
            holder = new ViewHolder();

            //scene_group_name
            TextView scene_group_name = (TextView) view.findViewById(R.id.scene_group_name);
            holder.setName(scene_group_name);

            //scene_group_id
            TextView scene_group_id = (TextView) view.findViewById(R.id.scene_group_id);
            holder.setId(scene_group_id);

            CheckBox image_defence_state = (CheckBox) view.findViewById(R.id.checkBox1);
            holder.setImage_defence_state(image_defence_state);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppGroup scenegroup = datas.get(position);

        holder.getName().setText(scenegroup.getGroup_name());
        holder.getId().setText(""+scenegroup.getGroup_id());

        if (scenegroup.getSelected()){
            holder.getImage_defence_state().setChecked(true);
        }else{
            holder.getImage_defence_state().setChecked(false);
        }

        holder.getImage_defence_state().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (scenegroup.getSelected()) {
                    scenegroup.setSelected(false);
                } else {
                    scenegroup.setSelected(true);
                }
            }
        });

        return view;
    }
}
