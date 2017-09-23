package com.adurosmart.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adurosmart.activitys.SceneDetailActivity;
import com.adurosmart.sdk.R;
import com.adurosmart.util.ColorUtils;
import com.core.entity.AppScene;
import com.core.gatewayinterface.SerialHandler;

import java.util.List;

import static com.adurosmart.global.Constants.sceneId;
import static com.adurosmart.global.Constants.scene_group_id;

/**
 * Created by best on 2016/8/11.
 */
public class SceneInfoAdapter extends BaseAdapter{
    private Context context;
    private List<AppScene> datas;

    public SceneInfoAdapter(Context context, List<AppScene> datas) {
        this.datas = datas;
        this.context = context;
    }

    class ViewHolder {
        private TextView scene_id;
        private TextView scene_name;
        private TextView scene_group_id;
        private ImageView control_btn;

        public void setScene_id(TextView textView){
            this.scene_id = textView;
        }
        public void setScene_name(TextView textView){
            this.scene_name = textView;
        }

        public void setScene_Group_id(TextView textView){
            this.scene_group_id = textView;
        }

        public void setScene_Control_Btn(ImageView imageView){
            this.control_btn = imageView;
        }

        public TextView getScene_id(){
            return scene_id;
        }

        public TextView getScene_name(){
            return scene_name;
        }
        public TextView getScene_group_id(){
            return scene_group_id;
        }

        public ImageView getControl_btn(){
            return control_btn;
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
            view = LayoutInflater.from(context).inflate(R.layout.scenes_item_list, null);
            holder = new ViewHolder();

            //场景ID
            TextView scene_id_text = (TextView)view.findViewById(R.id.scene_id);
            holder.setScene_id(scene_id_text);

            //场景名称
            TextView scene_name_text = (TextView)view.findViewById(R.id.scene_name);
            holder.setScene_name(scene_name_text);

            TextView scene_group_id = (TextView) view.findViewById(R.id.scene_group_id);
            holder.setScene_Group_id(scene_group_id);

            ImageView control_btn = (ImageView) view.findViewById(R.id.control_btn);
            holder.setScene_Control_Btn(control_btn);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppScene sceneInfo = datas.get(position);

        holder.getScene_id().setText("编号: " + sceneInfo.getSencesId());
        holder.getScene_name().setText(""+sceneInfo.getSencesName());
        holder.getScene_group_id().setText("" + sceneInfo.getGroups_id());
        holder.getControl_btn().setImageResource(R.mipmap.ic_launcher);
        holder.getControl_btn().setColorFilter(ColorUtils.getColor(context,R.color.colorPrimary));

        holder.getControl_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SerialHandler.getInstance().RecallScene(sceneInfo.getGroups_id(),sceneInfo.getSencesId());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_view, null);
                dialog.setView(layout);
                final EditText et_search = (EditText)layout.findViewById(R.id.searchC);
                dialog.setPositiveButton("update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String searchC = et_search.getText().toString().trim();
                        SerialHandler.getInstance().ChangeSceneName(sceneInfo.getSencesId(),searchC);
                    }
                });

                dialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SerialHandler.getInstance().deleteScence(sceneInfo.getSencesId());
                        datas.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.show();

                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sceneId = sceneInfo.getSencesId();
                scene_group_id = sceneInfo.getGroups_id();
                Intent intent = new Intent(context, SceneDetailActivity.class);
                intent.putExtra("SCENE_INFO",sceneInfo);
                context.startActivity(intent);
            }
        });


        return view;
    }
}
