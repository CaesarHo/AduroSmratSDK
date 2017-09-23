package com.adurosmart.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adurosmart.sdk.R;
import com.core.entity.AppTask2;
import com.core.gatewayinterface.SerialHandler;
import java.util.List;

/**
 * Created by best on 2016/9/7.
 */
public class TaskInfoAdapter extends BaseAdapter{

    private Context context;
    private List<AppTask2> datas;

    public TaskInfoAdapter(Context context, List<AppTask2> datas) {
        this.datas = datas;
        this.context = context;
    }

    class ViewHolder {
        private TextView task_no;
        private TextView task_name;
        private TextView task_isenabled;
        private ImageView control_btn;

        public void setTask_No(TextView textView){
            this.task_no = textView;
        }
        public void setTask_Name(TextView textView){
            this.task_name = textView;
        }

        public void setTask_Is_Enabled(TextView textView){
            this.task_isenabled = textView;
        }

        public void setTask_Control_Btn(ImageView imageView){
            this.control_btn = imageView;
        }

        public TextView getTask_No(){
            return task_no;
        }

        public TextView getTask_Name(){
            return task_name;
        }
        public TextView getTask_Is_Enabled(){
            return task_isenabled;
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
            view = LayoutInflater.from(context).inflate(R.layout.list_task_item, null);
            holder = new ViewHolder();

            //任务编号
            TextView task_no_text = (TextView)view.findViewById(R.id.task_no);
            holder.setTask_No(task_no_text);

            //任务名称
            TextView task_name_text = (TextView)view.findViewById(R.id.task_name);
            holder.setTask_Name(task_name_text);

            TextView task_is_enabled = (TextView) view.findViewById(R.id.task_is_text);
            holder.setTask_Is_Enabled(task_is_enabled);

            ImageView control_btn = (ImageView) view.findViewById(R.id.control_btn);
            holder.setTask_Control_Btn(control_btn);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        final AppTask2 taskInfo = datas.get(position);

        holder.getTask_No().setText("No.:" + taskInfo.getTask_no() + "," + "周期："
                + taskInfo.getTask_cycle() + "," + "传感器:" + taskInfo.getSensor_mac());
        holder.getTask_Name().setText("名称："+taskInfo.getTask_name() + "," +
                "时：" + taskInfo.getTask_hour() + "," + "分：" + taskInfo.getTask_minute());
        holder.getTask_Is_Enabled().setText("是否启用 ：" + taskInfo.getIsEnabled() + "," + "动作："
                + taskInfo.getTask_status() + "任务类型：" + taskInfo.getTask_type());

        holder.getControl_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                dialog.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String searchC = et_search.getText().toString().trim();
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SerialHandler.getInstance().DeleteTask(taskInfo.getTask_no());
                        datas.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.show();

                return true;
            }
        });

        return view;
    }
}
